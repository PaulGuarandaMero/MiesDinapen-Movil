package com.example.mies_dinapen.service;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceConsult extends AsyncTask<Void,Void,String> {
    private static final String Url1 = "https://miesdinapen.tk/api/Incidencias/hasIntervencion.php?cedula=";
    private String cedula;
    private Context context;

    public ServiceConsult(String cedula, Context context) {
        this.cedula=cedula;
        this.context=context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return ConsultarCedula(cedula);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }

    private String ConsultarCedula(String cedula){

        String wsURL = Url1;
        URL url = null;
        try {
            Log.e("Error","Error1"+cedula);
            // se crea la conexion al api: http://localhost:15009/WEBAPIREST/api/persona
            url = new URL(wsURL+cedula);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");// se puede cambiar por delete ,put ,etc
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            int responseCode=urlConnection.getResponseCode();// conexion OK?
            if(responseCode== HttpURLConnection.HTTP_OK){
                String server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
                if (server_response.equals("true")){
                    return "Este número de cedula esta registrado";
                }
                else {
                    return "Este número de cedula no esta registrado";
                }
            }
            else{
                new String("Error: "+ responseCode);
                Log.e("Error","No esta");
                return "No esta";

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No encontrado";
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



}
