package co.edu.uniquindio.poo.model.servicio;

public interface EstrategiaReembolso {

    double calcular(
            double valorPagado,
            double horasAntes);
}