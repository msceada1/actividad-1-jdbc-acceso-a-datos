package validators;

import exceptions.DNIException;
import exceptions.MatriculaException;
import exceptions.PropietarioException;
import exceptions.TraspasoException;
import models.Traspaso;

public class TraspasoValidator {

    static final double MIN_MONTO_ECONOMICO = 1;

    /**
     * metodo encargado de validar los requisitos de un traspaso
     * @param t el traspaso a validar
     * @throws MatriculaException si el traspaso no tiene matricula o no sigue el patron
     * @throws DNIException si el trapaso no tiene dni o no sigue el patron
     * @throws TraspasoException si el monto economico es menor igual a 0
     */
    public void validarTraspaso(Traspaso t) throws MatriculaException, DNIException, TraspasoException {
        PropietarioValidator p = new PropietarioValidator();
        CocheValidator c = new CocheValidator();

        //reciclo los metodos para validar el dni y la matricula en el traspaso
        p.validarDNI(t.getDniComprador());
        c.validarMatricula(t.getMatriculaCoche());

        if (t.getMontoEconomico() < MIN_MONTO_ECONOMICO) {
            throw new TraspasoException("Error: el monto económico debe ser mayor que cero");
        }
    }
}
