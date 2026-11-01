package co.uniquindio.edu.poo.model;

public class DescuentoFidelidad implements IEstrategiaDescuento {
    private int puntosRequeridos;
    private double porcentajeDescuento;

    /**
     * metodo constructor de la clase DescuentoFidelidad para inicializar sus valores
     * @param puntosRequeridos puntos necesarios para el descuento
     * @param porcentajeDescuento porcentaje de descuento a aplicar
     */
    public DescuentoFidelidad(int puntosRequeridos, double porcentajeDescuento) {
        this.puntosRequeridos = puntosRequeridos;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public int getPuntosRequeridos() {
        return puntosRequeridos;
    }

    public void setPuntosRequeridos(int puntosRequeridos) {
        this.puntosRequeridos = puntosRequeridos;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * metodo que sirve para calcular el descuento basado en la fidelidad del cliente
     * @param monto monto base de la compra
     * @return monto del descuento calculado
     */
    @Override
    public double calcularDescuento(double monto) {
        return monto * (porcentajeDescuento / 100.0);
    }

    @Override
    public String toString() {
        return "DescuentoFidelidad [puntosRequeridos=" + puntosRequeridos + ", porcentajeDescuento=" + porcentajeDescuento + "]";
    }
}