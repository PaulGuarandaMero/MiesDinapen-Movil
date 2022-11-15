package com.example.mies_dinapen.modelos;

public class Foto {
    String idFotos;
    String IDIntervencion;
    String FotoIncidente;
    String FechaRegistro;

    public Foto(String IDIntervencion, String fotoIncidente, String fechaRegistro) {
        this.IDIntervencion = IDIntervencion;
        FotoIncidente = fotoIncidente;
        FechaRegistro = fechaRegistro;
    }

    public String getIdFotos() {
        return idFotos;
    }

    public void setIdFotos(String idFotos) {
        this.idFotos = idFotos;
    }

    public String getIDIntervencion() {
        return IDIntervencion;
    }

    public void setIDIntervencion(String IDIntervencion) {
        this.IDIntervencion = IDIntervencion;
    }

    public String getFotoIncidente() {
        return FotoIncidente;
    }

    public void setFotoIncidente(String fotoIncidente) {
        FotoIncidente = fotoIncidente;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }
}
