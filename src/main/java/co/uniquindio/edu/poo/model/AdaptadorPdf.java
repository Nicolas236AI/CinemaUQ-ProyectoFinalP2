package co.uniquindio.edu.poo.model;

/**
 * Adapta el exportador PDF a la interfaz IExportador,
 * permitiendo utilizarlo mediante una interfaz común.
 * Implementa el patrón estructural Adapter.
 */
public class AdaptadorPdf implements IExportador {

    private ExportadorPdf exportadorPdf;

    public AdaptadorPdf() {
        this.exportadorPdf = new ExportadorPdf();
    }

    /**
     * Delega la exportación de los datos al exportador especializado en PDF.
     *
     * @param datos información que se desea guardar.
     */
    @Override
    public void exportar(String datos) {
        exportadorPdf.exportarPdf(datos);
    }

}