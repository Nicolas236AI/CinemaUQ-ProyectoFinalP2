package co.uniquindio.edu.poo.model;

import java.util.List;
import java.util.ArrayList;

public class Cinema {
    private String id;
    private String nombre;
    private List<Cliente> listaClientes;
    private List<Sala> listaSalas;
    private List<Funcion> listaFunciones;
    private List<Pelicula> listaPeliculas;
    private AdministradorCine administradorCine;
    private NotificadorCentro notificadorCentro;
    private static Cinema instancia;

    /**
     * metodo constructor de la clase Cinema para inicializar sus valores de forma privada para el patron Singleton
     * @param id identificador del cine
     * @param nombre nombre del cine
     */
    private Cinema(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.listaClientes = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaPeliculas = new ArrayList<>();
    }

    /**
     * metodo que sirve para obtener la unica instancia de la clase Cinema mediante el patron Singleton
     * @return la instancia unica de Cinema
     */
    public static Cinema getInstancia() {
        if (instancia == null) {
            instancia = new Cinema("C001", "CinemaUQ");
        }
        return instancia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Sala> getListaSalas() {
        return listaSalas;
    }

    public void setListaSalas(List<Sala> listaSalas) {
        this.listaSalas = listaSalas;
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public AdministradorCine getAdministradorCine() {
        return administradorCine;
    }

    public void setAdministradorCine(AdministradorCine administradorCine) {
        this.administradorCine = administradorCine;
    }

    public NotificadorCentro getNotificadorCentro() {
        return notificadorCentro;
    }

    public void setNotificadorCentro(NotificadorCentro notificadorCentro) {
        this.notificadorCentro = notificadorCentro;
    }

    /**
     * metodo que sirve para inicializar el sistema del cine cargando datos iniciales
     */
    public void InicializarSistema() {
        System.out.println("Inicializando sistema del cine...");
    }

    /**
     * metodo que sirve para registrar una nueva pelicula en el sistema
     * @param pelicula pelicula a registrar
     */
    public void registrarPelicula(Pelicula pelicula) {
        if (pelicula != null && !listaPeliculas.contains(pelicula)) {
            listaPeliculas.add(pelicula);
        }
    }

    /**
     * metodo que sirve para registrar un nuevo cliente en el sistema
     * @param cliente cliente a registrar
     */
    public void registrarCliente(Cliente cliente) {
        if (cliente != null && !listaClientes.contains(cliente)) {
            listaClientes.add(cliente);
        }
    }

    /**
     * metodo que sirve para consultar la cartelera general de peliculas y funciones
     */
    public void consultarCartelera() {
        System.out.println("Consultando cartelera general del cine " + nombre);
    }

    /**
     * metodo que sirve para registrar una nueva funcion en el sistema
     * @param funcion funcion a registrar
     */
    public void registrarFuncion(Funcion funcion) {
        if (funcion != null && !listaFunciones.contains(funcion)) {
            listaFunciones.add(funcion);
        }
    }

    @Override
    public String toString() {
        return "Cinema [id=" + id + ", nombre=" + nombre + "]";
    }
}