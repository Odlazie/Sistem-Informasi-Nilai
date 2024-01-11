package com.example.latihan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.provider.MediaStore;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Mem extends AppCompatActivity {

    private EditText editTextNim;
    private EditText editTextNama;

    private EditText editTextTanggal;
    private Button buttonAmbilGambar;

    private int GALLERY = 1;
    private int mYear, mMonth, mDay;
    ImageView gambar;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private Bitmap bitmap;
    private String tgl_hari_ini;
    private EditText editTextJurusan;
    private RadioGroup radioGroup;
    private Button buttonTambah;
    private Button buttonTampil;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordTextInputEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambahmhs_tgs10);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        editTextNim = findViewById(R.id.editTextNim);
        editTextNama = findViewById(R.id.editTextNama);
        editTextJurusan = findViewById(R.id.editTextJurusan);
        buttonTambah = findViewById(R.id.buttonAdd);
        buttonTampil = findViewById(R.id.buttonView);
        radioGroup = findViewById(R.id.JenisKl);

        editTextTanggal = findViewById(R.id.editTextTanggal);
        buttonAmbilGambar = findViewById(R.id.buttonAmbilGambar);
        gambar = findViewById(R.id.foto);
        gambar.setBackgroundColor(Color.GRAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl_hari_ini = sdf.format(new Date());
        editTextTanggal.setText(tgl_hari_ini);

        buttonAmbilGambar.setOnClickListener(view -> {
            showPictureDialog();
        });

        editTextTanggal.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Mem.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    editTextTanggal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        buttonTambah.setOnClickListener(view ->

        {
            String nim = editTextNim.getText().toString().trim();
            String nama = editTextNama.getText().toString().trim();
            String jurusan = editTextJurusan.getText().toString().trim();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString().trim();
            String JenisKelamin = "";

            if (selectedId != -1) {
                RadioButton radioButton = findViewById(selectedId);
                JenisKelamin = radioButton.getText().toString().trim();
            }

            if (TextUtils.isEmpty(nim) || TextUtils.isEmpty(nama) || TextUtils.isEmpty(jurusan) || TextUtils.isEmpty(JenisKelamin) || TextUtils.isEmpty(password)) {
                Toast.makeText(Mem.this, "Tolong Isi Fieldnya", Toast.LENGTH_SHORT).show();
            } else if (bitmap == null) {
                Toast.makeText(Mem.this, "Silakan pilih gambar", Toast.LENGTH_SHORT).show();
            }
            else {
                tambahMhs();
            }
        });

        buttonTampil.setOnClickListener(view ->
                startActivity(new Intent(Mem.this, TampilSemuaMhs.class)));
    }

    private void tambahMhs() {
        final String nim = editTextNim.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();
        final String password = Objects.requireNonNull(passwordTextInputEditText.getText()).toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String JenisKelamin = "";

        if (selectedId != -1) {
            RadioButton radioButton = findViewById(selectedId);
            JenisKelamin = radioButton.getText().toString().trim();
        }

        final String jkl = JenisKelamin;
        final String tanggal = editTextTanggal.getText().toString().trim();
        class AddEmployee extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Mem.this, "Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Mem.this, s, Toast.LENGTH_LONG).show();
            }

                @Override
                protected String doInBackground(Bitmap... v) {
                    HashMap<String, String> params = new HashMap<>();
                    Bitmap bitmap = v[0];
                    String uploadImage = getStringImage(bitmap);
                    params.put(konfigurasi.KEY_MHS_NIM, nim);
                    params.put(konfigurasi.KEY_MHS_NAMA, nama);
                    params.put(konfigurasi.KEY_MHS_JURUSAN, jurusan);
                    params.put(konfigurasi.KEY_MHS_JKL, jkl);
                    params.put(konfigurasi.KEY_MHS_PASSWORD, password);
                    params.put(konfigurasi.KEY_MHS_TANGGAL, tanggal);
                    params.put(konfigurasi.KEY_MHS_IMG, uploadImage);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(konfigurasi.URL_TAMBAH, params);
                    return res;
                }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Buka Galery",
                "Ambil Foto Dari Kamera"};
        pictureDialog.setItems(pictureDialogItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    PilihDariGaleri();
                    break;
                case 1:
                    AmbilFotoDariKamera();
                    break;
            }
        });
        pictureDialog.show();
    }
    private static final int CAMERA_REQUEST = 2;

    public void AmbilFotoDariKamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Kamera tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }
    public void PilihDariGaleri() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Mendapatkan gambar dari galeri
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                gambar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Mem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Mendapatkan gambar dari kamera
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = (Bitmap) extras.get("data");
                gambar.setImageBitmap(bitmap);
            }
        } else {
            Toast.makeText(Mem.this, "Tidak Ada Gambar", Toast.LENGTH_SHORT).show();
        }
    }

}