package com.example.mies_dinapen.service;
import com.example.mies_dinapen.modelos.Item_historial;
import com.example.mies_dinapen.modelos.Operador;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OperadorUser {


    @POST("api/Operadores/selectOperadorLogin.php")
    public abstract Call<Operador> getOperoador(@Body HashMap<String, String> datos);

    @GET("api/Incidencias/selectByPersona.php")
    public abstract Call<ArrayList<Item_historial>> getHistorialIncidencia(@Query("cedula") String cedula);

    @GET("api/Incidencias/hasIntervencion.php")
    public abstract Call<ArrayList<Item_historial>> getCedulaCheck(@Query("cedula") String cedula);


}
