package co.uniquindio.edu.poo.model;
/**
 * Representa un reporte visual de tipo diagrama de barras.
 * Permite procesar y mostrar información mediante una representación
 * basada en barras.
 */
public class DiagramaBarra extends ReporteVisual {

    private String datos;

    public DiagramaBarra(int anchoVentana, int altoVentana, String tituloGrafico) {
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
     * Prepara la información que será utilizada en el diagrama.
     */
    @Override
    public void procesarDatos() {

        datos = obtenerDatos();
    }
    /**
     * Obtiene la información que se representará en el gráfico.
     *
     * @return datos destinados al diagrama de barras.
     */
    @Override
    public String obtenerDatos() {
        return "Datos para el diagrama de barras";
    }
    /**
     * Representa el diagrama mostrando su título, tipo de gráfico,
     * información y dimensiones configuradas.
     */
    @Override
    public void renderizarVista() {
        System.out.println("=== " + tituloGrafico + " ===");
        System.out.println("Tipo: Diagrama de barras");
        System.out.println(datos);
        System.out.println("Dimensiones: " + anchoVentana + "x" + altoVentana);
    }
}