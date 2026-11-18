package co.uniquindio.edu.poo.model;

public abstract class ReporteVisual extends ReporteOperativo {

    protected int anchoVentana;
    protected int altoVentana;
    protected String tituloGrafico;

    /**
     * Inicializa las dimensiones y el título de un reporte visual.
     *
     * @param anchoVentana  ancho del componente gráfico.
     * @param altoVentana   alto del componente gráfico.
     * @param tituloGrafico título del gráfico.
     */
    public ReporteVisual(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(tituloGrafico);
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
        this.tituloGrafico = tituloGrafico;
    }

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

    /**
     * Renderiza la representación gráfica del reporte.
     */
    public abstract void renderizarVista();

    /**
     * Ejecuta el paso final del Template Method invocando el renderizado visual.
     *
     * @param datos información procesada para el gráfico.
     */
    @Override
    protected void finalizarGeneracion(String datos) {
        renderizarVista();
    }
}