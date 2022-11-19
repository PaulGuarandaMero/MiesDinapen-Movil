package com.example.mies_dinapen.View.Fragment.ReproducirAudio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mies_dinapen.View.Activity.Activity_Contenedor;
import com.example.mies_dinapen.View.Fragment.ReproducirAudio.Adaptador.Adaptador_ReproAudio;
import com.example.mies_dinapen.databinding.FragmentReproducirAudioBinding;

public class ReproducirAudioFragment extends Fragment {

    FragmentReproducirAudioBinding viewMain;
    Adaptador_ReproAudio adaptador_reproAudio;
    Activity_Contenedor activity;

    public ReproducirAudioFragment() {
    }

    public static ReproducirAudioFragment newInstance(String param1, String param2) {
        ReproducirAudioFragment fragment = new ReproducirAudioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = FragmentReproducirAudioBinding.inflate(getLayoutInflater());
        activity = (Activity_Contenedor) getActivity();
        adaptador_reproAudio = new Adaptador_ReproAudio((Activity_Contenedor) getActivity());
        viewMain.FConsultarCRecyclerVHinci.setAdapter(adaptador_reproAudio);
        viewMain.FConsultarCRecyclerVHinci.setHasFixedSize(true);
        viewMain.FConsultarCRecyclerVHinci.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador_reproAudio.setDatos(activity.getLstA());
        return viewMain.getRoot();
    }
}