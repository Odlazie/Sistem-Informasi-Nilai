package com.example.latihan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TampilSemuaMhs extends AppCompatActivity {

    private ListView listView;
    private String JSON_STRING;
    ActionBar actionBar;
    private MenuItem tombol_aksi;
    private EditText cari;
    private boolean pencarian_terbuka;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_mhs_tgs10);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TampilSemuaMhs.this, update_mahasiswa.class);
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(i);
                String mhsNim = map.get(konfigurasi.TAG_NIM).toString();
                intent.putExtra(konfigurasi.MHS_NIM, mhsNim);
                startActivityForResult(intent, 1);
            }
        });
        getJSON();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getJSON();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Panggil getJSON() untuk memperbarui data setelah pengeditan
                getJSON();
            }
        }
    }


    private void showMhs() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String nim = jo.getString(konfigurasi.TAG_NIM);
                String nama = jo.getString(konfigurasi.TAG_NAMA);
                String jurusan = jo.getString(konfigurasi.TAG_JURUSAN);
                String jkl = jo.getString(konfigurasi.TAG_JKL);
                String password = jo.getString(konfigurasi.TAG_PASSWORD);

                HashMap<String, String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_NIM, nim);
                employees.put(konfigurasi.TAG_NAMA, nama);
                employees.put(konfigurasi.TAG_JURUSAN, jurusan);
                employees.put(konfigurasi.TAG_JKL, jkl);
                employees.put(konfigurasi.TAG_PASSWORD, password);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                TampilSemuaMhs.this, list, R.layout.list_item_tgs10,
                new String[]{konfigurasi.TAG_NIM, konfigurasi.TAG_NAMA, konfigurasi.TAG_JURUSAN, konfigurasi.TAG_JKL, konfigurasi.TAG_PASSWORD},
                new int[]{R.id.nim, R.id.nama, R.id.jurusan, R.id.jkl, R.id.password});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSemuaMhs.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showMhs();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_TAMPIL_SEMUA_MHS);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cari, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        tombol_aksi = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        actionBar = getSupportActionBar();
        if (pencarian_terbuka) {
            SwitchIcon();
        } else {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.kolom_pencarian);
            actionBar.setDisplayShowTitleEnabled(false); //sembunyikan judul
            cari = actionBar.getCustomView().findViewById(R.id.idCcari); //the text editor
            cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }
            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                ((SimpleAdapter) TampilSemuaMhs.this.adapter).getFilter().filter(s);
            }
            @Override
            public void afterTextChanged (Editable s){
            }
            });
            cari.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(cari, InputMethodManager.SHOW_IMPLICIT);
            tombol_aksi.setIcon(R.drawable.ic_clear);
            pencarian_terbuka = true;
        }
    }
    private void SwitchIcon(){
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cari.getWindowToken(), 0);
        tombol_aksi.setIcon(R.drawable.baseline_search_24);
        ((SimpleAdapter)TampilSemuaMhs.this.adapter).getFilter().filter("");
        pencarian_terbuka=false;
    }
}