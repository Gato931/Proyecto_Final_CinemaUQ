package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class ReembolsoCliente
        implements EstrategiaReembolso {

    @Override
    public double calcular(
            double valorPagado,
            double horasAntes) {

        if (valorPagado <= 0) {

            throw new ValidacionException(
                    "El valor pagado debe ser mayor que cero");
        }

        if (horasAntes < 0) {

            throw new ValidacionException(
                    "La funcion ya paso");
        }

        if (horasAntes > 24) {

            return valorPagado;
        }

        if (horasAntes >= 2) {

            return valorPagado * 0.80;
        }

        throw new ValidacionException(
                "No se puede cancelar con menos de 2 horas de anticipacion");
    }
}