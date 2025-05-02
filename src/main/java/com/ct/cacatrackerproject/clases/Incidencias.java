package com.ct.cacatrackerproject.clases;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Incidencias {

    private Long id;
    private String direccion;
    private String codigopostal;
    private String nombreartistico;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechacreacion;

    private byte[] foto;

    @JsonProperty("idUsers")
    private Users idUsers;

    public Users getIdUser() {
        return idUsers;
    }

    public void setIdUser(Users idUsers) {
        this.idUsers = idUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public String getNombreartistico() {
        return nombreartistico;
    }

    public void setNombreartistico(String nombreartistico) {
        this.nombreartistico = nombreartistico;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public byte[] getFoto() {
        return foto;
    }
}
