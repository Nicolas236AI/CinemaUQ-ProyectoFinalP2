package co.uniquindio.edu.poo.model;
/**
 * Representa un reporte operativo que puede exportarse.
 * Utiliza la interfaz IExportador para delegar la exportación
 * a diferentes formatos de archivo.
 */
public class ReporteExportable extends ReporteOperativo {

    private String nombre;
    private IExportador exportador;

    public ReporteExportable(String nombre, IExportador exportador) {
        this.nombre = nombre;
        this.exportador = exportador;
    }

    /**
     * Genera el reporte procesando sus datos y delegando
     * la exportación al componente correspondiente.
     */
    @Override
    public void generarReporte() {
        procesarDatos();
        exportador.exportar(obtenerDatos());
    }

    /**
     * Procesa la información necesaria para construir el reporte.
     */
    @Override
    public void procesarDatos() {
        System.out.println("Procesando datos del reporte exportable: " + nombre);
    }

    /**
     * Obtiene el contenido textual del reporte.
     *
     * @return información del reporte.
     */
    @Override
    public String obtenerDatos() {
        return "Reporte exportable: " + nombre;
    }

    public void setExportador(IExportador exportador) {
        this.exportador = exportador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}