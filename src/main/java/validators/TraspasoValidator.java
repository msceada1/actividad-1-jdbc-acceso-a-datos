package validators;

import exceptions.DNIException;
import exceptions.MatriculaException;
import exceptions.PropietarioException;
import exceptions.TraspasoException;
import models.Traspaso;

public class TraspasoValidator {

    private static final double MIN_MONTO_ECONOMICO = 1;
    private static final int MIN_NUM_ID = 1;

    /**
     * metodo encargado de validar los requisitos de un traspaso
     * @param t el traspaso a validar
     * @throws MatriculaException si el traspaso no tiene matricula o no sigue el patron
     * @throws TraspasoException si el monto economico es menor igual a 0
     */
    public void validarTraspaso(Traspaso t) throws MatriculaException, TraspasoException {
        if(t == null) {
            throw new TraspasoException("error: el traspaso no existe");
        }
        CocheValidator c = new CocheValidator();

        //reciclo el metodo para validar la matricula en el traspaso
        c.validarMatricula(t.getMatriculaCoche());

        if (t.getIdComprador() == null || t.getIdComprador() < MIN_NUM_ID) {
            throw new TraspasoException("Error: el comprador debe tener un id válido");
        }

        if (t.getIdVendedor() != null && t.getIdVendedor() < MIN_NUM_ID) {
            throw new TraspasoException("error: el comprador debe tener un id valido");
        }

        if (t.getIdVendedor().equals(t.getIdComprador())) {
            throw new TraspasoException("error: el comprador y el vendedor no pueden tener el mismo id");
        }

        if (t.getMontoEconomico() < MIN_MONTO_ECONOMICO) {
            throw new TraspasoException("Error: el monto económico debe ser mayor que cero");
        }


    }
}
