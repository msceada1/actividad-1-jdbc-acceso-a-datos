package validators;

import exceptions.CocheException;
import exceptions.MatriculaException;
import models.Coche;

public class CocheValidator {

    private static final String PATRON_MATRICULA = "^\\d{4}[A-Z]{3}$";
    private static final int MIN_PRECIO_COCHE = 1;

    /**
     * metodo encargado de validar el patron de una matricula
     *
     * @param matricula la matricula a validar
     *
     */
    public void validarMatricula(String matricula) throws MatriculaException {
        if (matricula == null || matricula.isBlank() || !matricula.trim().toUpperCase().matches(PATRON_MATRICULA)) {
            throw new MatriculaException("Error: no se ha introducido matrícula o no se ha seguido el patrón");
        }
    }

    /**
     * metodo encargado de validar que un coche cumpla los rquisitos
     *
     * @param c el coche a validar
     * @throws MatriculaException si hay fallos en el formato de la matricula
     * @throws CocheException     si no se introduce marca modelo o se introduce un coche null
     */
    public void validarCoche(Coche c) throws MatriculaException, CocheException {
        if (c == null) {
            throw new CocheException("Coche no encontrado");
        }

        validarMatricula(c.getMatricula());

        if (c.getMarca() == null || c.getMarca().isBlank()) {
            throw new CocheException("Error: el coche debe tener marca");
        }

        if (c.getModelo() == null || c.getModelo().isBlank()) {
            throw new CocheException("Error: el coche debe tener modelo");
        }

        if (c.getPrecio() < MIN_PRECIO_COCHE) {
            throw new CocheException("Error: el precio del coche debe ser mayor que cero");
        }
    }
}
