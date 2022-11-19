package com.example.mies_dinapen.View.Fragment.MenuIncidencia;

import static android.app.Activity.RESULT_OK;
import static com.example.mies_dinapen.Controller.UtilClass.MetodosConvert.convertAudioEncoded;
import static com.example.mies_dinapen.Controller.UtilClass.MetodosConvert.convertImageEncoded;
import com.example.mies_dinapen.Controller.Ubicacion.Localizacion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDeepLinkRequest;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mies_dinapen.Model.Audio;
import com.example.mies_dinapen.Model.Foto;
import com.example.mies_dinapen.Model.Incidente;
import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.databinding.FragmentMenuIncidenciaBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuIncidenciaFragment extends Fragment implements View.OnClickListener {

    FragmentMenuIncidenciaBinding viewMain;
    Activity_Contenedor activity;

    Integer idOperador;
    String nombreOperador, ImagePath;


    public MenuIncidenciaFragment() {
    }

    public static MenuIncidenciaFragment newInstance(String param1, String param2) {
        MenuIncidenciaFragment fragment = new MenuIncidenciaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentMenuIncidenciaBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        initdata();
        setViewData();
        initEventClick();


        return viewMain.getRoot();
    }




    private void initdata(){
        nombreOperador = activity.getOperador().getOperaNombres()
                + " " + activity.getOperador().getOperaApellido1()
                + " " + activity.getOperador().getOperaApellido2();
        idOperador = activity.getOperador().getIDOperador();
        viewMain.FMenuITextViewCantidadAudios.setText("Total de audios: " + activity.getLstA().size());
        viewMain.FMenuITextViewCantidadFotos.setText("Total de fotos: " + activity.getLstF().size());
    }

    private void setViewData(){
        viewMain.FMenuITextViewOperador.setText(nombreOperador);
        viewMain.FMenuITextViewFecha.setText(new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss").format(new Date()));
        IniciarLocation();
    }

    private void initEventClick(){
        viewMain.FMenuIButtonGrabar.setOnClickListener(this);
        viewMain.FMenuIButtonMap.setOnClickListener(this);
        viewMain.FMenuIButtonTomarFoto.setOnClickListener(this);
        viewMain.FMenuIButtonGuardarFoto.setOnClickListener(this);
        viewMain.FMenuIButtonGuardarIncidencia.setOnClickListener(this);

        viewMain.FMenuITextViewCantidadAudios.setOnClickListener(this);
        viewMain.FMenuITextViewCantidadFotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == viewMain.FMenuITextViewCantidadAudios) {
            Navigation.findNavController(view).navigate(R.id.action_menuIncidenciaFragment_to_reproducirAudioFragment);
        }
        if (view == viewMain.FMenuITextViewCantidadFotos) {
            Navigation.findNavController(view).navigate(R.id.action_menuIncidenciaFragment_to_galeriaFragment);
        }
        if (view == viewMain.FMenuIButtonGrabar) {
            Navigation.findNavController(view).navigate(R.id.action_menuIncidenciaFragment_to_grabarAudioFragment);
        }
        if (view == viewMain.FMenuIButtonMap) {
            String map = "http://maps.google.com/maps?q="+viewMain.FMenuITextViewLatitud.getText()+","+ viewMain.FMenuITextViewLongitud.getText();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            startActivity(i);
        }
        if (view == viewMain.FMenuIButtonTomarFoto) {
            capturarFoto();
        }
        if (view == viewMain.FMenuIButtonGuardarFoto) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Informe")
                    .setMessage("¿Desea Guardar Esta Imagen?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "Agregado", Toast.LENGTH_LONG).show();
                            activity.getLstF().add(ImagePath);
                            viewMain.FMenuITextViewCantidadFotos.setText("Total de fotos: "+activity.getLstF().size());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "Imagen no guardador", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        }
        if (view == viewMain.FMenuIButtonGuardarIncidencia) {
            guardarIncidencia();
        }

    }



    private void capturarFoto() {
        int count = 0, auto;
        String nombreImagen = null;
        for (int i = 0; i <= 0; i++) {
            auto = count++;
            nombreImagen = "foto_" + nombreOperador
                    + "_" + auto;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = saveFoto(nombreImagen);
        ImagePath = imageFile.getAbsolutePath();
        if (ImagePath != null) {
            Uri imageUri = FileProvider.getUriForFile(getContext(),getContext().getPackageName()+".provider",imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            camaralauncher.launch(intent);
        }
    }

    ActivityResultLauncher<Intent> camaralauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                viewMain.FMenuIImageVFoto.setImageBitmap(BitmapFactory.decodeFile(ImagePath));
            }
        }
    });

    private File saveFoto(String path){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, path);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Mies-Dinapen/"+idOperador);
            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri fotoUri = getActivity().getContentResolver().insert(collection,values);
            this.pushFotoOut(0, fotoUri , null);
            values.clear();
            getActivity().getContentResolver().update(fotoUri,values,null,null);
            return new File(getRealPathFromURI(getContext(), fotoUri));
        }else{
            String fotoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String fotoName = path+".png";
            File fotoFile = new File(fotoDir,fotoName);
            this.pushFotoOut(1,null, fotoFile);
            return fotoFile;
        }
    }

    private void pushFotoOut(int opcionsdk , Uri path , File file){
        OutputStream outputStream = null;
        switch (opcionsdk){
            case 0:
                try {
                    outputStream = getActivity().getContentResolver().openOutputStream(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            case 1:
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
        }
    }

    private void guardarIncidencia() {
        new AlertDialog.Builder(getContext())
                .setTitle("Mensaje de Alerta")
                .setMessage("Finalizar la incidencia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Incidente incidente = new Incidente(
                                1,
                                Float.parseFloat(viewMain.FMenuITextViewLatitud.getText().toString()),
                                Float.parseFloat(viewMain.FMenuITextViewLongitud.getText().toString()),
                                viewMain.FMenuITextViewFecha.getText().toString(),
                                1,
                                idOperador,
                                viewMain.FMenuIEditTextReferencia.getText().toString(),
                                viewMain.FMenuIEditTextRepresentante.getText().toString()
                        );
                        insertIncidencia(incidente);
                        finalizar();
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Se Cancelo La Accion", Toast.LENGTH_LONG);

                    }
                }).show()
        ;
    }

    private void insertIncidencia(Incidente incidente) {
        Call<String> call = activity.getServicios().postIncidenciaTabla(incidente);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("TAG", "onResponse: " + response.message() + " + " + response.body() );
                if(!response.body().equals("")){
                    insertFoto(response.body());
                    insertarAudio(response.body());
                    Toast.makeText(getContext(), "Id de la Incidencia " + response.body() , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onResponse: " + t + " + " +call );
            }
        });
    }

    private void insertFoto(String id) {
        for (int q = 0; q < activity.getLstF().size(); q++) {
            Bitmap bits = BitmapFactory.decodeFile(activity.getLstF().get(q));
            String nombre = id + "_Fotos_" + q;
            String path = "https://miesdinapen.tk/api/Fotos/Uploads/" + nombre + ".png";
            String var = convertImageEncoded(bits);
            //
            insertarFotoFile(nombre, var);
            SimpleDateFormat simpleHourFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            String date1 = simpleHourFormat.format(new Date());
            Foto foto = new Foto(id, path, date1);
            //
            insertarFotoBase(foto);

        }
    }

    private void insertarFotoFile(String nombre, String var){

        HashMap<String, String> params = new HashMap<>();
        params.put("foto", var);
        params.put("nombre", nombre);

        Call<String> call = activity.getServicios().postFotoFile(var, nombre);
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

    private void insertarFotoBase(Foto foto){
        Call<String> call = activity.getServicios().postFotoBase(foto);
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

    private void insertarAudio(String id) {
        for (int i = 0; i < activity.getLstA().size(); i++) {
            String nombre = id + "_Audio_" + i;
            String path = "https://miesdinapen.tk/api/Audios/Uploads/" + nombre + ".mp3";
            String var = null;
            try {
                var = convertAudioEncoded(activity.getLstA().get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("TAG", "insertarAudio: " + var );
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

        Call<String> call = activity.getServicios().postAudioFile(var, nombre);
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
        Call<String>  call = activity.getServicios().postAudioBase(audio);
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

    private void finalizar() {
        new AlertDialog.Builder(getContext())
                .setTitle("Informe")
                .setMessage("¿Desea generar nuevo Incidente?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearView();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                })
                .show();
        ;
    }

    private void clearView(){
        activity.getLstF().clear();
        activity.getLstA().clear();
        viewMain.FMenuITextViewCantidadFotos.setText(activity.getLstF().size()+"");
        viewMain.FMenuITextViewCantidadAudios.setText(activity.getLstA().size()+"");
        viewMain.FMenuIEditTextReferencia.setText("");
        viewMain.FMenuIEditTextRepresentante.setText("");
        viewMain.FMenuIImageVFoto.setImageBitmap(null);
    }

    private void IniciarLocation() {

        LocationManager mlocManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion(viewMain, getContext());

        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}