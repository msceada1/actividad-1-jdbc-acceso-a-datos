package validators;

import exceptions.DNIException;
import exceptions.PropietarioException;
import models.Propietario;

public class PropietarioValidator {

    /**
     * metodo encargado de verificar que un dni
     * @param dni
     * @throws DNIException
     */
    public void validarDNI(String dni) throws DNIException {
        String patronDNI = "^[0-9]{8}[A-Z]$";

        if (dni == null || dni.isBlank() || !(dni.trim().toUpperCase().matches(patronDNI))) {
            throw new DNIException("Error: no se ha introducido DNI o no se ha resptado el patron");
        }
    }

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
