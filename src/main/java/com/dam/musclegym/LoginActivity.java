package com.dam.musclegym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText edUsuario, edContrasena;
    Button btn;
    TextView tv;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsuario =  findViewById(R.id.editTextloginUsuario);
        edContrasena = findViewById(R.id.editTextLoginContrasena);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNewUser);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = edUsuario.getText().toString();
                String contrasena = edContrasena.getText().toString();
                DataBase db = new DataBase(getApplicationContext(),"musclegym.db",null,1);
                if (usuario.length() == 0 || contrasena.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    db.login(usuario);
                    new LoginTask().execute(usuario, contrasena);
                }
            }
        });



        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    public class LoginTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            String usuario = params[0];
            String contrasena = params[1];
            int resultado = 0;

            try {
                // Construir la URL de la petición
                String url = Constants.API_URL+ "/login?usuario=" + usuario + "&contrasena=" + contrasena;

                // Crear un objeto URL
                URL urlObject = new URL(url);

                // Abrir la conexión HTTP
                HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

                // Configurar la conexión
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000); // Tiempo de espera para leer la respuesta en milisegundos
                connection.setConnectTimeout(15000); // Tiempo de espera para establecer la conexión en milisegundos

                // Realizar la petición y obtener la respuesta
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // La petición se realizó correctamente
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String responseText = reader.readLine();

                    // Interpretar el resultado de la respuesta
                    if (responseText.equals("1")) {
                        resultado = 1;

                    }

                    // Cerrar el lector
                    reader.close();
                }

                // Cerrar la conexión
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Integer resultado) {
            if (resultado == 1) {
                Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }

    }

}