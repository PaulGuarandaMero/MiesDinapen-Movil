package com.example.mies_dinapen.Model;

public class Incidente {

    private int IDPersonaIntervenida;
    private float Latitud;
    private float Longitud;
    private String FechaRegistro;
    private int IDOrganCooperante;
    private int IDOperador;
    private String Referencia;
    private String NombreRepresentante;

    public Incidente(int IDPersonaIntervenida, float latitud, float logitud, String fechaRegistro, int IDOrganCooperante, int IDOperador, String referencia, String nombreRepresentante) {
        this.IDPersonaIntervenida = IDPersonaIntervenida;
        Latitud = latitud;
        Longitud = logitud;
        FechaRegistro = fechaRegistro;
        this.IDOrganCooperante = IDOrganCooperante;
        this.IDOperador = IDOperador;
        Referencia = referencia;
        NombreRepresentante = nombreRepresentante;
    }

    public int getIDPersonaIntervenida() {
        return IDPersonaIntervenida;
    }

    public void setIDPersonaIntervenida(int IDPersonaIntervenida) {
        this.IDPersonaIntervenida = IDPersonaIntervenida;
    }

    public float getLatitud() {
        return Latitud;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }

    public float getLongitud() {
        return Longitud;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public int getIDOrganCooperante() {
        return IDOrganCooperante;
    }

    public void setIDOrganCooperante(int IDOrganCooperante) {
        this.IDOrganCooperante = IDOrganCooperante;
    }

    public int getIDOperador() {
        return IDOperador;
    }

    public void setIDOperador(int IDOperador) {
        this.IDOperador = IDOperador;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getNombreRepresentante() {
        return NombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        NombreRepresentante = nombreRepresentante;
    }

    @Override
    public String toString() {
        return "Incidente{" +
                "IDPersonaIntervenida=" + IDPersonaIntervenida +
                ", Latitud=" + Latitud +
                ", Longitud=" + Longitud +
                ", FechaRegistro='" + FechaRegistro + '\'' +
                ", IDOrganCooperante=" + IDOrganCooperante +
                ", IDOperador=" + IDOperador +
                ", Referencia='" + Referencia + '\'' +
                ", NombreRepresentante='" + NombreRepresentante + '\'' +
                '}';
    }
}



