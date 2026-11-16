package co.edu.uniquindio.poo.model;

public interface EstadoCompra {

    void validarConfirmacion();

    void validarCancelacion();

    void pagar(Compra compra);

    void cancelar(Compra compra);

    String nombre();

    boolean mantieneAsientosOcupados();
}