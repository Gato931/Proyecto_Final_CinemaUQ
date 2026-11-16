package co.edu.uniquindio.poo.model.observador;

public interface SujetoCompra {

    void agregarObservador(
            ObservadorCompra observador);

    void eliminarObservador(
            ObservadorCompra observador);
}