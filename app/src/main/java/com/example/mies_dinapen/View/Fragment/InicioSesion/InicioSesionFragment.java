package com.example.mies_dinapen.View.Fragment.InicioSesion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mies_dinapen.Model.Operador;
import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.databinding.FragmentInicioSesionBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InicioSesionFragment extends Fragment implements View.OnClickListener {

    FragmentInicioSesionBinding viewMain;
    Activity_Contenedor activity;

    public InicioSesionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentInicioSesionBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        viewMain.FIniciSButtonIngresar.setOnClickListener(this);
        return viewMain.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view == viewMain.FIniciSButtonIngresar){
            getConsulta(view);
        }
    }

    private void getConsulta(View view) {
        Call<Operador> call = activity.getServicios().getOperoador(getInputData());
        call.enqueue(new Callback<Operador>() {
            @Override
            public void onResponse(Call<Operador> call, Response<Operador> response) {
                Log.e("TAG", "onResponse: "+  response.body());
                activity.setOperador(response.body());
                nav_fragment(view);
            }

            @Override
            public void onFailure(Call<Operador> call, Throwable t) {
                Toast.makeText(getContext(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nav_fragment(View view) {
        Navigation.findNavController(view).navigate(R.id.action_inicioSesionFragment_to_menuIncidenciaFragment);
    }


    private HashMap<String, String> getInputData() {
        HashMap<String, String> datosInput = new HashMap<>();
        datosInput.put("OperaNCedula", viewMain.FIniciSEdittextCedula.getText().toString());
        datosInput.put("password", viewMain.FIniciSEdittextContraseA.getText().toString());
        return datosInput;
    }
}