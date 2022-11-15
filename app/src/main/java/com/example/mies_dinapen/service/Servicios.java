package com.example.mies_dinapen.service;
import com.example.mies_dinapen.modelos.Audio;
import com.example.mies_dinapen.modelos.Incidente;
import com.example.mies_dinapen.modelos.Item_historial;
import com.example.mies_dinapen.modelos.Operador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Servicios {


    @POST("api/Operadores/selectOperadorLogin.php")
    public abstract Call<Operador> getOperoador(@Body HashMap<String, String> datos);

    @GET("api/Incidencias/selectByPersona.php")
    public abstract Call<ArrayList<Item_historial>> getHistorialIncidencia(@Query("cedula") String cedula);


    @POST("api/Incidencias/insert.php")
    public abstract Call<String> postIncidenciaTabla(@Body Incidente incidente);

    @POST("api/Audios/Upload_A.php")
    @FormUrlEncoded
    public abstract Call<String> postAudioFile(@Field("audio") String audio , @Field("nombre") String nombre );

    @POST("api/Audios/insert.php")
    public abstract Call<String> postAudioBase(@Body Audio audio);


}
