package co.edu.uniquindio.poo.model.reporte;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;

public class ReporteTxt
        implements ExportadorReporte {

    @Override
    public String exportar(
            String titulo,
            String contenido) {

        if (titulo == null
                || titulo.isBlank()) {

            throw new ValidacionException(
                    "El titulo del reporte es obligatorio");
        }

        if (contenido == null
                || contenido.isBlank()) {

            throw new ValidacionException(
                    "El contenido del reporte es obligatorio");
        }

        return titulo
                + "\n"
                + "=============================="
                + "\n"
                + contenido;
    }
}