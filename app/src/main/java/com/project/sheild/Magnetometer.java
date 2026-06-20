package com.project.sheild;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.womensafety.R;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import static java.lang.Math.sqrt;

public class Magnetometer extends AppCompatActivity implements SensorEventListener {

    private TextView magR, show_conditions, x_cor, y_cor, z_cor;
    SpeedometerView Speed;
    private Sensor magnetometer, accelerometer;
    private SensorManager sensorManager;
    MediaPlayer mediaPlayer;

    private double magD;
    private boolean movementDetected = false;
    private Queue<Double> magnetometerReadings = new LinkedList<>();
    private final int WINDOW_SIZE = 5;

    private AudioRecord audioRecord;
    private int bufferSize;
    private boolean isRecording = false;
    private static final int SAMPLE_RATE = 44100;
    private static final int TARGET_FREQUENCY_50HZ = 50;
    private static final int TARGET_FREQUENCY_60HZ = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.magnetoInst);
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MagBtnInst.class)));

//        Speed = findViewById(R.id.speedometer);
        Speed = (SpeedometerView)findViewById(R.id.speedometer);
        Speed.setLabelConverter(new SpeedometerView.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        Speed.setMaxSpeed(100);

        Speed.setMajorTickStep(10);
        Speed.setMinorTicks(0);

        // Configure speedometer color ranges
        Speed.addColoredRange(0, 10, Color.GREEN);
        Speed.addColoredRange(10, 20, Color.YELLOW);
        Speed.addColoredRange(20, 30, Color.RED);
        Speed.addColoredRange(30, 40, Color.GREEN);
        Speed.addColoredRange(40, 50, Color.YELLOW);
        Speed.addColoredRange(50, 60, Color.RED);
        Speed.addColoredRange(60, 70, Color.GREEN);
        Speed.addColoredRange(70, 80, Color.YELLOW);
        Speed.addColoredRange(80, 90, Color.RED);
        Speed.addColoredRange(90, 100, Color.GREEN);

        magR = findViewById(R.id.value);
        show_conditions = findViewById(R.id.show_conditions);
        x_cor = findViewById(R.id.x_cor);
        y_cor = findViewById(R.id.y_cor);
        z_cor = findViewById(R.id.z_cor);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        } else {
            magR.setText("Magnetometer not Supported");
        }

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        startMicrophoneDetection();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0], y = event.values[1], z = event.values[2];
            double acceleration = sqrt(x * x + y * y + z * z);
            movementDetected = acceleration > 1.2; // Detect movement threshold
        }

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            double x = event.values[0], y = event.values[1], z = event.values[2];
            double fieldStrength = sqrt(x * x + y * y + z * z);

            // Apply moving average filter to smooth out noise
            if (magnetometerReadings.size() >= WINDOW_SIZE) {
                magnetometerReadings.poll();
            }
            magnetometerReadings.add(fieldStrength);
            double filteredField = magnetometerReadings.stream().mapToDouble(val -> val).average().orElse(fieldStrength);

            BigDecimal bd = new BigDecimal(filteredField).setScale(0, RoundingMode.HALF_UP);
            double newx = bd.doubleValue();

            if (newx > 0 && newx < 100) {
                Speed.setSpeed(newx, 1, 1);
            } else if (newx >= 100) {
                Speed.setSpeed(100, 1, 1);
            }

            magR.setText(newx + " μT");
            x_cor.setText(String.valueOf((int) x));
            x_cor.setBackgroundColor( Color.GREEN );
            y_cor.setText(String.valueOf((int) y));
            y_cor.setBackgroundColor( Color.RED );
            z_cor.setText(String.valueOf((int) z));
            z_cor.setBackgroundColor( Color.YELLOW );

            // Adjust detection threshold based on movement
            double detectionThreshold = movementDetected ? 80 : 50;

            if (filteredField > detectionThreshold) {
                mediaPlayer = MediaPlayer.create(this, R.raw.beep);
                mediaPlayer.start();
                show_conditions.setText("Electronic device detected nearby!");
            } else {
                show_conditions.setText("No electronic device detected.");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void startMicrophoneDetection() {
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        isRecording = true;

        new Thread(() -> {
            audioRecord.startRecording();
            short[] buffer = new short[bufferSize];

            while (isRecording) {
                audioRecord.read(buffer, 0, buffer.length);
                double detectedFrequency = getDominantFrequency(buffer);

                if (Math.abs(detectedFrequency - TARGET_FREQUENCY_50HZ) < 5 || Math.abs(detectedFrequency - TARGET_FREQUENCY_60HZ) < 5) {
                    runOnUiThread(() -> show_conditions.setText("Power hum detected! Electronic device likely."));
                }
            }
        }).start();
    }

    private double getDominantFrequency(short[] buffer) {
        double maxAmplitude = 0;
        int maxIndex = 0;

        for (int i = 0; i < buffer.length; i++) {
            if (Math.abs(buffer[i]) > maxAmplitude) {
                maxAmplitude = Math.abs(buffer[i]);
                maxIndex = i;
            }
        }

        return (double) maxIndex * SAMPLE_RATE / buffer.length;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
    }
}