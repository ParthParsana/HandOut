package com.example.parth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class setting extends AppCompatActivity {

    TextView logout;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_KEY = "loginKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear login state
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGIN_KEY, false);
                editor.apply();

                // Redirect to Login activity
                Intent intent = new Intent(setting.this, login.class);
                startActivity(intent);
                finish();  // Close MainActivity
            }
        });

    }
}