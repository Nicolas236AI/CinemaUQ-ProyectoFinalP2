package co.uniquindio.edu.poo.model;

/**
 * Clase abstracta que establece la estructura general de los reportes
 * operativos del sistema de cine.
 */
public abstract class ReporteOperativo {
    /**
     * Define el proceso de generación de un reporte operativo.
     */
    public abstract void generarReporte();

    /**
     * Establece el procesamiento de los datos necesarios para construir
     * el reporte.
     */
    public abstract void procesarDatos();

    /**
     * Permite obtener la información procesada del reporte.
     *
     * @return información correspondiente al reporte.
     */
    public abstract String obtenerDatos();
}