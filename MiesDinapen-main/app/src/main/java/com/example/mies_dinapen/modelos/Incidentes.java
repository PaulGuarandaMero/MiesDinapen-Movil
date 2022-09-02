package com.example.mies_dinapen.modelos;

import java.sql.Timestamp;

public class Incidentes {

    private int idPersona;
    private float logitud;
    private float latitud;
    private Timestamp fecha;
    private int idOrgOperador;
    private int idOperador;

    public Incidentes(){

    }

    public Incidentes(int idPersona, float logitud, float latitud, Timestamp fecha, int idOrgOperador, int idOperador) {
        this.idPersona = idPersona;
        this.logitud = logitud;
        this.latitud = latitud;
        this.fecha = fecha;
        this.idOrgOperador = idOrgOperador;
        this.idOperador = idOperador;

    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public float getLogitud() {
        return logitud;
    }

    public void setLogitud(float logitud) {
        this.logitud = logitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getIdOrgOperador() {
        return idOrgOperador;
    }

    public void setIdOrgOperador(int idOrgOperador) {
        this.idOrgOperador = idOrgOperador;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }
}
