package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class DiagramaBarra extends ReporteVisual {

    private String datos;
    private List<String> etiquetas;
    private List<Double> valores;

    /**
     * Construye un reporte visual de diagrama de barras.
     *
     * @param anchoVentana  ancho del gráfico.
     * @param altoVentana   alto del gráfico.
     * @param tituloGrafico título del gráfico.
     */
    public DiagramaBarra(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(anchoVentana, altoVentana, tituloGrafico);
        this.etiquetas = new ArrayList<>();
        this.valores = new ArrayList<>();
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public List<Double> getValores() {
        return valores;
    }

    /**
     * Calcula las entradas vendidas por cada película registrada en el cine.
     */
    @Override
    public void procesarDatos() {
        etiquetas.clear();
        valores.clear();
        Cinema cinema = Cinema.getInstancia();
        StringBuilder sb = new StringBuilder();
        for (Pelicula pelicula : cinema.getListaPeliculas()) {
            double entradas = cinema.contarEntradasPorPelicula(pelicula.getIdPelicula());
            etiquetas.add(pelicula.getTitulo());
            valores.add(entradas);
            sb.append(pelicula.getTitulo()).append(": ").append((int) entradas).append(" entradas\n");
        }
        datos = sb.toString();
    }

    /**
     * Devuelve los datos procesados para las barras del gráfico.
     *
     * @return texto con las categorías y sus valores.
     */
    @Override
    public String obtenerDatos() {
        return datos;
    }

    /**
     * Prepara la vista del diagrama de barras con los datos consolidados.
     */
    @Override
    public void renderizarVista() {
        if (datos == null) {
            procesarDatos();
        }
    }
}