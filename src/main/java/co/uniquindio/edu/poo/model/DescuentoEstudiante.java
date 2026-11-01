package co.uniquindio.edu.poo.model;

public class DescuentoEstudiante implements IEstrategiaDescuento {
    private double porcentajeEstudiante;

    /**
     * metodo constructor de la clase DescuentoEstudiante para inicializar sus valores
     * @param porcentajeEstudiante porcentaje de descuento para estudiantes
     */
    public DescuentoEstudiante(double porcentajeEstudiante) {
        this.porcentajeEstudiante = porcentajeEstudiante;
    }

    public double getPorcentajeEstudiante() {
        return porcentajeEstudiante;
    }

    public void setPorcentajeEstudiante(double porcentajeEstudiante) {
        this.porcentajeEstudiante = porcentajeEstudiante;
    }

    /**
     * metodo que sirve para calcular el descuento aplicable a estudiantes
     * @param monto monto base de la compra
     * @return monto del descuento calculado
     */
    @Override
    public double calcularDescuento(double monto) {
        return monto * (porcentajeEstudiante / 100.0);
    }

    @Override
    public String toString() {
        return "DescuentoEstudiante [porcentajeEstudiante=" + porcentajeEstudiante + "]";
    }
}