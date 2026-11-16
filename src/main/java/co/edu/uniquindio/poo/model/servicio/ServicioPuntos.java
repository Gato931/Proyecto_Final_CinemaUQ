package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.AcumulacionPuntos;
import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

import java.time.LocalDate;

public class ServicioPuntos {

    private static final double PESOS_POR_PUNTO_REDIMIDO = 10.0;

    public int calcularPuntosNecesarios(
            double valorBeneficio) {

        if (valorBeneficio <= 0) {
            throw new ValidacionException(
                    "El valor del beneficio debe ser mayor que cero");
        }

        return (int) Math.ceil(
                valorBeneficio
                        / PESOS_POR_PUNTO_REDIMIDO);
    }

    public double calcularValorPuntos(
            int puntos) {

        if (puntos < 0) {
            throw new ValidacionException(
                    "La cantidad de puntos no puede ser negativa");
        }

        return puntos
                * PESOS_POR_PUNTO_REDIMIDO;
    }

    public double calcularDescuento(
            Cliente cliente,
            int puntos,
            double totalCompra,
            LocalDate fecha) {

        if (cliente == null) {
            throw new ValidacionException(
                    "El cliente es obligatorio");
        }

        if (fecha == null) {
            throw new ValidacionException(
                    "La fecha es obligatoria");
        }

        if (puntos < 0) {
            throw new ValidacionException(
                    "La cantidad de puntos no puede ser negativa");
        }

        if (puntos == 0) {
            return 0;
        }

        if (cliente
                .getCuentaPuntos()
                .consultarDisponibles(fecha) < puntos) {

            throw new ValidacionException(
                    "El cliente no tiene puntos suficientes");
        }

        double descuento = calcularValorPuntos(
                puntos);

        if (descuento > totalCompra) {
            throw new ValidacionException(
                    "Los puntos redimidos no pueden superar el valor de la compra");
        }

        return descuento;
    }

    public void redimirPuntos(
            Cliente cliente,
            int puntos,
            LocalDate fecha) {

        if (puntos > 0) {

            cliente.getCuentaPuntos()
                    .redimir(
                            puntos,
                            fecha);
        }
    }

    public void redimirBeneficioCompleto(
            Cliente cliente,
            double valorBeneficio,
            LocalDate fecha) {

        int puntosNecesarios = calcularPuntosNecesarios(
                valorBeneficio);

        cliente.getCuentaPuntos()
                .redimir(
                        puntosNecesarios,
                        fecha);
    }

    public void revertirPorCancelacion(
            Compra compra,
            LocalDate fecha) {

        AcumulacionPuntos generados = compra.getAcumulacionPuntosGanados();

        if (generados != null) {
            generados.anular();
        }

        if (compra.getPuntosRedimidos() > 0) {

            compra.getCliente()
                    .getCuentaPuntos()
                    .acumular(
                            compra.getPuntosRedimidos(),
                            fecha);
        }
    }
}