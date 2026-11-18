package model;

import exceptions.CocheException;

import java.util.Objects;

public class Coche {

    private String matricula;
    private String marca;
    private String modelo;

    public Coche(String matricula, String marca, String modelo) throws CocheException {
        setMatricula(matricula);
        setMarca(marca);
        setModelo(modelo);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) throws CocheException {
        if (matricula == null || matricula.isBlank()) {
            throw new CocheException("Error: se debe indicar la matricula del coche");
        }

        if (!cumplePatronMatricula(matricula.trim().toUpperCase())) {
            throw new CocheException("Error: La matricula no pasa el patron");
        }

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

    /**
     * metodo encargado de verificar que la matricula siga el patron correspondiente
     * @param matricula la matricula del coche
     * @return true si cumple el patron, false en caso contrario
     */
    private boolean cumplePatronMatricula(String matricula) {
        String regex = "\\d{4}[B-DF-HJ-NP-TV-Z]{3}";

        return matricula.matches(regex);
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
                '}';
    }
}
