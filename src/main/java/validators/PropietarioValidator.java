package validators;

import exceptions.DNIException;
import exceptions.PropietarioException;
import exceptions.TelefonoException;
import models.Propietario;

public class PropietarioValidator {

    /**
     * metodo encargado de verificar que un dni
     * @param dni el dni a verificar
     * @throws DNIException si es nulo, vacio o no cumple el patron
     */
    public void validarDNI(String dni) throws DNIException {
        String patronDNI = "^[0-9]{8}[A-Z]$";

        if (dni == null || dni.isBlank() || !(dni.trim().toUpperCase().matches(patronDNI))) {
            throw new DNIException("Error: no se ha introducido DNI o no se ha resptado el patron");
        }
    }

    /**
     * metodo encargado de validar el telefono
     * @param numTelfn el numero de telefono a evaluar
     * @throws TelefonoException si es nulo, vacio o no respeta el patron
     */
    public void validarTelefono(String numTelfn) throws TelefonoException {
        String patronTlfn = "^[0-9]{9}$";

        if (numTelfn == null || numTelfn.isBlank() || !(numTelfn.matches(patronTlfn))) {
            throw new TelefonoException("Error: no se ha introducido el numero de telefono o no se ha resptado el patron");
        }
    }

    /**
     * metodo encargado de validar un propietario en base a unos requisitos
     * @param p el propietario a analizar
     * @throws DNIException si el dni no cumple el formato
     * @throws PropietarioException si el nombre o apellido son nulos o vacios
     */
    public void validarPropietario(Propietario p) throws DNIException, PropietarioException {
        validarDNI(p.getDni());

        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new PropietarioException("error: el propietario debe tener nombre");
        }

        if (p.getApellido() == null || p.getApellido().isBlank()) {
            throw new PropietarioException("error: el propietario debe tener apellido");
        }
    }
}
