package com.example.mies_dinapen.service;
import com.example.mies_dinapen.modelos.Operador;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OperadorUser {


    @POST("api/Operadores/selectOperadorLogin.php")
    public abstract Call<Operador> getOperoador(@Body HashMap<String, String> datos);



}
