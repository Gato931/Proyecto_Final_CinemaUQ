package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Administrador;
import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.servicio.GestorTarjetas;
import co.edu.uniquindio.poo.model.servicio.ServicioTarjetas;
import co.edu.uniquindio.poo.model.servicio.ServicioTarjetasProxy;

public class PruebaProxy {

    public static void main(String[] args) {

        Cliente cliente = new Cliente(
                "C-PROXY",
                "Cliente Proxy",
                "cliente.proxy@cinemauq.com",
                "1234");

        Administrador administrador = new Administrador(
                "A-PROXY",
                "Administrador Proxy",
                "admin.proxy@cinemauq.com",
                "admin");

        GestorTarjetas gestor = new ServicioTarjetasProxy(
                new ServicioTarjetas());

        try {

            gestor.recargar(
                    cliente,
                    cliente,
                    20000);

        } catch (DominioException exception) {

            System.out.println(
                    "Cliente rechazado: "
                            + exception.getMessage());
        }

        gestor.recargar(
                administrador,
                cliente,
                50000);

        System.out.println(
                "Saldo despues de recarga autorizada: $"
                        + cliente
                                .getTarjetaVirtual()
                                .getSaldo());
    }
}