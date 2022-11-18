package com.example.mies_dinapen.Controller.Ubicacion;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.mies_dinapen.databinding.FragmentMenuIncidenciaBinding;


public class Localizacion implements LocationListener {

    FragmentMenuIncidenciaBinding fragmentContenedor;
    Context context;

    public Localizacion(FragmentMenuIncidenciaBinding fragmentContenedor, Context context) {
        this.fragmentContenedor = fragmentContenedor;
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        fragmentContenedor.FMenuITextViewLatitud.setText(String.valueOf(loc.getLatitude()));
        fragmentContenedor.FMenuITextViewLongitud.setText(String.valueOf(loc.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "El provedor de Ubicacion esta deshabilita ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "El provedor de Ubicacion esta habilita ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

}
