package com.faresa.loginapp.koneksi;

public class Utils {


    // 10.0.2.2 ini adalah localhost.
    public static final String BASE_URL_API = "http://10.0.2.2:8000/api/v1/";

    // Mendeklarasikan Interface BaseApiService
    public static Service getAPIService(){
        return RetroConfig.getClient(BASE_URL_API).create(Service.class);
    }
}
