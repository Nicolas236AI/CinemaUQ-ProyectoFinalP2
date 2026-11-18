package co.uniquindio.edu.poo.model;

/**
 * Define el contrato común para el acceso a la gestión del sistema del cine.
 * Es implementado tanto por el servicio real como por su proxy de seguridad.
 */
public interface ISeguridadCine {

    /**
     * Ejecuta las operaciones de gestión del sistema del cine.
     */
    void gestionarSistema();
}
