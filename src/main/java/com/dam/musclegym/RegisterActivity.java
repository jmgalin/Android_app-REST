package com.dam.musclegym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.DefaultTaskExecutor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsuario, edEmail, edContrasena, edConfirm;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsuario =  findViewById(R.id.editTextRegUsuario);
        edEmail = findViewById(R.id.editTextRegEmail);
        edContrasena = findViewById(R.id.editTextRegContrasena);
        edConfirm = findViewById(R.id.editTextRegConfirmContrasena);

        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = edUsuario.getText().toString();
                String email = edEmail.getText().toString();
                String contrasena = edContrasena.getText().toString();
                String confirm = edConfirm.getText().toString();
                DataBase db = new DataBase(getApplicationContext(),"musclegym.db",null,1);
                if(usuario.length()==0 || contrasena.length()==0){
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(contrasena.compareTo(confirm)==0) {
                        new RegisterActivity.RegisterTask().execute(usuario, email, contrasena);

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String email = params[1];
            String contrasena = params[2];
            String url = Constants.API_URL+"/registro?usuario=" + usuario + "&email=" + email + "&contrasena=" + contrasena;

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String responseString = "";

            try {
                URL urlObject = new URL(url);
                connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    responseString = response.toString();
                } else {
                    responseString = "Error en la petición";
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseString = "Error en la petición";
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseBody) {
            Toast.makeText(RegisterActivity.this, responseBody, Toast.LENGTH_SHORT).show();
        }
    }
}