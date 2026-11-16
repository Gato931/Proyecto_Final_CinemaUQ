package co.edu.uniquindio.poo.model.observador;

import co.edu.uniquindio.poo.model.AcumulacionPuntos;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.servicio.CalculadorPuntos;

import java.time.LocalDate;

public class ObservadorPuntos
        implements ObservadorCompra {

    private final CalculadorPuntos calculadorPuntos;

    public ObservadorPuntos(
            CalculadorPuntos calculadorPuntos) {

        if (calculadorPuntos == null) {
            throw new ValidacionException(
                    "El calculador de puntos es obligatorio");
        }

        this.calculadorPuntos = calculadorPuntos;
    }

    @Override
    public void actualizar(
            Compra compra) {

        int puntos = calculadorPuntos.calcular(
                compra.getValorPagado());

        AcumulacionPuntos acumulacion = compra.getCliente()
                .getCuentaPuntos()
                .acumular(
                        puntos,
                        LocalDate.now());

        compra.registrarAcumulacionPuntosGanados(
                acumulacion);
    }
}