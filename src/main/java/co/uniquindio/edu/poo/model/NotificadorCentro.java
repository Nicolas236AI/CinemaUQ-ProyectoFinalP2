package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class NotificadorCentro {

    private List<IClienteObserver> observadores;

    public NotificadorCentro() {
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(IClienteObserver observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(IClienteObserver observador) {
        observadores.remove(observador);
    }

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