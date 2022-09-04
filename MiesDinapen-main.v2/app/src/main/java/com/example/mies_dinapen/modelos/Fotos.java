package com.example.mies_dinapen.modelos;

public class Fotos {
    String idIncidentes;
    String idFotos;
    String file;

    public Fotos(String idIncidentes, String idFotos, String file) {
        this.idIncidentes = idIncidentes;
        this.idFotos = idFotos;
        this.file = file;
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


}
