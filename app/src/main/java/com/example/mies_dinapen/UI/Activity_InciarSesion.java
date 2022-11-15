package com.example.mies_dinapen.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.Retrofit.BaseDato;
import com.example.mies_dinapen.modelos.Operador;
import com.example.mies_dinapen.service.Servicios;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_InciarSesion extends AppCompatActivity implements View.OnClickListener {
    EditText cedulad;
    EditText passwordd;
    Button ingresarIn;

    Servicios servicios;
    Operador Operador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);
        this.initUi();
        this.initDataBase();
    }

    private void initUi() {
        cedulad = findViewById(R.id.editcedula);
        passwordd = findViewById(R.id.editpassword);
        ingresarIn = findViewById(R.id.btningresar);
        ingresarIn.setOnClickListener(this);
    }
    private void initDataBase(){
        servicios = BaseDato.getConnetion().create(Servicios.class);
    }

    @Override
    public void onClick(View view) {
        if(view == ingresarIn){
            getConsulta();
        }
    }

    private HashMap<String, String> getInputData() {
        HashMap<String, String> datosInput = new HashMap<>();
        datosInput.put("OperaNCedula", cedulad.getText().toString());
        datosInput.put("password", passwordd.getText().toString());
        return datosInput;
    }

    private void getConsulta(){
        Call<Operador> call = servicios.getOperoador(getInputData());
        call.enqueue(new Callback<Operador>() {
            @Override
            public void onResponse(Call<Operador> call, Response<Operador> response) {
                Operador operador = response.body();
                Log.e("TAG", "onResponse: " + operador);
                goMIesDinapenActivity(operador.getOperaNombres()+" "+ operador.getOperaApellido1(), operador.getIDOperador());
            }

            @Override
            public void onFailure(Call<Operador> call, Throwable t) {
                Toast.makeText(Activity_InciarSesion.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void MensajeToast(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG).show();
    }

    private void goMIesDinapenActivity(String nombre, int id){
        Intent intent = new Intent(Activity_InciarSesion.this, Activity_MenuIncidencia.class);
        intent.putExtra("nombre" , nombre );
        intent.putExtra("id" , id );
        startActivity(intent);
    }
}