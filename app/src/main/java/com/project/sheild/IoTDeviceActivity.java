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

import java.util.Random;

public class IoTDeviceActivity extends AppCompatActivity {

    private TextView tvConnectionStatus;
    private TextView tvDeviceBattery;
    private TextView tvGpsLat;
    private TextView tvGpsLng;
    private Button btnConnect;
    private Button btnGetGps;
    private ImageView btnBack;

    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_device);

        tvConnectionStatus = findViewById(R.id.tv_connection_status);
        tvDeviceBattery = findViewById(R.id.tv_device_battery);
        tvGpsLat = findViewById(R.id.tv_gps_lat);
        tvGpsLng = findViewById(R.id.tv_gps_lng);
        btnConnect = findViewById(R.id.btn_connect);
        btnGetGps = findViewById(R.id.btn_get_gps);
        btnBack = findViewById(R.id.btn_back);

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
                simulateGpsData();
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
            tvDeviceBattery.setText("Battery: 85%");
            btnConnect.setText("Disconnect");
            btnConnect.setEnabled(true);
            Toast.makeText(IoTDeviceActivity.this, "IoT Device Connected Successfully", Toast.LENGTH_SHORT).show();
        }, 1500);
    }

    private void disconnectDevice() {
        isConnected = false;
        tvConnectionStatus.setText("Status: Disconnected");
        tvConnectionStatus.setTextColor(getResources().getColor(R.color.theme_white));
        tvDeviceBattery.setText("Battery: --%");
        btnConnect.setText("Simulate Connection");
        tvGpsLat.setText("Latitude: Waiting...");
        tvGpsLng.setText("Longitude: Waiting...");
    }

    private void simulateGpsData() {
        tvGpsLat.setText("Latitude: Fetching...");
        tvGpsLng.setText("Longitude: Fetching...");
        btnGetGps.setEnabled(false);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Random random = new Random();
            // Simulating coordinates around India
            double lat = 20.0 + (random.nextDouble() * 5.0);
            double lng = 77.0 + (random.nextDouble() * 5.0);

            tvGpsLat.setText(String.format("Latitude: %.6f", lat));
            tvGpsLng.setText(String.format("Longitude: %.6f", lng));
            btnGetGps.setEnabled(true);
        }, 1000);
    }
}
