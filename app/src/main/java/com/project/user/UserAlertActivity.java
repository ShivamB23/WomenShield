package com.project.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.util.AppController;
import com.project.util.Keys;
import com.project.util.Loggers;
import com.project.util.SharedPreference;
import com.project.womensafety.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

// to wake up screen
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

// to release screen lock
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