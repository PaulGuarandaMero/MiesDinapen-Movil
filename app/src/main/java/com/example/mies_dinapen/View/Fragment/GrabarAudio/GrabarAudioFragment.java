package com.example.mies_dinapen.View.Fragment.GrabarAudio;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.databinding.FragmentGrabarAudioBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GrabarAudioFragment extends Fragment implements View.OnClickListener{

    FragmentGrabarAudioBinding viewMain;
    Activity_Contenedor activity;

    Boolean isRecording;
    String fileName;
    String filepath;

    MediaRecorder recorder;


    public GrabarAudioFragment() {
        // Required empty public constructor
    }

    public static GrabarAudioFragment newInstance(String param1, String param2) {
        GrabarAudioFragment fragment = new GrabarAudioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentGrabarAudioBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();

        isRecording = false;
        fileName = "";
        filepath = "";

        viewMain.FGrabarAButtonRecordbtn.setOnClickListener(this);
        return viewMain.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view == viewMain.FGrabarAButtonRecordbtn){
            if(isRecording) {
                viewMain.FGrabarAButtonRecordbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_gray)));
                isRecording = false;
                stopRecording();
                activity.onBackPressed();
            } else {
                startRecording();
                viewMain.FGrabarAButtonRecordbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ligth_blue)));
                isRecording = true;
            }
        }
    }

    private String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        return formatter.format(new Date());
    }

    private void startRecording() {
        viewMain.FGrabarAChronometerRecordtimer.setBase(SystemClock.elapsedRealtime());
        viewMain.FGrabarAChronometerRecordtimer.start();
        fileName ="" + getDate();
        filepath = saveAudio(fileName);
        viewMain.FGrabarATextViewNombre.setText("Recording, File Name : " + fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filepath);
        activity.getLstA().add(filepath);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        viewMain.FGrabarAChronometerRecordtimer.stop();
        viewMain.FGrabarATextViewNombre.setText("Recording Stopped, File Saved : " + fileName);
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private String saveAudio(String path){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.DISPLAY_NAME, path);
            values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg");
            values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Podcasts/Mies-Dinapen/");
            Uri collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri auidoUri = getActivity().getContentResolver().insert(collection,values);
            this.pushAudioOut(0, auidoUri , null);
            values.clear();
            getActivity().getContentResolver().update(auidoUri,values,null,null);
            return getRealPathFromURI(getContext(),auidoUri);
        }else{
            String audioDir = Environment.getExternalStoragePublicDirectory("Podcasts")+"/Mies-Dinapen/";
            String audioName = path+".mp3";
            File audioFile = new File(audioDir,audioName);
            this.pushAudioOut(1,null, audioFile);
            return audioFile.getPath();
        }
    }

    private void pushAudioOut(int opcionsdk , Uri path , File file){
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

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Log.e("TAG", "getRealPathFromURI: " + contentUri );
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}