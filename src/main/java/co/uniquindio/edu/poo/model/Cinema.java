package co.uniquindio.edu.poo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cinema {

    private String id;
    private String nombre;
    private List<Cliente> listaClientes;
    private List<Sala> listaSalas;
    private List<Funcion> listaFunciones;
    private List<Pelicula> listaPeliculas;
    private List<IElementoConfiteria> listaConfiteria;
    private List<Compra> listaCompras;
    private List<Recarga> solicitudesRecarga;
    private List<IEstrategiaDescuento> promocionesActivas;
    private AdministradorCine administradorCine;
    private NotificadorCentro notificadorCentro;
    private static Cinema instancia;

    /**
     * Inicializa de forma privada la instancia única del cine (Singleton).
     *
     * @param id     identificador del cine.
     * @param nombre nombre comercial del cine.
     */
    private Cinema(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.listaClientes = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaPeliculas = new ArrayList<>();
        this.listaConfiteria = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
        this.solicitudesRecarga = new ArrayList<>();
        this.promocionesActivas = new ArrayList<>();
        this.notificadorCentro = new NotificadorCentro();
    }

    /**
     * Retorna la única instancia activa del sistema CinemaUQ.
     *
     * @return instancia única de Cinema.
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

    public List<IElementoConfiteria> getListaConfiteria() {
        return listaConfiteria;
    }

    public void setListaConfiteria(List<IElementoConfiteria> listaConfiteria) {
        this.listaConfiteria = listaConfiteria;
    }

    public List<Compra> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public List<Recarga> getSolicitudesRecarga() {
        return solicitudesRecarga;
    }

    public List<IEstrategiaDescuento> getPromocionesActivas() {
        return promocionesActivas;
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
     * Precarga películas, salas, funciones, confitería, promociones y un cliente inicial de prueba.
     */
    public void InicializarSistema() {
        if (!listaPeliculas.isEmpty()) {
            return;
        }
        this.administradorCine = new AdministradorCine("ADM-01", "Administrador CinemaUQ", "admin@cinemauq.edu.co", this);

        Pelicula p1 = new Pelicula("PEL-01", "Beyond Horizons", 135, "Ciencia Ficción");
        Pelicula p2 = new Pelicula("PEL-02", "Echoes", 118, "Drama");
        Pelicula p3 = new Pelicula("PEL-03", "Future We Build", 142, "Aventura");
        registrarPelicula(p1);
        registrarPelicula(p2);
        registrarPelicula(p3);

        Sala s1 = new Sala(1, 1, 40, this);
        Sala s2 = new Sala(2, 2, 50, this);
        administradorCine.gestionarSalas(s1);
        administradorCine.gestionarSalas(s2);

        Funcion f1 = new Funcion("FUN-01", LocalDateTime.now().plusDays(2), 16000.0, p1, s1);
        Funcion f2 = new Funcion("FUN-02", LocalDateTime.now().plusHours(10), 18000.0, p2, s2);
        registrarFuncion(f1);
        registrarFuncion(f2);

        ProductoConfiteria crispetas = new ProductoConfiteria("PROD-01", "Crispetas Grandes", 14000.0);
        ProductoConfiteria gaseosa = new ProductoConfiteria("PROD-02", "Gaseosa 32oz", 8000.0);
        Combo combo1 = new Combo("Combo CinemaUQ", 20000.0);
        combo1.agregarElemento(crispetas);
        combo1.agregarElemento(gaseosa);
        registrarElementoConfiteria(crispetas);
        registrarElementoConfiteria(gaseosa);
        registrarElementoConfiteria(combo1);

        promocionesActivas.add(new DescuentoEstudiante(15.0));
        promocionesActivas.add(new DescuentoFidelidad(300, 10.0));

        Cliente clienteDemo = registrarNuevoCliente("CLI-01", "Nicolas", "nicolas@uniquindio.edu.co", "1234");
        Recarga recargaInicial = clienteDemo.solicitarRecargaTarjeta(100000.0);
        administradorCine.aprobarRecargaTarjeta(recargaInicial);
    }

    /**
     * Registra una nueva película validando que no exista previamente con el mismo identificador.
     *
     * @param pelicula película a incorporar en el catálogo.
     */
    public void registrarPelicula(Pelicula pelicula) {
        if (pelicula == null || pelicula.getIdPelicula() == null) {
            throw new IllegalArgumentException("La película y su identificador son obligatorios.");
        }
        for (Pelicula p : listaPeliculas) {
            if (p.getIdPelicula().equalsIgnoreCase(pelicula.getIdPelicula())) {
                throw new IllegalArgumentException("Ya existe una película con el ID: " + pelicula.getIdPelicula());
            }
        }
        listaPeliculas.add(pelicula);
    }

    /**
     * Registra un cliente existente en el cine y lo suscribe al centro de notificaciones.
     *
     * @param cliente cliente a registrar.
     */
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        cliente.validarDatosRegistro();
        for (Cliente c : listaClientes) {
            if (c.getCorreo().equalsIgnoreCase(cliente.getCorreo())) {
                throw new IllegalArgumentException("Ya existe un cliente registrado con el correo: " + cliente.getCorreo());
            }
        }
        listaClientes.add(cliente);
        notificadorCentro.agregarObservador(cliente);
    }

    /**
     * Crea y registra un nuevo cliente desde el formulario de registro asignándole tarjeta y cuenta de puntos.
     *
     * @param idCliente  identificador del cliente.
     * @param nombre     nombre completo.
     * @param correo     correo electrónico.
     * @param contrasena contraseña de acceso.
     * @return cliente recién creado y registrado.
     */
    public Cliente registrarNuevoCliente(String idCliente, String nombre, String correo, String contrasena) {
        Cliente nuevo = new Cliente(idCliente, nombre, correo, contrasena, this);
        registrarCliente(nuevo);
        return nuevo;
    }

    /**
     * Autentica un usuario por correo y contraseña retornando el Cliente correspondiente o null si no coincide.
     *
     * @param correo     correo electrónico ingresado.
     * @param contrasena contraseña ingresada.
     * @return cliente autenticado o null.
     */
    public Cliente iniciarSesionCliente(String correo, String contrasena) {
        for (Cliente c : listaClientes) {
            if (c.autenticar(correo, contrasena)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retorna la lista completa de funciones disponibles en la cartelera del cine.
     *
     * @return lista de funciones programadas.
     */
    public List<Funcion> consultarCartelera() {
        return listaFunciones;
    }

    /**
     * Valida y programa una nueva función evitando cruces de horario en la misma sala y notificando a los clientes.
     *
     * @param funcion función a programar.
     */
    public void registrarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función no puede ser nula.");
        }
        funcion.verificarFuncion();
        for (Funcion existente : listaFunciones) {
            if (existente.getSala().getIdSala() == funcion.getSala().getIdSala()
                    && existente.getFechaHora().equals(funcion.getFechaHora())) {
                throw new IllegalStateException("Ya existe una función programada en esa sala y horario.");
            }
        }
        listaFunciones.add(funcion);
        notificadorCentro.notificarClientes("Nueva función en cartelera: " + funcion.getPelicula().getTitulo());
    }

    /**
     * Registra un producto individual o combo en el catálogo de confitería del cine.
     *
     * @param elemento producto o combo a agregar.
     */
    public void registrarElementoConfiteria(IElementoConfiteria elemento) {
        if (elemento == null || elemento.obtenerPrecio() <= 0) {
            throw new IllegalArgumentException("El elemento de confitería debe tener un precio válido.");
        }
        listaConfiteria.add(elemento);
    }

    /**
     * Almacena una compra completada en el registro histórico del cine.
     *
     * @param compra compra realizada.
     */
    public void registrarCompra(Compra compra) {
        if (compra != null && !listaCompras.contains(compra)) {
            listaCompras.add(compra);
        }
    }

    /**
     * Crea una solicitud de recarga pendiente de aprobación por el administrador.
     *
     * @param cliente cliente que solicita la recarga.
     * @param monto   valor monetario solicitado.
     * @return instancia de Recarga creada.
     */
    public Recarga registrarSolicitudRecarga(Cliente cliente, double monto) {
        if (cliente == null || cliente.getTarjeta() == null) {
            throw new IllegalArgumentException("El cliente debe tener una tarjeta virtual asociada.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la recarga debe ser mayor a cero.");
        }
        String idRecarga = "REC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Recarga solicitud = new Recarga(idRecarga, monto, LocalDate.now().toString(), cliente.getTarjeta());
        solicitudesRecarga.add(solicitud);
        return solicitud;
    }

    /**
     * Calcula el total de entradas vendidas para una película específica.
     *
     * @param idPelicula identificador de la película.
     * @return cantidad total de boletos vendidos para esa película.
     */
    public int contarEntradasPorPelicula(String idPelicula) {
        int total = 0;
        for (Compra compra : listaCompras) {
            if (compra.getEstado() == EstadoCompra.COMPLETADA && compra.getListaDetalles() != null) {
                for (DetalleCompra detalle : compra.getListaDetalles()) {
                    if (detalle.getBoleto() != null && detalle.getAsientoFuncionActual() != null) {
                        Pelicula p = detalle.getAsientoFuncionActual().getOwnedByFuncion().getPelicula();
                        if (p != null && p.getIdPelicula().equalsIgnoreCase(idPelicula)) {
                            total += detalle.getCantidad();
                        }
                    }
                }
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "Cinema [id=" + id + ", nombre=" + nombre + "]";
    }
}