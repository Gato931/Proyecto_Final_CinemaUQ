package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.Entrada;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.repositorio.RegistroCompras;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServicioEstadisticas {

    private final RegistroCompras registroCompras;

    public ServicioEstadisticas(
            RegistroCompras registroCompras) {

        if (registroCompras == null) {
            throw new ValidacionException(
                    "El registro de compras es obligatorio");
        }

        this.registroCompras = registroCompras;
    }

    public Map<String, Integer> entradasVendidasPorPelicula() {

        Map<String, Integer> resultado = new LinkedHashMap<>();

        for (Compra compra : registroCompras.listar()) {

            if (!estaPagada(compra)) {
                continue;
            }

            for (Entrada entrada : compra.getEntradas()) {

                String pelicula = entrada.getFuncion()
                        .getPelicula()
                        .getTitulo();

                resultado.merge(
                        pelicula,
                        1,
                        Integer::sum);
            }
        }

        return resultado;
    }

    public Map<String, Double> ingresosPorPelicula() {

        Map<String, Double> resultado = new LinkedHashMap<>();

        for (Compra compra : registroCompras.listar()) {

            if (!estaPagada(compra)) {
                continue;
            }

            for (Entrada entrada : compra.getEntradas()) {

                String pelicula = entrada.getFuncion()
                        .getPelicula()
                        .getTitulo();

                resultado.merge(
                        pelicula,
                        entrada.getPrecio(),
                        Double::sum);
            }
        }

        return resultado;
    }

    public Map<LocalDate, Double> ventasPorDia() {

        Map<LocalDate, Double> resultado = new LinkedHashMap<>();

        for (Compra compra : registroCompras.listar()) {

            if (!estaPagada(compra)) {
                continue;
            }

            LocalDate fecha = compra.getFecha()
                    .toLocalDate();

            resultado.merge(
                    fecha,
                    compra.getValorPagado(),
                    Double::sum);
        }

        return resultado;
    }

    private boolean estaPagada(
            Compra compra) {

        return "PAGADA".equals(
                compra.getEstado());
    }
}