package com.project.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.util.AppController;
import com.project.util.Keys;
import com.project.util.SharedPreference;
import com.project.womensafety.R;

public class UserAlertActivity extends AppCompatActivity {

    MediaPlayer mp;
    ImageView ivClose;
    TextView tvATitle, tvAMsg;
    Button btnDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        setContentView(R.layout.activity_user_alert);

        SharedPreference.initialize(getApplicationContext());
        AppController.initialize(getApplicationContext());

        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        mp.start();

        ivClose = findViewById(R.id.imageViewClose);
        tvATitle = findViewById(R.id.textViewAlertTitle);
        tvAMsg = findViewById(R.id.textViewAlertMessage);
        btnDismiss = findViewById(R.id.buttonDismiss);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp != null && mp.isPlaying())
                    mp.stop();
                finish();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp != null && mp.isPlaying())
                    mp.stop();
                finish();
            }
        });

        getUserLocation();
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED 
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            // Default fallback if no permissions granted
            useMockLocation();
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
            .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        String lat = String.valueOf(location.getLatitude());
                        String lng = String.valueOf(location.getLongitude());
                        String locationData = "http://maps.google.com/maps?q=loc:" + lat + "," + lng;
                        sendAlertSMS("I am in danger. Please checkout my location at " + locationData);
                        callUser();
                    } else {
                        useMockLocation();
                    }
                }
            });
    }

    private void useMockLocation() {
        String lat = "28.6139";
        String lng = "77.2090";
        String locationData = "http://maps.google.com/maps?q=loc:" + lat + "," + lng;
        sendAlertSMS("I am in danger. Please checkout my location at " + locationData);
        callUser();
    }

    private void sendAlertSMS(String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(SharedPreference.get("u_relative_one"), null, message, null, null);
        smsManager.sendTextMessage(SharedPreference.get("u_relative_two"), null, message, null, null);
        smsManager.sendTextMessage(SharedPreference.get("u_relative_three"), null, message, null, null);
        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
    }

    private void callUser() {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + SharedPreference.get("u_relative_one")));
        startActivity(i);
    }
}