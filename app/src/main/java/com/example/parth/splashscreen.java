package com.example.parth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Retrieve login state from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Retrieve userId from SharedPreferences or UserSession
            String userId = sharedPreferences.getString("userId", null);
            if (userId != null) {
                UserSession.getInstance().setUserId(userId);
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // User ID not found, redirect to login page
                redirectToLogin();
            }
        } else {
            // User not logged in, redirect to login page
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(splashscreen.this, login.class);
        startActivity(intent);
        finish();
    }
}
