package com.example.mies_dinapen.Audio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mies_dinapen.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {



    private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView filenameText;

    private boolean isRecording = false;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;
    private  String recordFile;

    private Chronometer timer;

    private static String key;

    ////********* EL ACTIVITY DEL GRABAR AUDIO*************///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
      //listBtn = findViewById(R.id.record_list_btn);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
        filenameText =findViewById(R.id.record_filename);

        key = getIntent().getStringExtra("idIncendicia");



/**       listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Lista");
                Intent i2 = new Intent(RecordActivity.this , AudioListActivity.class);
                startActivity(i2);
            }
        });*/
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording) {
                    //Stop Recording

                    // Change button image and set Recording state to false
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                    String path = stopRecording();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("datos",path);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();


                } else {
                    //Cheick permission to record audio
                    if(checkPermissions()) {
                        //Start Recording
                        startRecording();

                        // Change button image and set Recording state to false
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        isRecording = true;
                    }
                }
            }
        });



    }


    @SuppressLint("NewApi")
    private String stopRecording() {
        //Stop Timer, very obvious
        timer.stop();

        //Change text on page to file saved
        filenameText.setText("Recording Stopped, File Saved : " + recordFile);
        //Stop media recorder and set it to null for further use to record new audio
        String path =getExternalFilesDir(recordFile).getAbsolutePath();
    /**    String path = getActivity().getExternalFilesDir(recordFile).getAbsolutePath();;
      */


        String f = path;
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        return f;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public String mostrar(String path) {
        try {
            //return  new BASE64Encoder().encode(Files.readAllBytes(new File(ruta).toPath())); //otra forma
            return com.example.mies_dinapen.service.Base64.getEncoder().encodeToString(Files.readAllBytes(new File(path).toPath()));

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        //int count=0;

        //Get app external directory path getExternalFilesDir
        String recordPath = getExternalFilesDir("/").getAbsolutePath();

        //Get current date and time
        SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
        int count=0, auto;

        for(int i=0; i<=0; i++ ){
            auto=count++;
            recordFile = "incidencia_"+key
                    +"_"+auto
                    + formatter.format(now) + ".mp3";
        }



        filenameText.setText("Recording, File Name : " + recordFile);

        //Setup Media Recorder for recording
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            //Start Recording
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
        }
    }

}