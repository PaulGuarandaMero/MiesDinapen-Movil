package com.example.mies_dinapen.Model;

public class Item_historial {
    private String IDIntervencion;
    private String FechaIntervencion;
    private String Estado;
    private Operador operador;

    public Item_historial(String IDIntervencion, String fechaIntervencion, String estado, Operador operador) {
        this.IDIntervencion = IDIntervencion;
        FechaIntervencion = fechaIntervencion;
        Estado = estado;
        this.operador = operador;
    }

    public String getIDIntervencion() {
        return IDIntervencion;
    }

    public void setIDIntervencion(String IDIntervencion) {
        this.IDIntervencion = IDIntervencion;
    }

    public String getFechaIntervencion() {
        return FechaIntervencion;
    }

    public void setFechaIntervencion(String fechaIntervencion) {
        FechaIntervencion = fechaIntervencion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    @Override
    public String toString() {
        return "Item_historial{" +
                "IDIntervencion='" + IDIntervencion + '\'' +
                ", FechaIntervencion='" + FechaIntervencion + '\'' +
                ", Estado='" + Estado + '\'' +
                ", operador=" + operador +
                '}';
    }
}







