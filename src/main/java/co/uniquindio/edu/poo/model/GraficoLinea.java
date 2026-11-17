package co.uniquindio.edu.poo.model;

/**
 * Representa un reporte visual de tipo gráfico de líneas.
 * Permite mostrar el comportamiento de la información (por ejemplo ventas
 * o recargas) a lo largo del tiempo.
 */
public class GraficoLinea extends ReporteVisual {

    private String datos;

    public GraficoLinea(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(anchoVentana, altoVentana, tituloGrafico);
    }

    /**
     * Genera el reporte ejecutando el procesamiento de los datos
     * y posteriormente mostrando su representación visual.
     */
    @Override
    public void generarReporte() {
        procesarDatos();
        renderizarVista();
    }

    /**
     * Prepara la información que será utilizada en el gráfico.
     */
    @Override
    public void procesarDatos() {
        datos = obtenerDatos();
    }

    /**
     * Obtiene la información que se representará en el gráfico.
     *
     * @return datos destinados al gráfico de líneas.
     */
    @Override
    public String obtenerDatos() {
        return "Datos para el gráfico de líneas";
    }

    /**
     * Representa el gráfico mostrando su título, tipo de gráfico,
     * información y dimensiones configuradas.
     */
    @Override
    public void renderizarVista() {
        System.out.println("=== " + tituloGrafico + " ===");
        System.out.println("Tipo: Gráfico de líneas");
        System.out.println(datos);
        System.out.println("Dimensiones: " + anchoVentana + "x" + altoVentana);
    }
}