package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.AcumulacionPuntos;
import co.edu.uniquindio.poo.model.Administrador;
import co.edu.uniquindio.poo.model.Asiento;
import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.Compra;
import co.edu.uniquindio.poo.model.Entrada;
import co.edu.uniquindio.poo.model.Funcion;
import co.edu.uniquindio.poo.model.Pelicula;
import co.edu.uniquindio.poo.model.Producto;
import co.edu.uniquindio.poo.model.Sala;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.observador.ObservadorPuntos;
import co.edu.uniquindio.poo.model.repositorio.RegistroCompras;
import co.edu.uniquindio.poo.model.repositorio.RegistroUsuarios;
import co.edu.uniquindio.poo.model.servicio.CalculadorPuntosEstandar;
import co.edu.uniquindio.poo.model.servicio.GestorTarjetas;
import co.edu.uniquindio.poo.model.servicio.ServicioCancelaciones;
import co.edu.uniquindio.poo.model.servicio.ServicioCompras;
import co.edu.uniquindio.poo.model.servicio.ServicioRegistroUsuarios;
import co.edu.uniquindio.poo.model.servicio.ServicioTarjetas;
import co.edu.uniquindio.poo.model.servicio.ServicioTarjetasProxy;
import co.edu.uniquindio.poo.model.reporte.ExportadorReporte;
import co.edu.uniquindio.poo.model.reporte.ReporteConFechaDecorator;
import co.edu.uniquindio.poo.model.reporte.ReporteConFirmaDecorator;
import co.edu.uniquindio.poo.model.reporte.ReporteTxt;
import co.edu.uniquindio.poo.model.reporte.ServicioReportes;
import co.edu.uniquindio.poo.model.servicio.ServicioEstadisticas;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainConsola {

    public static void main(String[] args) {

        RegistroUsuarios registroUsuarios = new RegistroUsuarios();

        ServicioRegistroUsuarios servicioUsuarios = new ServicioRegistroUsuarios(
                registroUsuarios);

        RegistroCompras registroCompras = new RegistroCompras();

        ServicioCompras servicioCompras = new ServicioCompras(
                registroCompras);

        servicioCompras.agregarObservador(
                new ObservadorPuntos(
                        new CalculadorPuntosEstandar()));

        ServicioCancelaciones servicioCancelaciones = new ServicioCancelaciones(
                registroCompras);

        GestorTarjetas servicioTarjetas = new ServicioTarjetasProxy(
                new ServicioTarjetas());

        Cliente cliente = servicioUsuarios.registrarCliente(
                "C-001",
                "Laura",
                "laura@correo.com",
                "1234");

        Administrador administrador = servicioUsuarios.registrarAdministrador(
                "A-001",
                "Administrador",
                "admin@cinemauq.com",
                "admin");

        servicioTarjetas.recargar(
                administrador,
                cliente,
                100000);

        try {

            servicioTarjetas.recargar(
                    cliente,
                    cliente,
                    10000);

        } catch (DominioException exception) {

            System.out.println(
                    "Proxy rechazo la recarga: "
                            + exception.getMessage());
        }

        Pelicula pelicula = new Pelicula(
                "P-001",
                "Interestelar",
                "Ciencia ficcion",
                169,
                "+12");

        Sala sala = new Sala(
                "S-01",
                "Sala 1");

        sala.agregarAsiento(
                new Asiento(
                        "A1",
                        "A",
                        1));

        sala.agregarAsiento(
                new Asiento(
                        "A2",
                        "A",
                        2));

        Funcion funcion = new Funcion(
                "F-001",
                pelicula,
                sala,
                LocalDateTime.now()
                        .plusDays(2),
                18000);

        Funcion funcionCopia = funcion.copiarPara(
                "F-002",
                LocalDateTime.now()
                        .plusDays(3));

        Asiento asientoA1 = funcion.buscarAsiento(
                "A1");

        Entrada entrada = new Entrada(
                "E-001",
                funcion,
                asientoA1);

        Compra compra = new Compra.Builder()
                .conCodigo(
                        "COMP-PRUEBA-001")
                .conCliente(
                        cliente)
                .conEntrada(
                        entrada)
                .build();

        servicioCompras.confirmarCompra(
                compra);

        System.out.println(
                "Compra confirmada: "
                        + compra.getCodigo());

        System.out.println(
                "Saldo: $"
                        + cliente
                                .getTarjetaVirtual()
                                .getSaldo());

        System.out.println(
                "Puntos: "
                        + cliente
                                .getCuentaPuntos()
                                .consultarDisponibles(
                                        LocalDate.now()));

        System.out.println(
                "A1 ocupada en F-001: "
                        + funcion
                                .buscarAsiento("A1")
                                .isOcupado());

        System.out.println(
                "A1 ocupada en F-002: "
                        + funcionCopia
                                .buscarAsiento("A1")
                                .isOcupado());

        try {

            Compra repetida = new Compra.Builder()
                    .conCodigo(
                            "COMP-PRUEBA-002")
                    .conCliente(
                            cliente)
                    .conEntrada(
                            new Entrada(
                                    "E-002",
                                    funcion,
                                    asientoA1))
                    .build();

            servicioCompras.confirmarCompra(
                    repetida);

        } catch (DominioException exception) {

            System.out.println(
                    "Asiento duplicado rechazado: "
                            + exception.getMessage());
        }

        System.out.println();
        System.out.println(
                "--- ESTADISTICAS Y REPORTES ---");

        ServicioEstadisticas estadisticas = new ServicioEstadisticas(
                registroCompras);

        System.out.println(
                "Entradas vendidas por pelicula: "
                        + estadisticas
                                .entradasVendidasPorPelicula());

        System.out.println(
                "Ingresos por pelicula: "
                        + estadisticas
                                .ingresosPorPelicula());

        System.out.println(
                "Ventas por dia: "
                        + estadisticas
                                .ventasPorDia());

        ExportadorReporte exportadorTxt = new ReporteConFirmaDecorator(
                new ReporteConFechaDecorator(
                        new ReporteTxt()));

        ServicioReportes servicioReportes = new ServicioReportes(
                exportadorTxt);

        Path archivoReporte = servicioReportes
                .guardarReporteEstadisticas(
                        estadisticas,
                        Path.of(
                                "reportes",
                                "reporte-estadisticas.txt"));

        System.out.println(
                "Reporte guardado en: "
                        + archivoReporte);
        System.out.println();
        System.out.println(
                "--- CANCELACION DEL CLIENTE ---");

        double reembolso = servicioCancelaciones
                .cancelarPorCliente(
                        compra,
                        funcion.getFechaHora()
                                .minusHours(5));

        System.out.println(
                "Reembolso cliente: $"
                        + reembolso);

        System.out.println(
                "Estado compra: "
                        + compra.getEstado());

        System.out.println(
                "Saldo despues del reembolso: $"
                        + cliente
                                .getTarjetaVirtual()
                                .getSaldo());

        System.out.println(
                "A1 disponible despues de cancelar: "
                        + asientoA1
                                .estaDisponible());

        System.out.println();
        System.out.println(
                "--- CANCELACION TARDIA ---");

        Asiento asientoA2 = funcion.buscarAsiento(
                "A2");

        Compra compraTardia = new Compra.Builder()
                .conCodigo(
                        "COMP-PRUEBA-003")
                .conCliente(
                        cliente)
                .conEntrada(
                        new Entrada(
                                "E-003",
                                funcion,
                                asientoA2))
                .build();

        servicioCompras.confirmarCompra(
                compraTardia);

        try {

            servicioCancelaciones
                    .cancelarPorCliente(
                            compraTardia,
                            funcion.getFechaHora()
                                    .minusHours(1));

        } catch (DominioException exception) {

            System.out.println(
                    "Cancelacion tardia rechazada: "
                            + exception.getMessage());
        }

        System.out.println(
                "Estado despues del rechazo: "
                        + compraTardia.getEstado());

        System.out.println();
        System.out.println(
                "--- CANCELACION DEL CINE ---");

        int comprasCanceladas = servicioCancelaciones
                .cancelarFuncion(
                        funcion);

        System.out.println(
                "Compras canceladas por el cine: "
                        + comprasCanceladas);

        System.out.println(
                "Estado compra: "
                        + compraTardia
                                .getEstado());

        System.out.println(
                "Estado funcion: "
                        + funcion.getEstado());

        System.out.println(
                "A2 disponible despues de cancelar la funcion: "
                        + asientoA2
                                .estaDisponible());

        System.out.println(
                "Saldo final: $"
                        + cliente
                                .getTarjetaVirtual()
                                .getSaldo());
        System.out.println();
        System.out.println(
                "--- PUNTOS Y REDENCION ---");

        Cliente clientePuntos = new Cliente(
                "C-PUNTOS",
                "Cliente Puntos",
                "puntos@cinemauq.com",
                "1234");

        clientePuntos
                .getTarjetaVirtual()
                .recargar(
                        50000,
                        "Saldo prueba puntos");

        LocalDate hoy = LocalDate.now();

        AcumulacionPuntos loteVencido = clientePuntos
                .getCuentaPuntos()
                .acumular(
                        100,
                        hoy.minusYears(2));

        AcumulacionPuntos loteAntiguo = clientePuntos
                .getCuentaPuntos()
                .acumular(
                        300,
                        hoy.minusMonths(6));

        AcumulacionPuntos loteNuevo = clientePuntos
                .getCuentaPuntos()
                .acumular(
                        500,
                        hoy);

        System.out.println(
                "Puntos disponibles antes: "
                        + clientePuntos
                                .getCuentaPuntos()
                                .consultarDisponibles(
                                        hoy));

        Producto productoPuntos = new Producto(
                "P-PUNTOS",
                "Producto prueba puntos",
                18000,
                10);

        Compra compraPuntos = new Compra.Builder()
                .conCodigo(
                        "COMP-PUNTOS-001")
                .conCliente(
                        clientePuntos)
                .conProducto(
                        productoPuntos,
                        1)
                .build();

        servicioCompras.confirmarCompra(
                compraPuntos,
                400);

        System.out.println(
                "Puntos redimidos: "
                        + compraPuntos
                                .getPuntosRedimidos());

        System.out.println(
                "Descuento por puntos: $"
                        + compraPuntos
                                .getDescuentoPuntos());

        System.out.println(
                "Valor realmente pagado: $"
                        + compraPuntos
                                .getValorPagado());

        System.out.println(
                "Saldo final: $"
                        + clientePuntos
                                .getTarjetaVirtual()
                                .getSaldo());

        System.out.println(
                "Lote vencido ignorado: "
                        + loteVencido
                                .getPuntosDisponibles());

        System.out.println(
                "Lote antiguo restante: "
                        + loteAntiguo
                                .getPuntosDisponibles());

        System.out.println(
                "Lote nuevo restante: "
                        + loteNuevo
                                .getPuntosDisponibles());

        System.out.println(
                "Puntos disponibles despues: "
                        + clientePuntos
                                .getCuentaPuntos()
                                .consultarDisponibles(
                                        hoy));
    }
}