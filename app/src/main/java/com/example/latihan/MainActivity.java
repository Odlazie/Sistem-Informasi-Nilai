package com.example.latihan;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Undipa", MODE_PRIVATE);
        String status_login = sharedPreferences.getString("status","");
        if (!status_login.equalsIgnoreCase("Sudah Login")){
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
        setContentView(R.layout.gojek_layout_tugas3);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        }

    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void onGorideClicked(View view) {
        Toast.makeText(getApplicationContext(), "Tugas 1 Install Android Studio", Toast.LENGTH_SHORT).show();

    }

    public void onGocarClicked(View view) {
        Intent pindahtugas2 = new Intent(this, Main4Activity.class);
        startActivity(pindahtugas2);
    }

    public void onGoFoodClicked(View view) {
        Intent pindahgopud = new Intent(this, Gofood.class);
        startActivity(pindahgopud);
    }

    public void onLainyaClicked(View view) {
        String statusUser = getIntent().getStringExtra("status_user");
        if (statusUser != null && statusUser.equals("Admin")) {
            Intent pindahgopud = new Intent(this, Mem.class);
            startActivity(pindahgopud);
        } else {
            Toast.makeText(getApplicationContext(), "Anda Bukan Admin", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            SharedPreferences settings = getSharedPreferences("Undipa", MODE_PRIVATE);
            settings.edit().clear().commit();
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin keluar?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLogout() {
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}