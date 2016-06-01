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

public class Register extends AppCompatActivity {

    EditText name, surname, age, username, password;
    Button btnRegister;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = (EditText)findViewById(R.id.nameText);
        surname = (EditText)findViewById(R.id.surnameText);
        age = (EditText)findViewById(R.id.ageText);
        username = (EditText)findViewById(R.id.usernameText);
        password = (EditText)findViewById(R.id.passwordText);

        btnRegister = (Button)findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRegister();
            }
        });

    }

    public void OnRegister() {
        String nameText = name.getText().toString();
        String surnameText = surname.getText().toString();
        String ageText = age.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "http://pierosilvestri1.altervista.org/mysql-android/registration.php?name="+nameText+
                "&surname="+surnameText+"&age="+ageText+"&username="+usernameText+"&password="+passwordText;
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
                    res = jsonArray.getJSONObject(0).getString("registration");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(res.equals("true")){
                    OpenLogin();
                }else{
                    Toast.makeText(Register.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Register.this, "Errore. Problema di connessione", Toast.LENGTH_SHORT).show();
            }
        });
        //String miaRoba = backgroundWorker.returnRes();
        //Toast.makeText(this, miaRoba, Toast.LENGTH_SHORT).show();
    }

    public void OpenLogin() {
        startActivity(new Intent(this, Login.class));
    }

}
