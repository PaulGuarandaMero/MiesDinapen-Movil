package com.example.mies_dinapen.View.Fragment.ConsultarCedula.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.Model.Item_historial;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;

import java.util.ArrayList;

public class Adaptador_IncidenciaHistorial extends RecyclerView.Adapter<Adaptador_IncidenciaHistorial.ViewHolder> {

    Activity_Contenedor activityContenedor;
    LayoutInflater layoutInflater;
    ArrayList<Item_historial> datos;

    public Adaptador_IncidenciaHistorial(Activity_Contenedor activityContenedor) {
        this.activityContenedor = activityContenedor;
        this.layoutInflater = LayoutInflater.from(activityContenedor);
        this.datos = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adaptador_IncidenciaHistorial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_historialinsidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_IncidenciaHistorial.ViewHolder holder, int position) {
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
            fecha.setText(item_historial.getFechaRegistro());
            estado.setText(item_historial.getEstado());
            id.setText(item_historial.getIDIntervencion());


        }
    }
}
