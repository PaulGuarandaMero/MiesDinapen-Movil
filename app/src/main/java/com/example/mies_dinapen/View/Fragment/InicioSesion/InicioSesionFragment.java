package com.example.mies_dinapen.View.Fragment.InicioSesion;

import android.content.Context;
import android.content.SharedPreferences;
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
        getPreference();
        return viewMain.getRoot();
    }

    private void getPreference() {
        viewMain.FIniciSButtonIngresar.setEnabled(false);
        SharedPreferences preferences = activity.getSharedPreferences("TokenSesion", Context.MODE_PRIVATE);
        String cedula = preferences.getString("cedula", null);
        String contra = preferences.getString("contra", null);
        if(preferences.getBoolean("token", false)){
            HashMap<String, String> datosInput = new HashMap<>();
            datosInput.put("OperaNCedula", cedula);
            datosInput.put("password", contra);
            getConsulta(datosInput);
        }else{
            Toast.makeText(activity, "Operador no Identificado", Toast.LENGTH_SHORT).show();
            viewMain.FIniciSButtonIngresar.setEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        if(view == viewMain.FIniciSButtonIngresar){
            getConsulta(getInputData());
        }
    }

    private void getConsulta(HashMap<String, String> datosInput) {
        Call<Operador> call = activity.getServicios().getOperoador(datosInput);
        call.enqueue(new Callback<Operador>() {
            @Override
            public void onResponse(Call<Operador> call, Response<Operador> response) {
                Log.e("TAG", "onResponse: "+  response.body());
                activity.setOperador(response.body());
                tokenSesion();
                nav_fragment(getView());
            }

            @Override
            public void onFailure(Call<Operador> call, Throwable t) {
                Toast.makeText(getContext(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tokenSesion() {
        SharedPreferences preferences = activity.getSharedPreferences("TokenSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cedula", viewMain.FIniciSEdittextCedula.getText().toString());
        editor.putString("contra", viewMain.FIniciSEdittextContraseA.getText().toString());
        editor.putBoolean("token", true);
        editor.commit();
    }

    private void nav_fragment(View view) {
        Navigation.findNavController(view).navigate(R.id.action_inicioSesionFragment_to_menuOperadorFragment);
    }


    private HashMap<String, String> getInputData() {
        HashMap<String, String> datosInput = new HashMap<>();
        datosInput.put("OperaNCedula", viewMain.FIniciSEdittextCedula.getText().toString());
        datosInput.put("password", viewMain.FIniciSEdittextContraseA.getText().toString());
        return datosInput;
    }
}