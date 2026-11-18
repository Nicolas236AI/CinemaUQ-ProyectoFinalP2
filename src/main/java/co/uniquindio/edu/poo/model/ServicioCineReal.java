package co.uniquindio.edu.poo.model;

public class ServicioCineReal implements ISeguridadCine {

    private boolean permisosActivos;
    private ProxyCine proxyCine;

    /**
     * Inicializa el servicio real de gestión del cine con permisos operativos activos.
     */
    public ServicioCineReal() {
        this.permisosActivos = true;
    }

    /**
     * Inicializa el servicio real vinculándolo a su proxy de seguridad correspondiente.
     *
     * @param proxyCine proxy encargado de controlar el acceso a este servicio.
     */
    public ServicioCineReal(ProxyCine proxyCine) {
        this.permisosActivos = true;
        this.proxyCine = proxyCine;
    }

    public boolean isPermisosActivos() {
        return permisosActivos;
    }

    public void setPermisosActivos(boolean permisosActivos) {
        this.permisosActivos = permisosActivos;
    }

    public ProxyCine getProxyCine() {
        return proxyCine;
    }

    public void setProxyCine(ProxyCine proxyCine) {
        this.proxyCine = proxyCine;
    }

    /**
     * Valida que los permisos operativos estén habilitados para ejecutar acciones administrativas.
     */
    @Override
    public void gestionarSistema() {
        if (!permisosActivos) {
            throw new SecurityException("Los permisos del servicio están desactivados.");
        }
    }
}