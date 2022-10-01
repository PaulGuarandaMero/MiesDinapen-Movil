package com.example.mies_dinapen.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.camera.core.impl.annotation.ExecutedBy;

import com.example.mies_dinapen.modelos.Incidentes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class ServicioTask extends AsyncTask<Void, Void, String> {
    //variables del hilo

    private Context httpContext;//contexto
    ProgressDialog progressDialog;//dialogo cargando
    public static String resultadoapi="";
    private String Id="";
    public String linkrequestAPI="";//link  para consumir el servicio rest
    public Incidentes incidentes;

    //constructor del hilo (Asynctask)
    public ServicioTask(Context ctx, String linkAPI, Incidentes incidentes){
        this.httpContext=ctx;
        this.linkrequestAPI=linkAPI;
        this.incidentes=incidentes;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result= null;
        String wsURL = linkrequestAPI;
        URL url = null;
        Toast.makeText(httpContext,"ESAASAS",Toast.LENGTH_LONG);
        try {
            // se crea la conexion al api: http://localhost:15009/WEBAPIREST/api/persona
            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //crear el objeto json para enviar por POST
            String query ="{\n" +
                    ""+"\"IDOperador\":"+incidentes.getIdOperador() +"," +
                    "\n \"IDOrganCooperante\":"+incidentes.getIdOrgOperador()+","+
                    "\n \"IDPersonaIntervenida\":"+incidentes.getIdPersona()+","+
                    "\n \"Latitud\":"+incidentes.getLatitud()+","+
                    "\n \"Longitud\":"+incidentes.getLogitud()+","+
                    "\n \"FechaRegistro\":\""+incidentes.getFecha()+"\"" +
                    "\n}"
                    ;

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");// se puede cambiar por delete ,put ,etc
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);


//            System.out.println(query);

            //OBTENER EL RESULTADO DEL REQUEST
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int responseCode=urlConnection.getResponseCode();// conexion OK?
            if(responseCode== HttpURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;

                }
                in.close();
                result= sb.toString();
            }
            else{
                result= new String("Error: "+ responseCode);


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return  result;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        resultadoapi=s;
     //   Toast.makeText(httpContext,resultadoapi,Toast.LENGTH_LONG).show();//mostrara una notificacion con el resultado del request

    }


}