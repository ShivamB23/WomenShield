package com.project.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.project.util.SharedPreference;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    Button btnAddUser;
    EditText edtUName, edtUEmail, edtUPhone, edtUAddress, edtUPass, edtUCPass, edtUROne, edtURTwo, edtURThree;
    ProgressBar pBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        btnAddUser = findViewById(R.id.buttonAddUser);
        edtUName = findViewById(R.id.editTextUName);
        edtUEmail = findViewById(R.id.editTextUEmail);
        edtUPhone = findViewById(R.id.editTextUPhone);
        edtUAddress = findViewById(R.id.editTextUAddress);
        edtUPass = findViewById(R.id.editTextUPass);
        edtUCPass = findViewById(R.id.editTextUCPass);
        edtUROne = findViewById(R.id.editTextROne);
        edtURTwo = findViewById(R.id.editTextRTwo);
        edtURThree = findViewById(R.id.editTextRThree);
        pBar = findViewById(R.id.progressBar);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_name = edtUName.getText().toString().trim();
                String u_email = edtUEmail.getText().toString().trim();
                String u_phone = edtUPhone.getText().toString().trim();
                String u_address = edtUAddress.getText().toString().trim();
                String u_password = edtUPass.getText().toString().trim();
                String u_c_password = edtUCPass.getText().toString().trim();
                String u_relative_one = edtUROne.getText().toString().trim();
                String u_relative_two = edtURTwo.getText().toString().trim();
                String u_relative_three = edtURThree.getText().toString().trim();

                if(u_name.equals("") || u_email.equals("") || u_phone.equals("") || u_address.equals("") || u_password.equals("") || u_c_password.equals("") || u_relative_one.equals("") || u_relative_two.equals("") || u_relative_three.equals(""))
                    Toast.makeText(AddUserActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                else if(!Patterns.EMAIL_ADDRESS.matcher(u_email).matches())
                    edtUEmail.setError("Invalid Email ID");
                else if(u_phone.length() != 10)
                    edtUPhone.setError("Please enter 10 digit phone number");
                else if(u_relative_one.length() != 10)
                    edtUROne.setError("Invalid relative one phone number");
                else if(u_relative_two.length() != 10)
                    edtURTwo.setError("Invalid relative two phone number");
                else if(u_relative_three.length() != 10)
                    edtURThree.setError("Invalid relative three phone number");
                else if(!u_password.equals(u_c_password))
                    edtUCPass.setError("Password and confirm password not matching");
                else if(u_password.length() < 6)
                    edtUPass.setError("Password must be at least 6 characters length");
                else
                    addUser(u_name, u_email, u_phone, u_address, u_password, u_relative_one, u_relative_two, u_relative_three);

            }
        });
    }

    private void addUser(String uName, String uEmail, String uPhone, String uAddress, String uPassword, String uRelativeOne, String uRelativeTwo, String uRelativeThree) {
        pBar.setVisibility(View.VISIBLE);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.GONE);

                SharedPreference.save(uPhone + "_name", uName);
                SharedPreference.save(uPhone + "_email", uEmail);
                SharedPreference.save(uPhone + "_password", uPassword);
                SharedPreference.save(uPhone + "_address", uAddress);
                SharedPreference.save(uPhone + "_relative_one", uRelativeOne);
                SharedPreference.save(uPhone + "_relative_two", uRelativeTwo);
                SharedPreference.save(uPhone + "_relative_three", uRelativeThree);

                Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                clearAllFields();
            }
        }, 1000);
    }

    private void clearAllFields() {
        edtUName.setText("");
        edtUEmail.setText("");
        edtUPhone.setText("");
        edtUAddress.setText("");
        edtUPass.setText("");
        edtUCPass.setText("");
        edtUROne.setText("");
        edtURTwo.setText("");
        edtURThree.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            SharedPreference.removeKey("a_id");
            SharedPreference.removeKey("a_name");
            SharedPreference.removeKey("a_email");
            SharedPreference.removeKey("a_phone");
            SharedPreference.removeKey("a_password");

            Intent i = new Intent(AddUserActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}