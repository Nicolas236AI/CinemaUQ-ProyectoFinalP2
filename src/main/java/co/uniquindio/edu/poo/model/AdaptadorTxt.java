package co.uniquindio.edu.poo.model;

public class AdaptadorTxt implements IExportador {

    private ExportadorTxt exportadorTxt;

    public AdaptadorTxt() {
        this.exportadorTxt = new ExportadorTxt();
    }

    @Override
    public void exportar(String datos) {
        exportadorTxt.exportarTxt(datos);
    }

}