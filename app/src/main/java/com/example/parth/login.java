package com.example.parth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    TextView t1;
    EditText loginemail, loginpassword;
    Button login;
    ProgressBar progressBar;

    // SharedPreferences to store login state
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_KEY = "loginKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(LOGIN_KEY, false)) {
            // If already logged in, skip the login activity and go to MainActivity
            Intent i = new Intent(login.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_login);

        t1 = findViewById(R.id.text4);
        loginemail = findViewById(R.id.edit1);
        loginpassword = findViewById(R.id.edit2);
        login = findViewById(R.id.button1);
        //progressBar = findViewById(R.id.progress_bar);  // Optional: Add ProgressBar in XML

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateemail() || !validatepassword()) {
                    return;
                }
                checkuser();
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, signup.class);
                startActivity(i);
            }
        });
    }

    public boolean validateemail() {
        String val = loginemail.getText().toString().trim();

        if (val.isEmpty()) {
            loginemail.setError("Email cannot be empty");
            return false;
        } else {
            loginemail.setError(null);
            return true;
        }
    }

    public boolean validatepassword() {
        String val = loginpassword.getText().toString().trim();

        if (val.isEmpty()) {
            loginpassword.setError("Password cannot be empty");
            return false;
        } else {
            loginpassword.setError(null);
            return true;
        }
    }

    public void checkuser() {
        String useremail = loginemail.getText().toString().trim();
        String userpassword = loginpassword.getText().toString().trim();

        // Show ProgressBar while logging in (Optional)
        // progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(useremail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginemail.setError(null);

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Fetch the password associated with the email from the database
                        String passwordFromDb = userSnapshot.child("password").getValue(String.class);

                        if (passwordFromDb != null && passwordFromDb.equals(userpassword)) {
                            // Save login state in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(LOGIN_KEY, true);
                            editor.apply();

                            // Password matches, user can log in
                            loginemail.setError(null);
                            Intent i = new Intent(login.this, MainActivity.class);
                            startActivity(i);
                            finish();  // Optional: Close the login activity
                        } else {
                            // Password does not match
                            loginpassword.setError("Invalid Password");
                            loginpassword.requestFocus();
                        }
                    }

                } else {
                    // User does not exist
                    loginemail.setError("User does not exist");
                    loginemail.requestFocus();
                }

                // Hide ProgressBar after operation (Optional)
                // progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error occurred during the Firebase operation
                // progressBar.setVisibility(View.GONE);
                Toast.makeText(login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
