package co.uniquindio.edu.poo.model;

import java.time.LocalDate;

public class ReporteExportable extends ReporteOperativo implements IExportador {

    private String nombre;
    private String formato;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String contenidoProcesado;

    /**
     * Construye un reporte exportable indicando su nombre y formato de salida.
     *
     * @param nombre  nombre del reporte operativo.
     * @param formato formato de exportación (TXT o PDF).
     */
    public ReporteExportable(String nombre, String formato) {
        this(nombre, formato, null, null);
    }

    /**
     * Construye un reporte exportable compatible con instancias de adaptador de exportación.
     *
     * @param nombre     nombre del reporte operativo.
     * @param exportador adaptador utilizado para determinar el formato.
     */
    public ReporteExportable(String nombre, IExportador exportador) {
        this(nombre, (exportador instanceof AdaptadorPdf) ? "PDF" : "TXT", null, null);
    }

    /**
     * Construye un reporte exportable filtrado por un rango de fechas determinado.
     *
     * @param nombre      nombre del reporte operativo.
     * @param formato     formato de exportación (TXT o PDF).
     * @param fechaInicio fecha inicial del periodo consultado.
     * @param fechaFin    fecha final del periodo consultado.
     */
    public ReporteExportable(String nombre, String formato, LocalDate fechaInicio, LocalDate fechaFin) {
        super(nombre);
        this.nombre = nombre;
        this.formato = (formato != null) ? formato.trim().toUpperCase() : "TXT";
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.contenidoProcesado = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getContenidoProcesado() {
        return contenidoProcesado;
    }

    public void setContenidoProcesado(String contenidoProcesado) {
        this.contenidoProcesado = contenidoProcesado;
    }

    /**
     * Evalúa si una fecha en formato texto (YYYY-MM-DD) pertenece al periodo configurado en el reporte.
     *
     * @param fechaTexto fecha del registro evaluado.
     * @return true si está dentro del rango o si no hay filtro de fechas activo.
     */
    private boolean estaEnRangoFechas(String fechaTexto) {
        if (fechaInicio == null && fechaFin == null) {
            return true;
        }
        if (fechaTexto == null || fechaTexto.length() < 10) {
            return false;
        }
        LocalDate fechaRegistro = LocalDate.parse(fechaTexto.substring(0, 10));
        if (fechaInicio != null && fechaRegistro.isBefore(fechaInicio)) {
            return false;
        }
        if (fechaFin != null && fechaRegistro.isAfter(fechaFin)) {
            return false;
        }
        return true;
    }

    /**
     * Consolida la información de cualquiera de los 7 reportes operativos exigidos por el sistema.
     */
    @Override
    public void procesarDatos() {
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        Cinema cinema = Cinema.getInstancia();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(nombre.toUpperCase()).append(" - CINEMAUQ ===\n");
        sb.append("Fecha de generación: ").append(fechaGeneracion).append("\n");
        if (fechaInicio != null || fechaFin != null) {
            sb.append("Periodo consultado: ")
                    .append(fechaInicio != null ? fechaInicio : "Inicio")
                    .append(" al ")
                    .append(fechaFin != null ? fechaFin : "Actualidad")
                    .append("\n");
        }
        sb.append("\n");

        String clave = nombre.toLowerCase();
        if (clave.contains("venta")) {
            double totalVentas = 0.0;
            int registros = 0;
            for (Compra c : cinema.getListaCompras()) {
                if (c.getEstado() == EstadoCompra.COMPLETADA && estaEnRangoFechas(c.getFecha())) {
                    sb.append("Compra ").append(c.getIdCompra()).append(" | Fecha: ").append(c.getFecha())
                            .append(" | Total: $").append(c.getTotal()).append("\n");
                    totalVentas += c.getTotal();
                    registros++;
                }
            }
            sb.append("\nRegistros encontrados: ").append(registros);
            sb.append("\nTotal ventas: $").append(totalVentas);
        } else if (clave.contains("pelicula") || clave.contains("entrada")) {
            int totalEntradas = 0;
            for (Pelicula p : cinema.getListaPeliculas()) {
                int entradasPelicula = cinema.contarEntradasPorPelicula(p.getIdPelicula());
                totalEntradas += entradasPelicula;
                sb.append("Película: ").append(p.getTitulo()).append(" | Entradas: ").append(entradasPelicula).append("\n");
            }
            sb.append("\nTotal entradas: ").append(totalEntradas);
        } else if (clave.contains("ocupacion") || clave.contains("sala")) {
            for (Funcion f : cinema.getListaFunciones()) {
                sb.append("Función ").append(f.getIdFuncion()).append(" (").append(f.getPelicula().getTitulo())
                        .append(" - Sala ").append(f.getSala().getNumeroSala()).append("): ")
                        .append(String.format("%.2f", f.calcularPorcentajeOcupacion())).append("%\n");
            }
        } else if (clave.contains("producto") || clave.contains("combo") || clave.contains("confiteria")) {
            int unidadesVendidas = 0;
            double totalConfiteria = 0.0;
            for (Compra c : cinema.getListaCompras()) {
                if (c.getEstado() == EstadoCompra.COMPLETADA && estaEnRangoFechas(c.getFecha()) && c.getListaDetalles() != null) {
                    for (DetalleCompra d : c.getListaDetalles()) {
                        if (d.getElementoConfiteria() != null) {
                            sb.append("Ítem: ").append(d.getElementoConfiteria().obtenerNombre())
                                    .append(" | Cant: ").append(d.getCantidad())
                                    .append(" | Subtotal: $").append(d.getSubtotal()).append("\n");
                            unidadesVendidas += d.getCantidad();
                            totalConfiteria += d.getSubtotal();
                        }
                    }
                }
            }
            sb.append("\nTotal productos/combos vendidos: ").append(unidadesVendidas);
            sb.append("\nTotal recaudado confitería: $").append(totalConfiteria);
        } else if (clave.contains("recarga")) {
            int totalRecargas = 0;
            double montoRecargado = 0.0;
            for (Cliente cli : cinema.getListaClientes()) {
                if (cli.getTarjeta() != null) {
                    for (Recarga r : cli.getTarjeta().getListaRecargas()) {
                        if (estaEnRangoFechas(r.getFecha())) {
                            sb.append("Recarga ").append(r.getIdRecarga()).append(" | Cliente: ").append(cli.getNombre())
                                    .append(" | Fecha: ").append(r.getFecha())
                                    .append(" | Monto: $").append(r.getMonto()).append("\n");
                            totalRecargas++;
                            montoRecargado += r.getMonto();
                        }
                    }
                }
            }
            sb.append("\nTotal recargas realizadas: ").append(totalRecargas);
            sb.append("\nMonto total recargado: $").append(montoRecargado);
        } else if (clave.contains("reembolso") || clave.contains("cancelacion")) {
            double totalReembolsado = 0.0;
            int cancelaciones = 0;
            for (Compra c : cinema.getListaCompras()) {
                if (c.getEstado() == EstadoCompra.CANCELADA && estaEnRangoFechas(c.getFecha())) {
                    sb.append("Compra ").append(c.getIdCompra()).append(" | Reembolsado: $").append(c.getMontoReembolsado()).append("\n");
                    totalReembolsado += c.getMontoReembolsado();
                    cancelaciones++;
                }
            }
            sb.append("\nTotal cancelaciones: ").append(cancelaciones);
            sb.append("\nTotal reembolsado: $").append(totalReembolsado);
        } else if (clave.contains("punto")) {
            int totalPuntosVigentes = 0;
            for (Cliente cli : cinema.getListaClientes()) {
                int pts = (cli.getCuentaPuntos() != null) ? cli.getCuentaPuntos().getSaldoPuntos() : 0;
                totalPuntosVigentes += pts;
                sb.append("Cliente: ").append(cli.getNombre()).append(" | Puntos vigentes: ").append(pts).append("\n");
            }
            sb.append("\nTotal puntos vigentes en el sistema: ").append(totalPuntosVigentes);
        } else {
            sb.append("Clientes registrados: ").append(cinema.getListaClientes().size()).append("\n");
            sb.append("Funciones activas: ").append(cinema.getListaFunciones().size()).append("\n");
            sb.append("Compras registradas: ").append(cinema.getListaCompras().size()).append("\n");
        }
        this.contenidoProcesado = sb.toString();
    }

    /**
     * Retorna el contenido textual estructurado listo para ser exportado.
     *
     * @return texto completo del reporte.
     */
    @Override
    public String obtenerDatos() {
        return contenidoProcesado;
    }

    /**
     * Exporta los datos del reporte al formato configurado (TXT o PDF) mediante su adaptador correspondiente.
     *
     * @param datos contenido textual a exportar.
     */
    @Override
    public void exportar(String datos) {
        IExportador adaptador = "PDF".equalsIgnoreCase(formato) ? new AdaptadorPdf() : new AdaptadorTxt();
        adaptador.exportar(datos);
    }

    /**
     * Ejecuta el paso final del Template Method invocando la exportación del reporte.
     *
     * @param datos contenido final del reporte.
     */
    @Override
    protected void finalizarGeneracion(String datos) {
        exportar(datos);
    }
}