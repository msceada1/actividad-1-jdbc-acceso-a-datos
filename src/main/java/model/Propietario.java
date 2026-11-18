package model;

import exceptions.PropietarioException;

import java.util.Objects;

public class Propietario {
    private String nombre;
    private int id;

    public Propietario(String nombre) throws PropietarioException {
        setNombre(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setNombre(String nombre) throws PropietarioException {
        if (nombre == null || nombre.isBlank()) {
            throw new PropietarioException("Error: El propietario debe tener nombre");
        }
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Propietario{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Propietario that = (Propietario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
