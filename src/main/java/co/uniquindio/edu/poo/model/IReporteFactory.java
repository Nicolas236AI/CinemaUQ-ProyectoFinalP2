package co.uniquindio.edu.poo.model;

/**
 * Define el contrato para las fábricas encargadas de crear los reportes
 * operativos del sistema (exportables y visuales).
 * Permite desacoplar a quien solicita un reporte de las clases concretas
 * que lo implementan.
 */
public interface IReporteFactory {

    /**
     * Crea un reporte exportable en el formato indicado.
     *
     * @param nombre  nombre del reporte.
     * @param formato formato de exportación ("TXT" o "PDF").
     * @return reporte exportable configurado con su exportador.
     */
    ReporteExportable crearReporteExportable(String nombre, String formato);

    /**
     * Crea un reporte visual del tipo indicado.
     *
     * @param tipo   tipo de gráfico ("BARRAS", "PASTEL" o "LINEAS").
     * @param titulo título que tendrá el gráfico.
     * @return reporte visual configurado.
     */
    ReporteVisual crearReporteVisual(String tipo, String titulo);
}
