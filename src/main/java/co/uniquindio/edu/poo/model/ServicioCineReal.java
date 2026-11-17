package co.uniquindio.edu.poo.model;

/**
 * Representa el servicio real que ejecuta la gestión del sistema del cine.
 * No valida quién lo invoca: esa responsabilidad corresponde a ProxyCine.
 * Actúa como el sujeto real del patrón estructural Proxy.
 */
public class ServicioCineReal implements ISeguridadCine {

    private boolean permisosActivos;

    public ServicioCineReal() {
        this.permisosActivos = true;
    }

    public boolean isPermisosActivos() {
        return permisosActivos;
    }

    public void setPermisosActivos(boolean permisosActivos) {
        this.permisosActivos = permisosActivos;
    }

    /**
     * Ejecuta la gestión del sistema (películas, salas, funciones,
     * productos, promociones, recargas, reportes, etc.).
     */
    @Override
    public void gestionarSistema() {
        if (!permisosActivos) {
            throw new SecurityException("Los permisos del servicio están desactivados.");
        }
        System.out.println("Gestionando el sistema del cine...");
    }
}