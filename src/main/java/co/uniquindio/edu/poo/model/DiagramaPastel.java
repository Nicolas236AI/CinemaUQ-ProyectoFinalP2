package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class DiagramaPastel extends ReporteVisual {

    private String datos;
    private List<String> categorias;
    private List<Double> porcentajes;

    /**
     * Construye un reporte visual de diagrama de pastel.
     *
     * @param anchoVentana  ancho del gráfico.
     * @param altoVentana   alto del gráfico.
     * @param tituloGrafico título del gráfico.
     */
    public DiagramaPastel(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(anchoVentana, altoVentana, tituloGrafico);
        this.categorias = new ArrayList<>();
        this.porcentajes = new ArrayList<>();
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public List<Double> getPorcentajes() {
        return porcentajes;
    }

    /**
     * Calcula el porcentaje de ocupación de cada función programada en el cine.
     */
    @Override
    public void procesarDatos() {
        categorias.clear();
        porcentajes.clear();
        Cinema cinema = Cinema.getInstancia();
        StringBuilder sb = new StringBuilder();
        for (Funcion funcion : cinema.getListaFunciones()) {
            double ocupacion = funcion.calcularPorcentajeOcupacion();
            String etiqueta = funcion.getIdFuncion() + " - " + funcion.getPelicula().getTitulo();
            categorias.add(etiqueta);
            porcentajes.add(ocupacion);
            sb.append(etiqueta).append(": ").append(String.format("%.1f", ocupacion)).append("%\n");
        }
        datos = sb.toString();
    }

    /**
     * Devuelve los porcentajes calculados para el diagrama de pastel.
     *
     * @return texto con las proporciones por categoría.
     */
    @Override
    public String obtenerDatos() {
        return datos;
    }

    /**
     * Prepara la vista del diagrama de pastel con los porcentajes calculados.
     */
    @Override
    public void renderizarVista() {
        if (datos == null) {
            procesarDatos();
        }
    }
}