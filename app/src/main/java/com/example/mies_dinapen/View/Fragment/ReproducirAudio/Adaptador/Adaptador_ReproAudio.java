package com.example.mies_dinapen.View.Fragment.ReproducirAudio.Adaptador;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Adaptador_ReproAudio extends RecyclerView.Adapter<Adaptador_ReproAudio.ViewHolder> {

    Activity_Contenedor activityContenedor;
    LayoutInflater layoutInflater;
    ArrayList<String> datos;

    public Adaptador_ReproAudio(Activity_Contenedor activityContenedor) {
        this.activityContenedor = activityContenedor;
        this.layoutInflater = LayoutInflater.from(activityContenedor);
        this.datos = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adaptador_ReproAudio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_audioslista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_ReproAudio.ViewHolder holder, int position) {
        holder.bindata(datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setDatos(ArrayList<String> datos_item){
        this.datos = datos_item;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombre;

        Button delete, play;

        MediaPlayer mediaPlayer;
        int posicion = 0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.I_AudioL_TextView_Nombre);
            play = itemView.findViewById(R.id.I_AudioL_Button_Play);
            delete = itemView.findViewById(R.id.I_AudioL_Button_Delete);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    play.setBackground(activityContenedor.getResources().getDrawable(R.drawable.ic_play_24));
                }
            });
            play.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        public void bindata(String name) {
            File archivo = new File(name);
            nombre.setText(archivo.getName());
        }

        @Override
        public void onClick(View view) {
            if(view == delete){
                activityContenedor.getLstA().remove(getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view == play){
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setBackground(activityContenedor.getResources().getDrawable(R.drawable.ic_play_24));
                } else {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(activityContenedor.getLstA().get(getAdapterPosition()));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        play.setBackground(activityContenedor.getResources().getDrawable(R.drawable.ic_pause_24));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
