package com.ct.cacatrackerproject.clases;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String codigoPosta;

    public User() {
    }

    public User(String codigoPosta, String password, String username) {
        this.codigoPosta = codigoPosta;
        this.password = password;
        this.username = username;
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
        return codigoPosta;
    }

    public void setCodigoPosta(String codigoPosta) {
        this.codigoPosta = codigoPosta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(codigoPosta, user.codigoPosta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, codigoPosta);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", codigoPosta='" + codigoPosta + '\'' +
                '}';
    }
}

