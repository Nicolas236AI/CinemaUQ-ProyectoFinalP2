package co.uniquindio.edu.poo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tarjeta {

    private String idTarjeta;
    private double saldo;
    private boolean estaActiva;
    private Cliente cliente;
    private List<Recarga> listaRecargas;
    private HistorialMovimientos historialMovimientos;

    /**
     * Inicializa una tarjeta virtual asociada a un cliente y vincula su historial de movimientos.
     *
     * @param idTarjeta            identificador único de la tarjeta.
     * @param saldo                saldo inicial disponible.
     * @param estaActiva           estado de activación de la tarjeta.
     * @param cliente              cliente propietario.
     * @param historialMovimientos historial de movimientos asociado.
     */
    public Tarjeta(String idTarjeta, double saldo, boolean estaActiva, Cliente cliente, HistorialMovimientos historialMovimientos) {
        this.idTarjeta = idTarjeta;
        this.saldo = saldo;
        this.estaActiva = estaActiva;
        this.cliente = cliente;
        this.listaRecargas = new ArrayList<>();
        this.historialMovimientos = (historialMovimientos != null)
                ? historialMovimientos
                : new HistorialMovimientos("HIST-" + idTarjeta, this);
    }

    public String getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(boolean estaActiva) {
        this.estaActiva = estaActiva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Recarga> getListaRecargas() {
        return listaRecargas;
    }

    public void setListaRecargas(List<Recarga> listaRecargas) {
        this.listaRecargas = listaRecargas;
    }

    public HistorialMovimientos getHistorialMovimientos() {
        return historialMovimientos;
    }

    public void setHistorialMovimientos(HistorialMovimientos historialMovimientos) {
        this.historialMovimientos = historialMovimientos;
    }

    /**
     * Verifica que la tarjeta esté activa y cuente con saldo suficiente para cubrir un monto.
     *
     * @param monto valor que se pretende debitar.
     */
    public void validarDisponibilidadPago(double monto) {
        if (!estaActiva) {
            throw new IllegalStateException("La tarjeta virtual " + idTarjeta + " está inactiva.");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto a debitar no puede ser negativo.");
        }
        if (saldo < monto) {
            throw new IllegalStateException("Saldo insuficiente en la tarjeta. Disponible: $" + saldo + ", Requerido: $" + monto);
        }
    }

    /**
     * Descuenta un monto del saldo si la tarjeta está activa y posee fondos suficientes, registrando el movimiento.
     *
     * @param monto cantidad a debitar.
     * @return true si el débito se efectuó correctamente.
     */
    public boolean debitarSaldo(double monto) {
        if (estaActiva && monto >= 0 && saldo >= monto) {
            saldo -= monto;
            historialMovimientos.registrarMovimiento("COMPRA | -$" + monto + " | " + LocalDateTime.now());
            return true;
        }
        return false;
    }

    /**
     * Incrementa el saldo de la tarjeta cuando se aprueba una recarga y registra el movimiento.
     *
     * @param monto valor a acreditar.
     */
    public void acreditarSaldo(double monto) {
        if (!estaActiva) {
            throw new IllegalStateException("No se puede recargar una tarjeta inactiva.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de recarga debe ser mayor a cero.");
        }
        saldo += monto;
        historialMovimientos.registrarMovimiento("RECARGA | +$" + monto + " | " + LocalDateTime.now());
    }

    /**
     * Devuelve dinero a la tarjeta producto de la cancelación de una compra y registra el reembolso.
     *
     * @param monto    valor a reembolsar.
     * @param idCompra identificador de la compra reembolsada.
     */
    public void acreditarReembolso(double monto, String idCompra) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de reembolso debe ser mayor a cero.");
        }
        saldo += monto;
        historialMovimientos.registrarMovimiento("REEMBOLSO (" + idCompra + ") | +$" + monto + " | " + LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Tarjeta [idTarjeta=" + idTarjeta + ", saldo=" + saldo + ", estaActiva=" + estaActiva + "]";
    }
}