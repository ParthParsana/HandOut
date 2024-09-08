package com.example.parth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class userprofile extends AppCompatActivity {

    TextView profilename, profileemail, profilemobile;
    DatabaseReference reference;
    ImageView choosephoto, imageView;
    ActivityResultLauncher<Intent> resultLauncher;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        profilename = findViewById(R.id.name);
        profileemail = findViewById(R.id.email);
        profilemobile = findViewById(R.id.number);
        choosephoto = findViewById(R.id.photo1);
        imageView = findViewById(R.id.photo);

        // Retrieve userId from UserSession Singleton or your preferred method
        userId = UserSession.getInstance().getUserId(); // Adjust as needed
        if (userId != null) {
            // Initialize DatabaseReference for the user
            reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Load user profile data
            loadProfileData();
            loadImage();

            // Register result launcher for image picking
            registerResult();

            // Set up photo selection
            choosephoto.setOnClickListener(view -> pickImage());
        } else {
            Toast.makeText(this, "No userId found in session", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileData() {
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
                Log.e("DatabaseError", "Database error: " + error.getMessage());
            }
        });
    }

    private void loadImage() {
        reference.child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.getValue(String.class);
                    if (imageUrl != null) {
                        Glide.with(userprofile.this).load(imageUrl).into(imageView);
                    } else {
                        Toast.makeText(userprofile.this, "No image URL found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(userprofile.this, "No image URL found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userprofile.this, "Failed to load image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DatabaseError", "Failed to load image: " + error.getMessage());
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        uploadImage(imageUri);
                    } else {
                        Toast.makeText(userprofile.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(userprofile.this, "Failed to pick image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            // Create a reference to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("user_images").child(userId + ".png");

            // Upload the image
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the image URL in Firebase Realtime Database
                            reference.child("profileImageUrl").setValue(uri.toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(userprofile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(userprofile.this, "Failed to save image URL: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(userprofile.this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("StorageError", "Failed to get image URL: " + e.getMessage());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(userprofile.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("StorageError", "Image upload failed: " + e.getMessage());
                    });
        }
    }
}
