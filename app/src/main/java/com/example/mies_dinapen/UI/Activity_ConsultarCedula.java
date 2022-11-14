package com.example.mies_dinapen.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mies_dinapen.R;
import com.example.mies_dinapen.service.ServiceConsult;

public class Activity_ConsultarCedula extends AppCompatActivity implements View.OnClickListener{


    Button consultaHis;
    EditText cedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultacedula);

        consultaHis=findViewById(R.id.consultacedulabtnl);
        cedula=findViewById(R.id.consultacedula);

        consultaHis.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == consultaHis){
            if(toValidateNoIdentificacion(cedula.getText().toString())){

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
            ServiceConsult consulta = new ServiceConsult(cedula.getText().toString(), Activity_ConsultarCedula.this);
            consulta.execute();
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