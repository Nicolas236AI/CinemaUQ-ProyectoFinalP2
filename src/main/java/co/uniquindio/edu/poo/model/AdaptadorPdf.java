package co.uniquindio.edu.poo.model;

public class AdaptadorPdf implements IExportador {

    private ExportadorPdf exportadorPdf;

    public AdaptadorPdf() {
        this.exportadorPdf = new ExportadorPdf();
    }

    @Override
    public void exportar(String datos) {
        exportadorPdf.exportarPdf(datos);
    }

}