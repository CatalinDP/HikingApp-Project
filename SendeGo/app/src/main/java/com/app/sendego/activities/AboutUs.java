package com.app.sendego.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.sendego.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Button btnSupport = findViewById(R.id.btnSupport);

        btnSupport.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:soporte@sendego.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Soporte SendeGo");

            startActivity(intent);
        });
    }
}
