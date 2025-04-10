package com.ct.cacatrackerproject.clases;

import java.util.Objects;

public class Direccion {

    private Long id;
    private String direccion;
    private String codigoPostal;

    public Direccion() {
    }

    public Direccion(String direccion, String codigoPostal) {
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
    }

    public Direccion(String direccion, Long id, String codigoPostal) {
        this.direccion = direccion;
        this.id = id;
        this.codigoPostal = codigoPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return Objects.equals(direccion, direccion.direccion) && Objects.equals(codigoPostal, direccion.codigoPostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccion, codigoPostal);
    }

    @Override
    public String toString() {
        return "Direcciones{" + "id=" + id + ", direccion=" + direccion + ", codigoPostal=" + codigoPostal + '}';
    }
}
