package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class EstadoPagada implements EstadoCompra {

    @Override
    public void validarConfirmacion() {
        throw new ValidacionException(
                "La compra ya fue pagada");
    }

    @Override
    public void pagar(Compra compra) {
        throw new ValidacionException(
                "La compra ya fue pagada");
    }

    @Override
    public void cancelar(Compra compra) {
        compra.cambiarEstado(
                new EstadoCancelada());
    }

    @Override
    public String nombre() {
        return "PAGADA";
    }

    @Override
    public boolean mantieneAsientosOcupados() {
        return true;
    }
}