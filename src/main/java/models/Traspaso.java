package models;

import java.util.Objects;

public class Traspaso {

    private Integer idTraspaso;
    private String matriculaCoche;
    private Integer idVendedor;
    private Integer idComprador;
    private double montoEconomico;

    public Traspaso(String matriculaCoche, Integer idVendedor, Integer idComprador, double montoEconomico) {
        this.idTraspaso = null;
        this.matriculaCoche = matriculaCoche;
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.montoEconomico = montoEconomico;
    }

    public Traspaso(Integer idTraspaso, String matriculaCoche, Integer idVendedor, Integer idComprador, double montoEconomico) {
        this.idTraspaso = idTraspaso;
        this.matriculaCoche = matriculaCoche;
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.montoEconomico = montoEconomico;
    }

    public Integer getIdTraspaso() {
        return idTraspaso;
    }

    public void setIdTraspaso(Integer idTraspaso) {
        this.idTraspaso = idTraspaso;
    }

    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Integer getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Integer idComprador) {
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
        return Objects.equals(idTraspaso, traspaso.idTraspaso);
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