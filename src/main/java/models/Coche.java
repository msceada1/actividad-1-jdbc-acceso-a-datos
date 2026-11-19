package models;

import exceptions.CocheException;
import exceptions.MatriculaException;

import java.util.Objects;

import static utils.MiEntradaSalida.cumplePatronMatricula;

public class Coche {

    private String matricula;
    private String marca;
    private String modelo;
    private Propietario propietario;

    public Coche(String matricula, String marca, String modelo) throws CocheException, MatriculaException {
        setMatricula(matricula);
        setMarca(marca);
        setModelo(modelo);
    }

    public Coche(String matricula, String marca, String modelo, Propietario propietario) throws CocheException, MatriculaException {
        setMatricula(matricula);
        setMarca(marca);
        setModelo(modelo);
        setPropietario(propietario);
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) throws CocheException, MatriculaException {
        if (matricula == null || matricula.isBlank()) {
            throw new CocheException("Error: se debe indicar la matricula del coche");
        }
        cumplePatronMatricula(matricula);

        this.matricula = matricula.trim().toUpperCase();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) throws CocheException {
        if (marca == null || marca.isBlank()) {
            throw new CocheException("Error: se debe indicar la marca del coche");
        }
        this.marca = marca.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) throws CocheException {
        if (modelo == null || modelo.isBlank()) {
            throw new CocheException("Error: se debe indicar el modelo del coche");
        }
        this.modelo = modelo.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coche coche = (Coche) o;
        return Objects.equals(matricula, coche.matricula) && Objects.equals(propietario, coche.propietario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula, propietario);
    }

    @Override
    public String toString() {
        return "Coche{" +
                "matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                '}';
    }
}
