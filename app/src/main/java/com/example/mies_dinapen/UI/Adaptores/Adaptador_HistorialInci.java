package com.example.mies_dinapen.UI.Adaptores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.UI.Activity_ConsultarCedula;
import com.example.mies_dinapen.modelos.Item_historial;

import java.util.ArrayList;

public class Adaptador_HistorialInci extends RecyclerView.Adapter<Adaptador_HistorialInci.ViewHolder> {

    Activity_ConsultarCedula activity_consultarCedula;
    LayoutInflater layoutInflater;
    ArrayList<Item_historial> datos;

    public Adaptador_HistorialInci(Activity_ConsultarCedula activity_consultarCedula) {
        this.activity_consultarCedula = activity_consultarCedula;
        this.layoutInflater = LayoutInflater.from(activity_consultarCedula);
        this.datos = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adaptador_HistorialInci.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_historialinsidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_HistorialInci.ViewHolder holder, int position) {
        holder.bindata(datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setDatos(ArrayList<Item_historial> datos_item){
        this.datos = datos_item;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre , fecha , estado , id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.I_BuscarR_TextView_NombreOpe);
            fecha = itemView.findViewById(R.id.I_BuscarR_TextView_Fecha);
            estado = itemView.findViewById(R.id.I_BuscarR_TextView_idEstado);
            id = itemView.findViewById(R.id.I_BuscarR_TextView_IdInter);

        }

        public void bindata(Item_historial item_historial) {
            nombre.setText(item_historial.getOperador().getOperaNombres()+" "+item_historial.getOperador().getOperaApellido1());
            fecha.setText(item_historial.getFechaIntervencion());
            estado.setText(item_historial.getEstado());
            id.setText(item_historial.getIDIntervencion());


        }
    }
}
