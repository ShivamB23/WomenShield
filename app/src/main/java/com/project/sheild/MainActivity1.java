package com.project.sheild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.admin.AdminLoginActivity;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

public class MainActivity1 extends AppCompatActivity {

    private View siren;
    private CardView settings, currentLocation, news, aboutUs, therapy, fakecall, iotModule;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            Intent i = new Intent(MainActivity1.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Start the background service for screen on/off detection
        Intent backgroundService = new Intent(getApplicationContext(), ScreenOnOffBackgroundService.class);
        startService(backgroundService);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "Activity onCreate");

        // Initialize UI components
        initializeUI();

        // Check for necessary permissions and request if not granted
        checkAndRequestPermissions();
    }

    private void initializeUI() {
        siren = findViewById(R.id.Siren);
//        location = findViewById(R.id.send_Location);
        settings = findViewById(R.id.Settings);
        currentLocation = findViewById(R.id.Currentlocation);
        news = findViewById(R.id.News);
        aboutUs = findViewById(R.id.about_us);
        therapy = findViewById(R.id.therapy);
        fakecall = findViewById(R.id.fakecall);
        iotModule = findViewById(R.id.iot_module);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle back button action
//                onBackPressed(); // This will take you to the previous screen
//            }
//        });


        // Set click listeners for each feature
        siren.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Flashing.class)));
//        location.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Instructions.class)));
        settings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SmsActivity.class)));
        currentLocation.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChoosenActivity.class)));
        news.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewsActivity.class)));
        aboutUs.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AboutUs.class)));
        therapy.setOnClickListener(v -> startActivity(new Intent(MainActivity1.this, SukoonWebViewActivity.class)));
        fakecall.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FakeCall.class)));
        iotModule.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), IoTDeviceActivity.class)));

    }

    private void checkAndRequestPermissions() {
        int smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (smsPermission != PackageManager.PERMISSION_GRANTED ||
                locationPermission != PackageManager.PERMISSION_GRANTED ||
                callPermission != PackageManager.PERMISSION_GRANTED) {

            // Show custom dialog for permission explanation and request
            showPermissionRequestDialog();
        }
    }

    private void showPermissionRequestDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_mainactivity, null);
        alert.setView(dialogView);

        TextView heading = dialogView.findViewById(R.id.heading);
        heading.setText("SHEild needs access to");

        // Setting up the dialog content
        setupDialogContent(dialogView);

        Button btnOkay = dialogView.findViewById(R.id.btn_okay);
        CheckBox checkbox = dialogView.findViewById(R.id.checkBox);
        TextView checkBoxText = dialogView.findViewById(R.id.checkBoxText);

        // Make privacy policy clickable
        checkBoxText.setText(Html.fromHtml("I accept the " +
                "<a href='https://www.websitepolicies.com/policies/view/IaK4RjyB'>PRIVACY POLICY</a> of the app"));
        checkBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnOkay.setOnClickListener(v -> {
            if (checkbox.isChecked()) {
                alertDialog.dismiss();
                ActivityCompat.requestPermissions(MainActivity1.this,
                        new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE);
            } else {
                Toast.makeText(MainActivity1.this, "Please accept the privacy policy", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }

    private void setupDialogContent(View dialogView) {
        TextView smsPermissionText = dialogView.findViewById(R.id.sms);
        TextView smsDescription = dialogView.findViewById(R.id.textFormodal);
        smsPermissionText.setText("Sending SMS:");
        smsDescription.setText("Emergency messaging requires SEND SMS permission");

        TextView locationPermissionText = dialogView.findViewById(R.id.location);
        TextView locationDescription = dialogView.findViewById(R.id.textLocation);
        locationPermissionText.setText("Location:");
        locationDescription.setText("Sending live location requires Location permission");

        TextView callPermissionText = dialogView.findViewById(R.id.call);
        TextView callDescription = dialogView.findViewById(R.id.textCall);
        callPermissionText.setText("Phone Call:");
        callDescription.setText("Emergency calling requires CALL PHONE permission");

        TextView declarationText = dialogView.findViewById(R.id.declaration);
        TextView declarationDescription = dialogView.findViewById(R.id.textDeclaration);
        declarationText.setText("Declaration");
        declarationDescription.setText("The app is developed by Aditya Konda, and all data is stored locally on your phone.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions are required for full functionality", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBackToMainActivity(View view) {
        Intent intent = new Intent(MainActivity1.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
