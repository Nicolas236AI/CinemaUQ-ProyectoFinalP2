package co.uniquindio.edu.poo.model;

public class ContextoDescuento {
    private IEstrategiaDescuento estrategia;
    private Compra compra;

    /**
     * metodo constructor de la clase ContextoDescuento para inicializar sus valores
     * @param estrategia estrategia de descuento inicial
     * @param compra compra asociada al contexto de descuento
     */
    public ContextoDescuento(IEstrategiaDescuento estrategia, Compra compra) {
        this.estrategia = estrategia;
        this.compra = compra;
    }

    public IEstrategiaDescuento getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(IEstrategiaDescuento estrategia) {
        this.estrategia = estrategia;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * metodo que sirve para aplicar la estrategia de descuento actual sobre un valor base
     * @param monto monto base al que se le aplicara el descuento
     * @return monto final con el descuento aplicado
     */
    public double aplicarEstrategia(double monto) {
        return estrategia.calcularDescuento(monto);
    }

    @Override
    public String toString() {
        return "ContextoDescuento [estrategia=" + estrategia + ", compra=" + compra + "]";
    }
}