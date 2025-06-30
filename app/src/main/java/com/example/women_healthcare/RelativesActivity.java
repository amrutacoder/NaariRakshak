package com.example.women_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RelativesActivity extends AppCompatActivity {

    private DatabaseReference relativesRef;
    private RecyclerView recyclerView;
    private RelativeManager adapter;
    private List<Relative> relativeList;

    private EditText nameInput, phoneInput, emailInput;
    private Button addButton, updateButton, deleteButton;
    private String currentUserId, selectedId = null; // To track selected item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativesxml);

        // Get the current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        relativesRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Relatives");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        relativeList = new ArrayList<>();
        adapter = new RelativeManager(this, relativeList, this::selectRelative); // Passing callback
        recyclerView.setAdapter(adapter);

        fetchRelatives();
        updateButton.setOnClickListener(v -> updateRelative());
        deleteButton.setOnClickListener(v -> deleteRelative());
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
        // Implement logout logic
        finish();
    }
    private void fetchRelatives() {
        relativesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                relativeList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Relative relative = data.getValue(Relative.class);
                    if (relative != null) {
                        relativeList.add(relative);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RelativesActivity.this, "Failed to load relatives!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateRelative() {
        if (selectedId == null) {
            Toast.makeText(this, "Select a relative first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();

        Relative updatedRelative = new Relative(selectedId, name, phone, email);
        relativesRef.child(selectedId).setValue(updatedRelative)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RelativesActivity.this, "Relative updated!", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(RelativesActivity.this, "Failed to update relative!", Toast.LENGTH_SHORT).show()
                );
    }

    private void deleteRelative() {
        if (selectedId == null) {
            Toast.makeText(this, "Select a relative first!", Toast.LENGTH_SHORT).show();
            return;
        }

        relativesRef.child(selectedId).removeValue()
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(RelativesActivity.this, "Relative deleted!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(RelativesActivity.this, "Failed to delete relative!", Toast.LENGTH_SHORT).show()
                );

        clearFields();
    }

    private void selectRelative(Relative relative) {
        nameInput.setText(relative.getName());
        phoneInput.setText(relative.getPhone());
        emailInput.setText(relative.getEmail());
        selectedId = relative.getId();
    }

    private void clearFields() {
        nameInput.setText("");
        phoneInput.setText("");
        emailInput.setText("");
        selectedId = null;
    }
}
