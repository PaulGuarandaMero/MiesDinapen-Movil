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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ControlDeEnvio extends AsyncTask<Void,Void,String>{
    private ArrayList<String> audios;
    private ArrayList<String> fotos;
    private Incidentes incidentes;
    private Context context;
    public static String id;
    ProgressDialog progressDialog;//dialogo cargando

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String KEY_AUDIO = "audio";
    private static final String Url1 = "https://miesdinapen.tk/api/Incidencias/insert.php";
    private static final String Url2 = "https://miesdinapen.tk/api/Fotos/insert.php";
    private static final String Url3 = "https://miesdinapen.tk/api/Audios/insert.php";
    private static final String UPLOAD_URL= "https://miesdinapen.tk/api/Fotos/Upload_F.php";
    private static final String UPLOAD_URL2 = "https://miesdinapen.tk/api/Audios/Upload_A.php";

    public ControlDeEnvio(ArrayList<String> audios, ArrayList<String> fotos, Incidentes incidentes, Context context) {
        this.audios = audios;
        this.fotos = fotos;
        this.incidentes = incidentes;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Subiendo foto", "por favor, espere");

    }

    @Override
    protected String doInBackground(Void... voids) {
        String alerta="";
        try {
            crearIncidencia();
            guardarlsta();
            guardarlstf();
            alerta = "Resultado deseado";
        } catch (ExecutionException e) {
            alerta = e.toString();
            Toast.makeText(context,""+e,Toast.LENGTH_LONG);
        } catch (InterruptedException e) {
            alerta = e.toString();
            Toast.makeText(context,""+e,Toast.LENGTH_LONG);
        } catch (IOException e) {
            alerta = e.toString();
            Toast.makeText(context,""+e,Toast.LENGTH_LONG);
        }


        return alerta;
    }

    public void crearIncidencia() throws ExecutionException, InterruptedException {
        ServicioTask servicioTask = new ServicioTask(context, Url1, incidentes);
        servicioTask.execute();
        id = servicioTask.get();
    }
    public void guardarlsta() throws ExecutionException, InterruptedException, IOException {
        for (int i = 0; i < audios.size(); i++){
            String nombre = id+"_Audio_"+i;
            String path = "https://miesdinapen.tk/api/Audios/Uploads/"+nombre+".mp3" ;
            String var =convertBinario(audios.get(i));
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

    public String convertBinario(String ruta) throws IOException {
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
        final ProgressDialog loading = ProgressDialog.show(context, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
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
        final ProgressDialog loading = ProgressDialog.show(context, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //    Toast.makeText(Mies_Dinapen.this, response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                // Toast.makeText(Mies_Dinapen.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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
