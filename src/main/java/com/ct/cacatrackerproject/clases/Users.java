package com.ct.cacatrackerproject.clases;

import java.util.List;
import java.util.Objects;

public class Users {
    private Integer id;
    private String username;
    private String password;
    private String codigopostal;
    private String codigoactiva;
    private String email;
    private String recuperapass;
    private boolean activado;
    private List<Incidencias> incidenciasCollection;

    public Users() {
    }

    public Users(Integer id, String username, String password, String codigopostal, String codigoactiva, String email, String recuperapass, boolean activado, List<Incidencias> incidenciasCollection) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.codigopostal = codigopostal;
        this.codigoactiva = codigoactiva;
        this.email = email;
        this.recuperapass = recuperapass;
        this.activado = activado;
        this.incidenciasCollection = incidenciasCollection;
    }

    public List<Incidencias> getIncidenciasCollection() {
        return incidenciasCollection;
    }

    public void setIncidenciasCollection(List<Incidencias> incidenciasCollection) {
        this.incidenciasCollection = incidenciasCollection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoactiva() {
        return codigoactiva;
    }

    public void setCodigoactiva(String codigoactiva) {
        this.codigoactiva = codigoactiva;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public String getActivado() {
        return codigoactiva;
    }

    public void setActivado(String codigoactiva) {
        this.codigoactiva = codigoactiva;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecuperapass() {
        return recuperapass;
    }

    public void setRecuperapass(String recuperapass) {
        this.recuperapass = recuperapass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigoPosta() {
        return codigopostal;
    }

    public void setCodigoPosta(String codigoPosta) {
        this.codigopostal = codigoPosta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users user = (Users) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(codigopostal, user.codigopostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, codigopostal);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", codigoPosta='" + codigopostal + '\'' +
                '}';
    }
}

