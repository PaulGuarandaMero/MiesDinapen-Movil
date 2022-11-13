package com.example.mies_dinapen.modelos;

public class Audio {
    String idIncidete;
    String idAudio;
    String files;
    String fechaRegistro;


    public Audio(String idIncidete, String files, String fechaRegistro) {
        this.idIncidete = idIncidete;
        this.files = files;
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdIncidete() {
        return idIncidete;
    }

    public void setIdIncidete(String idIncidete) {
        this.idIncidete = idIncidete;
    }

    public String getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(String idAudio) {
        this.idAudio = idAudio;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
}
