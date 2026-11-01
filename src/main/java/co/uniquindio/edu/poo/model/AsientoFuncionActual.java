package co.uniquindio.edu.poo.model;

public class AsientoFuncionActual {
    private String idAsientoFuncion;
    private Asiento asiento;
    private DetalleCompra detalleCompra;
    private BoletoBase boletoBase;
    private EstadoAsiento estadoAsiento;
    private Funcion ownedByFuncion;

    /**
     * metodo constructor de la clase AsientoFuncionActual para inicializar sus valores
     * @param idAsientoFuncion identificador unico
     * @param asiento asiento asociado
     * @param detalleCompra detalle de compra asociado
     * @param boletoBase boleto base asociado
     * @param estadoAsiento estado actual del asiento en la funcion
     * @param ownedByFuncion funcion a la que pertenece
     */
    public AsientoFuncionActual(String idAsientoFuncion, Asiento asiento, DetalleCompra detalleCompra, BoletoBase boletoBase, EstadoAsiento estadoAsiento, Funcion ownedByFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
        this.asiento = asiento;
        this.detalleCompra = detalleCompra;
        this.boletoBase = boletoBase;
        this.estadoAsiento = estadoAsiento;
        this.ownedByFuncion = ownedByFuncion;
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

    /**
     * metodo que sirve para bloquear el asiento en la funcion actual
     */
    public void bloquearAsiento() {
        this.estadoAsiento = EstadoAsiento.BLOQUEADO;
        System.out.println("Bloqueando asiento...");
    }

    /**
     * metodo que sirve para liberar el asiento en la funcion actual
     */
    public void liberarAsiento() {
        this.estadoAsiento = EstadoAsiento.DISPONIBLE;
        System.out.println("Liberando asiento...");
    }

    /**
     * metodo que sirve para verificar si el asiento se encuentra disponible en la funcion actual
     * @return verdadero si esta disponible
     */
    public boolean verificarDisponibilidad() {
        return this.estadoAsiento == EstadoAsiento.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "AsientoFuncionActual [idAsientoFuncion=" + idAsientoFuncion + ", estadoAsiento=" + estadoAsiento + "]";
    }
}