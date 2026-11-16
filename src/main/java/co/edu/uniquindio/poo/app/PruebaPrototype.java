package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Asiento;
import co.edu.uniquindio.poo.model.Funcion;
import co.edu.uniquindio.poo.model.Pelicula;
import co.edu.uniquindio.poo.model.Sala;

import java.time.LocalDateTime;

public class PruebaPrototype {

    public static void main(String[] args) {

        Pelicula pelicula = new Pelicula(
                "P-PROT",
                "Interestelar",
                "Ciencia ficcion",
                169,
                "+12");

        Sala sala = new Sala(
                "S-PROT",
                "Sala Prototype");

        sala.agregarAsiento(
                new Asiento(
                        "A1",
                        "A",
                        1));

        Funcion funcionOriginal = new Funcion(
                "F-PROT-001",
                pelicula,
                sala,
                LocalDateTime.now().plusDays(1),
                18000);

        Funcion funcionCopia = funcionOriginal.copiarPara(
                "F-PROT-002",
                LocalDateTime.now().plusDays(2));

        Asiento asientoOriginal = funcionOriginal.buscarAsiento("A1");

        Asiento asientoCopia = funcionCopia.buscarAsiento("A1");

        asientoOriginal.ocupar();

        System.out.println(
                "Son objetos distintos: "
                        + (asientoOriginal != asientoCopia));

        System.out.println(
                "A1 ocupada en original: "
                        + asientoOriginal.isOcupado());

        System.out.println(
                "A1 ocupada en copia: "
                        + asientoCopia.isOcupado());
    }
}