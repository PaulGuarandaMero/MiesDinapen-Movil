package com.example.mies_dinapen.modelos;

public class Foto {
    String idIncidentes;
    String idFotos;
    String file;
    String fechaRegistro;



    public Foto(String idIncidentes, String file, String fechaRegistro) {
        this.idIncidentes = idIncidentes;
        this.file = file;
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdIncidentes() {
        return idIncidentes;
    }

    public void setIdIncidentes(String idIncidentes) {
        this.idIncidentes = idIncidentes;
    }

    public String getIdFotos() {
        return idFotos;
    }

    public void setIdFotos(String idFotos) {
        this.idFotos = idFotos;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
