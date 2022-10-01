package com.example.mies_dinapen;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mies_dinapen.modelos.Audios;
import com.example.mies_dinapen.modelos.Fotos;
import com.example.mies_dinapen.modelos.Incidentes;
import com.example.mies_dinapen.service.ServiceTaskAudio;
import com.example.mies_dinapen.service.ServicioTask;
import com.example.mies_dinapen.service.ServicioTaskFotos;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ServiceInsert extends AsyncTask<Void,Void,String>{
    public static ArrayList<String> audios;
    public static ArrayList<String> fotos;
    public static Incidentes incidentes;
    public static Context context;
    public static String id="";
    ProgressDialog progressDialog;//dialogo cargando

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String KEY_AUDIO = "audio";
    private static final String Url1 = "https://miesdinapen.tk/api/Incidencias/insert.php";
    private static final String Url2 = "https://miesdinapen.tk/api/Fotos/insert.php";
    private static final String Url3 = "https://miesdinapen.tk/api/Audios/insert.php";
    private static final String UPLOAD_URL= "https://miesdinapen.tk/api/Fotos/Upload_F.php";
    private static final String UPLOAD_URL2 = "https://miesdinapen.tk/api/Audios/Upload_A.php";

    public ServiceInsert(ArrayList<String> audios, ArrayList<String> fotos, Incidentes incidentes, Context context) {
        this.audios = audios;
        this.fotos = fotos;
        this.incidentes = incidentes;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog.show(context, "Subiendo...", "Espere por favor");


    }

    @Override
    protected String doInBackground(Void... voids) {
        String alerta="";
        id = insertarIncidencia();
        if(id!="") {

            for (int i = 0; i < audios.size(); i++) {
                String nombre = id + "_Audio_" + i;
                String path = "https://miesdinapen.tk/api/Audios/Uploads/" + nombre + ".mp3";
                String var = null;
                try {
                    var = convertBinarioAudio(audios.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadAudio(nombre, var);
                SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
                String date1 = simpleHourFormat.format(new Date());
                Audios audio = new Audios(id, path, date1);
                insertarAudio(audio);
            }
            for (int q = 0; q < fotos.size(); q++) {
                Bitmap bits = BitmapFactory.decodeFile(fotos.get(q));
                String nombre = id + "_Fotos_" + q;
                String path = "https://miesdinapen.tk/api/Fotos/Uploads/" + nombre + ".png";
                String var = getStringImagen(bits);
                uploadImage(nombre, var);
                SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
                String date1 = simpleHourFormat.format(new Date());
                Fotos foto = new Fotos(id, path, date1);
                insertarFoto(foto);

            }
            alerta="Finalizo";
        }else{
            alerta="Error en la incidencia";
        }
        return alerta;
    }


    private String insertarIncidencia() {
        String result= null;
        String wsURL = Url1;
        URL url = null;
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



    private void insertarFoto(Fotos fotos){
        String wsURL = Url2;
        URL url = null;
        try {
            // se crea la conexion al api: http://localhost:15009/WEBAPIREST/api/persona
            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //crear el objeto json para enviar por POST
            String query ="{\n" +
                    ""+"\"IDIntervencion\":"+fotos.getIdIncidentes() +"," +
                    "\n \"FotoIncidente\":\""+fotos.getFile()+"\","+
                    "\n \"FechaRegistro\":\""+fotos.getFechaRegistro()+"\"" +
                    "\n}"
                    ;
            System.out.println(query);
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
                 sb.toString();
            }
            else{
                new String("Error: "+ responseCode);


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void insertarAudio(Audios audios){
        String wsURL = Url3;
        URL url = null;
        try {
            // se crea la conexion al api: http://localhost:15009/WEBAPIREST/api/persona
            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //crear el objeto json para enviar por POST
            String query ="{\n" +
                    ""+"\"IDIntervencion\":"+audios.getIdIncidete() +"," +
                    "\n \"Audio\":\""+audios.getFiles()+"\","+
                    "\n \"FechaRegistro\":\""+audios.getFechaRegistro()+"\"" +
                    "\n}"
                    ;
            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");// se puede cambiar por delete ,put ,etc
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
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
                 sb.toString();
            }
            else{
                new String("Error: "+ responseCode);


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /////Antiguo formas de incertar
    public void crearIncidencia() throws ExecutionException, InterruptedException {
        System.out.println("llego aqui crearIncidente");
        ServicioTask servicioTask = new ServicioTask(context, Url1, incidentes);
        servicioTask.execute();
        id = servicioTask.get();
        System.out.println(id);
        Toast.makeText(context,id,Toast.LENGTH_LONG);
    }

    public void guardarlsta() throws ExecutionException, InterruptedException, IOException {
        System.out.println("llego aqui crearIncidente");

        for (int i = 0; i < audios.size(); i++){
            String nombre = id+"_Audio_"+i;
            String path = "https://miesdinapen.tk/api/Audios/Uploads/"+nombre+".mp3" ;
            String var =convertBinarioAudio(audios.get(i));
            uploadAudio(nombre,var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Audios audio = new Audios(id,path,date1);
            ServiceTaskAudio servicioTask = new ServiceTaskAudio(context, Url3, audio);
            servicioTask.execute();
        }
    }

    public void guardarlstf() throws ExecutionException, InterruptedException {
        for (int q = 0; q < fotos.size(); q++){
            System.out.println("llego aqui crearIncidente");

            Bitmap bits = BitmapFactory.decodeFile(fotos.get(q));
            String nombre = id+"_Fotos_"+q;
            String path = "https://miesdinapen.tk/api/Fotos/Uploads/"+nombre+".png" ;
            String var = getStringImagen(bits);
            System.out.println(var);
            uploadImage(nombre,var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Fotos foto = new Fotos(id,path,date1);
            ServicioTaskFotos servicioTaskFotos = new ServicioTaskFotos(context, Url2, foto);
            servicioTaskFotos.execute();
        }
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] AudioBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(AudioBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String convertBinarioAudio(String ruta) throws IOException {
        String encoded = null;
        try {
            File file = new File(ruta);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encoded = Base64.encodeToString(bytes, 0);

            return encoded;
        }catch (Exception e){
        }
        return encoded;
    }

    public void uploadImage(String nombre , String imagen) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void uploadAudio(String nombre , String audio) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                //
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_AUDIO, audio);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}
