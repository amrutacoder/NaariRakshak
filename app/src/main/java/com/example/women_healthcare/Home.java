package com.example.women_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Check if the user is logged in
        if (currentUser == null) {
            // Redirect to Login activity
            Intent intent = new Intent(Home.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.home);
        }
    }

    public void onAddRelativeClick(View view) {
        startActivity(new Intent(this, addrelative.class));
    }

    public void onHelplineClick(View view) {
        startActivity(new Intent(this, emailalter.class));
    }

    public void onDevelopedByClick(View view) {
        startActivity(new Intent(this, RelativesActivity.class));
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
        Intent intent = new Intent(Home.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);

        finish(); // Close current activity
    }
}
