package co.uniquindio.edu.poo.model;

/**
 * Representa un reporte visual de tipo diagrama de pastel.
 * Permite mostrar información distribuida en diferentes categorías.
 */
public class DiagramaPastel extends ReporteVisual {

    private String datos;

    public DiagramaPastel(int anchoVentana, int altoVentana, String tituloGrafico) {
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
     * @return datos destinados al diagrama de pastel.
     */
    @Override
    public String obtenerDatos() {

        return "Datos para el diagrama de pastel";
    }
    /**
     * Representa el diagrama mostrando su título, tipo de gráfico,
     * información y dimensiones configuradas.
     */
    @Override
    public void renderizarVista() {
        System.out.println("=== " + tituloGrafico + " ===");
        System.out.println("Tipo: Diagrama de pastel");
        System.out.println(datos);
        System.out.println("Dimensiones: " + anchoVentana + "x" + altoVentana);
    }
}