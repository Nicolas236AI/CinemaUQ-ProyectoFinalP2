package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra los observadores registrados y les envía notificaciones.
 * Implementa el comportamiento central del patrón Observer.
 */
public class NotificadorCentro {

    private List<IClienteObserver> observadores;

    public NotificadorCentro() {
        this.observadores = new ArrayList<>();
    }
/**
 * Registra un nuevo observador para que pueda recibir notificaciones.
 * Evita agregar referencias nulas o clientes que ya estén registrados.
 *
 * @param observador cliente que recibirá las notificaciones.
 */

    public void agregarObservador(IClienteObserver observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     * Elimina un observador de la lista de clientes registrados.
     *
     * @param observador observador que se desea eliminar.
     */

    public void eliminarObservador(IClienteObserver observador) {
        observadores.remove(observador);
    }

    /**
     * Envía un mensaje a todos los observadores registrados,
     * notificándoles sobre un evento ocurrido en el sistema.
     *
     * @param mensaje contenido de la notificación.
     */

    public void notificarClientes(String mensaje) {
        for (IClienteObserver observador : observadores) {
            observador.actualizar(mensaje);
        }
    }

    public List<IClienteObserver> getObservadores() {
        return observadores;
    }

    public void setObservadores(List<IClienteObserver> observadores) {
        this.observadores = observadores;
    }
}