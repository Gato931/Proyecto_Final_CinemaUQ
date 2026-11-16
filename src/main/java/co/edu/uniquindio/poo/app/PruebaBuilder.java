package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.excepcion.DominioException;

public class PruebaBuilder {

    public static void main(String[] args) {

        Cliente cliente = new Cliente(
                "C-BUILDER",
                "Cliente Builder",
                "builder@cinemauq.com",
                "1234");

        Producto producto = new Producto(
                "P-BUILDER",
                "Crispetas",
                10000,
                10);

        Compra compra = new Compra.Builder()
                .conCodigo("COMP-BUILDER-001")
                .conCliente(cliente)
                .conProducto(producto, 2)
                .build();

        System.out.println(
                "Compra creada: "
                        + compra.getCodigo());

        System.out.println(
                "Cantidad de productos: "
                        + compra.getProductos().size());

        System.out.println(
                "Subtotal: $"
                        + compra.calcularSubtotal());

        System.out.println(
                "Estado inicial: "
                        + compra.getEstado());

        try {

            new Compra.Builder()
                    .conCodigo("COMP-BUILDER-002")
                    .conCliente(cliente)
                    .build();

        } catch (DominioException exception) {

            System.out.println(
                    "Compra incompleta rechazada: "
                            + exception.getMessage());
        }
    }
}