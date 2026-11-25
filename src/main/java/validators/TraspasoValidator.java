package validators;

import exceptions.MatriculaException;
import exceptions.TraspasoException;
import models.Traspaso;

public class TraspasoValidator {

    private static final double MIN_MONTO_ECONOMICO = 1;
    private static final int MIN_NUM_ID = 1;
    private static final CocheValidator cv = new CocheValidator();

    /**
     * metodo encargado de validar que el monto económico de un traspaso sea mayor que cero
     *
     * @param montoEconomico el importe económico del traspaso
     * @throws TraspasoException si el monto económico es menor que el mínimo permitido
     */
    public void validarMonto(double montoEconomico) throws TraspasoException {
        if (montoEconomico < MIN_MONTO_ECONOMICO) {
            throw new TraspasoException("Error: el monto económico debe ser mayor que cero");
        }
    }

    /**
     * metodo encargado de validar los requisitos de un traspaso.
     *
     * @param t el traspaso a validar.
     * @throws MatriculaException si el traspaso no tiene matrícula o no sigue el patrón.
     * @throws TraspasoException  si el traspaso es nulo, los IDs no son válidos o el monto económico es menor o igual a 0.
     */
    public void validarTraspaso(Traspaso t) throws MatriculaException, TraspasoException {
        if (t == null) {
            throw new TraspasoException("Error: el traspaso no existe");
        }

        cv.validarMatricula(t.getMatriculaCoche());

        if (t.getIdComprador() == null || t.getIdComprador() < MIN_NUM_ID) {
            throw new TraspasoException("Error: el comprador debe tener un id válido");
        }

        if (t.getIdVendedor() != null) {
            if (t.getIdVendedor() < MIN_NUM_ID) {
                throw new TraspasoException("Error: el vendedor debe tener un id válido");
            }
            if (t.getIdVendedor().equals(t.getIdComprador())) {
                throw new TraspasoException("Error: el comprador y el vendedor no pueden tener el mismo id");
            }
        }

        validarMonto(t.getMontoEconomico());
    }
}