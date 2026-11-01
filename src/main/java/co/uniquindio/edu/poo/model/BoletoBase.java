package co.uniquindio.edu.poo.model;

public class BoletoBase implements IBoleto {
    private double precioBase;
    private String descripcion;
    private AsientoFuncionActual asientoFuncionActual;

    /**
     * metodo constructor de la clase BoletoBase para inicializar sus valores
     * @param precioBase precio base del boleto
     * @param descripcion descripcion del boleto
     * @param asientoFuncionActual asiento y funcion asociados
     */
    public BoletoBase(double precioBase, String descripcion, AsientoFuncionActual asientoFuncionActual) {
        this.precioBase = precioBase;
        this.descripcion = descripcion;
        this.asientoFuncionActual = asientoFuncionActual;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public AsientoFuncionActual getAsientoFuncionActual() {
        return asientoFuncionActual;
    }

    public void setAsientoFuncionActual(AsientoFuncionActual asientoFuncionActual) {
        this.asientoFuncionActual = asientoFuncionActual;
    }

    /**
     * metodo que sirve para saber el costo del boleto
     * @return el precioBase del Boleto
     */
    @Override
    public double calcularCosto() {
        return precioBase;
    }

    /**
     * metodo que sirve para saber la descripcion del boleto
     * @return la descripcion
     */
    @Override
    public String obtenerDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "BoletoBase [precioBase=" + precioBase + ", descripcion=" + descripcion + ", asientoFuncionActual=" + asientoFuncionActual + "]";
    }
}