package com.example.latihan;


public class konfigurasi {

    public static final String SERVER_IP = "192.168.237.229";
    public static final String URL_TAMBAH = "http://" + SERVER_IP + "/tambahMhs.php";
    public static final String URL_TAMPIL_SEMUA_MHS = "http://" + SERVER_IP + "/tampilSemuaMhs.php";
    public static final String URL_TAMPIL_MHS = "http://" + SERVER_IP + "/tampilMhs.php?nim=";
    public static final String URL_UBAH_MHS = "http://" + SERVER_IP + "/updateMhs.php";
    public static final String URL_HAPUS_MHS = "http://" + SERVER_IP + "/hapusMhs.php?nim=";
    public static final String LOGIN_URL = "http://" + SERVER_IP + "/login.php";



    public static final String KEY_LOGIN_NIM = "nim";
    public static final String KEY_LOGIN_PASS = "password";
    public static final String LOGIN_SUCCESS = "success";
    public static final String KEY_MHS_NIM = "nim";
    public static final String KEY_MHS_NAMA = "nama";
    public static final String KEY_MHS_JURUSAN = "jurusan";
    public static final String KEY_MHS_JKL = "jkl";
    public static final String KEY_MHS_PASSWORD="password";
    public static final String KEY_MHS_IMG="lokasi_foto";
    public static final String KEY_MHS_TANGGAL="tanggal";


    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_NIM = "nim";
    public static final String TAG_NAMA ="nama";
    public static final String TAG_JURUSAN ="jurusan";
    public static final String TAG_JKL ="jkl";
    public static final String TAG_PASSWORD="password";
    public static final String TAG_IMG="lokasi_foto";
    public static final String TAG_TANGGAL="tanggal";
    public static final String MHS_NIM ="mhs_nim";
}