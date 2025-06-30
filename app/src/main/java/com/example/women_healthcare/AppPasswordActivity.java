package com.example.women_healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class AppPasswordActivity extends AppCompatActivity {

    private VideoView tutorialVideoView;
    private EditText appPasswordEditText;
    private Button submitPasswordButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_password);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user is not logged in
            return;
        }

        // Get database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("AppPassword");

        // Initialize UI components
        tutorialVideoView = findViewById(R.id.tutorialVideoView);
        appPasswordEditText = findViewById(R.id.appPasswordEditText);
        submitPasswordButton = findViewById(R.id.submitPasswordButton);

        // Play tutorial video
        playVideoFromAssets();

        // Handle submit button click
        submitPasswordButton.setOnClickListener(v -> saveAppPassword());
    }

    private void playVideoFromAssets() {
        try {
            // Set the path to the video from assets folder
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.tutorial;
            tutorialVideoView.setVideoURI(Uri.parse(videoPath));

            // Add a built-in MediaController for play/pause/seek
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(tutorialVideoView);
            tutorialVideoView.setMediaController(mediaController);

            // Start the video
            tutorialVideoView.start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing video.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onAddRelativeClick(View view) {
        startActivity(new Intent(this, addrelative.class));
    }

    public void onHelplineClick(View view) {
        startActivity(new Intent(this, emailalter.class));
    }

    public void onDevelopedByClick(View view) {
        //startActivity(new Intent(this, DevelopedByActivity.class));
    }

    public void onStartServiceClick(View view) {

        startActivity(new Intent(this, demo.class));
    }

    public void onHowToUseClick(View view) {
        startActivity(new Intent(this, AppPasswordActivity.class));
    }

    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut(); // Sign out the user from Firebase

        // Redirect to Login Activity
        Intent intent = new Intent(AppPasswordActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);

        finish(); // Close current activity
    }
    private void saveAppPassword() {
        String appPassword = appPasswordEditText.getText().toString().trim();

        if (appPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your app password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the app password to Firebase Realtime Database
        databaseReference.setValue(appPassword)
                .addOnSuccessListener(avoid->{Toast.makeText(AppPasswordActivity.this, "App password saved successfully!", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppPassowrd",appPassword);
        editor.apply();})
                .addOnFailureListener(e -> Toast.makeText(AppPasswordActivity.this, "Failed to save app password.", Toast.LENGTH_SHORT).show());
    }
}
