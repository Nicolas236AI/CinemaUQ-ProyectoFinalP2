package co.uniquindio.edu.poo.model;

public class Asiento {
    private String idAsiento;
    private String fila;
    private int numero;
    private Sala ownedBySala;

    /**
     * metodo constructor de la clase Asiento para inicializar sus valores
     * @param idAsiento identificador del asiento
     * @param fila fila del asiento
     * @param numero numero del asiento
     * @param estado estado actual del asiento
     * @param ownedBySala sala a la que pertenece
     */
    public Asiento(String idAsiento, String fila, int numero, Sala ownedBySala) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
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

    public Sala getOwnedBySala() {
        return ownedBySala;
    }

    public void setOwnedBySala(Sala ownedBySala) {
        this.ownedBySala = ownedBySala;
    }

    @Override
    public String toString() {
        return "Asiento [idAsiento=" + idAsiento + ", fila=" + fila + ", numero=" + numero  + "]";
    }
}