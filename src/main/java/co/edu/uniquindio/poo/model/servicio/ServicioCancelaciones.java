package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.DetalleComboCompra;
import co.edu.uniquindio.poo.model.DetalleProductoCompra;
import co.edu.uniquindio.poo.model.Entrada;
import co.edu.uniquindio.poo.model.EstadoFuncion;
import co.edu.uniquindio.poo.model.Funcion;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.repositorio.RegistroCompras;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ServicioCancelaciones {

    private final RegistroCompras registroCompras;
    private final GestorReembolsos gestorReembolsos;
    private final ServicioPuntos servicioPuntos;

    public ServicioCancelaciones(
            RegistroCompras registroCompras) {

        if (registroCompras == null) {
            throw new ValidacionException(
                    "El registro de compras es obligatorio");
        }

        this.registroCompras = registroCompras;

        this.gestorReembolsos = new GestorReembolsos(
                new ReembolsoCliente());
        this.servicioPuntos = new ServicioPuntos();
    }

    public double cancelarPorCliente(
            Compra compra,
            LocalDateTime fechaSolicitud) {

        validarCompra(compra);

        if (fechaSolicitud == null) {
            throw new ValidacionException(
                    "La fecha de solicitud es obligatoria");
        }

        compra.validarCancelacion();

        Funcion funcion = obtenerFuncion(compra);

        double horasAntes = Duration.between(
                fechaSolicitud,
                funcion.getFechaHora())
                .toMinutes()
                / 60.0;

        gestorReembolsos.setEstrategia(
                new ReembolsoCliente());

        double valorReembolso = gestorReembolsos.calcular(
                compra.getValorPagado(),
                horasAntes);

        completarCancelacion(
                compra,
                valorReembolso,
                "Cancelacion de compra "
                        + compra.getCodigo());

        return valorReembolso;
    }

    public int cancelarFuncion(
            Funcion funcion) {

        if (funcion == null) {
            throw new ValidacionException(
                    "La funcion es obligatoria");
        }

        if (funcion.getEstado() != EstadoFuncion.PROGRAMADA) {

            throw new ValidacionException(
                    "La funcion no se encuentra programada");
        }

        funcion.cancelar();

        gestorReembolsos.setEstrategia(
                new ReembolsoCine());

        int comprasCanceladas = 0;

        for (Compra compra : registroCompras.listar()) {

            if (compra.mantieneAsientosOcupados()
                    && perteneceAFuncion(
                            compra,
                            funcion)) {

                compra.validarCancelacion();

                double valorReembolso = gestorReembolsos.calcular(
                        compra.getValorPagado(),
                        0);

                completarCancelacion(
                        compra,
                        valorReembolso,
                        "Cancelacion de funcion "
                                + funcion.getCodigo());

                comprasCanceladas++;
            }
        }

        return comprasCanceladas;
    }

    private void completarCancelacion(
            Compra compra,
            double valorReembolso,
            String descripcion) {

        if (valorReembolso > 0) {

            compra.getCliente()
                    .getTarjetaVirtual()
                    .reembolsar(
                            valorReembolso,
                            descripcion);
        }

        servicioPuntos.revertirPorCancelacion(
                compra,
                LocalDate.now());

        devolverStock(compra);

        liberarAsientos(compra);

        compra.cancelar();
    }

    private void liberarAsientos(
            Compra compra) {

        for (Entrada entrada : compra.getEntradas()) {

            entrada.getAsiento()
                    .liberar();
        }
    }

    private void devolverStock(
            Compra compra) {

        Map<Producto, Integer> cantidades = new HashMap<>();

        for (DetalleProductoCompra detalle : compra.getProductos()) {

            detalle.getProducto()
                    .acumularStockNecesario(
                            cantidades,
                            detalle.getCantidad());
        }

        for (DetalleComboCompra detalle : compra.getCombos()) {

            detalle.getCombo()
                    .acumularStockNecesario(
                            cantidades,
                            detalle.getCantidad());
        }

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            entrada.getKey()
                    .aumentarStock(
                            entrada.getValue());
        }
    }

    private Funcion obtenerFuncion(
            Compra compra) {

        if (compra.getEntradas().isEmpty()) {

            throw new ValidacionException(
                    "La compra no tiene entradas para cancelar");
        }

        return compra.getEntradas()
                .get(0)
                .getFuncion();
    }

    private boolean perteneceAFuncion(
            Compra compra,
            Funcion funcion) {

        for (Entrada entrada : compra.getEntradas()) {

            if (entrada.getFuncion()
                    .getCodigo()
                    .equalsIgnoreCase(
                            funcion.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    private void validarCompra(
            Compra compra) {

        if (compra == null) {

            throw new ValidacionException(
                    "La compra es obligatoria");
        }
    }
}