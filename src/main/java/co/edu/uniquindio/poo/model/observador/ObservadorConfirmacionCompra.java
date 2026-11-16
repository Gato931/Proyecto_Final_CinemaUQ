package co.edu.uniquindio.poo.model.observador;

import co.edu.uniquindio.poo.model.Compra;

public class ObservadorConfirmacionCompra
        implements ObservadorCompra {

    @Override
    public void actualizar(
            Compra compra) {

        System.out.println(
                "Observer confirmo la compra "
                        + compra.getCodigo()
                        + " del cliente "
                        + compra.getCliente()
                                .getNombre());
    }
}