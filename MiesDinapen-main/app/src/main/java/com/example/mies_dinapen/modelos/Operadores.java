package com.example.mies_dinapen.modelos;

public class Operadores {
    private int IDOperador;
    private String IDInstitucion;
    private String OperaCargo;
    private int OperaNCedula;
    private String OperaApellido1;
    private String OperaApellido2;
    private String OperaNombres;

    public Operadores() {
    }

    public Operadores(int IDOperador, String IDInstitucion, String operaCargo, int operaNCedula, String operaApellido1, String operaApellido2, String operaNombres) {
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

    public int getOperaNCedula() {
        return OperaNCedula;
    }

    public void setOperaNCedula(int operaNCedula) {
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
}
