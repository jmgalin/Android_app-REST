package com.dam.musclegym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        CardView exit = findViewById(R.id.cardLogOut);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        CardView reserva = findViewById(R.id.cardReserva);
        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReservaActivity.class));
            }
        });

        CardView entrenos = findViewById(R.id.cardHealthArticles);
        entrenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WorkoutAct.class));
            }
        });

        CardView noticias = findViewById(R.id.cardLabNoticias);
        noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NoticiasActivity.class));
            }
        });

        CardView calcularimc = findViewById(R.id.cardIMC);
        calcularimc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CalcularIMC.class));
            }
        });

        CardView vereservas = findViewById(R.id.cardVerReserva);
        vereservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, VerReservas.class));
            }
        });

        CardView realizarpago = findViewById(R.id.cardPayment);
        realizarpago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
            }
        });

        // Obtener el TextView del CardView
        CardView cardViewSaludo = findViewById(R.id.cardsaludo);
        textViewUsuario = cardViewSaludo.findViewById(R.id.nombreusuario);

        // Obtener el nombre de usuario actual desde la base de datos
        DataBase db = new DataBase(getApplicationContext(), "musclegym.db", null, 1);
        String usuarioActual = db.getVariableExternaUsuario();

        // Establecer el nombre de usuario en el TextView
        textViewUsuario.setText("BIENVENIDO\n" + usuarioActual);
    }
}