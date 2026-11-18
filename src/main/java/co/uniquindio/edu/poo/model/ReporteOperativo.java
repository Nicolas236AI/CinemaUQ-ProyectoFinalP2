package co.uniquindio.edu.poo.model;

import java.time.LocalDate;

public abstract class ReporteOperativo {

    protected String titulo;
    protected String fechaGeneracion;
    protected AdministradorCine administradorCine;

    /**
     * Inicializa un reporte operativo asignando su título y la fecha actual de generación.
     *
     * @param titulo título descriptivo del reporte.
     */
    public ReporteOperativo(String titulo) {
        this.titulo = titulo;
        this.fechaGeneracion = LocalDate.now().toString();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public AdministradorCine getAdministradorCine() {
        return administradorCine;
    }

    public void setAdministradorCine(AdministradorCine administradorCine) {
        this.administradorCine = administradorCine;
    }

    /**
     * Define el algoritmo fijo (Template Method) para construir y generar cualquier reporte operativo.
     */
    public final void generarReporte() {
        procesarDatos();
        String datos = obtenerDatos();
        finalizarGeneracion(datos);
    }

    /**
     * Procesa y consolida la información necesaria desde el sistema del cine.
     */
    public abstract void procesarDatos();

    /**
     * Construye y retorna la información formateada del reporte.
     *
     * @return cadena con los datos consolidados del reporte.
     */
    public abstract String obtenerDatos();

    /**
     * Ejecuta el paso final de salida del reporte (exportación o renderizado visual).
     *
     * @param datos datos procesados del reporte.
     */
    protected abstract void finalizarGeneracion(String datos);
}