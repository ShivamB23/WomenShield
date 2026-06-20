package com.project.sheild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.project.womensafety.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_about_us);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        Button button=findViewById (R.id.contactUs);
        button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                composeEmail();
            }
        });
    }
    public void composeEmail() {
        String subject="Contacting for SHEild - Women Safety Application";
        String mailto = "mailto:adityakonda04@gmail.com" +
                "?cc="+"07rashimall@gmail.com"+
                "&bcc="+"anmolgupta.2021@kccemsr.edu.in"+
                "&subject=" + Uri.encode(subject);


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }


    }
}