package co.uniquindio.edu.poo.model;

public abstract class BoletoDecorador implements IBoleto {
    protected IBoleto boleto;

    /**
     * metodo constructor de la clase BoletoDecorador para inicializar sus valores
     * @param boleto boleto base a decorar
     */
    public BoletoDecorador(IBoleto boleto) {
        this.boleto = boleto;
    }

    public IBoleto getBoleto() {
        return boleto;
    }

    public void setBoleto(IBoleto boleto) {
        this.boleto = boleto;
    }

    /**
     * metodo que sirve para calcular el costo del boleto decorado
     * @return costo calculado del boleto
     */
    @Override
    public double calcularCosto() {
        return boleto.calcularCosto();
    }

    /**
     * metodo que sirve para obtener la descripcion del boleto decorado
     * @return descripcion del boleto
     */
    @Override
    public String obtenerDescripcion() {
        return boleto.obtenerDescripcion();
    }

    @Override
    public String toString() {
        return "BoletoDecorador [boleto=" + boleto + "]";
    }
}