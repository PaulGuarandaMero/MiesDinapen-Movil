package com.example.mies_dinapen.View.Fragment.ConsultarCedula;

import static com.example.mies_dinapen.Controller.UtilClass.MetodoCedula.toValidateNoIdentificacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mies_dinapen.Model.Item_historial;
import com.example.mies_dinapen.View.Fragment.ConsultarCedula.Adaptador.Adaptador_IncidenciaHistorial;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.databinding.FragmentConsultarCedulaBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarCedulaFragment extends Fragment implements View.OnClickListener {

    FragmentConsultarCedulaBinding viewMain;
    Adaptador_IncidenciaHistorial adaptador_Incidencia_historial;
    Activity_Contenedor activity;

    public ConsultarCedulaFragment() {
    }

    public static ConsultarCedulaFragment newInstance(String param1, String param2) {
        ConsultarCedulaFragment fragment = new ConsultarCedulaFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentConsultarCedulaBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        adaptador_Incidencia_historial = new Adaptador_IncidenciaHistorial((Activity_Contenedor) getActivity());
        viewMain.FConsultarCRecyclerVHinci.setAdapter(adaptador_Incidencia_historial);
        viewMain.FConsultarCRecyclerVHinci.setHasFixedSize(true);
        viewMain.FConsultarCRecyclerVHinci.setLayoutManager(new LinearLayoutManager(getContext()));

        viewMain.FConsultarCButtonBuscar.setOnClickListener(this);

        return viewMain.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view == viewMain.FConsultarCButtonBuscar){
            if(toValidateNoIdentificacion(viewMain.FConsultarCEditTextCedula.getText().toString(), getContext())){
                Call<ArrayList<Item_historial>> call = activity.getServicios().getHistorialIncidencia(viewMain.FConsultarCEditTextCedula.getText().toString());
                call.enqueue(new Callback<ArrayList<Item_historial>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item_historial>> call, Response<ArrayList<Item_historial>> response) {
                        ArrayList<Item_historial> datos = new ArrayList<>();
                        for (Item_historial item_historial:response.body()) {
                            datos.add(item_historial);
                        }
                        if(datos.size()!= 0){
                            viewMain.FConsultarCTextViewContador.setText("Numero de Incidencia: " + datos.size());
                        }else{
                            viewMain.FConsultarCTextViewContador.setText("No existen incidencia");
                        }
                        adaptador_Incidencia_historial.setDatos(datos);
                        adaptador_Incidencia_historial.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item_historial>> call, Throwable t) {
                        Log.e("TAG", "onResponse: " + t);
                    }
                });
            }

        }
    }
}