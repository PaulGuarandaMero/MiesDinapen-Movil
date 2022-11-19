package com.example.mies_dinapen.Model;

public class Item_historial {
    private String IDIntervencion;
    private String FechaRegistro;
    private String Estado;
    private Operador operador;

    public Item_historial(String IDIntervencion, String fechaRegistro, String estado, Operador operador) {
        this.IDIntervencion = IDIntervencion;
        FechaRegistro = fechaRegistro;
        Estado = estado;
        this.operador = operador;
    }

    public String getIDIntervencion() {
        return IDIntervencion;
    }

    public void setIDIntervencion(String IDIntervencion) {
        this.IDIntervencion = IDIntervencion;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
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
                ", FechaRegistro='" + FechaRegistro + '\'' +
                ", Estado='" + Estado + '\'' +
                ", operador=" + operador +
                '}';
    }
}







