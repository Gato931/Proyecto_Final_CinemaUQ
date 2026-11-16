package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class ReembolsoCine
        implements EstrategiaReembolso {

    @Override
    public double calcular(
            double valorPagado,
            double horasAntes) {

        if (valorPagado <= 0) {

            throw new ValidacionException(
                    "El valor pagado debe ser mayor que cero");
        }

        return valorPagado;
    }
}