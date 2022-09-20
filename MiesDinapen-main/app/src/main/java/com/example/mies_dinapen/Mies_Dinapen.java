package com.example.mies_dinapen;

import static com.google.common.io.ByteStreams.toByteArray;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.PlatformVpnProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mies_dinapen.Audio.RecordActivity;
import com.example.mies_dinapen.BDSQLITE.BaseDeDatos;
import com.example.mies_dinapen.Mapa.MapsActivity;
import com.example.mies_dinapen.modelos.Audios;
import com.example.mies_dinapen.modelos.Fotos;
import com.example.mies_dinapen.modelos.Incidentes;
import com.example.mies_dinapen.service.ServiceTaskAudio;
import com.example.mies_dinapen.service.ServicioTask;
import com.example.mies_dinapen.service.ServicioTaskFotos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Mies_Dinapen extends AppCompatActivity implements View.OnClickListener {

    //************************ Tiempo Hora **************************************
    TextView GetDateTime;
    //************************ BASE DE DATOS *********************************
    BaseDeDatos DB;
    //*****************  CONEXION ****************************/
    private static final String Url1 = "https://miesdinapen.tk/api/Incidencias/insert.php";
    private static final String Url2 = "https://miesdinapen.tk/api/Fotos/insert.php";
    private static final String Url3 = "https://miesdinapen.tk/api/Audios/insert.php";
    String UPLOAD_URL = "https://miesdinapen.tk/api/Fotos/Upload_F.php";
    String UPLOAD_URL2 = "https://miesdinapen.tk/api/Audios/Upload_A.php";

    TextView txtlatitud;
    TextView txtlongitud;
    public TextView txtOperador;

    // Valores globales estaticos
    public static float ilatitud = 0.0f;
    public static float ilongitud;
    public static String sLatitud;
    public static String sLongitud;
    public static String idI = "";
    public static String id;
    public static String nombreOperador;
    public static String date;
    String rutaImagen;
    //FOTO
    ImageButton BtonTomarFoto, BTonSaveImagen, btnMap;
    FloatingActionButton btnAudio, BtnGuardar;


    //permisos para tomar fotos, permiso de la camara, permiso que se guarda en el movil
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 200;

    ImageView ivFoto;


    Bitmap imgBitmap;
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String KEY_AUDIO = "audio";
    static ArrayList<String> lstA;
    static ArrayList<String> lstF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mies_dinapen);
        setTitle("INCIDENTE");
        initUI();
        lstA=new ArrayList<>();
        lstF=new ArrayList<>();
        nombreOperador=getIntent().getStringExtra("nombre") ;
        //****************************** BASE DE DATOS **************************/////////////


        DB = new BaseDeDatos(this);
        //Fotos
        txtOperador = findViewById(R.id.txtOperador);
        txtOperador.setText(nombreOperador);
        GetDateTime = findViewById(R.id.txthora);
        txtlatitud = findViewById(R.id.txtAreaLatitud);
        txtlongitud = findViewById(R.id.txtAreaLongitud);
        ivFoto = findViewById(R.id.ImagenFoto);
        BtonTomarFoto.setOnClickListener(this);
        BTonSaveImagen.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        //******************************* Tiempo y hora*************
        SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        GetDateTime.setText(simpleHourFormat.format(new Date()));
        date = simpleHourFormat.format(new Date());
        //*********************************************************************************
        //******************************************************
        ////************************************************************************************************
        //UBICACION COORDENADAS
        if(txtlatitud.getText()!="Asignando"){
            locationStart();
        }
        //UI

        BtonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermise();
            }
        });
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        btnAudio = (FloatingActionButton) findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(Mies_Dinapen.this, RecordActivity.class);
                i2.putExtra("idIncendicia", idI);
                startActivityForResult(i2,2);
            }
        });
        BtnGuardar = (FloatingActionButton) findViewById(R.id.GuardarBtn);
        BtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.botonTomarFoto) {
            checkPermise();
        } else if (id == R.id.botonGuardar) {
            new AlertDialog.Builder(this)
                    .setTitle("Informe")
                    .setMessage("¿Desea generar Guardar Esta Imagen?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            almacenarImagen();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Mies_Dinapen.this,
                                    "Audio no guardador",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

        } else if (id == R.id.btnDireccion) {
            Intent i1 = new Intent(Mies_Dinapen.this, MapsActivity.class);
            startActivity(i1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        } else if (requestCode == REQUEST_PERMISSION_WRITE_STORAGE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        } else if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            ivFoto.setImageBitmap(imgBitmap);
        }
        if(requestCode ==2 && resultCode == RESULT_OK){
            //////add Audio a lst
            String result=data.getStringExtra("datos");
            lstA.add(result);
        }
    }

    private void initUI() {
        BTonSaveImagen = findViewById(R.id.botonGuardar);
        BtonTomarFoto = findViewById(R.id.BtnTomarFotos);
        btnMap = findViewById(R.id.btnDireccion);
        BtnGuardar = (FloatingActionButton) findViewById(R.id.GuardarBtn);
    }



    public void checkPermise() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                tomarFoto();
            }
        } else {
            tomarFoto();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }


    private void mostrarDialogo(){
        new AlertDialog.Builder(this)
                .setTitle("Mensaje de Alerta")
                .setMessage("Finalizar la incidencia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                 newIncidencia();
                                 guardarlsta();
                                 guardarlstf();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        finalizar();

                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Mies_Dinapen.this,"Se Cancelo La Accion",Toast.LENGTH_LONG);

                    }
                }).show()
        ;
    }

    private void finalizar(){
            new AlertDialog.Builder(this)
            .setTitle("Informe")
            .setMessage("¿Desea generar nuevo Incidente?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            recreate();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();

    ;}

    public void guardarlsta() throws ExecutionException, InterruptedException, IOException {
        for (int i = 0; i < lstA.size(); i++){
            String nombre = idI+"_Audio_"+i;
            String path = "https://miesdinapen.tk/api/Audios/Uploads/"+nombre+".mp3" ;
            String var =convertBinario(lstA.get(i));
            uploadAudio(nombre,var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Audios audio = new Audios(idI,path,date1);
            ServiceTaskAudio servicioTask = new ServiceTaskAudio(this, Url3, audio);
            servicioTask.execute();
      }
    }

    public void guardarlstf() throws ExecutionException, InterruptedException {
        for (int q = 0; q < lstF.size(); q++){
            Bitmap bits = BitmapFactory.decodeFile(lstF.get(q));
            String nombre = idI+"_Fotos_"+q;
            String path = "https://miesdinapen.tk/api/Fotos/Uploads/"+nombre+".png" ;
            String var = getStringImagen(bits);
            System.out.println(var);
            uploadImage(nombre,var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Fotos foto = new Fotos(idI,path,date1);
            ServicioTaskFotos servicioTaskFotos = new ServicioTaskFotos(this, Url2, foto);
            servicioTaskFotos.execute();
        }
    }

    public void newIncidencia() throws ExecutionException, InterruptedException {
        int idOperador = getIntent().getIntExtra("id", 0);
        Incidentes incidentes = new Incidentes(1, ilatitud, ilongitud, date, 1, idOperador);
        ServicioTask servicioTask = new ServicioTask(this, Url1, incidentes);
        servicioTask.execute();
        idI = servicioTask.get();
    }

    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      //  if(intent.resolveActivity(getPackageManager())!=null){
            File imagenArchivo = null;
            try {
                imagenArchivo= crearImagen();

            }catch (IOException ex){
                Log.e("Error", ex.toString());
            }
            if (imagenArchivo != null){
                Uri fotoUri = FileProvider.getUriForFile(this,"com.cdp.camara.fileprovider",imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
                startActivityForResult(intent,1);
            }

       // }

    }

    private File crearImagen() throws IOException {
        int count=0, auto;
        String nombreImagen=null;
        for(int i=0; i<=0; i++ ){
            auto=count++;
            nombreImagen = "foto_"+nombreOperador
                    +"_"+auto;
        }
        File directorio=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".png",directorio);
        rutaImagen = imagen.getAbsolutePath();
        return imagen;

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

    public void almacenarImagen() {
        Toast.makeText(Mies_Dinapen.this,"Agregado",Toast.LENGTH_LONG);
        lstF.add(rutaImagen);
    }


    public void uploadImage(String nombre , String imagen) {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(Mies_Dinapen.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Mies_Dinapen.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void uploadAudio(String nombre , String audio) {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(Mies_Dinapen.this, response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Mies_Dinapen.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage().toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

   // CAMARA\

     //***************************************************  UBICACION **************************************

    public class Localizacion implements LocationListener {
        Mies_Dinapen mies_dinapen;


        public Mies_Dinapen getMies_Dinapen() {
            return mies_dinapen;
        }
        public void setMainActivity(Mies_Dinapen mies_dinapen) {
            this.mies_dinapen = mies_dinapen;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();

            if(sLatitud==null){
                sLatitud = String.valueOf(loc.getLatitude());
                sLongitud = String.valueOf(loc.getLongitude());
                System.out.println(sLatitud +"   " +sLongitud);
                txtlatitud.setText(sLatitud);
                txtlongitud.setText(sLongitud);
                ilatitud= (float) loc.getLatitude();
                ilongitud = (float) loc.getLongitude();

            }

        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            txtlatitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            txtlongitud.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    }