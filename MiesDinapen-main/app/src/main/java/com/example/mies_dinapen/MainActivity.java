package com.example.mies_dinapen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mies_dinapen.BDSQLITE.BaseDeDatos;

import java.sql.DriverManager;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    //...
    Button iniciarSesion;
    private EditText et_cedula , et_contrasena;
    BaseDeDatos db;
    //...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("LOGIN");


        //Validacion logeo
        et_cedula = findViewById(R.id.et_cedula);
        et_contrasena = findViewById(R.id.et_contrasena);
        db = new BaseDeDatos(this);      //...
        iniciarSesion=(Button)findViewById(R.id.iniciar_sesiónBTN);
        Context c = this;

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast n= Toast.makeText(c,"acceso",Toast.LENGTH_LONG);

                Intent i1 = new Intent(MainActivity.this, com.example.mies_dinapen.ListIncidentes.class);
                startActivity(i1);
              /*
                if (verificar(view)==true){
                    String user = et_cedula.getText().toString();
                    String pass = et_contrasena.getText().toString();
                    boolean s = false;

                    if(s ==true){

                    }else{
                    }

                }*/
            }
        });
        //...
    }
    public boolean verificar(View v) {
        String clave=et_contrasena.getText().toString();
        if (clave.length()==0) {
            Toast notificacion= Toast.makeText(this,"La clave no puede quedar vacía",Toast.LENGTH_LONG);
            notificacion.show();
            return false;
        }else
            return true;

    }
}