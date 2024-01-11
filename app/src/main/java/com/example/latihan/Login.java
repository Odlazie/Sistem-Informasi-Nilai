package com.example.latihan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    AutoCompleteTextView status;
    String[] status_user = new String[]{"Admin", "Bukan Admin"};
    ProgressDialog pDialog;
    Context context;

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout_tgs9);
        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordTextInputEditText);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        context = Login.this;
        pDialog = new ProgressDialog(context);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_status, R.id.list_item_status, status_user);
        status = findViewById(R.id.status);
        status.setAdapter(adapter);
        buttonLogin.setOnClickListener(v -> {
            login();
        });

    }
    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }
    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
    private void keMenuUtama(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("Undipa",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("status","Sudah Login");
        myEdit.commit();
        intent.putExtra("status_user", status.getText().toString());
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
    private void login() {
        final String nim = editTextUsername.getText().toString().trim();
        final String pass = editTextPassword.getText().toString().trim();
        final String selectedStatus = status.getText().toString().trim();


        if (TextUtils.isEmpty(selectedStatus)) {
            Toast.makeText(Login.this, "Pilih Status", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(nim) && TextUtils.isEmpty(pass)) {
            Toast.makeText(Login.this, "Lengkapi Field", Toast.LENGTH_LONG).show();
            return;
        }
        pDialog.setMessage("Login Process...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfigurasi.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        if (response.contains(konfigurasi.LOGIN_SUCCESS)) {
                            keMenuUtama();
                        } else {
                            Toast.makeText(Login.this, "Kombinasi Username dan Password Salah", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(Login.this, "Koneksi Server Bermasalah", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_LOGIN_NIM, nim);
                params.put(konfigurasi.KEY_LOGIN_PASS, pass);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


}