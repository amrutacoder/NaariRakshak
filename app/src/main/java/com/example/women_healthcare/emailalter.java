package com.example.women_healthcare;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class emailalter extends AppCompatActivity {

    private ImageView startServiceButton;
    private FusedLocationProviderClient locationClient;
    private DatabaseReference relativesRef;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    String password="";

    private ArrayList<String> relativeEmails = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        startServiceButton = findViewById(R.id.startServiceButton);
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            relativesRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(currentUser.getUid()).child("Relatives");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Relatives");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot relativeSnapshot : dataSnapshot.getChildren()) {
                        Relative relative = relativeSnapshot.getValue(Relative.class);
                        if (relative != null) {
                            relativeEmails.add(relative.getEmail());
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
        // Fetch relatives' email addresses

        // Button Click Event for sending email alert
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencyAlert();
            }
        });
    }

    private void fetchRelatives() {
        if (relativesRef != null) {
            relativesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    relativeEmails.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String email = data.child("email").getValue(String.class);
                        if (email != null) {
                            relativeEmails.add(email);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error fetching relatives", error.toException());
                }
            });
        }
    }

    private void sendEmergencyAlert() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get Current Location
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String locationUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

//                    // Send Email Alerts
//                    for (String email : relativeEmails) {
//                        sendEmail(email, "Emergency Alert!", "I need help! Here is my location: " + locationUrl);
//                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "null");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("AppPassword");

// Fetch the value from Firebase
                    databaseReference.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String apppassword = task.getResult().getValue(String.class);
                                password=apppassword.toString();
                                Toast.makeText(emailalter.this.getApplicationContext(), "App Password: " + password, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(emailalter.this.getApplicationContext(), "App Password not set!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(emailalter.this.getApplicationContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if(password.toString().trim().length()<10){
                        Toast.makeText(getApplicationContext(), "Please Set Your AppPassword. Go to How To Use Page", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(password);
                        Log.d("password", password);

// Assign the modified string back to password
                        password = password.replace(" ", "");

                        System.out.println(password);
                        Log.d("password", password);

                        SendMail sm=new SendMail(emailalter.this.getApplicationContext(),email,password,relativeEmails,locationUrl);
                        sm.execute();
                    }



                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get location!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(String recipient, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(this, "No email clients installed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencyAlert(); // Retry fetching location
            } else {
                Toast.makeText(this, "Permission denied! Cannot access location.", Toast.LENGTH_SHORT).show();
            }
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
        Intent intent = new Intent(emailalter.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);

        finish(); // Close current activity
    }
}
