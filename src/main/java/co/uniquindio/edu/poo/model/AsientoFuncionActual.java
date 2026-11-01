package co.uniquindio.edu.poo.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AsientoFuncionActual {
    private String idAsientoFuncion;
    private Asiento asiento;
    private DetalleCompra detalleCompra;
    private BoletoBase boletoBase;
    private EstadoAsiento estadoAsiento;
    private Funcion ownedByFuncion;
    private LocalDateTime horaBloqueo;

    /**
     * Metodo constructor de la clase AsientoFuncionActual para inicializar sus valores
     * @param idAsientoFuncion identificador único
     * @param asiento asiento asociado
     * @param detalleCompra detalle de compra asociado
     * @param boletoBase boleto base asociado
     * @param estadoAsiento estado actual del asiento en la función
     * @param ownedByFuncion función a la que pertenece
     */
    public AsientoFuncionActual(String idAsientoFuncion, Asiento asiento, DetalleCompra detalleCompra, BoletoBase boletoBase, EstadoAsiento estadoAsiento, Funcion ownedByFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
        this.asiento = asiento;
        this.detalleCompra = detalleCompra;
        this.boletoBase = boletoBase;
        this.estadoAsiento = estadoAsiento;
        this.ownedByFuncion = ownedByFuncion;
        this.horaBloqueo = (estadoAsiento == EstadoAsiento.SELECCIONADO) ? LocalDateTime.now() : null;
    }

    public String getIdAsientoFuncion() {
        return idAsientoFuncion;
    }

    public void setIdAsientoFuncion(String idAsientoFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public DetalleCompra getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(DetalleCompra detalleCompra) {
        this.detalleCompra = detalleCompra;
    }

    public BoletoBase getBoletoBase() {
        return boletoBase;
    }

    public void setBoletoBase(BoletoBase boletoBase) {
        this.boletoBase = boletoBase;
    }

    public EstadoAsiento getEstadoAsiento() {
        liberarSiExpiro();
        return estadoAsiento;
    }

    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }

    public Funcion getOwnedByFuncion() {
        return ownedByFuncion;
    }

    public void setOwnedByFuncion(Funcion ownedByFuncion) {
        this.ownedByFuncion = ownedByFuncion;
    }

    public LocalDateTime getHoraBloqueo() {
        return horaBloqueo;
    }

    public void setHoraBloqueo(LocalDateTime horaBloqueo) {
        this.horaBloqueo = horaBloqueo;
    }

    /**
     * Metodo que sirve para bloquear el asiento 10 munutos una vez seleccionado
     */
    public void bloquearAsiento() {
        this.estadoAsiento = EstadoAsiento.SELECCIONADO;
        this.horaBloqueo = LocalDateTime.now();
        System.out.println("Asiento " + idAsientoFuncion + " bloqueado temporalmente por 10 minutos.");
    }

    /**
     * Metodo que sirve para liberar el asiento en la funcion actual y reestablecer su estado
     */
    public void liberarAsiento() {
        this.estadoAsiento = EstadoAsiento.DISPONIBLE;
        this.horaBloqueo = null;
        System.out.println("Asiento " + idAsientoFuncion + " liberado y disponible.");
    }

    /**
     *Metodo que sirve para verificar si el asiento esta disponible en la funcionActual
     * @return verdadero si está disponible
     */
    public boolean verificarDisponibilidad() {
        liberarSiExpiro();
        return this.estadoAsiento == EstadoAsiento.DISPONIBLE;
    }

    /**
     * Metodo que verifica si ya transcurrieron 10 minutos desde que se apartó el asiento temporalmente.
     * @return verdadero si el tiempo de reserva expiró.
     */
    public boolean verificarExpiracion() {
        if (this.estadoAsiento == EstadoAsiento.SELECCIONADO && this.horaBloqueo != null) {
            long minutosTranscurridos = ChronoUnit.MINUTES.between(this.horaBloqueo, LocalDateTime.now());
            return minutosTranscurridos >= 10;
        }
        return false;
    }

    /**
     * Metodo que sirve para liberar automáticamente el asiento si el tiempo de reserva de 10 minutos ha caducado.
     */
    public void liberarSiExpiro() {
        if (verificarExpiracion()) {
            liberarAsiento();
        }
    }

    @Override
    public String toString() {
        return "AsientoFuncionActual [idAsientoFuncion=" + idAsientoFuncion + ", estadoAsiento=" + estadoAsiento + "]";
    }
}