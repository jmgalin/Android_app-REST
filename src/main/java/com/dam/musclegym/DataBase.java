package com.dam.musclegym;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*String qry1 = "create table users(usuario text, email text, contrasena text)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table noticias(id integer primary key autoincrement, titulo text, descripcion text, fecha_creacion text)";
        sqLiteDatabase.execSQL(qry2);

        // insertar dos noticias con la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fechaActual = sdf.format(new Date());

        sqLiteDatabase.execSQL("INSERT INTO noticias (titulo, descripcion, fecha_creacion) VALUES ('Noticia 1', 'Descripción de la noticia 1', '" + fechaActual + "')");
        sqLiteDatabase.execSQL("INSERT INTO noticias (titulo, descripcion, fecha_creacion) VALUES ('Noticia 2', 'Descripción de la noticia 2', '" + fechaActual + "')");
        sqLiteDatabase.execSQL("INSERT INTO noticias (titulo, descripcion, fecha_creacion) VALUES ('Cerrado por festivo', 'Cerrado por celebración del Día del Trabajador', '05-05-2023 10:09:24')");

        String qry3 = "CREATE TABLE reservas (id INTEGER PRIMARY KEY AUTOINCREMENT, clase TEXT, profesor TEXT, lugar TEXT, plazas_disponibles INTEGER, fecha_hora TEXT, usuarios_inscritos TEXT)";
        sqLiteDatabase.execSQL(qry3);


        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Yoga', 'Ana', 'Sala A', 10, '03-05-2023 15:30:00')");
        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Spinning', 'Juan', 'Sala B', 15, '04-05-2023 18:00:00')");
        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Gap', 'Carlos', 'Sala A', 12, '05-05-2023 14:00:00')");
        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Abdominales', 'Maria', 'Sala C', 8, '05-05-2023 15:00:00')");
        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Cardio', 'Jose', 'Sala B', 20, '06-05-2023 12:00:00')");
        sqLiteDatabase.execSQL("INSERT INTO reservas (clase, profesor, lugar, plazas_disponibles, fecha_hora) VALUES ('Boxeo', 'Laura', 'Sala D', 8, '06-05-2023 18:00:00')");
    */
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void registro(String usuario, String email, String contrasena){
        ContentValues cv = new ContentValues();
        cv.put("usuario", usuario);
        cv.put("email", email);
        cv.put("contrasena",contrasena);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users",null,cv);
        db.close();
    }

    // Define la variable estática
    public static String variableexternausuario;

    public int login(String usuario){

            variableexternausuario = usuario;

            return 1;
    }


    public String getVariableExternaUsuario() {
        return variableexternausuario;
    }

}
