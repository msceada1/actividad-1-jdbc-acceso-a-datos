package validators;

import exceptions.DNIException;
import exceptions.PropietarioException;
import exceptions.TelefonoException;
import models.Propietario;

public class PropietarioValidator {

    private static final String PATRON_DNI = "^[0-9]{8}[A-Z]$";
    private static final String PATRON_TELEFONO = "^[0-9]{9}$";

    /**
     * metodo encargado de verificar que un DNI cumple el patrón
     *
     * @param dni el DNI a verificar.
     * @throws DNIException si es nulo, vacío o no cumple el patrón
     */
    public void validarDNI(String dni) throws DNIException {
        if (dni == null || dni.isBlank() || !dni.trim().toUpperCase().matches(PATRON_DNI)) {
            throw new DNIException("Error: no se ha introducido DNI o no se ha respetado el patrón");
        }
    }

    /**
     * metodo encargado de validar el teléfono
     *
     * @param numTelfn el número de teléfono a evaluar
     * @throws TelefonoException si es nulo, vacío o no respeta el patrón
     */
    public void validarTelefono(String numTelfn) throws TelefonoException {
        if (numTelfn == null || numTelfn.isBlank() || !numTelfn.trim().matches(PATRON_TELEFONO)) {
            throw new TelefonoException("Error: no se ha introducido el número de teléfono o no se ha respetado el patrón");
        }
    }

    /**
     * metodo encargado de validar un propietario en base a unos requisitos
     *
     * @param p el propietario a analizar
     * @throws DNIException
     * @throws PropietarioException
     * @throws TelefonoException
     */
    public void validarPropietario(Propietario p) throws DNIException, PropietarioException, TelefonoException {
        if (p == null) {
            throw new PropietarioException("Error: no hay propietario");
        }

        validarDNI(p.getDni());
        validarTelefono(p.getTelefono());

        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new PropietarioException("Error: el propietario debe tener nombre");
        }

        if (p.getApellido() == null || p.getApellido().isBlank()) {
            throw new PropietarioException("Error: el propietario debe tener apellido");
        }
    }
}