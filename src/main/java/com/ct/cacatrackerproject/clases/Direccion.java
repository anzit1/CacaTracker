package com.ct.cacatrackerproject.clases;

import java.util.Objects;

public class Direccion {

    private String nombre;
    private String codigoPostal;

    public Direccion() {
    }

    public Direccion(String nombre, String codigoPostal) {
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return Objects.equals(nombre, direccion.nombre) && Objects.equals(codigoPostal, direccion.codigoPostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, codigoPostal);
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "nombre='" + nombre + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                '}';
    }
}
