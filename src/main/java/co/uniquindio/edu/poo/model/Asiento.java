package co.uniquindio.edu.poo.model;

public class Asiento {
    private String idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estado;
    private Sala ownedBySala;

    /**
     * metodo constructor de la clase Asiento para inicializar sus valores
     * @param idAsiento identificador del asiento
     * @param fila fila del asiento
     * @param numero numero del asiento
     * @param estado estado actual del asiento
     * @param ownedBySala sala a la que pertenece
     */
    public Asiento(String idAsiento, String fila, int numero, EstadoAsiento estado, Sala ownedBySala) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
        this.ownedBySala = ownedBySala;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public Sala getOwnedBySala() {
        return ownedBySala;
    }

    public void setOwnedBySala(Sala ownedBySala) {
        this.ownedBySala = ownedBySala;
    }

    /**
     * metodo que sirve para verificar la disponibilidad del asiento
     * @return verdadero si esta disponible
     */
    public boolean verificarDisponibilidad() {
        return this.estado == EstadoAsiento.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Asiento [idAsiento=" + idAsiento + ", fila=" + fila + ", numero=" + numero + ", estado=" + estado + "]";
    }
}