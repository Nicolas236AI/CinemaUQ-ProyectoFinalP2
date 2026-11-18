package co.uniquindio.edu.poo.model;

/**
 * Proxy de protección que controla el acceso a la gestión del sistema.
 * Solo permite delegar en ServicioCineReal cuando quien lo invoca es el
 * administrador registrado del cine.
 * Implementa el patrón estructural Proxy.
 */
public class ProxyCine implements ISeguridadCine {

    private ServicioCineReal servicioReal;
    private AdministradorCine administrador;

    public ProxyCine(AdministradorCine administrador) {
        this.administrador = administrador;
        this.servicioReal = new ServicioCineReal();
    }

    public ServicioCineReal getServicioReal() {
        return servicioReal;
    }

    public void setServicioReal(ServicioCineReal servicioReal) {
        this.servicioReal = servicioReal;
    }

    public AdministradorCine getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorCine administrador) {
        this.administrador = administrador;
    }

    /**
     * Verifica que el usuario sea el administrador registrado en el cine.
     *
     * @return true si tiene permisos para gestionar el sistema.
     */
    public boolean validarPermisos() {
        if (administrador == null) {
            return false;
        }
        AdministradorCine adminRegistrado = Cinema.getInstancia().getAdministradorCine();
        return adminRegistrado != null
                && adminRegistrado.getIdAdmin() != null
                && adminRegistrado.getIdAdmin().equals(administrador.getIdAdmin());
    }

    /**
     * Delega la gestión del sistema al servicio real solo si el usuario
     * tiene permisos; de lo contrario, bloquea el acceso.
     *
     * @throws SecurityException si el usuario no es el administrador del cine.
     */
    @Override
    public void gestionarSistema() {
        if (!validarPermisos()) {
            throw new SecurityException("Acceso denegado: solo el administrador puede gestionar el sistema.");
        }
        servicioReal.gestionarSistema();
    }
}