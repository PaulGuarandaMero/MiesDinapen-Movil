package com.example.mies_dinapen;

import android.content.Intent;
import android.os.Bundle;

import com.example.mies_dinapen.Controlador.IncidentesController;
import com.example.mies_dinapen.modelos.Incidentes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListIncidentes extends AppCompatActivity {

    private List<Incidentes> lstIncidentes;
    private RecyclerView recyclerView;
    private AdaptadorIncidentes adaptadorIncidentes;
    private IncidentesController incidentesController;
    private FloatingActionButton btn_newIncidente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

         incidentesController = new IncidentesController(ListIncidentes.this);

        recyclerView = findViewById(R.id.incidentes_list_view);
        btn_newIncidente = findViewById(R.id.new_incidente);
        btn_newIncidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(ListIncidentes.this, com.example.mies_dinapen.Mies_Dinapen.class);
                startActivity(i1);
            }
        });
        lstIncidentes = new ArrayList<>();
        adaptadorIncidentes = new AdaptadorIncidentes(lstIncidentes);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setAdapter(adaptadorIncidentes);
        /*refrescarLista();*/

    } @Override
    protected void onResume() {
        super.onResume();
        /*refrescarLista();*/
    }
    public void refrescarLista() {
        /*
         * ==========
         * Justo aquí obtenemos la lista de la BD
         * y se la ponemos al RecyclerView
         * ============
         *
         * */
        if (adaptadorIncidentes == null) return;
        lstIncidentes = incidentesController.obtenerList();
        adaptadorIncidentes.setLstIncidientes(lstIncidentes);
        adaptadorIncidentes.notifyDataSetChanged();
    }


}