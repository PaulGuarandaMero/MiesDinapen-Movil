package com.example.mies_dinapen;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class Mies_Dinapen extends AppCompatActivity implements View.OnClickListener {

    //************************ Tiempo Hora **************************************
    TextView GetDateTime;
    //************************ BASE DE DATOS *********************************
    BaseDeDatos DB;
    //*****************  CONEXION ****************************/
    private static final String Url1 = "https://miesdinapen.cf/api/Incidencias/insert.php";
    private static final String Url2 = "https://miesdinapen.cf/api/Fotos/insert.php";
    private static final String Url3 = "https://miesdinapen.cf/api/Audios/insert.php";
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
        System.out.println(nombreOperador);

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
    private void mostrarDialogo(){
        new AlertDialog.Builder(this)
                .setTitle("Mensaje de Alerta")
                .setMessage("Finalizar la incidencia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(lstA.get(1)!=null&&lstF.get(1)!=null){
                            try {
                                newIncidencia();
                                guardarlsta();
                                guardarlstf();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            onRestart();
                        }else {

                        }
                        finish();
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Mensaje","Se cancelo la accion");
                    }
                })
        ;
    }

    public void guardarlsta(){
        for (int i = 0; i < lstA.size(); i++){
            String s =lstA.get(i);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Audios audio = new Audios(idI,s,date1);
            ServiceTaskAudio servicioTask = new ServiceTaskAudio(this, Url3, audio);
            servicioTask.execute();
      }

    }
    public void guardarlstf(){
        for (int i = 0; i < lstA.size(); i++){
            // aqui se puede referir al objeto con arreglo[i];
            String s=lstA.get(i);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Fotos foto = new Fotos(idI,s,date1);
            ServicioTaskFotos servicioTaska = new ServicioTaskFotos(this, Url2, foto);
            servicioTaska.execute();
        }
    }

    public void newIncidencia() throws ExecutionException, InterruptedException {
        int idOperador = getIntent().getIntExtra("id", 0);
        Incidentes incidentes = new Incidentes(1, ilatitud, ilongitud, date, 1, idOperador);
          Servicio(incidentes);
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
        File imagen = File.createTempFile(nombreImagen,".jpg",directorio);
        rutaImagen = imagen.getAbsolutePath();
        return imagen;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            ivFoto.setImageBitmap(imgBitmap);
            System.out.println(rutaImagen);
        }
        if(requestCode ==2 && resultCode == RESULT_OK){
            //////add Audio a lst
            String result=data.getStringExtra("datos");
            lstA.add(result);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mostrar() {
        System.out.println(rutaImagen);

        lstF.add(rutaImagen);
    }


    @SuppressLint("NewApi")
    public String convertBinario(String ruta) {
        try {
            //return  new BASE64Encoder().encode(Files.readAllBytes(new File(ruta).toPath())); //otra forma
            return java.util.Base64.getEncoder().encodeToString(Files.readAllBytes(new File(ruta).toPath()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.botonTomarFoto) {
            checkPermise();
        } else if (id == R.id.botonGuardar) {

            mostrar();
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

    // CAMARA\
    private void initUI() {
        BTonSaveImagen = findViewById(R.id.botonGuardar);
        BtonTomarFoto = findViewById(R.id.BtnTomarFotos);
        btnMap = findViewById(R.id.btnDireccion);
        BtnGuardar = (FloatingActionButton) findViewById(R.id.GuardarBtn);
    }


    public void Servicio(Incidentes incidentes) throws ExecutionException, InterruptedException {
        ServicioTask servicioTask = new ServicioTask(this, Url1, incidentes);
        servicioTask.execute();
        final String s = servicioTask.get();
        idI = s;
    }


  /*  public void guardar(Incidentes incidentes){
        incidentesController = new IncidentesController(Mies_Dinapen.this);

        long id = incidentesController.addIncidente(incidentes);
        if (id == -1) {
            // De alguna manera ocurrió un error
            Toast.makeText(Mies_Dinapen.this, "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
        } else {
            // Terminar
            finish();
        }

        // String imagen = new String(imagenFoto.toString());
        //    String IDLugar = +1;
        String coordenadaslatitud = latitud;
        String coordenadaslongitud = longitud;
        //   String audios =
        System.out.println("PRUEBA DE DATOS LATITUD Y LONGITUD *******************" + coordenadaslatitud + coordenadaslongitud);
        if (!TextUtils.isEmpty(coordenadaslatitud) || !TextUtils.isEmpty(coordenadaslongitud)) {
            ///  insert = DB.insertDataCoordenadas(coordenadaslatitud, coordenadaslongitud); // guardo coordenadas

            //guardo BLOB de imagen
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Incidentes/Fotos/";
            File f = new File(imageDir + nombreImagenIncidente + ".jpg");
            try { // convierto en binario antes de persistir
                FileInputStream fl = new FileInputStream(f);
                byte[] arr = new byte[(int) f.length()];
                fl.read(arr);
                fl.close();
                DB.insertDataFotos(arr, new Date());
                Toast.makeText(Mies_Dinapen.this, "Foto y coordenadas guardadas", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Mies_Dinapen.this, "Primero guarde la img", Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(Mies_Dinapen.this, "Error en coordenadas", Toast.LENGTH_SHORT).show();
        }
    }


*/


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
    //*************************************************************************************************
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