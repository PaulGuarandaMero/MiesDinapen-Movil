package com.example.mies_dinapen.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.Retrofit.BaseDato;
import com.example.mies_dinapen.UI.Adaptores.Adaptador_HistorialInci;
import com.example.mies_dinapen.modelos.Item_historial;
import com.example.mies_dinapen.service.OperadorUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ConsultarCedula extends AppCompatActivity implements View.OnClickListener{


    Button consultaHis;
    EditText cedula;
    TextView contador;
    OperadorUser operadorUser;
    RecyclerView recyclerView;
    Adaptador_HistorialInci adaptador_historialInci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultacedula);

        contador = findViewById(R.id.numero_incidencia);
        consultaHis=findViewById(R.id.consultacedulabtnl);
        cedula=findViewById(R.id.consultacedula);
        recyclerView=findViewById(R.id.RecyclerView);

        initDataBase();
        adaptador_historialInci = new Adaptador_HistorialInci(this);
        recyclerView.setAdapter(adaptador_historialInci);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        consultaHis.setOnClickListener(this);

    }

    private void initDataBase(){
        operadorUser = BaseDato.getConnetion().create(OperadorUser.class);
    }

    @Override
    public void onClick(View view) {
        if(view == consultaHis){
            if(toValidateNoIdentificacion(cedula.getText().toString())){
                Call<ArrayList<Item_historial>> call = operadorUser.getHistorialIncidencia(cedula.getText().toString());
                call.enqueue(new Callback<ArrayList<Item_historial>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item_historial>> call, Response<ArrayList<Item_historial>> response) {
                        ArrayList<Item_historial> datos = new ArrayList<>();
                        for (Item_historial item_historial:response.body()) {
                            datos.add(item_historial);
                        }
                        if(datos.size()!= 0){
                            contador.setText("Numero de Incidencia: " + datos.size());
                        }else{
                            contador.setText("No existen incidencia");
                        }
                        adaptador_historialInci.setDatos(datos);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item_historial>> call, Throwable t) {
                        Log.e("TAG", "onResponse: " + t);
                    }
                });
            }

        }
    }

    public boolean toValidateNoIdentificacion( String noIdentificacion) {
        if (noIdentificacion.length() != 10) { // si la cadena no tiene 10 caracteres
            Toast.makeText(this, "Una cédula se compone de 10 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toBuscarLetras(noIdentificacion)) { // si la cadena tiene espacios o letras
            Toast.makeText(this, "Una cédula no contiene letras.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toValidarCedulaRuc(noIdentificacion)) {
            Toast.makeText(this, "Si es correcto", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Cedula Incorrecta", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean toBuscarLetras(String noIdentificacion) {
        for (int x = 0; x < noIdentificacion.length(); x++) {
            char c = noIdentificacion.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ') {
                return true; // encontro una letra o espacio
            }
        }
        return false; // no hay letras.
    }

    private boolean toValidarCedulaRuc(String noIdentificacion) {
        int ubicacion = Integer.parseInt(noIdentificacion.substring(0, 2));
        if ((ubicacion > 0 && ubicacion <= 24) || ubicacion == 30) { // del 1 al 24 las provincias del país, el 30 es
            // para extranjeros
            int[] numeros = new int[10];
            int[] productos = new int[numeros.length - 1];
            for (int i = 0; i < numeros.length; i++) {
                numeros[i] = Integer.parseInt(noIdentificacion.substring(i, (i + 1)));
            }
            // CICLO CON LOS PRIMEROS 9 NUMEROS
            for (int indice = 1; indice <= productos.length; indice++) {
                if ((indice % 2) == 0) {
                    // POSICIÓN PAR SE AGREGA EL NUMERO SIN CAMBIOS
                    productos[indice - 1] = numeros[indice - 1];
                } else {
                    // POSICIÓN IMPAR SE MULTIPLICA POR 2
                    int nproducto = numeros[indice - 1] * 2;
                    if (nproducto >= 10) { // SI EL PRODUCTO ES MAYOR O IGUAL A 10 LE RESTA 9
                        productos[indice - 1] = nproducto - 9;
                    } else { // SI ES MENOR A 10 AGREGA A LA LISTA
                        productos[indice - 1] = nproducto;
                    }
                }
            }
            int suma = 0; // SUMA TODOS LOS PRODUCTOS
            for (int producto : productos) {
                suma += producto;
            }
            if ((suma % 10) == 0) {
                return numeros[numeros.length - 1] == 0; // caso especial a modulo 10 si el numero es 0 entonces es
                // valida
            } else {
                return (10 - (suma % 10)) == numeros[numeros.length - 1]; // si el residuo es igual al ultimo de la
                // cedula entonces es valida
            }
        }
        return false;
    }

}