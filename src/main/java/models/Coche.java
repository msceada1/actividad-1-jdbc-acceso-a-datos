package models;


import java.util.Objects;

public class Coche {

    private String matricula;
    private String marca;
    private String modelo;
    private Extra extras;
    private double precio;
    private int idPropietario;

    public Coche(String matricula, String marca, String modelo, Extra extras, double precio, int idPropietario) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.extras = extras;
        this.precio = precio;
        this.idPropietario = idPropietario;
    }

    public Coche(String matricula, String marca, String modelo, Extra extras, double precio) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.extras = extras;
        this.precio = precio;
        this.idPropietario = Integer.parseInt(null);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Extra getExtras() {
        return extras;
    }

    public void setExtras(Extra extras) {
        this.extras = extras;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coche coche = (Coche) o;
        return Objects.equals(matricula, coche.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matricula);
    }

    @Override
    public String toString() {
        return "Coche{" +
                "matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", extras=" + extras +
                ", precio=" + precio +
                ", idPropietario=" + idPropietario +
                '}';
    }
}
