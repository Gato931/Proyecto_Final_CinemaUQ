package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.ConsecutivoCompra;

public class PruebaSingleton {

    public static void main(String[] args) {

        ConsecutivoCompra instancia1 = ConsecutivoCompra.getInstancia();

        ConsecutivoCompra instancia2 = ConsecutivoCompra.getInstancia();

        System.out.println(
                "Misma instancia: "
                        + (instancia1 == instancia2));

        System.out.println(
                "Primer codigo: "
                        + instancia1.siguiente());

        System.out.println(
                "Segundo codigo: "
                        + instancia2.siguiente());
    }
}