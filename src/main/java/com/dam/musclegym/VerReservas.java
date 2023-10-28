package com.dam.musclegym;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerReservas extends AppCompatActivity {

    private ListView listViewReservas;
    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reservas);

        listViewReservas = findViewById(R.id.list_view_reservas);
        db = new DataBase(getApplicationContext(), "musclegym.db", null, 1);

        String usuarioactual = db.getVariableExternaUsuario();





        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String URL = Constants.API_URL+ "/verreservas?usuario="+usuarioactual;

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
                        R.layout.list_item_verreservas,
                        R.id.text_view_clase,
                        reservasList
                ) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_verreservas, parent, false);
                        }

                        TextView claseTextView = convertView.findViewById(R.id.text_clase);

                        TextView lugarTextView = convertView.findViewById(R.id.text_lugar);

                        TextView fechaTextView = convertView.findViewById(R.id.text_fecha_hora);

                        JSONObject reserva = getItem(position);
                        if (reserva != null) {
                            try {
                                String clase = reserva.getString("clase");

                                String lugar = reserva.getString("lugar");

                                String fecha = reserva.getString("fechaHora");
                                claseTextView.setText(clase);

                                lugarTextView.setText(lugar);

                                fechaTextView.setText(fecha);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        return convertView;
                    }
                };

                listViewReservas.setAdapter(adapter);
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

        TextView btnvolver = (TextView) findViewById(R.id.button_volver);
        btnvolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerReservas.this,HomeActivity.class));
            }
        });
    }
}