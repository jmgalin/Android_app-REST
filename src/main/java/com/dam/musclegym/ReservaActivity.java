package com.dam.musclegym;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReservaActivity extends AppCompatActivity {

    private ListView listViewReserva;
    private DataBase db;
    private Integer plazas_disponibles;
    private String usuariosInscritos, plazas;
    private String usuarioactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        listViewReserva = findViewById(R.id.list_view_reserva);
        db = new DataBase(getApplicationContext(), "musclegym.db", null, 1);
        usuarioactual = db.getVariableExternaUsuario();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String URL = Constants.API_URL+ "/reservas";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Se ha recibido una lista de reservas
                Toast.makeText(getApplicationContext(),
                        "Lista de reservas recibida",
                        Toast.LENGTH_SHORT).show();

                List<JSONObject> reservasList = new ArrayList<>();

                // Iterar sobre el JSONArray y agregar cada reserva a la lista
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject reserva = response.getJSONObject(i);
                        reservasList.add(reserva);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Crear el adaptador personalizado para la lista de reservas
                ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(
                        getApplicationContext(),
                        R.layout.list_item_reserva,
                        R.id.text_view_clase,
                        reservasList
                ) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reserva, parent, false);
                        }

                        TextView claseTextView = convertView.findViewById(R.id.text_view_clase);
                        TextView profesorTextView = convertView.findViewById(R.id.text_view_profesor);
                        TextView lugarTextView = convertView.findViewById(R.id.text_view_lugar);
                        TextView plazasTextView = convertView.findViewById(R.id.text_view_plazas);
                        TextView fechaTextView = convertView.findViewById(R.id.text_view_fecha);

                        JSONObject reserva = getItem(position);
                        if (reserva != null) {
                            try {
                                String clase = reserva.getString("clase");
                                String profesor = reserva.getString("profesor");
                                String lugar = reserva.getString("lugar");
                                plazas = reserva.getString("plazasDisponibles");
                                String fecha = reserva.getString("fechaHora");
                                claseTextView.setText(clase);
                                profesorTextView.setText(profesor);
                                lugarTextView.setText(lugar);
                                plazasTextView.setText("Plazas disponibles: " + plazas);
                                fechaTextView.setText(fecha);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        return convertView;
                    }
                };

                listViewReserva.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.v("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        // Agregar la solicitud a la cola
        requestQueue.add(request);




        listViewReserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Obtener la información de la reserva seleccionada en formato JSON

                    JSONObject reservaJson = (JSONObject) parent.getItemAtPosition(position);

                    // Obtener los valores de los campos necesarios del JSON
                    String clase = reservaJson.getString("clase");
                    String profesor = reservaJson.getString("profesor");
                    String lugar = reservaJson.getString("lugar");
                    String idReserva = reservaJson.getString("id");

                    // Crear un diálogo de alerta para confirmar la reserva
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservaActivity.this);
                    builder.setMessage("¿Deseas realizar la reserva de " + clase + " con " + profesor + " en " + lugar + "?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new ReservationTask().execute(idReserva);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Muestra un mensaje de error en el Toast
                    Toast.makeText(ReservaActivity.this, "Error al obtener la información de la reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });



        TextView btnVolver = (TextView) findViewById(R.id.button_volver);
        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReservaActivity.this, HomeActivity.class));
            }
        });


    }

    private class ReservationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String idReserva = params[0];
            String url = Constants.API_URL+ "/reservaaula?usuario=" + usuarioactual + "&id=" + idReserva;

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
            Toast.makeText(ReservaActivity.this, responseBody, Toast.LENGTH_SHORT).show();
        }
    }
}
