package com.example.mies_dinapen.UI;

import static com.example.mies_dinapen.UtilClass.MetodosConvert.convertBinarioAudio;

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
import com.example.mies_dinapen.Retrofit.BaseDato;
import com.example.mies_dinapen.modelos.Audio;
import com.example.mies_dinapen.modelos.Incidente;
import com.example.mies_dinapen.service.Servicios;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_MenuIncidencia extends AppCompatActivity implements View.OnClickListener {



    TextView txtlongitud , txtOperador , txtlatitud , GetDateTime;
    EditText txtreferencia , txtNombre;
    Button btnConsultar , btnAudio, BtnGuardar;
    ImageButton BtonTomarFoto, BTonSaveImagen, btnMap;
    ImageView ivFoto;
    ProgressDialog progressDialog;


    // Valores globales estaticos
    public static float ilatitud = 0.0f;
    public static float ilongitud;
    public static String sLatitud;
    public static String sLongitud;
    public static String idI = "";
    public static String nombreOperador;
    public static String date;
    public static String rutaImagen;
    public static Bitmap imgBitmap;
    public static int idOperador;
    public static Servicios servicios;

    public static ArrayList<String> lstA;
    public static ArrayList<String> lstF;


    //permisos para tomar fotos, permiso de la camara, permiso que se guarda en el movil
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuincidencia);

        setTitle("INCIDENTE");

        initUI();
        initdata();
        setViewData();
        initEventClick();
        checkPermise();
        initDataBase();
    }

    private void initDataBase(){
        servicios = BaseDato.getConnetion().create(Servicios.class);
    }

    private void initUI() {
        ivFoto = findViewById(R.id.ImagenFoto);

        txtreferencia = findViewById(R.id.edtxtReferencia);
        txtNombre = findViewById(R.id.txtNombreRepresentante);
        GetDateTime = findViewById(R.id.txthora);
        txtlatitud = findViewById(R.id.txtAreaLatitud);
        txtlongitud = findViewById(R.id.txtAreaLongitud);
        txtOperador = findViewById(R.id.txtOperador);

        BTonSaveImagen = findViewById(R.id.botonGuardar);
        BtonTomarFoto = findViewById(R.id.BtnTomarFotos);
        btnConsultar = findViewById(R.id.btnConsultaR);
        btnMap = findViewById(R.id.btnDireccion);

        btnAudio = findViewById(R.id.btnAudio);
        BtnGuardar = findViewById(R.id.GuardarBtn);
    }

    private void initdata(){
        nombreOperador = getIntent().getStringExtra("nombre");
        idOperador = getIntent().getIntExtra("id", 0);
        date = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss").format(new Date());
        lstA = new ArrayList<>();
        lstF = new ArrayList<>();
    }

    private void setViewData(){
        txtOperador.setText(nombreOperador);
        GetDateTime.setText(date);

        if (!txtlatitud.getText().equals("Asignando")) {
            locationStart();
        }
    }

    private void initEventClick(){

        BtonTomarFoto.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
        BTonSaveImagen.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        BtnGuardar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btnConsultar) {
            Intent intent = new Intent(this, Activity_ConsultarCedula.class);
            startActivity(intent);
        }
        if (view == BtonTomarFoto) {
            PermisesCamara();
            checkPermise();
        }
        if (view == BTonSaveImagen) {
            new AlertDialog.Builder(this)
                    .setTitle("Informe")
                    .setMessage("¿Desea generar Guardar Esta Imagen?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Activity_MenuIncidencia.this, "Agregado", Toast.LENGTH_LONG);
                            lstF.add(rutaImagen);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Activity_MenuIncidencia.this, "Imagen no guardador", Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

        }
        if (view == btnMap) {
            String map = "http://maps.google.com/maps?q="+txtlatitud.getText()+","+txtlongitud.getText();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            startActivity(i);
        }
        if (view == btnAudio) {
            Intent i2 = new Intent(Activity_MenuIncidencia.this, RecordActivity.class);
            i2.putExtra("idIncendicia", idI);
            startActivityForResult(i2, 2);
        }
        if (view == BtnGuardar) {
            dialogoGuardarIncidencia();
        }

    }


    //Yo creo que no hace nda, mas solo iniciar un metodo location?
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

    public void checkPermise() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    private void PermisesCamara(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Requiere permiso", Toast.LENGTH_SHORT).show();
            checkPermise();
        } else {
            tomarFoto();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            ivFoto.setImageBitmap(imgBitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("datos");
            lstA.add(result);
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



    private void finalizar() {
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

        ;
    }

    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //  if(intent.resolveActivity(getPackageManager())!=null){
        File imagenArchivo = null;
        try {
            imagenArchivo = crearFileImagen();
        } catch (IOException ex) {
            Log.e("Error", ex.toString());
        }
        if (imagenArchivo != null) {
            Uri fotoUri = FileProvider.getUriForFile(this, "com.cdp.camara.fileprovider", imagenArchivo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
            startActivityForResult(intent, 1);
        }
    }


    private File crearFileImagen() throws IOException {
        int count = 0, auto;
        String nombreImagen = null;
        for (int i = 0; i <= 0; i++) {
            auto = count++;
            nombreImagen = "foto_" + nombreOperador
                    + "_" + auto;
        }
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".png", directorio);
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
            if (sLatitud == null) {
                sLatitud = String.valueOf(loc.getLatitude());
                sLongitud = String.valueOf(loc.getLongitude());
                txtlatitud.setText(sLatitud);
                txtlongitud.setText(sLongitud);
                ilatitud = (float) loc.getLatitude();
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

    private void insertIncidencia(Incidente incidente){
        Call<String> call = servicios.postIncidenciaTabla(incidente);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("TAG", "onResponse: " + response.message() + " + " + response.body() );
                if(!response.body().equals("")){
                    insertFoto(response.body());
                    insertarAudio(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onResponse: " + t + " + " +call );
            }
        });
    }



    private void insertFoto(String body) {
    }
    private void insertarAudio(String id) {
        for (int i = 0; i < lstA.size(); i++) {
            String nombre = id + "_Audio_" + i;
            String path = "https://miesdinapen.tk/api/Audios/Uploads/" + nombre + ".mp3";
            String var = null;
            try {
                var = convertBinarioAudio(lstA.get(i));
                Log.e("TAG", "insertarAudio: " + var );
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            insertarAudioFile(nombre, var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Audio audio = new Audio(id, path, date1);
            //
            insertarAudioBase(audio);
        }
    }

    private void insertarAudioFile(String nombre, String var){

        HashMap<String, String> params = new HashMap<>();
        params.put("audio", var);
        params.put("nombre", nombre);

        Call<String> call = servicios.postAudioFile(var, nombre);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("TAG", "onResponse: " + response.body() + call + response.raw() + response.message() );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onResponse: " , t );
            }
        });
    }

    private void insertarAudioBase(Audio audio){
        Call<String>  call = servicios.postAudioBase(audio);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("TAG", "onResponse: " + response.body() );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onResponse: " , t );
            }
        });
    }

    private void dialogoGuardarIncidencia() {
        new AlertDialog.Builder(this)
                .setTitle("Mensaje de Alerta")
                .setMessage("Finalizar la incidencia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Incidente incidente = new Incidente(1, ilatitud, ilongitud, date, 1, idOperador, txtreferencia.getText().toString(), txtNombre.getText().toString());
                        insertIncidencia(incidente);

                        //ServiceInsert controlDeEnvio = new ServiceInsert(lstA, lstF, incidente, Activity_MenuIncidencia.this);
                        // controlDeEnvio.execute();
                        //finalizar();
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Activity_MenuIncidencia.this, "Se Cancelo La Accion", Toast.LENGTH_LONG);

                    }
                }).show()
        ;
    }
}