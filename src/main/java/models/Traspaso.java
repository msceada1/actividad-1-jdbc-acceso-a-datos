package models;

import java.util.Objects;

public class Traspaso {

    private int idTraspaso;
    private String matriculaCoche;
    private int idVendedor;
    private int idComprador;
    private double montoEconomico;

    public Traspaso(String matriculaCoche, int idVendedor, int idComprador, double montoEconomico) {
        this.matriculaCoche = matriculaCoche;
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.montoEconomico = montoEconomico;
    }

    public Traspaso(int idTraspaso, String matriculaCoche, int idVendedor, int idComprador, double montoEconomico) {
        this.idTraspaso = idTraspaso;
        this.matriculaCoche = matriculaCoche;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.montoEconomico = montoEconomico;
    }

    public int getIdTraspaso() {
        return idTraspaso;
    }

    public void setIdTraspaso(int idTraspaso) {
        this.idTraspaso = idTraspaso;
    }

    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }

    public double getMontoEconomico() {
        return montoEconomico;
    }

    public void setMontoEconomico(double montoEconomico) {
        this.montoEconomico = montoEconomico;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Traspaso traspaso = (Traspaso) o;
        return idTraspaso == traspaso.idTraspaso;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idTraspaso);
    }

    @Override
    public String toString() {
        return "Traspaso{" +
                "idTraspaso=" + idTraspaso +
                ", matriculaCoche='" + matriculaCoche + '\'' +
                ", idVendedor=" + idVendedor +
                ", idComprador=" + idComprador +
                ", montoEconomico=" + montoEconomico +
                '}';
    }
}
