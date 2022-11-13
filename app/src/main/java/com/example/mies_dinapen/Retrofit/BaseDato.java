package com.example.mies_dinapen.Retrofit;

import android.content.Context;

import org.json.JSONArray;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseDato {

    public static final String DBNAME="MiesDinapen.db";
    public static final String URL = "https://miesdinapen.tk/";
    public static Retrofit retrofit = null;

    public static Retrofit getConnetion(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
