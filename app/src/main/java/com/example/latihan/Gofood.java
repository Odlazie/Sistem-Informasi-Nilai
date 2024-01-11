package com.example.latihan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Gofood extends AppCompatActivity {

    private int jumlahMcFlurry = 0;
    private int jumlahBurger = 0;
    private int jumlahBBayam = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gofood_layout_tugas4);
        ImageView kurangEntitas = findViewById(R.id.KurangEntitas);
        ImageView tambahEntitas = findViewById(R.id.TambahEntitas);
        TextView mcfluryentitas = findViewById(R.id.mcfluryentitas);
        ImageView kurangEntitasBurger = findViewById(R.id.KurangEntitasBurger);
        ImageView tambahEntitasBurger = findViewById(R.id.TambahEntitasBurger);
        TextView burgerentitas = findViewById(R.id.BurgerEntitas);
        ImageView kurangEntitasBBayam = findViewById(R.id.KurangEntitasBBayam);
        ImageView tambahEntitasBBayam = findViewById(R.id.TambahEntitasBBayam);
        TextView bbayamentitas = findViewById(R.id.BBayamEntitas);
        setEntitas(kurangEntitas, mcfluryentitas, false, R.id.mcfluryentitas);
        setEntitas(tambahEntitas, mcfluryentitas, true, R.id.mcfluryentitas);
        setEntitas(kurangEntitasBurger, burgerentitas, false, R.id.BurgerEntitas);
        setEntitas(tambahEntitasBurger, burgerentitas, true, R.id.BurgerEntitas);
        setEntitas(kurangEntitasBBayam, bbayamentitas, false, R.id.BBayamEntitas);
        setEntitas(tambahEntitasBBayam, bbayamentitas, true, R.id.BBayamEntitas);
        TextView totalHargaTextView = findViewById(R.id.totalharga);
        double totalHarga = updateTotalHarga();
        @SuppressLint("DefaultLocale") String formattedTotalHarga = String.format("Rp. %,.0f", totalHarga);
        totalHargaTextView.setText(formattedTotalHarga);
        Button btnbayar = findViewById(R.id.btnBayar);
        btnbayar.setVisibility(View.GONE);
        if (savedInstanceState != null) {
            jumlahMcFlurry = savedInstanceState.getInt("jumlahMcFlurry", 0);
            jumlahBurger = savedInstanceState.getInt("jumlahBurger", 0);
            jumlahBBayam = savedInstanceState.getInt("jumlahBBayam", 0);
        }

    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("jumlahMcFlurry", jumlahMcFlurry);
        outState.putInt("jumlahBurger", jumlahBurger);
        outState.putInt("jumlahBBayam", jumlahBBayam);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        jumlahMcFlurry = savedInstanceState.getInt("jumlahMcFlurry", 0);
        jumlahBurger = savedInstanceState.getInt("jumlahBurger", 0);
        jumlahBBayam = savedInstanceState.getInt("jumlahBBayam", 0);
        TextView mcfluryentitas = findViewById(R.id.mcfluryentitas);
        TextView burgerentitas = findViewById(R.id.BurgerEntitas);
        TextView bbayamentitas = findViewById(R.id.BBayamEntitas);
        mcfluryentitas.setText(String.valueOf(jumlahMcFlurry));
        burgerentitas.setText(String.valueOf(jumlahBurger));
        bbayamentitas.setText(String.valueOf(jumlahBBayam));
        TextView totalHargaTextView = findViewById(R.id.totalharga);
        double totalHarga = updateTotalHarga();
        @SuppressLint("DefaultLocale") String formattedTotalHarga = String.format("Rp. %,.0f", totalHarga);
        totalHargaTextView.setText(formattedTotalHarga);

    }


    private void setEntitas(ImageView img, TextView txt, boolean isTambah, int id) {
        img.setOnClickListener(v -> {
            int value = Integer.parseInt(txt.getText().toString());
            if (isTambah) {
                value++;
            } else if (value > 0) {
                value--;
            }
            txt.setText(String.valueOf(value));
            updateJumlahEntitas(id, value);
            TextView totalHargaTextView = findViewById(R.id.totalharga);
//            updateTotalHarga(totalHargaTextView);
            double totalHarga = updateTotalHarga();
            @SuppressLint("DefaultLocale") String formattedTotalHarga = String.format("Rp. %,.0f", totalHarga);
            totalHargaTextView.setText(formattedTotalHarga);

        });

    }

    private void updateJumlahEntitas(int id, int value) {
        if (id == R.id.mcfluryentitas) {
            jumlahMcFlurry = value;

        } else if (id == R.id.BurgerEntitas) {
            jumlahBurger = value;

        } else if (id == R.id.BBayamEntitas) {
            jumlahBBayam = value;

        }
        Button btnbayar = findViewById(R.id.btnBayar);
        if (jumlahMcFlurry > 0 || jumlahBurger > 0 || jumlahBBayam > 0) {
            btnbayar.setVisibility(View.VISIBLE);
        } else {
            btnbayar.setVisibility(View.GONE);
        }


    }

    public double updateTotalHarga() {
        double hargaMcFlurry = 13500;
        double totalMcFlurry = hargaMcFlurry * jumlahMcFlurry;

        double hargaBurger = 60000;
        double totalBurger = hargaBurger * jumlahBurger;

        double hargaBBayam = 30000;
        double totalBBayam = hargaBBayam * jumlahBBayam;
        double totalHarga = totalMcFlurry + totalBurger + totalBBayam;
        return totalHarga;


    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void onButtonBayar(View view){
        Intent intent = new Intent(this,Pengiriman.class);
        intent.putExtra("totalHarga", updateTotalHarga());
        startActivity(intent);
    }
}
