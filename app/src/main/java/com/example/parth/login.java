package com.example.parth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

     TextView t1;
    EditText email,password;
    Button login;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent i=new Intent(login.this, MainActivity.class);
            startActivity(i);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        t1=findViewById(R.id.text4);
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.edit1);
        password=findViewById(R.id.edit2);
        login=findViewById(R.id.button1);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(login.this, signup.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String emails = email.getText().toString();
                String passwords = password.getText().toString();

                if(emails.isEmpty())
                {
                    Toast.makeText(login.this,"please enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwords.isEmpty())
                {
                    Toast.makeText(login.this,"please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emails, passwords)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login Successfull",
                                            Toast.LENGTH_SHORT).show();

                                    Intent i=new Intent(login.this,MainActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {


                                    Toast.makeText(login.this, "Email and Password not Match",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

    }

}