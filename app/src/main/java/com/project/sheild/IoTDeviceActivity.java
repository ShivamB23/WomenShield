package com.project.sheild;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.womensafety.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import java.util.Locale;

public class IoTDeviceActivity extends AppCompatActivity {

    private TextView tvConnectionStatus;
    private TextView tvGpsLat;
    private TextView tvGpsLng;
    private Button btnConnect;
    private Button btnGetGps;
    private ImageView btnBack;

    private boolean isConnected = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_device);

        tvConnectionStatus = findViewById(R.id.tv_connection_status);
        tvGpsLat = findViewById(R.id.tv_gps_lat);
        tvGpsLng = findViewById(R.id.tv_gps_lng);
        btnConnect = findViewById(R.id.btn_connect);
        btnGetGps = findViewById(R.id.btn_get_gps);
        btnBack = findViewById(R.id.btn_back);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnBack.setOnClickListener(v -> finish());

        btnConnect.setOnClickListener(v -> {
            if (!isConnected) {
                simulateConnection();
            } else {
                disconnectDevice();
            }
        });

        btnGetGps.setOnClickListener(v -> {
            if (isConnected) {
                fetchGpsData();
            } else {
                Toast.makeText(IoTDeviceActivity.this, "Please connect the device first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simulateConnection() {
        tvConnectionStatus.setText("Status: Connecting...");
        btnConnect.setEnabled(false);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isConnected = true;
            tvConnectionStatus.setText("Status: Connected");
            tvConnectionStatus.setTextColor(getResources().getColor(R.color.theme_red));
            btnConnect.setText("Disconnect");
            btnConnect.setEnabled(true);
            Toast.makeText(IoTDeviceActivity.this, "IoT Device Connected Successfully", Toast.LENGTH_SHORT).show();
        }, 1500);
    }

    private void disconnectDevice() {
        isConnected = false;
        tvConnectionStatus.setText("Status: Disconnected");
        tvConnectionStatus.setTextColor(getResources().getColor(R.color.theme_white));
        btnConnect.setText("Simulate Connection");
        tvGpsLat.setText("Latitude: Waiting...");
        tvGpsLng.setText("Longitude: Waiting...");
    }

    private void fetchGpsData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        tvGpsLat.setText("Latitude: Fetching...");
        tvGpsLng.setText("Longitude: Fetching...");
        btnGetGps.setEnabled(false);

        CancellationTokenSource tokenSource = new CancellationTokenSource();
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.getToken())
                .addOnCompleteListener(this, task -> {
                    Location location = task.isSuccessful() ? task.getResult() : null;
                    if (location != null) {
                        tvGpsLat.setText(String.format(Locale.ENGLISH, "Latitude: %.6f", location.getLatitude()));
                        tvGpsLng.setText(String.format(Locale.ENGLISH, "Longitude: %.6f", location.getLongitude()));
                    } else {
                        tvGpsLat.setText("Latitude: Unavailable");
                        tvGpsLng.setText("Longitude: Unavailable");
                        Toast.makeText(IoTDeviceActivity.this, "Failed to get real GPS data", Toast.LENGTH_SHORT).show();
                    }
                    btnGetGps.setEnabled(true);
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchGpsData();
            } else {
                Toast.makeText(this, "Location permission is required to fetch GPS data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
