package co.uniquindio.edu.poo.model;

public class ProductoConfiteria implements IElementoConfiteria {
    private String idProducto;
    private String nombre;
    private double precio;

    /**
     * metodo constructor de la clase ProductoConfiteria para inicializar sus valores
     * @param idProducto identificador del producto
     * @param nombre nombre del producto
     * @param precio precio del producto
     */
    public ProductoConfiteria(String idProducto, String nombre, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public double obtenerPrecio() {
        return precio;
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "ProductoConfiteria [idProducto=" + idProducto + ", nombre=" + nombre + ", precio=" + precio + "]";
    }
}