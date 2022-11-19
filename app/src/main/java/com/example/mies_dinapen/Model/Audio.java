package com.example.mies_dinapen.Model;

public class Audio {
    String IDIncidente;
    String IDIntervencion;
    String Audio;
    String FechaRegistro;

    public Audio(String IDIntervencion, String audio, String fechaRegistro) {
        this.IDIncidente = IDIncidente;
        this.IDIntervencion = IDIntervencion;
        Audio = audio;
        FechaRegistro = fechaRegistro;
    }

    public String getIDIncidente() {
        return IDIncidente;
    }

    public void setIDIncidente(String IDIncidente) {
        this.IDIncidente = IDIncidente;
    }

    public String getIDIntervencion() {
        return IDIntervencion;
    }

    public void setIDIntervencion(String IDIntervencion) {
        this.IDIntervencion = IDIntervencion;
    }

    public String getAudio() {
        return Audio;
    }

    public void setAudio(String audio) {
        Audio = audio;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }
}
