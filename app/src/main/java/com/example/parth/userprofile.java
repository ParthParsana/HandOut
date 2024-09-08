package com.example.parth;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userprofile extends AppCompatActivity {

    TextView profilename, profileemail, profilemobile;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        profilename = findViewById(R.id.name);
        profileemail = findViewById(R.id.email);
        profilemobile = findViewById(R.id.number);

        // Retrieve userId from UserSession Singleton
        String userId = UserSession.getInstance().getUserId();

        if (userId != null) {
            // Fetch user data from Firebase
            reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String number = snapshot.child("number").getValue(String.class);

                        profilename.setText(name != null ? name : "Name not available");
                        profileemail.setText(email != null ? email : "Email not available");
                        profilemobile.setText(number != null ? number : "Number not available");
                    } else {
                        Toast.makeText(userprofile.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(userprofile.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No userId found in session", Toast.LENGTH_SHORT).show();
        }
    }
}
