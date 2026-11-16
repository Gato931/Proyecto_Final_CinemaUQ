package co.edu.uniquindio.poo.model.reporte;

import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.servicio.ServicioEstadisticas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ServicioReportes {

    private final ExportadorReporte exportador;

    public ServicioReportes(
            ExportadorReporte exportador) {

        if (exportador == null) {
            throw new ValidacionException(
                    "El exportador es obligatorio");
        }

        this.exportador = exportador;
    }

    public String generarReporteVentas(
            List<Compra> compras) {

        StringBuilder contenido = new StringBuilder();

        contenido.append(
                "Cantidad de ventas: ")
                .append(
                        compras.size())
                .append("\n");

        double totalVentas = 0;

        for (Compra compra : compras) {

            contenido.append(
                    compra.getCodigo())
                    .append(
                            " - $")
                    .append(
                            compra.calcularTotal())
                    .append("\n");

            totalVentas += compra.calcularTotal();
        }

        contenido.append(
                "Total vendido: $")
                .append(
                        totalVentas);

        return exportador.exportar(
                "Reporte de ventas CinemaUQ",
                contenido.toString());
    }

    public String generarReporteEstadisticas(
            ServicioEstadisticas estadisticas) {

        if (estadisticas == null) {
            throw new ValidacionException(
                    "Las estadisticas son obligatorias");
        }

        StringBuilder contenido = new StringBuilder();

        contenido.append(
                "ENTRADAS VENDIDAS POR PELICULA\n");

        for (Map.Entry<String, Integer> dato : estadisticas
                .entradasVendidasPorPelicula()
                .entrySet()) {

            contenido.append(
                    dato.getKey())
                    .append(
                            ": ")
                    .append(
                            dato.getValue())
                    .append(
                            "\n");
        }

        contenido.append(
                "\nINGRESOS POR PELICULA\n");

        for (Map.Entry<String, Double> dato : estadisticas
                .ingresosPorPelicula()
                .entrySet()) {

            contenido.append(
                    dato.getKey())
                    .append(
                            ": $")
                    .append(
                            dato.getValue())
                    .append(
                            "\n");
        }

        contenido.append(
                "\nVENTAS POR DIA\n");

        for (Map.Entry<LocalDate, Double> dato : estadisticas
                .ventasPorDia()
                .entrySet()) {

            contenido.append(
                    dato.getKey())
                    .append(
                            ": $")
                    .append(
                            dato.getValue())
                    .append(
                            "\n");
        }

        return exportador.exportar(
                "Reporte de estadisticas CinemaUQ",
                contenido.toString());
    }

    public Path guardarReporteEstadisticas(
            ServicioEstadisticas estadisticas,
            Path ruta) {

        if (ruta == null) {
            throw new ValidacionException(
                    "La ruta del reporte es obligatoria");
        }

        String reporte = generarReporteEstadisticas(
                estadisticas);

        try {

            Path carpeta = ruta.getParent();

            if (carpeta != null) {
                Files.createDirectories(
                        carpeta);
            }

            Files.writeString(
                    ruta,
                    reporte,
                    StandardCharsets.UTF_8);

            return ruta.toAbsolutePath();

        } catch (IOException exception) {

            throw new ValidacionException(
                    "No se pudo guardar el reporte");
        }
    }
}