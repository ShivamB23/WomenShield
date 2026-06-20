package com.project.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.util.SharedPreference;
import com.project.util.Validator;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

public class AdminLoginActivity extends AppCompatActivity {

    EditText edtAEmail, edtAPass;
    Button btnALogin;
    ProgressBar pBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            Intent i = new Intent(AdminLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        btnALogin = findViewById(R.id.buttonALogin);
        edtAEmail = findViewById(R.id.editTextAEmail);
        edtAPass = findViewById(R.id.editTextAPassword);
        pBar = findViewById(R.id.progressBar);

        btnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a_email = edtAEmail.getText().toString().trim();
                String a_pass = edtAPass.getText().toString().trim();

                if (a_email.isEmpty() || a_pass.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else if (!Validator.isValidEmail(a_email)) {
                    edtAEmail.setError("Please enter a valid email address");
                } else if (!Validator.isValidPassword(a_pass)) {
                    edtAPass.setError("Password must be at least 6 characters");
                } else {
                    adminLogin(a_email, a_pass);
                }
            }
        });
    }

    private void adminLogin(String aEmail, String aPass) {
        btnALogin.setEnabled(false);
        pBar.setVisibility(View.VISIBLE);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.GONE);
                
                if ("admin@sheild.com".equalsIgnoreCase(aEmail) && "admin123".equals(aPass)) {
                    SharedPreference.save("a_id", "1");
                    SharedPreference.save("a_name", "Admin");
                    SharedPreference.save("a_email", aEmail);
                    SharedPreference.save("a_phone", "1234567890");
                    SharedPreference.save("a_password", aPass);

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(AdminLoginActivity.this, AddUserActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    btnALogin.setEnabled(true);
                    Toast.makeText(AdminLoginActivity.this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

    private void showLoginError(String message) {
        pBar.setVisibility(View.GONE);
        btnALogin.setEnabled(true);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
