package co.uniquindio.edu.poo.model;

import java.util.List;
import java.util.ArrayList;

public class Tarjeta {
    private String idTarjeta;
    private double saldo;
    private boolean estaActiva;
    private Cliente cliente;
    private List<Recarga> listaRecargas;
    private HistorialMovimientos historialMovimientos;

    /**
     * metodo constructor de la clase Tarjeta para inicializar sus valores
     * @param idTarjeta identificador de la tarjeta
     * @param saldo saldo inicial
     * @param estaActiva estado de activacion
     * @param cliente cliente propietario
     * @param historialMovimientos historial de movimientos asociado
     */
    public Tarjeta(String idTarjeta, double saldo, boolean estaActiva, Cliente cliente, HistorialMovimientos historialMovimientos) {
        this.idTarjeta = idTarjeta;
        this.saldo = saldo;
        this.estaActiva = estaActiva;
        this.cliente = cliente;
        this.listaRecargas = new ArrayList<>();
        this.historialMovimientos = historialMovimientos;
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
     * metodo que sirve para debitar saldo de la tarjeta
     * @param monto cantidad a debitar
     * @return verdadero si la transaccion fue exitosa
     */
    public boolean debitarSaldo(double monto) {
        if (estaActiva && saldo >= monto) {
            saldo -= monto;
            return true;
        }
        return false;
    }

    /**
     * metodo que sirve para acreditar saldo a la tarjeta
     * @param monto cantidad a acreditar
     */
    public void acreditarSaldo(double monto) {
        if (estaActiva && monto > 0) {
            saldo += monto;
        }
    }

    @Override
    public String toString() {
        return "Tarjeta [idTarjeta=" + idTarjeta + ", saldo=" + saldo + ", estaActiva=" + estaActiva + "]";
    }
}