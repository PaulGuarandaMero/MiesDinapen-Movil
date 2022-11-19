package com.example.mies_dinapen.Controller.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    public static final String DBNAME="MiesDinapen.db";
    public static final String URL = "https://miesdinapen.tk/";
    public static retrofit2.Retrofit retrofit = null;

    public static retrofit2.Retrofit getConnetion(){
        if (retrofit==null){
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new retrofit2.Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }

}
