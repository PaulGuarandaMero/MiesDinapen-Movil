package com.example.mies_dinapen.service;
import com.example.mies_dinapen.modelos.Operadores;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface OperadorUser {
    @GET("api/Operadores/select.php")
    public abstract Call<List<Operadores>>listOperador();
}
