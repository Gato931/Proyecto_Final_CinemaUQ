package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.DetalleComboCompra;
import co.edu.uniquindio.poo.model.DetalleProductoCompra;
import co.edu.uniquindio.poo.model.Entrada;
import co.edu.uniquindio.poo.model.EstadoFuncion;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.excepcion.AsientoNoDisponibleException;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.observador.ObservadorCompra;
import co.edu.uniquindio.poo.model.observador.SujetoCompra;
import co.edu.uniquindio.poo.model.repositorio.AlmacenCompras;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioCompras
        implements SujetoCompra {

    private final AlmacenCompras almacenCompras;

    private final List<ObservadorCompra> observadores = new ArrayList<>();
    private final ServicioPuntos servicioPuntos;

    public ServicioCompras(
            AlmacenCompras almacenCompras) {

        this(
                almacenCompras,
                new ServicioPuntos());
    }

    public ServicioCompras(
            AlmacenCompras almacenCompras,
            ServicioPuntos servicioPuntos) {

        if (almacenCompras == null) {
            throw new ValidacionException(
                    "El almacen de compras es obligatorio");
        }

        if (servicioPuntos == null) {
            throw new ValidacionException(
                    "El servicio de puntos es obligatorio");
        }

        this.almacenCompras = almacenCompras;

        this.servicioPuntos = servicioPuntos;
    }

    @Override
    public void agregarObservador(
            ObservadorCompra observador) {

        if (observador == null) {

            throw new ValidacionException(
                    "El observador es obligatorio");
        }

        if (!observadores.contains(
                observador)) {

            observadores.add(
                    observador);
        }
    }

    @Override
    public void eliminarObservador(
            ObservadorCompra observador) {

        observadores.remove(
                observador);
    }

    private void notificarCompraConfirmada(
            Compra compra) {

        for (ObservadorCompra observador : observadores) {

            observador.actualizar(
                    compra);
        }
    }

    public void confirmarCompra(
            Compra compra) {

        confirmarCompra(
                compra,
                0);
    }

    public void confirmarCompra(
            Compra compra,
            int puntosRedimir) {

        if (compra == null) {
            throw new ValidacionException(
                    "La compra es obligatoria");
        }

        compra.validarConfirmacion();

        validarEntradas(
                compra);

        validarConfiteria(
                compra);

        double total = compra.calcularTotal();

        LocalDate fecha = LocalDate.now();

        double descuentoPuntos = servicioPuntos.calcularDescuento(
                compra.getCliente(),
                puntosRedimir,
                total,
                fecha);

        double valorPagado = total
                - descuentoPuntos;

        if (valorPagado > 0) {

            compra.getCliente()
                    .getTarjetaVirtual()
                    .debitar(
                            valorPagado,
                            "Pago de compra "
                                    + compra.getCodigo());
        }

        servicioPuntos.redimirPuntos(
                compra.getCliente(),
                puntosRedimir,
                fecha);

        compra.registrarPago(
                valorPagado,
                puntosRedimir,
                descuentoPuntos);

        descontarStock(
                compra);

        compra.pagar();

        almacenCompras.guardar(
                compra);

        for (Entrada entrada : compra.getEntradas()) {

            entrada.getAsiento()
                    .ocupar();
        }

        notificarCompraConfirmada(
                compra);
    }

    private void validarEntradas(
            Compra compra) {

        for (Entrada entrada : compra.getEntradas()) {

            if (entrada.getFuncion()
                    .getEstado() != EstadoFuncion.PROGRAMADA) {

                throw new ValidacionException(
                        "La funcion de la entrada no se encuentra programada");
            }

            if (!entrada.getAsiento()
                    .estaDisponible()) {

                throw new AsientoNoDisponibleException(
                        "El asiento "
                                + entrada.getAsiento()
                                + " no esta disponible");
            }

            if (almacenCompras
                    .asientoVendido(
                            entrada.getFuncion(),
                            entrada.getAsiento())) {

                throw new AsientoNoDisponibleException(
                        "El asiento "
                                + entrada.getAsiento()
                                + " ya fue vendido para esa funcion");
            }
        }
    }

    private void validarConfiteria(
            Compra compra) {

        Map<Producto, Integer> necesidades = new HashMap<>();

        for (DetalleProductoCompra detalle : compra.getProductos()) {

            detalle.getProducto()
                    .acumularStockNecesario(
                            necesidades,
                            detalle.getCantidad());
        }

        for (DetalleComboCompra detalle : compra.getCombos()) {

            if (!detalle.getCombo()
                    .isActivo()) {

                throw new ValidacionException(
                        "Combo inactivo: "
                                + detalle.getCombo()
                                        .getNombre());
            }

            detalle.getCombo()
                    .acumularStockNecesario(
                            necesidades,
                            detalle.getCantidad());
        }

        for (Map.Entry<Producto, Integer> necesidad : necesidades.entrySet()) {

            Producto producto = necesidad.getKey();

            int cantidad = necesidad.getValue();

            if (!producto.isActivo()) {

                throw new ValidacionException(
                        "Producto inactivo: "
                                + producto.getNombre());
            }

            if (!producto.tieneStock(
                    cantidad)) {

                throw new ValidacionException(
                        "Stock insuficiente para "
                                + producto.getNombre());
            }
        }
    }

    private void descontarStock(
            Compra compra) {

        for (DetalleProductoCompra detalle : compra.getProductos()) {

            detalle.getProducto()
                    .descontarStock(
                            detalle.getCantidad());
        }

        for (DetalleComboCompra detalle : compra.getCombos()) {

            detalle.getCombo()
                    .descontarStock(
                            detalle.getCantidad());
        }
    }
}