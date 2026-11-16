package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.excepcion.ValidacionException;
import co.edu.uniquindio.poo.model.servicio.GestorReembolsos;
import co.edu.uniquindio.poo.model.servicio.ReembolsoCine;
import co.edu.uniquindio.poo.model.servicio.ReembolsoCliente;

public class PruebaStrategy {

    public static void main(String[] args) {

        GestorReembolsos gestor = new GestorReembolsos(
                new ReembolsoCliente());

        System.out.println(
                "Cliente 30 horas antes: $"
                        + gestor.calcular(
                                50000,
                                30));

        System.out.println(
                "Cliente 5 horas antes: $"
                        + gestor.calcular(
                                50000,
                                5));

        try {

            System.out.println(
                    "Cliente 1 hora antes: $"
                            + gestor.calcular(
                                    50000,
                                    1));

        } catch (ValidacionException e) {

            System.out.println(
                    "Cancelacion rechazada: "
                            + e.getMessage());
        }

        gestor.setEstrategia(
                new ReembolsoCine());

        System.out.println(
                "Cancelacion del cine: $"
                        + gestor.calcular(
                                50000,
                                1));
    }
}