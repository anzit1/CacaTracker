package com.ct.cacatrackerproject.clases;

public class DireccionCountDTO {
    private String direccion;
    private int total;

    public DireccionCountDTO() {
    }

    public DireccionCountDTO(String direccion, int total) {
        this.direccion = direccion;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "DireccionCountDTO{" +
                "direccion='" + direccion + '\'' +
                ", total=" + total +
                '}';
    }
}
