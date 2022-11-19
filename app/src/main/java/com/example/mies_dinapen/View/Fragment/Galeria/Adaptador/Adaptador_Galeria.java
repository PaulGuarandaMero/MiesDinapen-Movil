package com.example.mies_dinapen.View.Fragment.Galeria.Adaptador;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.View.Fragment.ReproducirAudio.Adaptador.Adaptador_ReproAudio;

import java.util.ArrayList;

public class Adaptador_Galeria extends RecyclerView.Adapter<Adaptador_Galeria.ViewHolder>  {

    Activity_Contenedor activityContenedor;
    LayoutInflater layoutInflater;
    ArrayList<String> datos;

    public Adaptador_Galeria(Activity_Contenedor activityContenedor)  {
        this.activityContenedor = activityContenedor;
        this.layoutInflater = LayoutInflater.from(activityContenedor);
        this.datos = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adaptador_Galeria.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_galeria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_Galeria.ViewHolder holder, int position) {
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

        ImageView foto;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.I_FotoL_ImageView_Foto);
            delete = itemView.findViewById(R.id.I_FotoL_Button_Delete);
            delete.setOnClickListener(this);
        }

        public void bindata(String path) {
            foto.setImageURI(Uri.parse(path));
        }

        @Override
        public void onClick(View view) {
            if(view == delete){
                activityContenedor.getLstF().remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }
}
