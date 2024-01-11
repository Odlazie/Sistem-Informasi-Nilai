package com.example.latihan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Objects;

public class Pengiriman extends AppCompatActivity {
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengiriman_layout_tugas5);
        Objects.requireNonNull(getSupportActionBar()).hide();
        double totalHarga = getIntent().getDoubleExtra("totalHarga", 0.0);
        TextView totalHargaTextView = findViewById(R.id.totalhargapengiriman);
        totalHargaTextView.setText(String.format("%,.0f", totalHarga));
        TextView totalPembayaranTextView = findViewById(R.id.totalpembayaran);
        double totalPembayaran = totalPembayaran(totalHarga);
        totalPembayaranTextView.setText(String.format("%,.0f", totalPembayaran));
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView totalPembayaranCashTextView = findViewById(R.id.id_tunai);
        totalPembayaranCashTextView.setText(String.format("%,.0f", totalPembayaran));
        CardView cardView = findViewById(R.id.cardview_ubahnotes);
        TextView textView = findViewById(R.id.String_notes);
        cardView.setOnClickListener(v -> {
            String previousText = textView.getText().toString();
            showPopupDialog(textView, previousText);
        });
        CardView detailalamat = findViewById(R.id.tombol_ubahdetail);
        TextView stringdetailalamat = findViewById(R.id.detail_rumahh);
        detailalamat.setOnClickListener(view -> {
            String previousText = stringdetailalamat.getText().toString();
            showPopupDialog(stringdetailalamat, previousText);
        });
        CardView pesanDanAntarCardView = findViewById(R.id.pesan_dan_antar);

        pesanDanAntarCardView.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Pengiriman.this);
            dialogBuilder.setTitle("Konfirmasi Pesanan")
                    .setMessage("Pesanan dikonfirmasi")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Tindakan yang akan dijalankan setelah tombol OK ditekan
                        }
                    })
                    .show();
        });

    }

    private void showPopupDialog(TextView textView, String previousText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_layout, null);
        EditText editText = dialogView.findViewById(R.id.edit_text);
        editText.setText(previousText); // Set teks awal pada EditText

        dialogBuilder.setView(dialogView)
                .setTitle("Edit Notes")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = editText.getText().toString();
                        updateTextView(textView, newText);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // Tidak ada perubahan pada TextView
                    }
                });

        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button saveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newText = editText.getText().toString();
                        if (!newText.equals(previousText)) {
                            updateTextView(textView, newText);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void updateTextView(TextView textView, String newText) {
        textView.setText(newText);
    }

    private double totalPembayaran(double totalHarga) {
        double biaya_ongkir = 20000;
        double biaya_lainya = 30000;
        return biaya_ongkir + biaya_lainya + totalHarga;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}