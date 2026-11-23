package validators;

import exceptions.CocheException;
import exceptions.MatriculaException;
import models.Coche;

public class CocheValidator {

    /**
     * metodo encargado de validar el patron de una matricula
     *
     * @param matricula la matricula a validar
     *
     */
    public void validarMatricula(String matricula) throws MatriculaException {
        String patronMatricula = "^\\\\d{4}[a-zA-Z]{3}$";

        if (matricula == null || matricula.isBlank() || !(matricula.trim().toUpperCase().matches(patronMatricula))) {
            throw new MatriculaException("Error: no se ha introducido matricula o no se ha seguido el patron");
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
    }
}
