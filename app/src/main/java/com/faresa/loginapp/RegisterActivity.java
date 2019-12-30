package com.faresa.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.faresa.loginapp.koneksi.Service;
import com.faresa.loginapp.koneksi.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etNama;
    EditText etEmail;
    EditText etPassword,etpw;
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    Service mApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mApiService = Utils.getAPIService();

        initComponents();
    }
    private   void  initComponents(){

        etNama = (EditText) findViewById(R.id.etNama);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etpw = (EditText) findViewById(R.id.pw);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNama.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pw = etPassword.getText().toString().trim();
                String c_pw = etpw.getText().toString().trim();
                boolean isEmptyFields = false;
                if (TextUtils.isEmpty(nama)) {
                    isEmptyFields = true;
                    etNama.setError("Field ini tidak boleh kosong");
                }

                if (TextUtils.isEmpty(email)) {
                    isEmptyFields = true;
                    etEmail.setError("Field ini tidak boleh kosong");
                }

                if (TextUtils.isEmpty(pw)) {
                    isEmptyFields = true;
                    etPassword.setError("Field ini tidak boleh kosong");
                }
                if (TextUtils.isEmpty(c_pw)) {
                    isEmptyFields = true;
                    etpw.setError("Field ini tidak boleh kosong");
                }

                if (!isEmptyFields) {
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestRegister();
                }

            }
        });
    }
    private void requestRegister(){
        mApiService.registerRequest(etNama.getText().toString(),
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etpw.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
//                            try {
//                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
//
//                                if (jsonRESULTS.getString("succes").equals("false")){
//                                    Toast.makeText(mContext, "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//
//                                    startActivity(intent);
//                                } else {
//                                    String error_message = jsonRESULTS.getString("error_msg");
//                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
