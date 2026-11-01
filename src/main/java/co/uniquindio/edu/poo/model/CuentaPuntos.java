package co.uniquindio.edu.poo.model;

public class CuentaPuntos {
    private int saldoPuntos;
    private Cliente ownedByCliente;

    /**
     * metodo constructor de la clase CuentaPuntos para inicializar sus valores
     * @param saldoPuntos saldo inicial de puntos
     * @param ownedByCliente cliente propietario de la cuenta
     */
    public CuentaPuntos(int saldoPuntos, Cliente ownedByCliente) {
        this.saldoPuntos = saldoPuntos;
        this.ownedByCliente = ownedByCliente;
    }

    public int getSaldoPuntos() {
        return saldoPuntos;
    }

    public void setSaldoPuntos(int saldoPuntos) {
        this.saldoPuntos = saldoPuntos;
    }

    public Cliente getOwnedByCliente() {
        return ownedByCliente;
    }

    public void setOwnedByCliente(Cliente ownedByCliente) {
        this.ownedByCliente = ownedByCliente;
    }

    /**
     * metodo que sirve para acumular puntos en la cuenta del cliente
     * @param puntos cantidad de puntos a acumular
     */
    public void acumularPuntos(int puntos) {
        if (puntos > 0) {
            this.saldoPuntos += puntos;
        }
    }

    /**
     * metodo que sirve para redimir puntos de la cuenta del cliente
     * @param puntos cantidad de puntos a redimir
     * @return verdadero si se pudieron redimir los puntos
     */
    public boolean redimirPuntos(int puntos) {
        if (puntos > 0 && this.saldoPuntos >= puntos) {
            this.saldoPuntos -= puntos;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "CuentaPuntos [saldoPuntos=" + saldoPuntos + "]";
    }
}