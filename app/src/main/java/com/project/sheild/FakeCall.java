package com.project.sheild;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.admin.AdminLoginActivity;
import com.project.womensafety.MainActivity;
import com.project.womensafety.R;

public class FakeCall extends AppCompatActivity {

    private static final int PICK_IMAGE = 1; // Request code for picking an image
    private EditText callerName;
    private EditText callerNumber;
    private ImageView callerImageView;
    private Uri imageUri; // URI to hold the selected image

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Assuming main_menu.xml contains a logout item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout){
            Intent i = new Intent(FakeCall.this, MainActivity1.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fakecall); // Make sure this matches your layout file

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        callerName = findViewById(R.id.caller_name);
        callerNumber = findViewById(R.id.caller_number);
        callerImageView = findViewById(R.id.caller_image_view);

        Button selectImage = findViewById(R.id.select_image);
        Button startFakeCall = findViewById(R.id.start_fake_call);
        Button defaultFakeCall = findViewById(R.id.default_fake_call);

        selectImage.setOnClickListener(v -> openGallery());
        startFakeCall.setOnClickListener(v -> startFakeCall());
        defaultFakeCall.setOnClickListener(v -> startDefaultFakeCall());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData(); // Get the image URI
            callerImageView.setImageURI(imageUri); // Display the selected image
        }
    }

    private void startFakeCall() {
        String name = callerName.getText().toString().trim();
        String number = callerNumber.getText().toString().trim();

        // Start the call screen activity with user-defined caller details
        initiateFakeCall(name, number);
    }

    private void startDefaultFakeCall() {
        // Start the call screen activity with default caller details
        String defaultName = "MOM";
        String defaultNumber = "+91 8080392560";
        initiateFakeCall(defaultName, defaultNumber);
    }

    private void initiateFakeCall(String name, String number) {
        Intent intent = new Intent(this, CallScreenActivity.class);
        intent.putExtra("CALLER_NAME", name);
        intent.putExtra("CALLER_NUMBER", number);
        intent.putExtra("CALLER_IMAGE", imageUri != null ? imageUri.toString() : null); // Pass image URI
        startActivity(intent);
    }
}
