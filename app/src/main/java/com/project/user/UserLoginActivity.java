package com.project.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.project.util.Validator;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends AppCompatActivity {

    Button btnULogin;
    ProgressBar pBar;
    EditText edtUPhone, edtUPass;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            SharedPreference.removeKey("u_id");
            SharedPreference.removeKey("u_name");
            SharedPreference.removeKey("u_email");
            SharedPreference.removeKey("u_phone");
            SharedPreference.removeKey("u_password");

            Intent i = new Intent(UserLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        edtUPass = findViewById(R.id.editTextUPassword);
        edtUPhone = findViewById(R.id.editTextUPhone);
        btnULogin = findViewById(R.id.buttonULogin);
        pBar = findViewById(R.id.progressBar);

        btnULogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_phone = edtUPhone.getText().toString().trim();
                String u_password = edtUPass.getText().toString().trim();

                if (u_phone.isEmpty() || u_password.isEmpty()) {
                    Toast.makeText(UserLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else if (!Validator.isValidPhone(u_phone)) {
                    edtUPhone.setError("Please enter a valid 10-digit phone number");
                } else if (!Validator.isValidPassword(u_password)) {
                    edtUPass.setError("Password must be at least 6 characters");
                } else if (checkPermissonStatus()) {
                    userLogin(u_phone, u_password);
                } else {
                    Toast.makeText(UserLoginActivity.this, "Please accept required permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermissonStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 789);
                    return false;
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 456);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        askNotificationPermission();
        Loggers.i(SharedPreference.get(Keys.FireKey.F_TOKEN));
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
            } else
                Toast.makeText(this, "Notification allowed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (isGranted(grantResults))
                    Toast.makeText(this, "Notification permission accepted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Application needs notification permission to get important alerts", Toast.LENGTH_SHORT).show();
                break;
            case 456:
                if (isGranted(grantResults)) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 789);
                } else
                    Toast.makeText(this, "Application needs SEND SMS permission", Toast.LENGTH_SHORT).show();
                break;
            case 789:
                if (isGranted(grantResults))
                    Toast.makeText(this, "Call Phone permission accepted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Application needs Call Phone permission", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean isGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void userLogin(String uPhone, String uPassword) {
        btnULogin.setEnabled(false);
        pBar.setVisibility(View.VISIBLE);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.GONE);

                String storedPassword = SharedPreference.get(uPhone + "_password");
                if (storedPassword != null) {
                    if (storedPassword.equals(uPassword)) {
                        SharedPreference.save("u_id", "2");
                        SharedPreference.save("u_name", SharedPreference.get(uPhone + "_name"));
                        SharedPreference.save("u_email", SharedPreference.get(uPhone + "_email"));
                        SharedPreference.save("u_phone", uPhone);
                        SharedPreference.save("u_password", uPassword);
                        SharedPreference.save("u_address", SharedPreference.get(uPhone + "_address"));
                        SharedPreference.save("u_relative_one", SharedPreference.get(uPhone + "_relative_one"));
                        SharedPreference.save("u_relative_two", SharedPreference.get(uPhone + "_relative_two"));
                        SharedPreference.save("u_relative_three", SharedPreference.get(uPhone + "_relative_three"));

                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(UserLoginActivity.this, UserDashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        btnULogin.setEnabled(true);
                        Toast.makeText(UserLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    btnULogin.setEnabled(true);
                    Toast.makeText(UserLoginActivity.this, "User not registered. Please register from Admin panel first.", Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);
    }

    private void showLoginError(String message) {
        pBar.setVisibility(View.GONE);
        btnULogin.setEnabled(true);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
