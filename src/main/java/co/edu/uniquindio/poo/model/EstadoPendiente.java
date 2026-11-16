package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class EstadoPendiente implements EstadoCompra {

    @Override
    public void validarConfirmacion() {
    }

    @Override
    public void validarCancelacion() {
        throw new ValidacionException(
                "No se puede cancelar una compra que no ha sido pagada");
    }

    @Override
    public void pagar(Compra compra) {
        compra.cambiarEstado(
                new EstadoPagada());
    }

    @Override
    public void cancelar(Compra compra) {
        throw new ValidacionException(
                "No se puede cancelar una compra que no ha sido pagada");
    }

    @Override
    public String nombre() {
        return "PENDIENTE";
    }

    @Override
    public boolean mantieneAsientosOcupados() {
        return false;
    }
}