package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class EstadoCancelada implements EstadoCompra {

    @Override
    public void validarConfirmacion() {
        throw new ValidacionException(
                "La compra fue cancelada");
    }

    @Override
    public void validarCancelacion() {
        throw new ValidacionException(
                "La compra ya fue cancelada");
    }

    @Override
    public void pagar(Compra compra) {
        throw new ValidacionException(
                "La compra fue cancelada");
    }

    @Override
    public void cancelar(Compra compra) {
        throw new ValidacionException(
                "La compra ya fue cancelada");
    }

    @Override
    public String nombre() {
        return "CANCELADA";
    }

    @Override
    public boolean mantieneAsientosOcupados() {
        return false;
    }
}