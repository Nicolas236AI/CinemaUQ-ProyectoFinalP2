package co.uniquindio.edu.poo.model;

import java.util.List;
import java.util.ArrayList;

public class Combo implements IElementoConfiteria {
    private String nombre;
    private double precio;
    private List<IElementoConfiteria> elementos;

    /**
     * metodo constructor de la clase Combo para inicializar sus valores
     * @param nombre nombre del combo
     * @param precio precio del combo
     */
    public Combo(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.elementos = new ArrayList<>();
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

    public List<IElementoConfiteria> getElementos() {
        return elementos;
    }

    public void setElementos(List<IElementoConfiteria> elementos) {
        this.elementos = elementos;
    }

    /**
     * metodo que sirve para agregar un elemento a la lista del combo
     * @param elemento elemento de confiteria a agregar
     */
    public void agregarElemento(IElementoConfiteria elemento) {
        this.elementos.add(elemento);
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
        return "Combo [nombre=" + nombre + ", precio=" + precio + ", elementos=" + elementos + "]";
    }
}