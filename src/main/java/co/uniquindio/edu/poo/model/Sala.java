package co.uniquindio.edu.poo.model;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private int idSala;
    private int numeroSala;
    private int capacidad;
    private List<Asiento> listaAsientos;
    private List<Funcion> listaFunciones;
    private Cinema ownedByCinema;

    /**
     * Crea una sala vacía inicializando sus listas de asientos y funciones.
     */
    public Sala() {
        this.listaAsientos = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
    }

    /**
     * Crea una sala de cine con su número, capacidad y referencia al cine propietario.
     *
     * @param idSala        identificador único de la sala.
     * @param numeroSala    número visible de la sala.
     * @param capacidad     cantidad máxima de asientos de la sala.
     * @param ownedByCinema cine al que pertenece la sala.
     */
    public Sala(int idSala, int numeroSala, int capacidad, Cinema ownedByCinema) {
        this.idSala = idSala;
        this.numeroSala = numeroSala;
        this.capacidad = capacidad;
        this.ownedByCinema = ownedByCinema;
        this.listaAsientos = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
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

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    /**
     * Genera automáticamente los asientos físicos de la sala distribuidos en filas de diez unidades,
     * marcando los primeros asientos de la fila A como accesibles.
     */
    public void configurarAsientos() {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad de la sala debe ser mayor a cero.");
        }
        listaAsientos.clear();
        int asientosPorFila = 10;
        for (int i = 0; i < capacidad; i++) {
            int numeroFila = i / asientosPorFila;
            int numeroAsiento = (i % asientosPorFila) + 1;
            String fila = obtenerNombreFila(numeroFila);
            String idAsiento = "S" + numeroSala + "-" + fila + numeroAsiento;
            boolean esAccesible = "A".equals(fila) && (numeroAsiento == 1 || numeroAsiento == 2);
            Asiento asiento = new Asiento(idAsiento, fila, numeroAsiento, esAccesible, this);
            listaAsientos.add(asiento);
        }
    }

    /**
     * Asocia una función programada a esta sala evitando duplicados.
     *
     * @param funcion función que se proyectará en la sala.
     */
    public void agregarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función no puede ser nula.");
        }
        if (!listaFunciones.contains(funcion)) {
            listaFunciones.add(funcion);
        }
    }

    /**
     * Desvincula una función de la programación de esta sala.
     *
     * @param funcion función a eliminar de la sala.
     */
    public void eliminarFuncion(Funcion funcion) {
        listaFunciones.remove(funcion);
    }

    /**
     * Convierte un índice numérico de fila en su nomenclatura alfabética correspondiente.
     *
     * @param numeroFila índice numérico base cero de la fila.
     * @return identificador alfabético de la fila.
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
                ", cantidadFunciones=" + listaFunciones.size() +
                '}';
    }
}