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

public class LoginActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    Service mApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = Utils.getAPIService(); // meng-init yang ada di package apihelper
        initComponents();
    }
    private void initComponents(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String pw = etPassword.getText().toString().trim();
                boolean isEmptyFields = false;
                if (TextUtils.isEmpty(email)) {
                    isEmptyFields = true;
                    etEmail.setError("Field ini tidak boleh kosong");

                }

                if (TextUtils.isEmpty(pw)) {
                    isEmptyFields = true;
                    etPassword.setError("Field ini tidak boleh kosong");
                }
                if (!isEmptyFields) {
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestLogin();
                }



            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

    }

        private void requestLogin(){
            mApiService.loginRequest(etEmail.getText().toString(), etPassword.getText().toString())
                    .enqueue(new Callback<ResponseBody>()
                    {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                loading.dismiss();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
//                                try {
//                                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
//                                    if (jsonRESULTS.getString("error").equals("false")){
//                                        // Jika login berhasil maka data nama yang ada di response API
//                                        // akan diparsing ke activity selanjutnya.
//                                        Toast.makeText(mContext, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();
//                                        String nama = jsonRESULTS.getJSONObject("user").getString("nama");
//                                        Intent intent = new Intent(mContext, MainActivity.class);
//                                        intent.putExtra("result_nama", nama);
//                                        startActivity(intent);
//                                    } else {
//                                        // Jika login gagal
//                                        String error_message = jsonRESULTS.getString("error_msg");
//                                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            } else {
                                loading.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            loading.dismiss();
                        }
                    });
    }
}
