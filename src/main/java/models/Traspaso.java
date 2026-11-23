package models;

import java.util.Objects;

public class Traspaso {

    private int idTraspaso;
    private String matriculaCoche;
    private String dniComprador;
    private double montoEconomico;

    public Traspaso(String matriculaCoche, String dniComprador, double montoEconomico) {
        this.matriculaCoche = matriculaCoche;
        this.dniComprador = dniComprador;
        this.montoEconomico = montoEconomico;
    }

    public Traspaso(int idTraspaso, String matriculaCoche, String dniComprador, double montoEconomico) {
        this.idTraspaso = idTraspaso;
        this.matriculaCoche = matriculaCoche;
        this.dniComprador = dniComprador;
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

    public String getDniComprador() {
        return dniComprador;
    }

    public void setDniComprador(String dniComprador) {
        this.dniComprador = dniComprador;
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
                ", dniComprador='" + dniComprador + '\'' +
                ", montoEconomico=" + montoEconomico +
                '}';
    }
}
