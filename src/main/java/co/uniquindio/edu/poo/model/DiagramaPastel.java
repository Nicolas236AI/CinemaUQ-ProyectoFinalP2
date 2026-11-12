package co.uniquindio.edu.poo.model;

public class DiagramaPastel extends ReporteVisual {

    private String datos;

    public DiagramaPastel(int anchoVentana, int altoVentana, String tituloGrafico) {
        super(anchoVentana, altoVentana, tituloGrafico);
    }

    @Override
    public void generarReporte() {
        procesarDatos();
        renderizarVista();
    }

    @Override
    public void procesarDatos() {
        datos = obtenerDatos();
    }

    @Override
    public String obtenerDatos() {
        return "Datos para el diagrama de pastel";
    }

    @Override
    public void renderizarVista() {
        System.out.println("=== " + tituloGrafico + " ===");
        System.out.println("Tipo: Diagrama de pastel");
        System.out.println(datos);
        System.out.println("Dimensiones: " + anchoVentana + "x" + altoVentana);
    }
}