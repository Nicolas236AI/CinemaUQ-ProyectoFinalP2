package co.uniquindio.edu.poo.model;

public class ReporteExportable extends ReporteOperativo {

    private String nombre;
    private IExportador exportador;

    public ReporteExportable(String nombre, IExportador exportador) {
        this.nombre = nombre;
        this.exportador = exportador;
    }

    @Override
    public void generarReporte() {
        procesarDatos();
        exportador.exportar(obtenerDatos());
    }

    @Override
    public void procesarDatos() {
        System.out.println("Procesando datos del reporte exportable: " + nombre);
    }

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