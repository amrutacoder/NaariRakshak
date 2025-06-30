package com.example.women_healthcare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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

public class startactivity extends AppCompatActivity {

    private ImageView startServiceButton;
    private FusedLocationProviderClient locationClient;
    private DatabaseReference relativesRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ArrayList<String> relativePhones = new ArrayList<>();
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
            relativesRef = FirebaseDatabase.getInstance().getReference("Relatives").child(currentUser.getUid());
        }

        // Fetch Relatives' Phone Numbers
        fetchRelatives();

        // Button Click Event - Start Emergency Alert
        startServiceButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                sendEmergencyAlert(); // Directly fetch location and send message
            }
        });
    }

    private void fetchRelatives() {
        if (relativesRef != null) {
            relativesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    relativePhones.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String phone = data.child("phone").getValue(String.class);
                        if (phone != null) {
                            relativePhones.add(phone);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String locationLink = "I'm in an emergency! My location: https://maps.google.com/?q=" + latitude + "," + longitude;

                // Send the location to all relatives via WhatsApp
                for (String phone : relativePhones) {
                    sendWhatsAppMessage(phone, locationLink);
                }

                Toast.makeText(getApplicationContext(), "Emergency alert sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendWhatsAppMessage(String phone, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + "9322807980" + "&text=" + message));
            intent.setPackage("com.whatsapp"); // Ensures only WhatsApp handles this intent
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencyAlert();
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
        //startActivity(new Intent(this, DevelopedByActivity.class));
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
}
