
package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Representa una sala de cine.
 * Administra su capacidad y los asientos que contiene.
 */
public class Sala {

    private int idSala;
    private int numeroSala;
    private int capacidad;
    private List<Asiento> listaAsientos;
    private Cinema ownedByCinema;

    public Sala() {
        this.listaAsientos = new ArrayList<>();
    }

    public Sala(int idSala, int numeroSala, int capacidad, Cinema ownedByCinema) {
        this.idSala = idSala;
        this.numeroSala = numeroSala;
        this.capacidad = capacidad;
        this.ownedByCinema = ownedByCinema;
        this.listaAsientos = new ArrayList<>();
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<Asiento> getListaAsientos() {
        return listaAsientos;
    }

    public void setListaAsientos(List<Asiento> listaAsientos) {
        this.listaAsientos = listaAsientos;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }
    /**
     * Genera automáticamente los asientos de acuerdo con la capacidad
     * de la sala, distribuyéndolos en filas de diez asientos.
     */
    public void configurarAsientos() {
        if (capacidad <= 0) {
            return;
        }

        listaAsientos.clear();

        int asientosPorFila = 10;

        for (int i = 0; i < capacidad; i++) {
            int numeroFila = i / asientosPorFila;
            int numeroAsiento = (i % asientosPorFila) + 1;

            String fila = obtenerNombreFila(numeroFila);
            String idAsiento = "S" + numeroSala + "-" + fila + numeroAsiento;

            Asiento asiento = new Asiento(
                    idAsiento,
                    fila,
                    numeroAsiento,
                    this
            );

            listaAsientos.add(asiento);
        }
    }
    /**
     * Convierte el índice numérico de una fila en su correspondiente
     * identificación alfabética.
     *
     * @param numeroFila índice numérico de la fila.
     * @return letra o combinación de letras correspondiente.
     */
    private String obtenerNombreFila(int numeroFila) {
        StringBuilder fila = new StringBuilder();

        while (numeroFila >= 0) {
            fila.insert(0, (char) ('A' + numeroFila % 26));
            numeroFila = numeroFila / 26 - 1;
        }

        return fila.toString();
    }

    @Override
    public String toString() {
        return "Sala{" +
                "idSala=" + idSala +
                ", numeroSala=" + numeroSala +
                ", capacidad=" + capacidad +
                ", cantidadAsientos=" + listaAsientos.size() +
                '}';
    }
}
