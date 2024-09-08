package com.example.parth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText loginemail, loginpassword;
    Button loginbtn;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginemail = findViewById(R.id.edit1);
        loginpassword = findViewById(R.id.edit2);
        loginbtn = findViewById(R.id.button1);

        reference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Redirect to MainActivity if already logged in
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    private void checkUser() {
        String useremail = loginemail.getText().toString().trim();
        String userpassword = loginpassword.getText().toString().trim();

        if (useremail.isEmpty() || userpassword.isEmpty()) {
            Toast.makeText(login.this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Query checkUserDatabase = reference.orderByChild("email").equalTo(useremail);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get userId
                    String userId = snapshot.getChildren().iterator().next().getKey();
                    String passwordFromDb = snapshot.child(userId).child("password").getValue(String.class);

                    if (passwordFromDb != null && passwordFromDb.equals(userpassword)) {
                        // Save login state to SharedPreferences
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userId", userId);
                        editor.apply(); // Apply changes

                        // Fetch user profile data and set it in the Singleton
                        fetchUserProfileData(userId);

                        // Redirect to MainActivity
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        loginpassword.setError("Invalid Credentials");
                        loginpassword.requestFocus();
                    }
                } else {
                    loginemail.setError("User does not exist");
                    loginemail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfileData(String userId) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String number = snapshot.child("number").getValue(String.class);

                    // Set user profile data in UserSession Singleton
                    UserSession userSession = UserSession.getInstance();
                    userSession.setUserId(userId);
                    userSession.setUserName(name);
                    userSession.setUserEmail(email);
                    userSession.setUserNumber(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Failed to fetch user profile data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
