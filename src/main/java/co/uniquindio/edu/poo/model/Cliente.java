package co.uniquindio.edu.poo.model;

import java.util.List;
import java.util.ArrayList;

public class Cliente implements IClienteObserver {
    private String idCliente;
    private String nombre;
    private String correo;
    private Tarjeta tarjeta;
    private CuentaPuntos cuentaPuntos;
    private List<Compra> listaCompras;
    private Cinema ownedByCinema;

    /**
     * metodo constructor de la clase Cliente para inicializar sus valores
     * @param idCliente identificador del cliente
     * @param nombre nombre del cliente
     * @param correo correo electronico
     * @param ownedByCinema cine al que pertenece
     */
    public Cliente(String idCliente, String nombre, String correo, Cinema ownedByCinema) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.correo = correo;
        this.ownedByCinema = ownedByCinema;
        this.listaCompras = new ArrayList<>();
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public void setCuentaPuntos(CuentaPuntos cuentaPuntos) {
        this.cuentaPuntos = cuentaPuntos;
    }

    public List<Compra> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public Cinema getOwnedByCinema() {
        return ownedByCinema;
    }

    public void setOwnedByCinema(Cinema ownedByCinema) {
        this.ownedByCinema = ownedByCinema;
    }

    /**
     * metodo que sirve para consultar la cartelera disponible en el cine
     */
    public void consultarCartelera() {
        System.out.println("Consultando cartelera para el cliente: " + nombre);
    }

    /**
     * metodo que sirve para realizar una compra en el sistema
     */
    public void realizarCompra() {
        System.out.println("Realizando compra...");
    }

    @Override
    public void actualizar() {
        System.out.println("Cliente " + nombre + " notificado de un cambio.");
    }

    @Override
    public String toString() {
        return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", correo=" + correo + "]";
    }
}