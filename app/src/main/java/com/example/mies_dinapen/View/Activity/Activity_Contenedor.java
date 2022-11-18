package com.example.mies_dinapen.View.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.mies_dinapen.Controller.Retrofit.Retrofit;
import com.example.mies_dinapen.Model.Operador;
import com.example.mies_dinapen.databinding.ActivityContendorBinding;
import com.example.mies_dinapen.service.Servicios;

import java.util.ArrayList;

public class Activity_Contenedor extends AppCompatActivity {

    ActivityContendorBinding viewMain;
    Servicios servicios;

    ArrayList<String> lstA , lstF;
    Operador operador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMain = ActivityContendorBinding.inflate(getLayoutInflater());
        setContentView(viewMain.getRoot());
        this.getPermises();
        lstA = new ArrayList<>();
        lstF = new ArrayList<>();
        operador = new Operador();
        servicios = Retrofit.getConnetion().create(Servicios.class);
    }

    public Servicios getServicios() {
        return servicios;
    }

    public void getPermises() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO
                }, PackageManager.PERMISSION_GRANTED);
    }

    public ArrayList<String> getLstA() {
        return lstA;
    }

    public void setLstA(ArrayList<String> lstA) {
        this.lstA = lstA;
    }

    public ArrayList<String> getLstF() {
        return lstF;
    }

    public void setLstF(ArrayList<String> lstF) {
        this.lstF = lstF;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

}