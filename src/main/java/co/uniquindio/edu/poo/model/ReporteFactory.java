package co.uniquindio.edu.poo.model;

/**
 * Fábrica concreta encargada de crear los reportes operativos del sistema.
 * Centraliza la creación de reportes exportables (con su adaptador TXT o PDF)
 * y de reportes visuales (barras, pastel o líneas).
 * Implementa el patrón creacional Factory Method / Abstract Factory.
 */
public class ReporteFactory implements IReporteFactory {

    private static final int ANCHO_POR_DEFECTO = 800;
    private static final int ALTO_POR_DEFECTO = 600;

    /**
     * Crea un reporte exportable seleccionando el adaptador según el formato.
     *
     * @param nombre  nombre del reporte.
     * @param formato "TXT" o "PDF" (sin distinguir mayúsculas).
     * @return reporte exportable listo para generarse.
     * @throws IllegalArgumentException si el formato no es soportado.
     */
    @Override
    public ReporteExportable crearReporteExportable(String nombre, String formato) {
        if (formato == null) {
            throw new IllegalArgumentException("El formato del reporte no puede ser nulo.");
        }
        IExportador exportador;
        switch (formato.trim().toUpperCase()) {
            case "TXT":
                exportador = new AdaptadorTxt();
                break;
            case "PDF":
                exportador = new AdaptadorPdf();
                break;
            default:
                throw new IllegalArgumentException("Formato de exportación no soportado: " + formato);
        }
        return new ReporteExportable(nombre, exportador);
    }

    /**
     * Crea un reporte visual según el tipo de gráfico solicitado.
     *
     * @param tipo   "BARRAS", "PASTEL" o "LINEAS" (sin distinguir mayúsculas).
     * @param titulo título del gráfico.
     * @return reporte visual listo para generarse.
     * @throws IllegalArgumentException si el tipo no es soportado.
     */
    @Override
    public ReporteVisual crearReporteVisual(String tipo, String titulo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de gráfico no puede ser nulo.");
        }
        switch (tipo.trim().toUpperCase()) {
            case "BARRAS":
                return new DiagramaBarra(ANCHO_POR_DEFECTO, ALTO_POR_DEFECTO, titulo);
            case "PASTEL":
                return new DiagramaPastel(ANCHO_POR_DEFECTO, ALTO_POR_DEFECTO, titulo);
            case "LINEAS":
                return new GraficoLinea(ANCHO_POR_DEFECTO, ALTO_POR_DEFECTO, titulo);
            default:
                throw new IllegalArgumentException("Tipo de gráfico no soportado: " + tipo);
        }
    }
}