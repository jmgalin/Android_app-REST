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

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;

import java.util.ArrayList;
import java.util.List;


public class NoticiasActivity extends AppCompatActivity {

    private ListView listViewNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        listViewNoticias = findViewById(R.id.list_view_noticias);
        // Crear la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String URL = Constants.API_URL+ "/noticias";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Se ha recibido una lista de noticias
                Toast.makeText(getApplicationContext(),
                        "Lista de noticias recibida",
                        Toast.LENGTH_SHORT).show();

                List<JSONObject> noticiasList = new ArrayList<>();

                // Iterar sobre el JSONArray y agregar cada noticia a la lista
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject noticia = response.getJSONObject(i);
                        noticiasList.add(noticia);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Crear el adaptador personalizado para la lista de noticias
                ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(
                        getApplicationContext(),
                        R.layout.list_item_noticias,
                        R.id.titulo,
                        noticiasList
                ) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_noticias, parent, false);
                        }

                        TextView tituloTextView = convertView.findViewById(R.id.titulo);
                        TextView descripcionTextView = convertView.findViewById(R.id.descripcion);

                        JSONObject noticia = getItem(position);
                        if (noticia != null) {
                            try {
                                String titulo = noticia.getString("titulo");
                                String descripcion = noticia.getString("descripcion");
                                tituloTextView.setText(titulo);
                                descripcionTextView.setText(descripcion);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        return convertView;
                    }
                };

                listViewNoticias.setAdapter(adapter);
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
                startActivity(new Intent(NoticiasActivity.this,HomeActivity.class));
            }
        });
    }
}
