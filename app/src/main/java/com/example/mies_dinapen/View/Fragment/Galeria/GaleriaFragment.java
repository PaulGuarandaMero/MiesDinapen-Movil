package com.example.mies_dinapen.View.Fragment.Galeria;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.View.Fragment.Galeria.Adaptador.Adaptador_Galeria;
import com.example.mies_dinapen.View.Fragment.ReproducirAudio.Adaptador.Adaptador_ReproAudio;
import com.example.mies_dinapen.databinding.FragmentGaleriaBinding;
import com.example.mies_dinapen.databinding.FragmentReproducirAudioBinding;

public class GaleriaFragment extends Fragment {

    FragmentGaleriaBinding viewMain;
    Adaptador_Galeria adaptador_galeria;
    Activity_Contenedor activity;

    public GaleriaFragment() {
        // Required empty public constructor
    }

    public static GaleriaFragment newInstance(String param1, String param2) {
        GaleriaFragment fragment = new GaleriaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentGaleriaBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        adaptador_galeria = new Adaptador_Galeria((Activity_Contenedor) getActivity());
        viewMain.FGaleriaRecyclerVGaleria.setAdapter(adaptador_galeria);
        viewMain.FGaleriaRecyclerVGaleria.setHasFixedSize(true);

        adaptador_galeria.setDatos(activity.getLstF());
        return viewMain.getRoot();
    }
}