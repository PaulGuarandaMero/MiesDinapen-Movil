package com.example.mies_dinapen.Model;

import java.util.Objects;

public class Operador {
    private int IDOperador;
    private String IDInstitucion;
    private String OperaCargo;
    private String OperaNCedula;
    private String OperaApellido1;
    private String OperaApellido2;
    private String OperaNombres;

    public Operador() {
    }


    public Operador(int IDOperador, String IDInstitucion, String operaCargo, String operaNCedula, String operaApellido1, String operaApellido2, String operaNombres) {
        this.IDOperador = IDOperador;
        this.IDInstitucion = IDInstitucion;
        OperaCargo = operaCargo;
        OperaNCedula = operaNCedula;
        OperaApellido1 = operaApellido1;
        OperaApellido2 = operaApellido2;
        OperaNombres = operaNombres;
    }

    public int getIDOperador() {
        return IDOperador;
    }

    public void setIDOperador(int IDOperador) {
        this.IDOperador = IDOperador;
    }

    public String getIDInstitucion() {
        return IDInstitucion;
    }

    public void setIDInstitucion(String IDInstitucion) {
        this.IDInstitucion = IDInstitucion;
    }

    public String getOperaCargo() {
        return OperaCargo;
    }

    public void setOperaCargo(String operaCargo) {
        OperaCargo = operaCargo;
    }

    public String getOperaNCedula() {
        return OperaNCedula;
    }

    public void setOperaNCedula(String operaNCedula) {
        OperaNCedula = operaNCedula;
    }

    public String getOperaApellido1() {
        return OperaApellido1;
    }

    public void setOperaApellido1(String operaApellido1) {
        OperaApellido1 = operaApellido1;
    }

    public String getOperaApellido2() {
        return OperaApellido2;
    }

    public void setOperaApellido2(String operaApellido2) {
        OperaApellido2 = operaApellido2;
    }

    public String getOperaNombres() {
        return OperaNombres;
    }

    public void setOperaNombres(String operaNombres) {
        OperaNombres = operaNombres;
    }

    @Override
    public String toString() {
        return "Operadores{" +
                "IDOperador=" + IDOperador +
                ", IDInstitucion='" + IDInstitucion + '\'' +
                ", OperaCargo='" + OperaCargo + '\'' +
                ", OperaNCedula=" + OperaNCedula +
                ", OperaApellido1='" + OperaApellido1 + '\'' +
                ", OperaApellido2='" + OperaApellido2 + '\'' +
                ", OperaNombres='" + OperaNombres + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operador)) return false;
        Operador operador = (Operador) o;
        return getIDOperador() == operador.getIDOperador() && Objects.equals(getIDInstitucion(), operador.getIDInstitucion()) && Objects.equals(getOperaCargo(), operador.getOperaCargo()) && Objects.equals(getOperaNCedula(), operador.getOperaNCedula()) && Objects.equals(getOperaApellido1(), operador.getOperaApellido1()) && Objects.equals(getOperaApellido2(), operador.getOperaApellido2()) && Objects.equals(getOperaNombres(), operador.getOperaNombres());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIDOperador(), getIDInstitucion(), getOperaCargo(), getOperaNCedula(), getOperaApellido1(), getOperaApellido2(), getOperaNombres());
    }
}
