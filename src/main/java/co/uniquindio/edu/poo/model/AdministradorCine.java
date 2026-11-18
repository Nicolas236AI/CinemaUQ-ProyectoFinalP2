package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class AdministradorCine {

    private String idAdmin;
    private String nombre;
    private String correo;
    private Cinema ownedByCinema;
    private List<ReporteOperativo> listaReportesOperativos;

    /**
     * Construye el administrador principal del cine con acceso a la gestión operativa.
     *
     * @param idAdmin       identificador único del administrador.
     * @param nombre        nombre del administrador.
     * @param correo        correo electrónico de acceso.
     * @param ownedByCinema cine administrado.
     */
    public AdministradorCine(String idAdmin, String nombre, String correo, Cinema ownedByCinema) {
        this.idAdmin = idAdmin;
        this.nombre = nombre;
        this.correo = correo;
        this.ownedByCinema = ownedByCinema;
        this.listaReportesOperativos = new ArrayList<>();
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    public List<ReporteOperativo> getListaReportesOperativos() {
        return listaReportesOperativos;
    }

    public void setListaReportesOperativos(List<ReporteOperativo> listaReportesOperativos) {
        this.listaReportesOperativos = listaReportesOperativos;
    }

    /**
     * Valida permisos mediante el proxy de seguridad y registra una nueva sala configurando sus asientos.
     *
     * @param sala sala a registrar en el cine.
     */
    public void gestionarSalas(Sala sala) {
        new ProxyCine(this).gestionarSistema();
        if (sala == null || sala.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La sala debe ser válida y tener capacidad mayor a cero.");
        }
        sala.configurarAsientos();
        if (ownedByCinema != null && !ownedByCinema.getListaSalas().contains(sala)) {
            ownedByCinema.getListaSalas().add(sala);
        }
    }

    /**
     * Aprueba y ejecuta una solicitud de recarga sobre la tarjeta virtual de un cliente.
     *
     * @param solicitud recarga pendiente de aprobación.
     */
    public void aprobarRecargaTarjeta(Recarga solicitud) {
        new ProxyCine(this).gestionarSistema();
        if (solicitud == null || solicitud.getTarjeta() == null) {
            throw new IllegalArgumentException("La solicitud de recarga o su tarjeta asociada no son válidas.");
        }
        solicitud.ejecutarRecarga();
        solicitud.getTarjeta().getListaRecargas().add(solicitud);
        if (ownedByCinema != null) {
            ownedByCinema.getSolicitudesRecarga().remove(solicitud);
        }
    }

    /**
     * Cancela una función programada y ejecuta el reembolso del 100% a todos los clientes afectados.
     *
     * @param funcion función a cancelar.
     */
    public void cancelarFuncion(Funcion funcion) {
        new ProxyCine(this).gestionarSistema();
        if (funcion == null) {
            throw new IllegalArgumentException("La función a cancelar no puede ser nula.");
        }
        funcion.cancelarFuncionPorCine();
        if (ownedByCinema != null && ownedByCinema.getNotificadorCentro() != null) {
            ownedByCinema.getNotificadorCentro().notificarClientes(
                    "La función " + funcion.getIdFuncion() + " fue cancelada. Se reembolsó el 100% de sus entradas.");
        }
    }

    /**
     * Solicita la creación de un reporte exportable mediante la fábrica de reportes y lo genera.
     *
     * @param factory fábrica de reportes a utilizar.
     * @param nombre  nombre del reporte operativo.
     * @param formato formato de exportación (TXT o PDF).
     * @return reporte exportable generado.
     */
    public ReporteExportable solicitarReporteExportable(IReporteFactory factory, String nombre, String formato) {
        new ProxyCine(this).gestionarSistema();
        if (factory == null) {
            throw new IllegalArgumentException("La fábrica de reportes no puede ser nula.");
        }
        ReporteExportable reporte = factory.crearReporteExportable(nombre, formato);
        reporte.setAdministradorCine(this);
        reporte.generarReporte();
        listaReportesOperativos.add(reporte);
        return reporte;
    }

    /**
     * Solicita la creación de un reporte visual mediante la fábrica de reportes y lo genera.
     *
     * @param factory fábrica de reportes a utilizar.
     * @param tipo    tipo de gráfico (BARRAS, PASTEL o LINEAS).
     * @param titulo  título del gráfico.
     * @return reporte visual generado.
     */
    public ReporteVisual solicitarReporteVisual(IReporteFactory factory, String tipo, String titulo) {
        new ProxyCine(this).gestionarSistema();
        if (factory == null) {
            throw new IllegalArgumentException("La fábrica de reportes no puede ser nula.");
        }
        ReporteVisual reporte = factory.crearReporteVisual(tipo, titulo);
        reporte.setAdministradorCine(this);
        reporte.generarReporte();
        listaReportesOperativos.add(reporte);
        return reporte;
    }

    /**
     * Genera un reporte operativo utilizando el patrón Template Method y lo almacena en el historial.
     *
     * @param reporte instancia de reporte exportable o visual.
     */
    public void generarReporteOperativo(ReporteOperativo reporte) {
        new ProxyCine(this).gestionarSistema();
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte operativo no puede ser nulo.");
        }
        reporte.setAdministradorCine(this);
        reporte.generarReporte();
        listaReportesOperativos.add(reporte);
    }

    @Override
    public String toString() {
        return "AdministradorCine{" +
                "idAdmin='" + idAdmin + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}