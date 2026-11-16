package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class GestorReembolsos {

    private EstrategiaReembolso estrategia;

    public GestorReembolsos(
            EstrategiaReembolso estrategia) {

        setEstrategia(estrategia);
    }

    public void setEstrategia(
            EstrategiaReembolso estrategia) {

        if (estrategia == null) {

            throw new ValidacionException(
                    "La estrategia de reembolso es obligatoria");
        }

        this.estrategia = estrategia;
    }

    public double calcular(
            double valorPagado,
            double horasAntes) {

        return estrategia.calcular(
                valorPagado,
                horasAntes);
    }
}