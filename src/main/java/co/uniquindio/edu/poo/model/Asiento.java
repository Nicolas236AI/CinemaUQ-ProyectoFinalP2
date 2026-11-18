package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class Asiento {

    private String idAsiento;
    private String fila;
    private int numero;
    private boolean accesible;
    private Sala ownedBySala;
    private List<AsientoFuncionActual> listaAsientoFuncionActual;

    /**
     * Inicializa un asiento estándar perteneciente a una sala.
     *
     * @param idAsiento   identificador único del asiento.
     * @param fila        letra de la fila del asiento.
     * @param numero      número del asiento dentro de la fila.
     * @param ownedBySala sala a la que pertenece.
     */
    public Asiento(String idAsiento, String fila, int numero, Sala ownedBySala) {
        this(idAsiento, fila, numero, false, ownedBySala);
    }

    /**
     * Inicializa un asiento especificando si cuenta con adecuación accesible.
     *
     * @param idAsiento   identificador único del asiento.
     * @param fila        letra de la fila del asiento.
     * @param numero      número del asiento dentro de la fila.
     * @param accesible   indica si el asiento es accesible.
     * @param ownedBySala sala a la que pertenece.
     */
    public Asiento(String idAsiento, String fila, int numero, boolean accesible, Sala ownedBySala) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.accesible = accesible;
        this.ownedBySala = ownedBySala;
        this.listaAsientoFuncionActual = new ArrayList<>();
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

    public boolean isAccesible() {
        return accesible;
    }

    public void setAccesible(boolean accesible) {
        this.accesible = accesible;
    }

    public Sala getOwnedBySala() {
        return ownedBySala;
    }

    public void setOwnedBySala(Sala ownedBySala) {
        this.ownedBySala = ownedBySala;
    }

    public List<AsientoFuncionActual> getListaAsientoFuncionActual() {
        return listaAsientoFuncionActual;
    }

    public void setListaAsientoFuncionActual(List<AsientoFuncionActual> listaAsientoFuncionActual) {
        this.listaAsientoFuncionActual = listaAsientoFuncionActual;
    }

    /**
     * Vincula un registro de disponibilidad por función a este asiento físico.
     *
     * @param asientoFuncion registro del asiento en una función específica.
     */
    public void agregarAsientoFuncion(AsientoFuncionActual asientoFuncion) {
        if (asientoFuncion != null && !listaAsientoFuncionActual.contains(asientoFuncion)) {
            listaAsientoFuncionActual.add(asientoFuncion);
        }
    }

    @Override
    public String toString() {
        return "Asiento [idAsiento=" + idAsiento + ", fila=" + fila + ", numero=" + numero + ", accesible=" + accesible + "]";
    }
}