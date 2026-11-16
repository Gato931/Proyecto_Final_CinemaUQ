package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Combo;
import co.edu.uniquindio.poo.model.Producto;

public class PruebaComposite {

    public static void main(String[] args) {

        Producto crispetas = new Producto(
                "P-COMP-001",
                "Crispetas",
                12000,
                10);

        Producto gaseosa = new Producto(
                "P-COMP-002",
                "Gaseosa",
                7000,
                20);

        Combo comboPareja = new Combo(
                "C-COMP-001",
                "Combo Pareja",
                25000);

        comboPareja.agregarElemento(
                crispetas,
                1);

        comboPareja.agregarElemento(
                gaseosa,
                2);

        Combo comboDoble = new Combo(
                "C-COMP-002",
                "Combo Doble",
                48000);

        comboDoble.agregarElemento(
                comboPareja,
                2);

        System.out.println(
                "Hay stock para el combo compuesto: "
                        + comboDoble.tieneStock(1));

        comboDoble.descontarStock(1);

        System.out.println(
                "Stock de crispetas despues: "
                        + crispetas.getStock());

        System.out.println(
                "Stock de gaseosas despues: "
                        + gaseosa.getStock());
    }
}