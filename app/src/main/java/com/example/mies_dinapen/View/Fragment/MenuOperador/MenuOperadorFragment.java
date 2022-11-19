package com.example.mies_dinapen.View.Fragment.MenuOperador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mies_dinapen.Model.Operador;
import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.databinding.FragmentMenuOperadorBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuOperadorFragment extends Fragment implements View.OnClickListener {

    FragmentMenuOperadorBinding viewMain;
    Activity_Contenedor activity;
    Boolean salir;

    public MenuOperadorFragment() {
        // Required empty public constructor
    }

    public static MenuOperadorFragment newInstance(String param1, String param2) {
        MenuOperadorFragment fragment = new MenuOperadorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentMenuOperadorBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        viewMain.FMenuOButtonCedula.setOnClickListener(this);
        viewMain.FMenuOButtonIncidencia.setOnClickListener(this);
        setViewData();
        backFragment();
        return viewMain.getRoot();
    }

    private void setViewData() {
        salir = true;
        if(activity.getOperador().getIDInstitucion().equals("1")){
            viewMain.FMenuOTextViewInstitucion.setText("Ministerio de Inclusion Económica y Social");
        } else {
            viewMain.FMenuOTextViewInstitucion.setText("Policia Nacional");
        }
        viewMain.FMenuOTextViewCargo.setText(activity.getOperador().getOperaCargo());
        viewMain.FMenuOTextViewCedula.setText(activity.getOperador().getOperaNCedula());
        viewMain.FMenuOTextViewApellidos.setText(activity.getOperador().getOperaApellido1() + " " + activity.getOperador().getOperaApellido2() );
        viewMain.FMenuOTextViewNombre.setText(activity.getOperador().getOperaNombres());
    }

    @Override
    public void onClick(View view) {
        if(view == viewMain.FMenuOButtonCedula){
            Navigation.findNavController(view).navigate(R.id.action_menuOperadorFragment_to_consultarCedulaFragment);
        }
        if(view == viewMain.FMenuOButtonIncidencia){
            Navigation.findNavController(view).navigate(R.id.action_menuOperadorFragment_to_menuIncidenciaFragment);
        }
    }

    private void backFragment(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(salir){
                    AlertDialog.Builder builderAlerta = new AlertDialog.Builder(getContext());
                    builderAlerta.setTitle("Salir");
                    builderAlerta.setMessage("Desea cerrar sesión")
                            .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    salir = false;
                                    activity.onBackPressed();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false).show();
                }else{
                    //Este remove cancela el OnBackPressedCallback "IMPORTANTE"
                    remove();
                    backpressed();

                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
    }

    private void backpressed(){
        salir = false;
        activity.setOperador(new Operador());
        tokenSesion();
        activity.onBackPressed();
    }

    private void tokenSesion() {
        SharedPreferences preferences = activity.getSharedPreferences("TokenSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cedula", null);
        editor.putString("contra", null);
        editor.putBoolean("token", false);
        editor.commit();
    }
}