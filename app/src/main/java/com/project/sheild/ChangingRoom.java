package com.project.sheild;


import androidx.appcompat.app.AppCompatActivity;
import com.project.womensafety.R;

import android.os.Bundle;
import android.widget.ImageView;

public class ChangingRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_changing_room );

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}