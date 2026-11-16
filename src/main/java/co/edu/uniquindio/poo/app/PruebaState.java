package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class PruebaState {

    public static void main(String[] args) {

        Cliente cliente = new Cliente(
                "C-STATE",
                "Cliente State",
                "state@cinemauq.com",
                "1234");

        Producto producto = new Producto(
                "P-STATE",
                "Crispetas",
                10000,
                10);

        Compra compra = new Compra.Builder()
                .conCodigo(
                        "COMP-STATE-001")
                .conCliente(
                        cliente)
                .conProducto(
                        producto,
                        1)
                .build();

        System.out.println(
                "Estado inicial: "
                        + compra.getEstado());

        compra.validarConfirmacion();

        compra.pagar();

        System.out.println(
                "Despues de pagar: "
                        + compra.getEstado());

        try {

            compra.validarConfirmacion();

        } catch (ValidacionException e) {

            System.out.println(
                    "Segundo pago rechazado: "
                            + e.getMessage());
        }

        compra.cancelar();

        System.out.println(
                "Despues de cancelar: "
                        + compra.getEstado());

        try {

            compra.cancelar();

        } catch (ValidacionException e) {

            System.out.println(
                    "Segunda cancelacion rechazada: "
                            + e.getMessage());
        }
    }
}