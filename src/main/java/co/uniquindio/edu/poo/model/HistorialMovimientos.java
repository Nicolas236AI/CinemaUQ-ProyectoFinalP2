package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class HistorialMovimientos {

    private String idHistorial;
    private Tarjeta tarjeta;
    private List<String> listaMovimientos;

    /**
     * Crea el historial de movimientos vinculado a una tarjeta virtual.
     *
     * @param idHistorial identificador único del historial.
     * @param tarjeta     tarjeta virtual asociada.
     */
    public HistorialMovimientos(String idHistorial, Tarjeta tarjeta) {
        this.idHistorial = idHistorial;
        this.tarjeta = tarjeta;
        this.listaMovimientos = new ArrayList<>();
    }

    public String getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(String idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public List<String> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<String> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    /**
     * Registra un movimiento genérico en el historial de la tarjeta.
     */
    public void registrarMovimiento() {
        registrarMovimiento("Movimiento registrado en tarjeta " + (tarjeta != null ? tarjeta.getIdTarjeta() : ""));
    }

    /**
     * Agrega el detalle de una transacción (compra, recarga o reembolso) al historial.
     *
     * @param detalleMovimiento descripción completa del movimiento realizado.
     */
    public void registrarMovimiento(String detalleMovimiento) {
        if (detalleMovimiento != null && !detalleMovimiento.isBlank()) {
            listaMovimientos.add(detalleMovimiento);
        }
    }

    @Override
    public String toString() {
        return "HistorialMovimientos [idHistorial=" + idHistorial + ", movimientos=" + listaMovimientos.size() + "]";
    }
}