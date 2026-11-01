package co.uniquindio.edu.poo.model;

public class DetalleCompra {
    private int idDetalle;
    private int cantidad;
    private double subtotal;
    private AsientoFuncionActual asientoFuncionActual;
    private IElementoConfiteria elementoConfiteria;
    private IBoleto boleto;
    private Compra ownedByCompra;

    /**
     * metodo constructor de la clase DetalleCompra para inicializar sus valores
     * @param idDetalle identificador del detalle
     * @param cantidad cantidad de elementos
     * @param subtotal subtotal del detalle
     * @param asientoFuncionActual asiento y funcion asociados
     * @param elementoConfiteria elemento de confiteria asociado
     * @param boleto boleto asociado
     * @param ownedByCompra compra a la que pertenece este detalle
     */
    public DetalleCompra(int idDetalle, int cantidad, double subtotal, AsientoFuncionActual asientoFuncionActual, IElementoConfiteria elementoConfiteria, IBoleto boleto, Compra ownedByCompra) {
        this.idDetalle = idDetalle;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.asientoFuncionActual = asientoFuncionActual;
        this.elementoConfiteria = elementoConfiteria;
        this.boleto = boleto;
        this.ownedByCompra = ownedByCompra;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public AsientoFuncionActual getAsientoFuncionActual() {
        return asientoFuncionActual;
    }

    public void setAsientoFuncionActual(AsientoFuncionActual asientoFuncionActual) {
        this.asientoFuncionActual = asientoFuncionActual;
    }

    public IElementoConfiteria getElementoConfiteria() {
        return elementoConfiteria;
    }

    public void setElementoConfiteria(IElementoConfiteria elementoConfiteria) {
        this.elementoConfiteria = elementoConfiteria;
    }

    public IBoleto getBoleto() {
        return boleto;
    }

    public void setBoleto(IBoleto boleto) {
        this.boleto = boleto;
    }

    public Compra getOwnedByCompra() {
        return ownedByCompra;
    }

    public void setOwnedByCompra(Compra ownedByCompra) {
        this.ownedByCompra = ownedByCompra;
    }

    /**
     * metodo que sirve para calcular el subtotal del detalle de compra
     * @return subtotal calculado
     */
    public double calcularSubtotal() {
        double precioUnitario = 0.0;
        if (boleto != null) {
            precioUnitario = boleto.calcularCosto();
        } else if (elementoConfiteria != null) {
            precioUnitario = elementoConfiteria.obtenerPrecio();
        }
        this.subtotal = precioUnitario * cantidad;
        return this.subtotal;
    }

    @Override
    public String toString() {
        return "DetalleCompra [idDetalle=" + idDetalle + ", cantidad=" + cantidad + ", subtotal=" + subtotal + "]";
    }
}