package com.example.mies_dinapen.modelos;

import java.sql.Date;
import java.sql.Timestamp;

public class IncidenteM {
    private int IDIntervencion;
    private int IDOperador;
    private int IDOrganCooperante;
    private int IDPersonaIntervenida;
    private float Latitud;
    private float Longitud;
    private int NumPerGrupo;
    private String Referencia;
    private String DerivEspecifi;
    private int IDCircunstancia;
    private int IDCondicion;
    private int IDEstudio;
    private String NoEstudio;
    private int UltAñoEstudio;
    private String InsEduEstudio;
    private int RefIDProvincia;
    private int RefIDCanton;
    private int RefIDParroquia;
    private String DireccionCallePrincial;
    private String DireccionNumero;
    private String DireccionCalleInterseccion;
    private int NumHijos;
    private int IDViveCon;
    private String NumTelefono;
    private Date FechaIntervencion;
    private Timestamp FechaRegistro;

    public IncidenteM(int IDOperador, int IDOrganCooperante, int IDPersonaIntervenida, float latitud, float longitud, Timestamp fechaRegistro) {
        this.IDOperador = IDOperador;
        this.IDOrganCooperante = IDOrganCooperante;
        this.IDPersonaIntervenida = IDPersonaIntervenida;
        Latitud = latitud;
        Longitud = longitud;
        FechaRegistro = fechaRegistro;
    }
}
