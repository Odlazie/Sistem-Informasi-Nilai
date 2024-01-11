package com.example.latihan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();
        int waktu_loading = 4000;
        new Handler().postDelayed(() -> {
            Intent home = new Intent(splash.this, MainActivity.class);
            startActivity(home);
            finish();
        }, waktu_loading);
    }
}