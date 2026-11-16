package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.observador.ObservadorConfirmacionCompra;
import co.edu.uniquindio.poo.model.observador.ObservadorPuntos;
import co.edu.uniquindio.poo.model.repositorio.RegistroCompras;
import co.edu.uniquindio.poo.model.servicio.CalculadorPuntosEstandar;
import co.edu.uniquindio.poo.model.servicio.ServicioCompras;

import java.time.LocalDate;

public class PruebaObserver {

    public static void main(String[] args) {

        RegistroCompras registroCompras = new RegistroCompras();

        ServicioCompras servicioCompras = new ServicioCompras(
                registroCompras);

        servicioCompras.agregarObservador(
                new ObservadorPuntos(
                        new CalculadorPuntosEstandar()));

        servicioCompras.agregarObservador(
                new ObservadorConfirmacionCompra());

        Cliente cliente = new Cliente(
                "C-OBS",
                "Cliente Observer",
                "observer@cinemauq.com",
                "1234");

        cliente.getTarjetaVirtual()
                .recargar(
                        50000,
                        "Recarga para prueba Observer");

        Producto producto = new Producto(
                "P-OBS",
                "Crispetas Observer",
                10000,
                10);

        Compra compra = new Compra.Builder()
                .conCodigo(
                        "COMP-OBS-001")
                .conCliente(
                        cliente)
                .conProducto(
                        producto,
                        1)
                .build();

        servicioCompras.confirmarCompra(
                compra);

        System.out.println(
                "Estado de la compra: "
                        + compra.getEstado());

        System.out.println(
                "Puntos despues de la notificacion: "
                        + cliente
                                .getCuentaPuntos()
                                .consultarDisponibles(
                                        LocalDate.now()));
    }
}