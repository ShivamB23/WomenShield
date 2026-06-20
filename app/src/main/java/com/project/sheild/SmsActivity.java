package com.project.sheild;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.textfield.TextInputEditText;
import com.project.womensafety.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.location.Location;
import android.net.Uri;

import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SmsActivity extends AppCompatActivity {

    TextInputEditText txt_pnumber1, txt_msg, txt_pnumber2, txt_pnumber3, txt_pnumber4;
    Button Save, startRecordingButton, stopRecordingButton, helplinebutton;
    FusedLocationProviderClient fusedLocationProviderClient;
    String prevStarted = "yesSms";


    // Media Recorder for audio recording
    private MediaRecorder mediaRecorder;
    private MediaPlayer alarmPlayer;
    private String audioFilePath;
    private boolean isRecording = false;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int SOS_PERMISSION_REQUEST_CODE = 201;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
            showFirstTimeDialog();
        }
    }

    private void showFirstTimeDialog() {
        // Initialize the AlertDialog Builder with the correct context
        AlertDialog.Builder alert = new AlertDialog.Builder(SmsActivity.this);

        // Inflate the custom dialog layout
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        // Find the "Okay" button from the custom dialog layout
        Button btn_okay = mView.findViewById(R.id.btn_okay);

        // Set the custom view for the dialog
        alert.setView(mView);

        // Create the dialog
        final AlertDialog alertDialog = alert.create();

        // Allow dismissing the dialog by tapping outside of it
        alertDialog.setCanceledOnTouchOutside(false);

        // Set the OnClickListener for the "Okay" button to dismiss the dialog
        btn_okay.setOnClickListener(v -> alertDialog.dismiss());

        // Show the dialog
        alertDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        txt_msg = findViewById(R.id.txt_sms);
        txt_pnumber1 = findViewById(R.id.txt_phone_number1);
        txt_pnumber2 = findViewById(R.id.txt_phone_number2);
        txt_pnumber3 = findViewById(R.id.txt_phone_number3);
        txt_pnumber4 = findViewById(R.id.txt_phone_number4);
        Save = findViewById(R.id.Save_btn);
        startRecordingButton = findViewById(R.id.startRecordingButton);
        stopRecordingButton = findViewById(R.id.stopRecordingButton);
        Button viewRecordingsButton = findViewById(R.id.viewRecordingsButton);
        helplinebutton = findViewById(R.id.helplinebutton);
        helplinebutton.setOnClickListener(v -> showHelplineNumbers());







        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        loadSavedData();

        Save.setOnClickListener(v -> saveData());
        checkAndRequestPermissions();

        // Save data listener
        Save.setOnClickListener(v -> saveData());

        // View recordings listener
        viewRecordingsButton.setOnClickListener(v -> listRecordings());

        // Start recording listener
        startRecordingButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                startRecording();
            } else {
                requestPermissions();
            }
        });

        // Stop recording listener
        stopRecordingButton.setOnClickListener(v -> {
            stopRecording();
            stopAlarm();
        });
    }

    // Generate a unique file name using a timestamp
    private String generateFileName() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        return getExternalFilesDir(null).getAbsolutePath() + "/SHEild_Recordings" + timeStamp + ".mp3";
    }

    // List all saved recordings
    private void listRecordings() {
        File directory = getExternalFilesDir(null);
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                StringBuilder fileList = new StringBuilder("Recordings:\n");
                for (File file : files) {
                    fileList.append(file.getName()).append("\n");
                }
                Toast.makeText(this, fileList.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No recordings found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Start recording audio
    private void startRecording() {
        if (isRecording) {
            Toast.makeText(this, "Already recording!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            audioFilePath = generateFileName();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Recording failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Stop recording audio
    private void stopRecording() {
        if (!isRecording) {
            Toast.makeText(this, "No recording to stop!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;

            Toast.makeText(this, "Recording saved to: " + audioFilePath, Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error stopping recording: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }






    private void showHelplineNumbers() {
        String[] helplineNumbers = {
                "Women Helpline: 1091",
                "Women Helpline (Domestic Abuse): 181",
                "National Commission for Women: 011-26942369",
                "Delhi Commission for Women: 011-23379181",
                "Child Helpline: 1098",
                "Police: 100",
                "Ambulance: 102",
                "National Emergency Number: 112",
                "Fire: 101",
                "Senior Citizen Helpline: 14567",
                "Cyber Crime Helpline: 1930",
                "Railway Protection Force: 182",
                "Railway Enquiry: 139",
                "Anti-Stalking Helpline: 1096",
                "Missing Child And Women: 1094",
                "Tourist Helpline: 1363",
                "Sakhi One Stop Centre: 7827170170",
                "SHEROES Helpline for Women: 8800454499",
                "Emergency Response Support System: 112",
                "Road Accident Emergency Service: 1073",
                "National Human Rights Commission: 011-24663333",
                "Anti-Poison: 1066",
                "AIDS Helpline: 1097",
                "Disaster Management Services: 108",
                "Air Ambulance: 9540161344",
                "LPG Leak Helpline: 1906",
                "Kisan Call Centre: 1551",
                "Railway Accident Emergency Services: 1072",
                "Road Accident Emergency Service on National Highways: 1033",
                "Relief Commissioner for Natural Calamities: 1070",
                "Earthquake/Flood/Disaster (N.D.R.F Headquarters): 011-24363260, 9711077372",
                "Central Vigilance Commission: 1964",
                "Arogyavani Health Helpline: 104",
                "Tele-MANAS (Mental Health Support): 14416",
                "AASRA (Mental Health Support): 91-9820466726",
                "Muktaa Mental Health Helpline: 0788-788-9882",
                "Samaritans Mumbai: +91 8422984530",
                "Vandrevala Foundation Helpline: +91 9999666555",
                "Snehi Suicide Prevention Helpline: 91-22-27546669"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SmsActivity.this);
        builder.setTitle("Helpline Numbers in India")
                .setItems(helplineNumbers, (dialog, which) -> {
                    // Extract the number from the selected item
                    String selectedNumber = helplineNumbers[which].split(": ")[1];

                    // Dial the selected helpline number
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + selectedNumber));
                    startActivity(intent);
                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }













    // Check if permissions are granted
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    // Request necessary permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO
                },
                PERMISSION_REQUEST_CODE);
    }

    // Check and request permissions at runtime
    private void checkAndRequestPermissions() {
        if (!checkPermissions()) {
            requestPermissions();
        }
    }




    private void loadSavedData() {
        SharedPreferences getShared = getSharedPreferences("demo", MODE_PRIVATE);
        txt_pnumber1.setText(getShared.getString("phone1", ""));
        txt_pnumber2.setText(getShared.getString("phone2", ""));
        txt_pnumber3.setText(getShared.getString("phone3", ""));
        txt_pnumber4.setText(getShared.getString("phone4", ""));
        txt_msg.setText(getShared.getString("msg", "I am in danger, please come fast..."));
    }

    private void saveData() {
        String phone1 = txt_pnumber1.getText().toString();
        String phone2 = txt_pnumber2.getText().toString();
        String phone3 = txt_pnumber3.getText().toString();
        String phone4 = txt_pnumber4.getText().toString();
        String msg = txt_msg.getText().toString();

        SharedPreferences shrd = getSharedPreferences("demo", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.putString("phone1", phone1);
        editor.putString("phone2", phone2);
        editor.putString("phone3", phone3);
        editor.putString("phone4", phone4);
        editor.putString("msg", msg);
        editor.apply();

        Toast.makeText(SmsActivity.this, "Saved...", Toast.LENGTH_SHORT).show();
    }

    public void btn_send(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Activate emergency SOS?")
                .setMessage("This starts the alarm and microphone recording, gets GPS location, sends SMS alerts, and calls the primary contact.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Activate SOS", (dialog, which) -> tryIt())
                .show();
    }

    public void tryIt() {
        if (!txt_pnumber1.getText().toString().trim().isEmpty()) {
            requestPermissionsAndCall();
        } else {
            Toast.makeText(this, "Please enter the first number...", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermissionsAndCall() {
        String[] permissions = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO
        };
        ArrayList<String> missing = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                missing.add(permission);
            }
        }
        if (missing.isEmpty()) {
            activateSos();
        } else {
            ActivityCompat.requestPermissions(this, missing.toArray(new String[0]), SOS_PERMISSION_REQUEST_CODE);
        }
    }

    private void activateSos() {
        saveData();
        startAlarm();
        if (!isRecording) {
            startRecording();
        }
        SendLocationMessage();
        makeCall();
    }

    private void startAlarm() {
        if (alarmPlayer == null) {
            alarmPlayer = MediaPlayer.create(this, R.raw.police_siren);
            alarmPlayer.setLooping(true);
        }
        if (!alarmPlayer.isPlaying()) {
            alarmPlayer.start();
        }
    }

    private void stopAlarm() {
        if (alarmPlayer != null) {
            alarmPlayer.stop();
            alarmPlayer.release();
            alarmPlayer = null;
        }
    }

    private void requestLocationPermissionAndSendMessage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            SendLocationMessage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void makeCall() {
        String phoneNumber = txt_pnumber1.getText().toString();
        if (!phoneNumber.trim().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } else {
            Toast.makeText(SmsActivity.this, "Please enter a number to make a call.", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendLocationMessage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        CancellationTokenSource tokenSource = new CancellationTokenSource();
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.getToken())
                .addOnCompleteListener(task -> {
            Location location = task.isSuccessful() ? task.getResult() : null;
            String message = txt_msg.getText().toString().trim();

            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                String shortLink = "https://maps.google.com/?q=" + lat + "," + lng; // Short link can also be created via a URL shortening service

                // Shortened message
                message += " Help! Location: " + shortLink;
            } else {
                message += " Unable to retrieve location.";
            }
            message += " | " + getDeviceContext() + " | SHEild";
            sendSmsToNumbers(message);
        });
    }

    private String getDeviceContext() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager == null
                ? null : connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        String network = "offline";
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                network = "Wi-Fi";
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                network = "cellular";
            } else {
                network = "connected";
            }
        }

        SensorManager sensors = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        boolean motion = sensors != null && sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
        boolean magnetic = sensors != null && sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null;
        return "Network: " + network + ", sensors: motion=" + motion + ", magnetic=" + magnetic;
    }

    private void sendSmsToNumbers(String message) {
        String[] phoneNumbers = {
                txt_pnumber1.getText().toString().trim(),
                txt_pnumber2.getText().toString().trim(),
                txt_pnumber3.getText().toString().trim(),
                txt_pnumber4.getText().toString().trim()
        };

        for (String phoneNumber : phoneNumbers) {
            if (!phoneNumber.isEmpty()) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(message);
                    smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
                    Toast.makeText(SmsActivity.this, "Message sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
                } catch (RuntimeException error) {
                    Toast.makeText(this, "Could not send SMS to " + phoneNumber, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case SOS_PERMISSION_REQUEST_CODE:
                    if (allPermissionsGranted(grantResults)) {
                        activateSos();
                    } else {
                        Toast.makeText(this, "SOS needs location, SMS, phone, and microphone permissions", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 0: // SEND_SMS
                case 1: // CALL_PHONE
                    tryIt(); // Retry sending SMS or making a call
                    break;
                case 44: // ACCESS_FINE_LOCATION
                    SendLocationMessage(); // Retry sending location message
                    break;
                case PERMISSION_REQUEST_CODE: // General permissions (e.g., RECORD_AUDIO, STORAGE)
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            switch (requestCode) {
                case 0: // SEND_SMS
                case 1: // CALL_PHONE
                case 44: // ACCESS_FINE_LOCATION
                case PERMISSION_REQUEST_CODE: // General permissions
                    Toast.makeText(this, "You don't have required permissions", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private boolean allPermissionsGranted(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mediaRecorder != null) {
            if (isRecording) {
                try {
                    mediaRecorder.stop();
                } catch (RuntimeException ignored) {
                    // A very short recording may not contain enough data to stop cleanly.
                }
            }
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
        stopAlarm();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructions_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Detailed_instructions) {
            startActivity(new Intent(getApplicationContext(), SosInsructionsActivity.class));
            return true;
        } else if (id == R.id.action_Short_instructions) {
            showShortInstructionsDialog();
            return true;
        } else {
            return false;
        }
    }



    private void showShortInstructionsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SmsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        Button btn_okay = mView.findViewById(R.id.btn_okay);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_okay.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle broadcast receiver logic
        }
    };
}
