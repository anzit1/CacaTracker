package com.ct.cacatrackerproject.clases;

public class CodigoPostalCountDTO {
    private String codigopostal;
    private int total;

    public CodigoPostalCountDTO() {
    }

    public CodigoPostalCountDTO(String codigopostal, int total) {
        this.codigopostal = codigopostal;
        this.total = total;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CodigoPostalCountDTO{" +
                "codigopostal='" + codigopostal + '\'' +
                ", total=" + total +
                '}';
    }
}
