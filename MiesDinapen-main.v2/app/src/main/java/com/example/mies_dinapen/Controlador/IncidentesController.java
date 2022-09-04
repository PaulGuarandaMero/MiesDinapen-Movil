package com.example.mies_dinapen.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mies_dinapen.BDSQLITE.BaseDeDatos;
import com.example.mies_dinapen.modelos.Incidentes;

import java.util.ArrayList;

public class IncidentesController {
    private BaseDeDatos bd;
    private String Nombre_Tabla = "Intervenciones";

    public IncidentesController (Context context){

        bd = new BaseDeDatos(context);
    }

    public long addIncidente(Incidentes incidentes){
        SQLiteDatabase baseDeDatos = bd.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
     //   valoresParaInsertar.put("idIntervencion",incidentes.getidIncidente());
        valoresParaInsertar.put("latitud",incidentes.getLatitud());
        valoresParaInsertar.put("logitud",incidentes.getLogitud());
     //   valoresParaInsertar.put("hora",incidentes.getHora());
        valoresParaInsertar.put("idoperador",incidentes.getIdOperador());
        return baseDeDatos.insert(Nombre_Tabla,null,valoresParaInsertar);
    }

    public ArrayList<Incidentes>obtenerList(){
        ArrayList<Incidentes> incidientes = new ArrayList<>();

        SQLiteDatabase baseDeDatos = bd.getReadableDatabase();
        String [] columnasAConsultar = {"idIntervencion","logitud","lagtitud","hora","idOperador"};
        Cursor cursor = baseDeDatos.query(
                Nombre_Tabla,
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null,
                null

        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return incidientes;

        }
        if (!cursor.moveToFirst()) return incidientes;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de mascotas
        do {
            // El 0 es el número de la columna, como seleccionamos
            // nombre, edad,id entonces el nombre es 0, edad 1 e id es 2
            String id_ObtenidoDeBD = cursor.getString(0);
            String logitudObtenidoDeBD = cursor.getString(1);
            String latitudObtenidoDeBD = cursor.getString(2);
            String horaObtenidoDeBD = cursor.getString(4);
            String idObtenidoDeBD = cursor.getString(4);
           /* Incidentes IncidentesObtenidaDeBD = new Incidentes(id_ObtenidoDeBD,logitudObtenidoDeBD,latitudObtenidoDeBD,horaObtenidoDeBD,idObtenidoDeBD);
            incidientes.add(IncidentesObtenidaDeBD);*/
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de mascotas :)
        cursor.close();
        return incidientes;
    }
    }

