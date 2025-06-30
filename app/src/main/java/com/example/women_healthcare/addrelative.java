package com.example.women_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addrelative extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
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
        Intent intent = new Intent(addrelative.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);

        finish(); // Close current activity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrelative);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user is not logged in
            return;
        }


        // Get database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Relatives");
        fetchRelatives();
        // Initialize UI components
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        emailEditText = findViewById(R.id.editTextEmail);
        saveButton = findViewById(R.id.buttonSave);

        // Save Button Click Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRelative();
            }
        });
    }

    private void saveRelative() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique ID for the relative
        String relativeId = databaseReference.push().getKey();

        // Create Relative object
        Relative relative = new Relative(relativeId, name, phone, email);

        // Save data under the logged-in user's ID
        databaseReference.child(relativeId).setValue(relative)
                .addOnSuccessListener(aVoid -> Toast.makeText(addrelative.this, "Relative Added Successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(addrelative.this, "Failed to Add Relative", Toast.LENGTH_SHORT).show());
        fetchRelatives();
    }

    private void fetchRelatives() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot relativeSnapshot : dataSnapshot.getChildren()) {
                        Relative relative = relativeSnapshot.getValue(Relative.class);
                        if (relative != null) {
                            System.out.println("Email: " + relative.getEmail());
                        }
                    }
                } else {
                    System.out.println("No relatives found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error fetching data: " + databaseError.getMessage());
            }
        });
    }


}
