package com.example.mies_dinapen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.modelos.Incidentes;

import java.util.List;

public class AdaptadorIncidentes extends RecyclerView.Adapter<AdaptadorIncidentes.MyViewHolder> {

    private List<Incidentes> lstIncidientes;

    public void setLstIncidientes(List<Incidentes> lstIncidientes){
        this.lstIncidientes=lstIncidientes;
    }

    public AdaptadorIncidentes(List<Incidentes> incidientes){
        this.lstIncidientes= incidientes;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filaIncidentes = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_menu, parent, false);
        return new MyViewHolder(filaIncidentes);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Incidentes incidentes = lstIncidientes.get(position);

        // Obtener los datos de la lista
        int item1 = incidentes.getIdOperador();
        Float item2 = incidentes.getLatitud();
        // Y poner a los TextView los datos con setText
        holder.item1.setText(item1);
        holder.item2.setText(String.valueOf(item2));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView item1, item2;

        MyViewHolder(View itemView){
            super(itemView);
            this.item1 = itemView.findViewById(R.id.list_item1);
            this.item2 = itemView.findViewById(R.id.list_item2);
        }
    }
}
