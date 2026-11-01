package co.uniquindio.edu.poo.model;

public class AsientoFuncionActual {
    private String idAsientoFuncion;
    private Asiento asiento;
    private DetalleCompra detalleCompra;
    private BoletoBase boletoBase;
    private Funcion ownedByFuncion;

    /**
     * metodo constructor de la clase AsientoFuncionActual para inicializar sus valores
     * @param idAsientoFuncion identificador unico
     * @param asiento asiento asociado
     * @param detalleCompra detalle de compra asociado
     * @param boletoBase boleto base asociado
     * @param ownedByFuncion funcion a la que pertenece
     */
    public AsientoFuncionActual(String idAsientoFuncion, Asiento asiento, DetalleCompra detalleCompra, BoletoBase boletoBase, Funcion ownedByFuncion) {
        this.idAsientoFuncion = idAsientoFuncion;
        this.asiento = asiento;
        this.detalleCompra = detalleCompra;
        this.boletoBase = boletoBase;
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
        System.out.println("Bloqueando asiento...");
    }

    /**
     * metodo que sirve para liberar el asiento en la funcion actual
     */
    public void liberarAsiento() {
        System.out.println("Liberando asiento...");
    }

    @Override
    public String toString() {
        return "AsientoFuncionActual [idAsientoFuncion=" + idAsientoFuncion + "]";
    }
}