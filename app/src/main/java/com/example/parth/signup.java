package com.example.parth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    TextView t1;
    EditText signupemail, signuppassword, signupname, signupnumber;
    Button register;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        t1 = findViewById(R.id.text4);
        signupname = findViewById(R.id.edit1);
        signupemail = findViewById(R.id.edit2);
        signuppassword = findViewById(R.id.edit3);
        signupnumber = findViewById(R.id.edit4);
        register = findViewById(R.id.button1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    saveUserToDatabase();
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signup.this, login.class);
                startActivity(i);
            }
        });
    }

    // Method to validate the inputs before saving to Firebase
    private boolean validateInputs() {
        String name = signupname.getText().toString().trim();
        String email = signupemail.getText().toString().trim();
        String password = signuppassword.getText().toString().trim();
        String number = signupnumber.getText().toString().trim();

        if (name.isEmpty()) {
            signupname.setError("Name cannot be empty");
            signupname.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            signupemail.setError("Email cannot be empty");
            signupemail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            signuppassword.setError("Password cannot be empty");
            signuppassword.requestFocus();
            return false;
        }

        if (number.isEmpty()) {
            signupnumber.setError("Number cannot be empty");
            signupnumber.requestFocus();
            return false;
        }

        return true;
    }

    // Method to save user information to Firebase Realtime Database
    private void saveUserToDatabase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        String name = signupname.getText().toString().trim();
        String email = signupemail.getText().toString().trim();
        String password = signuppassword.getText().toString().trim();
        String number = signupnumber.getText().toString().trim();

        // Use email as the unique key (replace '.' with ',' to avoid issues)
        //String modifiedEmail = email.replace(".", ",");

        helperclass helperclass = new helperclass(name, email, password, number);
        reference.child(name).setValue(helperclass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(signup.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(signup.this, login.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(signup.this, "Failed to sign up. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
