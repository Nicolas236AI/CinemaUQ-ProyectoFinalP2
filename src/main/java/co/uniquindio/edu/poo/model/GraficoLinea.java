package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class GraficoLinea extends ReporteVisual {

    private String datos;
    private List<String> fechas;
    private List<Double> montosPorFecha;

    /**
     * Construye un reporte visual de gráfico de líneas.
     *
     * @param anchoVentana  ancho del gráfico.
     * @param altoVentana   alto del gráfico.
     * @param tituloGrafico título del gráfico.
     */
    public GraficoLinea(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(anchoVentana, altoVentana, tituloGrafico);
        this.fechas = new ArrayList<>();
        this.montosPorFecha = new ArrayList<>();
    }

    public List<String> getFechas() {
        return fechas;
    }

    public List<Double> getMontosPorFecha() {
        return montosPorFecha;
    }

    /**
     * Agrupa los montos de ventas completadas por fecha para trazar su evolución temporal.
     */
    @Override
    public void procesarDatos() {
        fechas.clear();
        montosPorFecha.clear();
        Cinema cinema = Cinema.getInstancia();
        for (Compra compra : cinema.getListaCompras()) {
            if (compra.getEstado() == EstadoCompra.COMPLETADA && compra.getFecha() != null) {
                int indice = fechas.indexOf(compra.getFecha());
                if (indice == -1) {
                    fechas.add(compra.getFecha());
                    montosPorFecha.add(compra.getTotal());
                } else {
                    montosPorFecha.set(indice, montosPorFecha.get(indice) + compra.getTotal());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fechas.size(); i++) {
            sb.append(fechas.get(i)).append(": $").append(montosPorFecha.get(i)).append("\n");
        }
        datos = sb.toString();
    }

    /**
     * Devuelve la serie temporal de ventas procesada.
     *
     * @return texto con los valores ordenados por fecha.
     */
    @Override
    public String obtenerDatos() {
        return datos;
    }

    /**
     * Prepara la vista del gráfico de líneas con la serie temporal calculada.
     */
    @Override
    public void renderizarVista() {
        if (datos == null) {
            procesarDatos();
        }
    }
}