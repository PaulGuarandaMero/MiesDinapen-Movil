package com.example.mies_dinapen.Retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseDato {

    public static final String DBNAME="MiesDinapen.db";
    public static final String URL = "https://miesdinapen.tk/";
    public static Retrofit retrofit = null;

    public static Retrofit getConnetion(){
        if (retrofit==null){
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }

}
