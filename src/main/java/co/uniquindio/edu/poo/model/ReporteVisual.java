package co.uniquindio.edu.poo.model;

public abstract class ReporteVisual extends ReporteOperativo {

    protected int anchoVentana;
    protected int altoVentana;
    protected String tituloGrafico;

    public ReporteVisual(int anchoVentana, int altoVentana, String tituloGrafico) {
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
        this.tituloGrafico = tituloGrafico;
    }

    public abstract void renderizarVista();

    public int getAnchoVentana() {
        return anchoVentana;
    }

    public void setAnchoVentana(int anchoVentana) {
        this.anchoVentana = anchoVentana;
    }

    public int getAltoVentana() {
        return altoVentana;
    }

    public void setAltoVentana(int altoVentana) {
        this.altoVentana = altoVentana;
    }

    public String getTituloGrafico() {
        return tituloGrafico;
    }

    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }
}