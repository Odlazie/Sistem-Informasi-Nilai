package com.example.latihan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class update_mahasiswa extends AppCompatActivity {
    private boolean isMahasiswaExist = false;
    private EditText editTextNim;
    private EditText editTextNama;
    private EditText editTextJurusan;
    private RadioGroup radioGroup;
    private Button buttonUbah;
    private Button buttonHapus;
    private Button buttonTampil;
    private String nim;
    private TextInputEditText passwordTextInputEditText;


    private EditText editTextTanggal;
    private Button buttonAmbilGambar;

    private int GALLERY = 1;
    private int mYear, mMonth, mDay;
    ImageView gambar;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri filepath;
    private Bitmap bitmap;
    private String tgl_hari_ini;
    String image;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mahasiswa);

        Intent intent = getIntent();

        nim = intent.getStringExtra(konfigurasi.MHS_NIM);

        editTextNim = findViewById(R.id.editTextNim);
        editTextNama = findViewById(R.id.editTextNama);
        editTextJurusan = findViewById(R.id.editTextJurusan);

        buttonUbah = findViewById(R.id.buttonUpdate);
        buttonHapus = findViewById(R.id.buttonDelete);
        radioGroup = findViewById(R.id.JenisKl);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);

        editTextTanggal = findViewById(R.id.editTextTanggal);
        buttonAmbilGambar = findViewById(R.id.buttonAmbilGambar);
        gambar = findViewById(R.id.foto);
        gambar.setBackgroundColor(Color.GRAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl_hari_ini = sdf.format(new Date());
        editTextTanggal.setText(tgl_hari_ini);
        linearLayout = findViewById(R.id.updatemahasiswalayout);

        buttonAmbilGambar.setOnClickListener(view -> {
            showPictureDialog();
        });

        editTextTanggal.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    editTextTanggal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });


        buttonUbah.setOnClickListener(view -> {
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
                Toast.makeText(update_mahasiswa.this, "Tolong Isi Fieldnya", Toast.LENGTH_SHORT).show();
            } else if (bitmap == null) {
                Toast.makeText(this, "Silakan pilih gambar", Toast.LENGTH_SHORT).show();
            } else {
                updateMhs();
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                finish();
            }

        });

        buttonHapus.setOnClickListener(view -> {
            confirmDeleteMhs();
        });

        editTextNim.setText(nim);
        getMhs();

    }

    private void getMhs() {
        class GetEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_mahasiswa.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showMhs(s);


            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_TAMPIL_MHS, nim);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showMhs(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String jurusan = c.getString(konfigurasi.TAG_JURUSAN);
            String jkl = c.getString(konfigurasi.TAG_JKL);
            String password = c.getString(konfigurasi.TAG_PASSWORD);
            String tanggal = c.getString(konfigurasi.TAG_TANGGAL);
            image = c.getString(konfigurasi.TAG_IMG);


            editTextNama.setText(nama);
            editTextJurusan.setText(jurusan);
            if (jkl.equals("Pria")) {
                radioGroup.check(R.id.radioButtonPria);
            } else if (jkl.equals("Wanita")) {
                radioGroup.check(R.id.radioButtonWanita);
            }
            passwordTextInputEditText.setText(password);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String tgl = df.format(date);
            editTextTanggal.setText(tgl);
            Glide.with(update_mahasiswa.this)
                    .load(image)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(gambar);
            linearLayout.setTag("Tidak_Ganti_Foto");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateMhs() {
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


        class UpdateEmployee extends AsyncTask<Bitmap, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_mahasiswa.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(update_mahasiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... v) {
                HashMap<String, String> hashMap = new HashMap<>();
                if (linearLayout.getTag().equals("Ganti_Foto")) {
                    Bitmap bitmap = v[0];
                    String uploadImage = getStringImage(bitmap);
                    hashMap.put(konfigurasi.KEY_MHS_NIM, nim);
                    hashMap.put(konfigurasi.KEY_MHS_NAMA, nama);
                    hashMap.put(konfigurasi.KEY_MHS_JURUSAN, jurusan);
                    hashMap.put(konfigurasi.KEY_MHS_JKL, jkl);
                    hashMap.put(konfigurasi.KEY_MHS_PASSWORD, password);
                    hashMap.put(konfigurasi.KEY_MHS_TANGGAL, tanggal);
                    hashMap.put(konfigurasi.KEY_MHS_IMG, uploadImage);
                } else {
                    hashMap.put(konfigurasi.KEY_MHS_NIM, nim);
                    hashMap.put(konfigurasi.KEY_MHS_NAMA, nama);
                    hashMap.put(konfigurasi.KEY_MHS_JURUSAN, jurusan);
                    hashMap.put(konfigurasi.KEY_MHS_JKL, jkl);
                    hashMap.put(konfigurasi.KEY_MHS_PASSWORD, password);
                    hashMap.put(konfigurasi.KEY_MHS_TANGGAL, tanggal);

                }

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UBAH_MHS, hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void deleteMhs() {
        class DeleteEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_mahasiswa.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("Mahasiswa tidak ditemukan")) {
                    startActivity(new Intent(update_mahasiswa.this, TampilSemuaMhs.class));
                } else {
                    startActivity(new Intent(update_mahasiswa.this, Mem.class));
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_HAPUS_MHS, nim);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteMhs() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Mahasiswa ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMhs();
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Buka Galery",
                "Ambil Dari Kamera"};
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

    public void PilihDariGaleri() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }


    public void AmbilFotoDariKamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            handleGalleryResult(data);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            handleCameraResult(data);
        } else {
            showToast("Tidak Ada Gambar");
        }
    }

    private void handleGalleryResult(Intent data) {
        filepath = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
            gambar.setImageBitmap(bitmap);
            linearLayout.setTag("Ganti_Foto");
        } catch (IOException e) {
            showToast(e.getMessage());
        }
    }

    private void handleCameraResult(Intent data) {
        try {
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = (Bitmap) extras.get("data");
                gambar.setImageBitmap(bitmap);
                linearLayout.setTag("Ganti_Foto");
            }
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(update_mahasiswa.this, message, Toast.LENGTH_SHORT).show();
    }

}