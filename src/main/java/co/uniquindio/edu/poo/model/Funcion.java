package co.uniquindio.edu.poo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Funcion {

    private String idFuncion;
    private LocalDateTime fechaHora;
    private double precioBase;
    private Pelicula pelicula;
    private Sala sala;
    private Cinema ownedByCinema;
    private List<AsientoFuncionActual> listaAsientosFuncionActual;

    /**
     * Crea una función vacía inicializando su lista de asientos por función y asociándola al cine.
     */
    public Funcion() {
        this.listaAsientosFuncionActual = new ArrayList<>();
        this.ownedByCinema = Cinema.getInstancia();
    }

    /**
     * Crea una función asociada a una película, sala, fecha y precio, generando la disponibilidad de sus asientos.
     *
     * @param idFuncion  identificador único de la función.
     * @param fechaHora  fecha y hora de proyección.
     * @param precioBase tarifa base de entrada para la función.
     * @param pelicula   película proyectada.
     * @param sala       sala asignada.
     */
    public Funcion(String idFuncion, LocalDateTime fechaHora, double precioBase, Pelicula pelicula, Sala sala) {
        this(idFuncion, fechaHora, precioBase, pelicula, sala, Cinema.getInstancia());
    }

    /**
     * Crea una función especificando explícitamente el cine propietario.
     *
     * @param idFuncion     identificador único de la función.
     * @param fechaHora     fecha y hora de proyección.
     * @param precioBase    tarifa base de entrada para la función.
     * @param pelicula      película proyectada.
     * @param sala          sala asignada.
     * @param ownedByCinema cine propietario de la función.
     */
    public Funcion(String idFuncion, LocalDateTime fechaHora, double precioBase, Pelicula pelicula, Sala sala, Cinema ownedByCinema) {
        this.idFuncion = idFuncion;
        this.fechaHora = fechaHora;
        this.precioBase = precioBase;
        this.pelicula = pelicula;
        this.sala = sala;
        this.ownedByCinema = ownedByCinema;
        this.listaAsientosFuncionActual = new ArrayList<>();
        if (this.sala != null) {
            this.sala.agregarFuncion(this);
        }
        inicializarAsientosFuncion();
    }

    public String getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(String idFuncion) {
        this.idFuncion = idFuncion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        if (this.sala != null && this.sala != sala) {
            this.sala.eliminarFuncion(this);
        }
        this.sala = sala;
        if (this.sala != null) {
            this.sala.agregarFuncion(this);
        }
        inicializarAsientosFuncion();
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    public List<AsientoFuncionActual> getListaAsientosFuncionActual() {
        return listaAsientosFuncionActual;
    }

    public void setListaAsientosFuncionActual(List<AsientoFuncionActual> listaAsientosFuncionActual) {
        this.listaAsientosFuncionActual = listaAsientosFuncionActual;
    }

    /**
     * Crea los registros individuales de disponibilidad para cada asiento físico de la sala asignada.
     */
    public void inicializarAsientosFuncion() {
        listaAsientosFuncionActual.clear();
        if (sala != null && sala.getListaAsientos() != null) {
            for (Asiento asiento : sala.getListaAsientos()) {
                String idAsientoFuncion = idFuncion + "-" + asiento.getIdAsiento();
                AsientoFuncionActual asientoFuncion = new AsientoFuncionActual(
                        idAsientoFuncion, asiento, null, null, EstadoAsiento.DISPONIBLE, this);
                listaAsientosFuncionActual.add(asientoFuncion);
                asiento.agregarAsientoFuncion(asientoFuncion);
            }
        }
    }

    /**
     * Valida que los datos obligatorios de la función sean consistentes antes de programarla.
     */
    public void verificarFuncion() {
        if (idFuncion == null || idFuncion.isBlank()) {
            throw new IllegalArgumentException("El identificador de la función es obligatorio.");
        }
        if (pelicula == null) {
            throw new IllegalArgumentException("La función debe tener una película asociada.");
        }
        if (sala == null) {
            throw new IllegalArgumentException("La función debe tener una sala asignada.");
        }
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora de la función son obligatorias.");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio base de la función debe ser mayor a cero.");
        }
    }

    /**
     * Busca y retorna el estado actual de un asiento específico dentro de esta función.
     *
     * @param idAsiento identificador del asiento en la sala.
     * @return objeto AsientoFuncionActual correspondiente.
     */
    public AsientoFuncionActual obtenerAsientoFuncion(String idAsiento) {
        for (AsientoFuncionActual asientoFuncion : listaAsientosFuncionActual) {
            if (asientoFuncion.getAsiento().getIdAsiento().equalsIgnoreCase(idAsiento)) {
                return asientoFuncion;
            }
        }
        throw new IllegalArgumentException("El asiento " + idAsiento + " no pertenece a la sala de esta función.");
    }

    /**
     * Valida la disponibilidad de un asiento y lo bloquea temporalmente para la compra.
     *
     * @param idAsiento identificador del asiento a seleccionar.
     * @return asiento bloqueado listo para agregarse al detalle de compra.
     */
    public AsientoFuncionActual seleccionarAsiento(String idAsiento) {
        AsientoFuncionActual asientoFuncion = obtenerAsientoFuncion(idAsiento);
        if (!asientoFuncion.verificarDisponibilidad()) {
            throw new IllegalStateException("El asiento " + idAsiento + " no está disponible.");
        }
        asientoFuncion.bloquearAsiento();
        return asientoFuncion;
    }

    /**
     * Cancela la función por decisión del cine y reembolsa automáticamente el 100% a todas las compras realizadas.
     */
    public void cancelarFuncionPorCine() {
        Set<Compra> comprasAfectadas = new HashSet<>();
        for (AsientoFuncionActual asientoFuncion : listaAsientosFuncionActual) {
            if (asientoFuncion.getDetalleCompra() != null && asientoFuncion.getDetalleCompra().getOwnedByCompra() != null) {
                comprasAfectadas.add(asientoFuncion.getDetalleCompra().getOwnedByCompra());
            }
        }
        for (Compra compra : comprasAfectadas) {
            if (compra.getEstado() == EstadoCompra.COMPLETADA && !compra.isReembolsada()) {
                compra.cancelarCompra(true);
            }
        }
    }

    /**
     * Calcula el porcentaje de asientos ocupados respecto a la capacidad total de la función.
     *
     * @return porcentaje de ocupación entre 0 y 100.
     */
    public double calcularPorcentajeOcupacion() {
        if (listaAsientosFuncionActual.isEmpty()) {
            return 0.0;
        }
        int ocupados = 0;
        for (AsientoFuncionActual asiento : listaAsientosFuncionActual) {
            if (asiento.getEstadoAsiento() == EstadoAsiento.OCUPADO) {
                ocupados++;
            }
        }
        return (ocupados * 100.0) / listaAsientosFuncionActual.size();
    }

    @Override
    public String toString() {
        return "Funcion{" +
                "idFuncion='" + idFuncion + '\'' +
                ", fechaHora=" + fechaHora +
                ", precioBase=" + precioBase +
                ", pelicula=" + pelicula +
                ", sala=" + sala +
                '}';
    }
}