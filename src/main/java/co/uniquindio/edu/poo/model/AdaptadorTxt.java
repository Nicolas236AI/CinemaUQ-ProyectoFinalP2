package co.uniquindio.edu.poo.model;

/**
 * Adapta el exportador TXT a la interfaz IExportador,
 * permitiendo utilizarlo mediante una interfaz común.
 * Implementa el patrón estructural Adapter.
 */
public class AdaptadorTxt implements IExportador {

    private ExportadorTxt exportadorTxt;

    public AdaptadorTxt() {
        this.exportadorTxt = new ExportadorTxt();
    }

    /**
     * Delega la exportación de los datos al exportador especializado en TXT.
     *
     * @param datos información que se desea guardar.
     */
    @Override
    public void exportar(String datos) {
        exportadorTxt.exportarTxt(datos);
    }

}