package co.uniquindio.edu.poo.model;

/**
 * Define el contrato para los objetos que desean recibir notificaciones.
 * Forma parte del patrón de comportamiento Observer.
 */
public interface IClienteObserver {

    void actualizar();

    /**
     * Permite recibir y procesar las notificaciones enviadas
     * por el centro de notificaciones.
     *
     * @param mensaje mensaje recibido.
     */
    void actualizar(String mensaje);

}