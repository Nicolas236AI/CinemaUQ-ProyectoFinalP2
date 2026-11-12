package co.uniquindio.edu.poo.model;

/**
 * Define el contrato para los componentes encargados de exportar
 * reportes en diferentes formatos.
 */
public interface IExportador {

    /**
     * Establece el mecanismo para exportar la información de un reporte.
     *
     * @param datos contenido que se desea exportar.
     */
    void exportar(String datos);

}