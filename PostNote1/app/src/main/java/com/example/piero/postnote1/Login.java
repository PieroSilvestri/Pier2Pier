package com.example.piero.postnote1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    EditText usernameText, passwordText;
    Button btnLogin, btnRegister;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.textPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLogin();
            }
        });

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenReg();
            }
        });

    }

    public void OnLogin() {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "http://pierosilvestri1.altervista.org/mysql-android/login.php?username=" + username + "&password=" + password;
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = "";

                try {
                    jsonObject = new JSONObject(new String(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("MYJSON", jsonObject.toString());

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    res = jsonArray.getJSONObject(0).getString("autentication");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(res.equals("true")){
                    OpenMain();
                }else{
                    Toast.makeText(Login.this, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Login.this, "Errore. Problema di connessione", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void OpenReg() {
        startActivity(new Intent(this, Register.class));
    }

    public void OpenMain() {
        startActivity(new Intent(this, MainActivity.class));
    }


}
