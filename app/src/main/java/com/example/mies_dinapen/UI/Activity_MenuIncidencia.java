package com.example.mies_dinapen.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.mies_dinapen.Audio.RecordActivity;
import com.example.mies_dinapen.R;
import com.example.mies_dinapen.modelos.Incidente;
import com.example.mies_dinapen.service.ServiceInsert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Activity_MenuIncidencia extends AppCompatActivity implements View.OnClickListener {

    //************************ Tiempo Hora **************************************

    //************************ BASE DE DATOS *********************************
    //*****************  CONEXION ****************************/
    private static final String Url1 = "https://miesdinapen.tk/api/Incidencias/insert.php";
    private static final String Url2 = "https://miesdinapen.tk/api/Fotos/insert.php";
    private static final String Url3 = "https://miesdinapen.tk/api/Audios/insert.php";
    private static final String Url4= "https://miesdinapen.tk/api/Incidencias/hasIntervencion.php";
    String UPLOAD_URL = "https://miesdinapen.tk/api/Fotos/Upload_F.php";
    String UPLOAD_URL2 = "https://miesdinapen.tk/api/Audios/Upload_A.php";


    TextView GetDateTime;
    TextView txtlatitud;
    TextView txtlongitud;
    TextView txtOperador;
    public EditText txtreferencia;
    public EditText txtCedula;
    public EditText txtNombre;
    Button btnConsultar;


    ProgressDialog progressDialog;//dialogo cargando

    // Valores globales estaticos
    public static float ilatitud = 0.0f;
    public static float ilongitud;
    public static String sLatitud;
    public static String sLongitud;
    public static String idI = "";
    public static String id;
    public static String referencia ="";
    public static String nombreOperador;
    public static String date;
    String rutaImagen;
    //FOTO
    ImageButton BtonTomarFoto, BTonSaveImagen, btnMap;
    Button btnAudio, BtnGuardar;
    ImageView ivFoto;

    //permisos para tomar fotos, permiso de la camara, permiso que se guarda en el movil
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 200;

    Bitmap imgBitmap;
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String KEY_AUDIO = "audio";
    static ArrayList<String> lstA;
    static ArrayList<String> lstF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuincidencia);

        setTitle("INCIDENTE");
        initUI();
        lstA=new ArrayList<>();
        lstF=new ArrayList<>();
        nombreOperador=getIntent().getStringExtra("nombre") ;
        txtOperador.setText(nombreOperador);
        //****************************** BASE DE DATOS **************************/////////////

        //Fotos

        BtonTomarFoto.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
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
        if(!txtlatitud.getText().equals("Asignando")){
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
        btnAudio = findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(Activity_MenuIncidencia.this, RecordActivity.class);
                i2.putExtra("idIncendicia", idI);
                startActivityForResult(i2,2);
            }
        });
        BtnGuardar = findViewById(R.id.GuardarBtn);
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

        if(id == R.id.btnConsultaR){
            Intent intent = new Intent(this, Activity_ConsultarCedula.class);
            startActivity(intent);
        }
        if (id == R.id.BtnTomarFotos) {
            checkPermise();
        }
        if (id == R.id.botonGuardar) {
            new AlertDialog.Builder(this)
                    .setTitle("Informe")
                    .setMessage("¿Desea generar Guardar Esta Imagen?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Activity_MenuIncidencia.this,"Agregado",Toast.LENGTH_LONG);
                            lstF.add(rutaImagen);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Activity_MenuIncidencia.this,"Imagen no guardador", Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

        }
        if (id == R.id.btnDireccion) {
            //corregir intent impplicito
/*            Intent i1 = new Intent(Activity_MenuIncidencia.this, MapsActivity.class);
            startActivity(i1);*/
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

        txtOperador = findViewById(R.id.txtOperador);
        txtOperador.setText(nombreOperador);
        GetDateTime = findViewById(R.id.txthora);
        txtlatitud = findViewById(R.id.txtAreaLatitud);
        txtlongitud = findViewById(R.id.txtAreaLongitud);
        ivFoto = findViewById(R.id.ImagenFoto);
        BTonSaveImagen = findViewById(R.id.botonGuardar);
        BtonTomarFoto = findViewById(R.id.BtnTomarFotos);
        txtreferencia = findViewById(R.id.edtxtReferencia);
        //txtCedula= findViewById(R.id.txtCedula);
        txtNombre=findViewById(R.id.txtNombreRepresentante);
        btnConsultar=findViewById(R.id.btnConsultaR);
        btnMap = findViewById(R.id.btnDireccion);
        BtnGuardar = findViewById(R.id.GuardarBtn);
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
                        int idOperador = getIntent().getIntExtra("id", 0);
                        Incidente incidente = new Incidente(1, ilatitud,ilongitud ,date, 1, idOperador,txtreferencia.getText().toString(),txtNombre.getText().toString() );
                        ServiceInsert controlDeEnvio = new ServiceInsert(lstA,lstF, incidente, Activity_MenuIncidencia.this);
                        controlDeEnvio.execute();
                        finalizar();
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Activity_MenuIncidencia.this,"Se Cancelo La Accion",Toast.LENGTH_LONG);

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

   // CAMARA\

     //***************************************************  UBICACION **************************************

    public class Localizacion implements LocationListener {
        Activity_MenuIncidencia mies_dinapen;


        public Activity_MenuIncidencia getMies_Dinapen() {
            return mies_dinapen;
        }
        public void setMainActivity(Activity_MenuIncidencia mies_dinapen) {
            this.mies_dinapen = mies_dinapen;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            sLatitud = null;
            sLongitud = null;
            if(sLatitud==null){
                sLatitud = String.valueOf(loc.getLatitude());
                sLongitud = String.valueOf(loc.getLongitude());
                txtlatitud.setText(sLatitud);
                txtlongitud.setText(sLongitud);
                ilatitud= (float) loc.getLatitude();
                ilongitud = (float) loc.getLongitude();

            }

        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            txtlatitud.setText(sLatitud);
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            txtlongitud.setText(sLongitud);
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