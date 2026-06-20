package com.project.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.util.AppController;
import com.project.util.SharedPreference;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

public class UserDashboardActivity extends AppCompatActivity {

    ProgressBar pBar;
    Button btnOpenLocation;
    LinearLayout llayout;
    TextView tvData;

    String locationData;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());

        pBar = findViewById(R.id.progressBar);
        btnOpenLocation = findViewById(R.id.buttonOpenLocation);
        llayout = findViewById(R.id.linearLayout);
        tvData = findViewById(R.id.textViewUData);

        getUserLocation();

        btnOpenLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationData == null || locationData.trim().isEmpty()) {
                    Toast.makeText(UserDashboardActivity.this, "Location is not available yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Universal Google Maps search URL - opens in native Google Maps app if installed, or web browser if not
                String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(locationData);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                startActivity(i);
            }
        });
    }

    private void getUserLocation() {
        pBar.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED 
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
            .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    pBar.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        String lat = String.valueOf(location.getLatitude());
                        String lng = String.valueOf(location.getLongitude());
                        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                        
                        String userData = "Location :- " + lat + "," + lng;
                        userData += "\nTime :- " + time;
                        locationData = lat + "," + lng;
                        
                        tvData.setText(userData);
                        llayout.setVisibility(View.VISIBLE);
                        btnOpenLocation.setVisibility(View.VISIBLE);
                        
                        Toast.makeText(UserDashboardActivity.this, "Location loaded from device", Toast.LENGTH_SHORT).show();
                    } else {
                        useMockLocation("Location service unavailable. Loaded mock coordinates.");
                    }
                }
            });
    }

    private void useMockLocation(String message) {
        String lat = "28.6139";
        String lng = "77.2090";
        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
        
        String userData = "Location :- " + lat + "," + lng;
        userData += "\nTime :- " + time;
        locationData = lat + "," + lng;
        
        tvData.setText(userData);
        llayout.setVisibility(View.VISIBLE);
        btnOpenLocation.setVisibility(View.VISIBLE);
        
        Toast.makeText(UserDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                useMockLocation("Permission denied. Loaded mock coordinates.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            userLogout();
        }else if(item.getItemId() == R.id.menuRefresh){
            getUserLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void userLogout() {
        pBar.setVisibility(View.VISIBLE);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.GONE);
                userLogoutProceed();
            }
        }, 500);
    }


    private void userLogoutProceed() {
        SharedPreference.removeKey("u_id");
        SharedPreference.removeKey("u_name");
        SharedPreference.removeKey("u_email");
        SharedPreference.removeKey("u_phone");
        SharedPreference.removeKey("u_password");
        SharedPreference.removeKey("u_address");
        SharedPreference.removeKey("u_relative_one");
        SharedPreference.removeKey("u_relative_two");
        SharedPreference.removeKey("u_relative_three");

        Intent i = new Intent(UserDashboardActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showError(String message) {
        pBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
