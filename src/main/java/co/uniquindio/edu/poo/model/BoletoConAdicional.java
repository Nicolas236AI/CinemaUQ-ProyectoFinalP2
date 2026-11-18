package co.uniquindio.edu.poo.model;

public class BoletoConAdicional extends BoletoDecorador {
    private double costoAdicional;

    /**
     * metodo constructor de la clase BoletoConAdicional para inicializar sus valores
     * @param boleto boleto decorado
     * @param costoAdicional costo adicional a sumar
     */
    public BoletoConAdicional(IBoleto boleto, double costoAdicional) {
        super(boleto);
        this.costoAdicional = costoAdicional;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(double costoAdicional) {
        this.costoAdicional = costoAdicional;
    }

    /**
     * metodo que sirve para calcular el costo total del boleto incluyendo el adicional
     * @return costo total calculado
     */
    @Override
    public double calcularCosto() {
        return boleto.calcularCosto() + costoAdicional;
    }

    /**
     * metodo que sirve para obtener la descripcion completa del boleto con adicional
     * @return descripcion detallada del boleto
     */
    @Override
    public String obtenerDescripcion() {
        return boleto.obtenerDescripcion() + " con servicio adicional";
    }

    @Override
    public String toString() {
        return "BoletoConAdicional [costoAdicional=" + costoAdicional + ", boleto=" + boleto + "]";
    }
}