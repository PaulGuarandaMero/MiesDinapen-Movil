package com.example.mies_dinapen.modelos;

public class Incidente {

    private int idPersona;
    private float logitud;
    private float latitud;
    private String fecha;
    private String referencia;
    private String nombrerepresentante;
    private int idOrgOperador;
    private int idOperador;

    public Incidente(int idPersona, float latitud, float logitud , String fecha, int idOrgOperador, int idOperador, String referencia, String nombrerepresentante) {
        this.idPersona = idPersona;
        this.logitud = logitud;
        this.latitud = latitud;
        this.fecha = fecha;
        this.idOrgOperador = idOrgOperador;
        this.idOperador = idOperador;
        this.referencia = referencia;
        this.nombrerepresentante=nombrerepresentante;

    }

    public String getNombrerepresentante() {
        return nombrerepresentante;
    }

    public void setNombrerepresentante(String nombrerepresentante) {
        this.nombrerepresentante = nombrerepresentante;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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
