package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.model.AdministradorCine;
import co.uniquindio.edu.poo.model.AsientoFuncionActual;
import co.uniquindio.edu.poo.model.BoletoBase;
import co.uniquindio.edu.poo.model.BoletoConAdicional;
import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Combo;
import co.uniquindio.edu.poo.model.Compra;
import co.uniquindio.edu.poo.model.ContextoDescuento;
import co.uniquindio.edu.poo.model.DescuentoEstudiante;
import co.uniquindio.edu.poo.model.DescuentoFidelidad;
import co.uniquindio.edu.poo.model.DetalleCompra;
import co.uniquindio.edu.poo.model.DiagramaBarra;
import co.uniquindio.edu.poo.model.DiagramaPastel;
import co.uniquindio.edu.poo.model.EstadoAsiento;
import co.uniquindio.edu.poo.model.EstadoCompra;
import co.uniquindio.edu.poo.model.Funcion;
import co.uniquindio.edu.poo.model.GraficoLinea;
import co.uniquindio.edu.poo.model.IBoleto;
import co.uniquindio.edu.poo.model.IElementoConfiteria;
import co.uniquindio.edu.poo.model.IEstrategiaDescuento;
import co.uniquindio.edu.poo.model.IReporteFactory;
import co.uniquindio.edu.poo.model.Pelicula;
import co.uniquindio.edu.poo.model.ProductoConfiteria;
import co.uniquindio.edu.poo.model.Recarga;
import co.uniquindio.edu.poo.model.ReporteExportable;
import co.uniquindio.edu.poo.model.ReporteFactory;
import co.uniquindio.edu.poo.model.Sala;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControlGeneral {

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final double valorAdicionalBoleto = 5000.0;

    private static Cliente clienteActivo;
    private static AdministradorCine adminActivo;
    private static boolean modoDemoActivo;
    private static int contadorDetalles = 1;

    private static final List<String> nombresPromociones = new ArrayList<>();
    private static final List<IEstrategiaDescuento> estrategiasPromociones = new ArrayList<>();

    private final Cinema cinema = Cinema.getInstancia();
    private final IReporteFactory reporteFactory = new ReporteFactory();

    static {
        Cinema.getInstancia().InicializarSistema();
        inicializarPromociones();
    }

    /**
     * Sincroniza la lista visual de promociones con las estrategias configuradas en el cine.
     */
    private static void inicializarPromociones() {
        if (!nombresPromociones.isEmpty()) {
            return;
        }
        nombresPromociones.add("Sin descuento");
        estrategiasPromociones.add(null);

        for (IEstrategiaDescuento est : Cinema.getInstancia().getPromocionesActivas()) {
            if (est instanceof DescuentoEstudiante de) {
                nombresPromociones.add("Descuento Estudiante (" + de.getPorcentajeEstudiante() + "%)");
                estrategiasPromociones.add(de);
            } else if (est instanceof DescuentoFidelidad df) {
                nombresPromociones.add("Descuento Fidelidad (" + df.getPorcentajeDescuento() + "% - req. " + df.getPuntosRequeridos() + " pts)");
                estrategiasPromociones.add(df);
            }
        }
    }

    /**
     * Valida credenciales e inicia sesión como Cliente o Administrador.
     */
    public void iniciarSesion(String rol, String correo, String contrasena) {
        if (estaVacio(correo) || estaVacio(contrasena)) {
            throw new IllegalArgumentException("Ingresa tu correo y contraseña.");
        }
        String correoLimpio = correo.trim();
        if ("Administrador".equalsIgnoreCase(rol)) {
            AdministradorCine admin = cinema.getAdministradorCine();
            if (admin != null && admin.getCorreo().equalsIgnoreCase(correoLimpio) && contrasena.length() >= 4) {
                adminActivo = admin;
                clienteActivo = null;
                modoDemoActivo = false;
                return;
            }
            throw new IllegalArgumentException("Credenciales de administrador incorrectas (usa admin@cinemauq.edu.co).");
        } else {
            Cliente encontrado = cinema.iniciarSesionCliente(correoLimpio, contrasena);
            if (encontrado != null) {
                clienteActivo = encontrado;
                adminActivo = null;
                modoDemoActivo = false;
                return;
            }
            throw new IllegalArgumentException("Correo o contraseña de cliente incorrectos.");
        }
    }

    /**
     * Activa el modo demostración con permisos completos para explorar todas las vistas.
     */
    public void iniciarModoDemo() {
        modoDemoActivo = true;
        adminActivo = cinema.getAdministradorCine();
        clienteActivo = cinema.getListaClientes().isEmpty() ? null : cinema.getListaClientes().get(0);
    }

    /**
     * Registra un nuevo cliente, crea su tarjeta virtual con saldo inicial y abre su sesión.
     */
    public void registrarNuevoClienteConCuenta(String id, String nombre, String correo, String contrasena) {
        validarDatosCliente(id, nombre, correo);
        if (estaVacio(contrasena) || contrasena.trim().length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (existeIdCliente(id.trim(), null)) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con esa identificación.");
        }
        Cliente nuevo = cinema.registrarNuevoCliente(id.trim(), nombre.trim(), correo.trim(), contrasena.trim());
        nuevo.getTarjeta().acreditarSaldo(50000.0);
        clienteActivo = nuevo;
        adminActivo = null;
        modoDemoActivo = false;
    }

    public void cerrarSesion() {
        clienteActivo = null;
        adminActivo = null;
        modoDemoActivo = false;
    }

    public boolean esAdministradorActivo() {
        return adminActivo != null || modoDemoActivo;
    }

    public Cliente getClienteActivo() {
        return clienteActivo;
    }

    public String obtenerTextoUsuarioActivo() {
        if (modoDemoActivo) {
            return "Modo Demostración (Admin + Cliente)";
        }
        if (adminActivo != null) {
            return "Administrador: " + adminActivo.getNombre();
        }
        if (clienteActivo != null) {
            return "Cliente: " + clienteActivo.getNombre();
        }
        return "Usuario: Invitado";
    }

    public int contarPeliculas() {
        return cinema.getListaPeliculas().size();
    }

    public int contarFunciones() {
        return cinema.getListaFunciones().size();
    }

    public int contarComprasCompletadas() {
        int total = 0;
        for (Compra c : obtenerTodasLasCompras()) {
            if (c.getEstado() == EstadoCompra.COMPLETADA) {
                total++;
            }
        }
        return total;
    }

    public double calcularIngresosTotales() {
        double suma = 0.0;
        for (Compra c : obtenerTodasLasCompras()) {
            if (c.getEstado() == EstadoCompra.COMPLETADA) {
                suma += c.getTotal();
            }
        }
        return suma;
    }

    public LocalDateTime parsearFecha(String texto) {
        return LocalDateTime.parse(texto == null ? "" : texto.trim(), formatoFecha);
    }

    public String formatearFecha(LocalDateTime fecha) {
        return fecha.format(formatoFecha);
    }

    public List<Pelicula> obtenerPeliculas() {
        return cinema.getListaPeliculas();
    }

    public List<Pelicula> buscarPeliculas(String filtro) {
        if (estaVacio(filtro)) {
            return new ArrayList<>(cinema.getListaPeliculas());
        }
        String q = filtro.trim().toLowerCase();
        List<Pelicula> filtradas = new ArrayList<>();
        for (Pelicula p : cinema.getListaPeliculas()) {
            boolean coincideTitulo = p.getTitulo() != null && p.getTitulo().toLowerCase().contains(q);
            boolean coincideGenero = p.getGenero() != null && p.getGenero().toLowerCase().contains(q);
            if (coincideTitulo || coincideGenero) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public void registrarPelicula(String id, String titulo, int duracion, String genero) {
        validarDatosPelicula(id, titulo, duracion, genero);
        cinema.registrarPelicula(new Pelicula(id, titulo, duracion, genero));
    }

    public void actualizarPelicula(Pelicula pelicula, String id, String titulo, int duracion, String genero) {
        if (pelicula == null) {
            throw new IllegalArgumentException("Selecciona una película de la tabla para actualizarla.");
        }
        validarDatosPelicula(id, titulo, duracion, genero);
        if (existeIdPelicula(id, pelicula)) {
            throw new IllegalArgumentException("Otra película ya utiliza ese identificador.");
        }
        pelicula.setIdPelicula(id);
        pelicula.setTitulo(titulo);
        pelicula.setDuracion(duracion);
        pelicula.setGenero(genero);
    }

    public void eliminarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("Selecciona una película para eliminarla.");
        }
        cinema.getListaPeliculas().remove(pelicula);
    }

    private void validarDatosPelicula(String id, String titulo, int duracion, String genero) {
        if (estaVacio(id) || estaVacio(titulo) || estaVacio(genero)) {
            throw new IllegalArgumentException("Completa todos los campos de la película.");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser un número entero mayor que cero.");
        }
    }

    private boolean existeIdPelicula(String id, Pelicula excluida) {
        for (Pelicula p : cinema.getListaPeliculas()) {
            if (p != excluida && p.getIdPelicula() != null && p.getIdPelicula().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public List<Funcion> obtenerFunciones() {
        return cinema.getListaFunciones();
    }

    public void registrarFuncion(String id, LocalDateTime fecha, double precio, Pelicula pelicula, Sala sala) {
        validarDatosFuncion(id, fecha, precio, pelicula, sala);
        if (existeIdFuncion(id, null)) {
            throw new IllegalArgumentException("Ya existe una función con ese ID.");
        }
        cinema.registrarFuncion(new Funcion(id, fecha, precio, pelicula, sala));
    }

    public void actualizarFuncion(Funcion funcion, String id, LocalDateTime fecha, double precio, Pelicula pelicula, Sala sala) {
        if (funcion == null) {
            throw new IllegalArgumentException("Selecciona una función de la tabla.");
        }
        validarDatosFuncion(id, fecha, precio, pelicula, sala);
        if (existeIdFuncion(id, funcion)) {
            throw new IllegalArgumentException("Ya existe otra función con ese ID.");
        }
        funcion.setIdFuncion(id);
        funcion.setFechaHora(fecha);
        funcion.setPrecioBase(precio);
        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
    }

    public void cancelarFuncionPorCine(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("Selecciona una función para cancelarla.");
        }
        if (cinema.getAdministradorCine() != null) {
            cinema.getAdministradorCine().cancelarFuncion(funcion);
        } else {
            funcion.cancelarFuncionPorCine();
        }
    }

    public void eliminarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("Selecciona una función para eliminar.");
        }
        cinema.getListaFunciones().remove(funcion);
    }

    public List<AsientoFuncionActual> obtenerAsientosDeFuncion(Funcion funcion) {
        if (funcion == null) {
            return new ArrayList<>();
        }
        return funcion.getListaAsientosFuncionActual();
    }

    public void reservarAsientoTemporal(AsientoFuncionActual asientoFuncion, int cantidadActualSeleccionada) {
        if (asientoFuncion == null) {
            throw new IllegalArgumentException("Asiento no válido.");
        }
        if (cantidadActualSeleccionada >= 6) {
            throw new IllegalStateException("Solo puedes seleccionar hasta 6 entradas por compra.");
        }
        if (!asientoFuncion.verificarDisponibilidad()) {
            throw new IllegalStateException("El asiento seleccionado no está disponible.");
        }
        asientoFuncion.bloquearAsiento();
    }

    public void liberarAsientoTemporal(AsientoFuncionActual asientoFuncion) {
        if (asientoFuncion != null && asientoFuncion.getEstadoAsiento() == EstadoAsiento.SELECCIONADO) {
            asientoFuncion.liberarAsiento();
        }
    }

    private void validarDatosFuncion(String id, LocalDateTime fecha, double precio, Pelicula pelicula, Sala sala) {
        if (estaVacio(id)) {
            throw new IllegalArgumentException("El ID de la función es obligatorio.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha y hora de la función son obligatorias.");
        }
        if (!Double.isFinite(precio) || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (pelicula == null || sala == null) {
            throw new IllegalArgumentException("Debes seleccionar una película y una sala.");
        }
    }

    private boolean existeIdFuncion(String id, Funcion excluida) {
        for (Funcion f : cinema.getListaFunciones()) {
            if (f != excluida && f.getIdFuncion() != null && f.getIdFuncion().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public List<Sala> obtenerSalas() {
        return cinema.getListaSalas();
    }

    public void registrarSala(int id, int numero, int capacidad) {
        validarDatosSala(id, numero, capacidad);
        if (existeIdSala(id, null)) {
            throw new IllegalArgumentException("Ya existe una sala con ese identificador.");
        }
        if (existeNumeroSala(numero, null)) {
            throw new IllegalArgumentException("Ya existe una sala con ese número.");
        }
        Sala nueva = new Sala(id, numero, capacidad, cinema);
        if (cinema.getAdministradorCine() != null) {
            cinema.getAdministradorCine().gestionarSalas(nueva);
        } else {
            nueva.configurarAsientos();
            cinema.getListaSalas().add(nueva);
        }
    }

    public void actualizarSala(Sala sala, int id, int numero, int capacidad) {
        if (sala == null) {
            throw new IllegalArgumentException("Debes seleccionar una sala de la tabla.");
        }
        validarDatosSala(id, numero, capacidad);
        if (existeIdSala(id, sala)) {
            throw new IllegalArgumentException("Otra sala ya utiliza ese identificador.");
        }
        if (existeNumeroSala(numero, sala)) {
            throw new IllegalArgumentException("Otra sala ya utiliza ese número.");
        }
        boolean tieneFunciones = tieneFuncionesAsociadas(sala);
        if (tieneFunciones && capacidad != sala.getCapacidad()) {
            throw new IllegalArgumentException("No puedes cambiar la capacidad de una sala con funciones programadas.");
        }
        sala.setIdSala(id);
        sala.setNumeroSala(numero);
        sala.setCapacidad(capacidad);
        if (!tieneFunciones) {
            sala.configurarAsientos();
        }
    }

    public void eliminarSala(Sala sala) {
        if (sala == null) {
            throw new IllegalArgumentException("Debes seleccionar una sala para eliminarla.");
        }
        if (tieneFuncionesAsociadas(sala)) {
            throw new IllegalArgumentException("No puedes eliminar esta sala porque tiene funciones asociadas.");
        }
        cinema.getListaSalas().remove(sala);
    }

    private void validarDatosSala(int id, int numero, int capacidad) {
        if (id <= 0 || numero <= 0 || capacidad <= 0) {
            throw new IllegalArgumentException("El ID, número y capacidad deben ser mayores que cero.");
        }
    }

    private boolean existeIdSala(int id, Sala excluida) {
        for (Sala s : cinema.getListaSalas()) {
            if (s != excluida && s.getIdSala() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean existeNumeroSala(int numero, Sala excluida) {
        for (Sala s : cinema.getListaSalas()) {
            if (s != excluida && s.getNumeroSala() == numero) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneFuncionesAsociadas(Sala sala) {
        for (Funcion f : cinema.getListaFunciones()) {
            if (f.getSala() == sala) {
                return true;
            }
        }
        return false;
    }

    public List<Cliente> obtenerClientes() {
        return cinema.getListaClientes();
    }

    public void registrarCliente(String id, String nombre, String correo) {
        validarDatosCliente(id, nombre, correo);
        if (existeIdCliente(id, null)) {
            throw new IllegalArgumentException("Ya existe un cliente con ese identificador.");
        }
        cinema.registrarCliente(new Cliente(id, nombre, correo, cinema));
    }

    public void actualizarCliente(Cliente cliente, String id, String nombre, String correo) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debes seleccionar un cliente de la tabla.");
        }
        validarDatosCliente(id, nombre, correo);
        if (existeIdCliente(id, cliente)) {
            throw new IllegalArgumentException("Otro cliente ya utiliza ese identificador.");
        }
        cliente.setIdCliente(id);
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
    }

    public void eliminarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debes seleccionar un cliente para eliminarlo.");
        }
        if (cliente.getListaCompras() != null && !cliente.getListaCompras().isEmpty()) {
            throw new IllegalArgumentException("No puedes eliminar este cliente porque tiene compras registradas.");
        }
        cinema.getListaClientes().remove(cliente);
    }

    public String obtenerIdTarjetaCliente(Cliente cliente) {
        if (cliente == null || cliente.getTarjeta() == null) {
            return "Sin tarjeta";
        }
        return cliente.getTarjeta().getIdTarjeta();
    }

    public double obtenerSaldoTarjetaCliente(Cliente cliente) {
        if (cliente == null || cliente.getTarjeta() == null) {
            return 0.0;
        }
        return cliente.consultarSaldoTarjeta();
    }

    public List<String> obtenerMovimientosCliente(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }
        return cliente.consultarMovimientosTarjeta();
    }

    public int obtenerPuntosVigentesCliente(Cliente cliente) {
        if (cliente == null) {
            return 0;
        }
        return cliente.consultarPuntos();
    }

    public List<String> obtenerDetalleLotesPuntosCliente(Cliente cliente) {
        List<String> detalles = new ArrayList<>();
        if (cliente != null && cliente.getCuentaPuntos() != null) {
            List<LocalDate> obtenciones = cliente.getCuentaPuntos().getFechasObtencion();
            List<LocalDate> vencimientos = cliente.getCuentaPuntos().getFechasVencimiento();
            for (int i = 0; i < obtenciones.size(); i++) {
                detalles.add("Lote #" + (i + 1) + " | Obtenido: " + obtenciones.get(i) + " | Vence: " + vencimientos.get(i));
            }
        }
        if (detalles.isEmpty()) {
            detalles.add("No hay registros de puntos aún.");
        }
        return detalles;
    }

    public void solicitarRecargaTarjeta(Cliente cliente, double monto) {
        if (cliente == null) {
            throw new IllegalArgumentException("Selecciona un cliente.");
        }
        cliente.solicitarRecargaTarjeta(monto);
    }

    public List<Recarga> obtenerSolicitudesRecarga() {
        return cinema.getSolicitudesRecarga();
    }

    public void aprobarRecarga(Recarga solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("Selecciona una solicitud de recarga de la tabla.");
        }
        if (cinema.getAdministradorCine() != null) {
            cinema.getAdministradorCine().aprobarRecargaTarjeta(solicitud);
        } else {
            solicitud.ejecutarRecarga();
            cinema.getSolicitudesRecarga().remove(solicitud);
        }
    }

    public void redimirPuntosCliente(Cliente cliente, String tipoBeneficio) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no válido.");
        }
        cliente.redimirPuntosPorBeneficio(tipoBeneficio);
    }

    private void validarDatosCliente(String id, String nombre, String correo) {
        if (estaVacio(id) || estaVacio(nombre) || estaVacio(correo)) {
            throw new IllegalArgumentException("Debes completar todos los campos del cliente.");
        }
        if (!correo.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Introduce una dirección de correo válida.");
        }
    }

    private boolean existeIdCliente(String id, Cliente excluido) {
        for (Cliente c : cinema.getListaClientes()) {
            if (c != excluido && c.getIdCliente() != null && c.getIdCliente().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public List<IElementoConfiteria> obtenerCatalogoConfiteria() {
        return cinema.getListaConfiteria();
    }

    public List<ProductoConfiteria> obtenerProductosIndividuales() {
        List<ProductoConfiteria> productos = new ArrayList<>();
        for (IElementoConfiteria item : cinema.getListaConfiteria()) {
            if (item instanceof ProductoConfiteria prod) {
                productos.add(prod);
            }
        }
        return productos;
    }

    public List<String> obtenerDescripcionesCatalogoConfiteria() {
        List<String> lista = new ArrayList<>();
        for (IElementoConfiteria item : cinema.getListaConfiteria()) {
            lista.add(item.obtenerNombre() + " — $" + String.format("%.2f", item.obtenerPrecio()));
        }
        return lista;
    }

    public void registrarProductoConfiteria(String id, String nombre, double precio) {
        if (estaVacio(id) || estaVacio(nombre)) {
            throw new IllegalArgumentException("Completa el ID y el nombre del producto.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a cero.");
        }
        cinema.registrarElementoConfiteria(new ProductoConfiteria(id.trim(), nombre.trim(), precio));
    }

    public void registrarComboConfiteria(String nombreCombo, double precioCombo, List<IElementoConfiteria> elementos) {
        if (estaVacio(nombreCombo)) {
            throw new IllegalArgumentException("Ingresa un nombre para el combo.");
        }
        if (precioCombo <= 0) {
            throw new IllegalArgumentException("El precio del combo debe ser mayor a cero.");
        }
        if (elementos == null || elementos.isEmpty()) {
            throw new IllegalArgumentException("Agrega al menos un producto antes de guardar el combo.");
        }
        Combo combo = new Combo(nombreCombo.trim(), precioCombo);
        for (IElementoConfiteria e : elementos) {
            combo.agregarElemento(e);
        }
        cinema.registrarElementoConfiteria(combo);
    }

    public List<String> obtenerNombresPromociones() {
        return nombresPromociones;
    }

    public void registrarPromocion(String tipo, double porcentaje, int puntosRequeridos) {
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 1 y 100.");
        }
        if ("Fidelidad".equalsIgnoreCase(tipo)) {
            DescuentoFidelidad est = new DescuentoFidelidad(puntosRequeridos, porcentaje);
            cinema.getPromocionesActivas().add(est);
            nombresPromociones.add("Descuento Fidelidad (" + porcentaje + "% - req. " + puntosRequeridos + " pts)");
            estrategiasPromociones.add(est);
        } else {
            DescuentoEstudiante est = new DescuentoEstudiante(porcentaje);
            cinema.getPromocionesActivas().add(est);
            nombresPromociones.add("Descuento Estudiante (" + porcentaje + "%)");
            estrategiasPromociones.add(est);
        }
    }

    private IEstrategiaDescuento obtenerEstrategiaPorNombre(String nombrePromo) {
        if (nombrePromo == null) {
            return null;
        }
        int idx = nombresPromociones.indexOf(nombrePromo);
        if (idx >= 0 && idx < estrategiasPromociones.size()) {
            return estrategiasPromociones.get(idx);
        }
        return null;
    }

    public double calcularTotalPreliminar(Cliente cliente, Funcion funcion, int cantidadAsientos,
                                          boolean boletoAdicional, List<IElementoConfiteria> confiteria,
                                          String nombrePromocion) {
        double subtotal = 0.0;
        if (funcion != null && cantidadAsientos > 0) {
            double precioUnitarioBoleto = funcion.getPrecioBase() + (boletoAdicional ? valorAdicionalBoleto : 0.0);
            subtotal += precioUnitarioBoleto * cantidadAsientos;
        }
        if (confiteria != null) {
            for (IElementoConfiteria item : confiteria) {
                subtotal += item.obtenerPrecio();
            }
        }
        IEstrategiaDescuento estrategia = obtenerEstrategiaPorNombre(nombrePromocion);
        if (estrategia != null && subtotal > 0) {
            if (estrategia instanceof DescuentoFidelidad df && cliente != null && cliente.consultarPuntos() < df.getPuntosRequeridos()) {
                return subtotal;
            }
            ContextoDescuento ctx = new ContextoDescuento(estrategia, null);
            double descuento = ctx.aplicarEstrategia(subtotal);
            subtotal = Math.max(0.0, subtotal - descuento);
        }
        return subtotal;
    }

    public int calcularPuntosEstimados(double total) {
        return (int) Math.floor(total / 100.0);
    }

    public void procesarCompraCompleta(Cliente cliente, Funcion funcion,
                                       List<AsientoFuncionActual> asientos, boolean boletoAdicional,
                                       List<IElementoConfiteria> confiteria, String nombrePromocion) {
        if (cliente == null) {
            throw new IllegalArgumentException("Selecciona un cliente comprador.");
        }
        if ((asientos == null || asientos.isEmpty()) && (confiteria == null || confiteria.isEmpty())) {
            throw new IllegalArgumentException("Selecciona al menos un asiento o un producto de confitería.");
        }
        if (asientos != null && !asientos.isEmpty() && funcion == null) {
            throw new IllegalArgumentException("Selecciona una función válida para los asientos.");
        }

        String idCompra = "CMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Compra compra = new Compra.Builder()
                .idCompra(idCompra)
                .fecha(LocalDate.now().toString())
                .estado(EstadoCompra.PENDIENTE)
                .cliente(cliente)
                .build();

        if (asientos != null) {
            for (AsientoFuncionActual af : asientos) {
                IBoleto boleto = new BoletoBase(
                        funcion.getPrecioBase(),
                        "Entrada " + funcion.getPelicula().getTitulo() + " (" + af.getAsiento().getIdAsiento() + ")",
                        af
                );
                if (boletoAdicional) {
                    boleto = new BoletoConAdicional(boleto, valorAdicionalBoleto);
                }
                DetalleCompra detalle = new DetalleCompra(
                        contadorDetalles++, 1, boleto.calcularCosto(), af, null, boleto, compra
                );
                af.setDetalleCompra(detalle);
                compra.getListaDetalles().add(detalle);
            }
        }

        if (confiteria != null) {
            for (IElementoConfiteria item : confiteria) {
                DetalleCompra detalle = new DetalleCompra(
                        contadorDetalles++, 1, item.obtenerPrecio(), null, item, null, compra
                );
                compra.getListaDetalles().add(detalle);
            }
        }

        IEstrategiaDescuento estrategia = obtenerEstrategiaPorNombre(nombrePromocion);
        if (estrategia != null) {
            if (estrategia instanceof DescuentoFidelidad df && cliente.consultarPuntos() < df.getPuntosRequeridos()) {
                throw new IllegalStateException("El cliente no cumple los " + df.getPuntosRequeridos() + " puntos requeridos para este descuento.");
            }
            compra.setContextoDescuento(new ContextoDescuento(estrategia, compra));
        }

        cliente.realizarCompra(compra);
    }

    public List<Compra> obtenerTodasLasCompras() {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : cinema.getListaCompras()) {
            if (!resultado.contains(c)) {
                resultado.add(c);
            }
        }
        for (Cliente cliente : cinema.getListaClientes()) {
            if (cliente.getListaCompras() != null) {
                for (Compra c : cliente.getListaCompras()) {
                    if (!resultado.contains(c)) {
                        resultado.add(c);
                    }
                }
            }
        }
        return resultado;
    }

    public List<Compra> obtenerComprasDeCliente(Cliente cliente) {
        if (cliente == null || cliente.getListaCompras() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(cliente.getListaCompras());
    }

    public List<Compra> filtrarComprasPorEstado(String estadoTexto) {
        List<Compra> todas = obtenerTodasLasCompras();
        if (estadoTexto == null || "TODAS".equalsIgnoreCase(estadoTexto)) {
            return todas;
        }
        List<Compra> filtradas = new ArrayList<>();
        for (Compra c : todas) {
            if (c.getEstado() != null && c.getEstado().name().equalsIgnoreCase(estadoTexto)) {
                filtradas.add(c);
            }
        }
        return filtradas;
    }

    public void registrarCompra(String id, String fecha, double total, Cliente cliente) {
        validarDatosCompra(id, fecha, total, cliente);
        if (existeIdCompra(id, null)) {
            throw new IllegalArgumentException("Ya existe una compra con ese identificador.");
        }
        Compra compra = new Compra.Builder()
                .idCompra(id)
                .fecha(fecha.trim())
                .total(total)
                .estado(EstadoCompra.PENDIENTE)
                .cliente(cliente)
                .build();

        cliente.getListaCompras().add(compra);
        cinema.registrarCompra(compra);
    }

    public void actualizarCompra(Compra compra, String id, String fecha, double total, Cliente nuevoCliente) {
        if (compra == null) {
            throw new IllegalArgumentException("Selecciona una compra de la tabla.");
        }
        if (compra.getEstado() != EstadoCompra.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden editar compras pendientes.");
        }
        validarDatosCompra(id, fecha, total, nuevoCliente);
        if (existeIdCompra(id, compra)) {
            throw new IllegalArgumentException("Ya existe otra compra con ese identificador.");
        }

        Cliente clienteAnterior = compra.getCliente();
        if (clienteAnterior != nuevoCliente) {
            if (clienteAnterior != null && clienteAnterior.getListaCompras() != null) {
                clienteAnterior.getListaCompras().remove(compra);
            }
            nuevoCliente.getListaCompras().add(compra);
        }

        compra.setIdCompra(id);
        compra.setFecha(fecha.trim());
        compra.setTotal(total);
        compra.setCliente(nuevoCliente);
    }

    public double cancelarCompraConPolitica(Compra compra, boolean canceladaPorCine) {
        if (compra == null) {
            throw new IllegalArgumentException("Selecciona una compra para cancelar.");
        }
        if (compra.getEstado() == EstadoCompra.COMPLETADA) {
            return compra.cancelarCompra(canceladaPorCine);
        }
        if (compra.getEstado() == EstadoCompra.CANCELADA) {
            throw new IllegalStateException("Esta compra ya fue cancelada.");
        }
        compra.setEstado(EstadoCompra.CANCELADA);
        return 0.0;
    }

    private void validarDatosCompra(String id, String fecha, double total, Cliente cliente) {
        if (estaVacio(id)) {
            throw new IllegalArgumentException("El identificador de la compra es obligatorio.");
        }
        if (estaVacio(fecha)) {
            throw new IllegalArgumentException("La fecha de la compra es obligatoria.");
        }
        parsearFecha(fecha);
        if (!Double.isFinite(total) || total <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor que cero.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Selecciona el cliente de la compra.");
        }
    }

    private boolean existeIdCompra(String id, Compra excluida) {
        for (Compra c : obtenerTodasLasCompras()) {
            if (c != excluida && c.getIdCompra() != null && c.getIdCompra().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public String construirReportePorTipo(String tipoReporte, LocalDate inicio, LocalDate fin) {
        String nombre = (tipoReporte == null || tipoReporte.isBlank()) ? "Ventas totales" : tipoReporte;
        ReporteExportable reporte = new ReporteExportable(nombre, "TXT", inicio, fin);
        reporte.procesarDatos();
        return reporte.obtenerDatos();
    }

    public void exportarReportePorTipo(String tipoReporte, LocalDate inicio, LocalDate fin, String formato) {
        String nombre = (tipoReporte == null || tipoReporte.isBlank()) ? "Ventas totales" : tipoReporte;
        ReporteExportable reporte = reporteFactory.crearReporteExportable(nombre, formato);
        reporte.setFechaInicio(inicio);
        reporte.setFechaFin(fin);
        if (cinema.getAdministradorCine() != null) {
            cinema.getAdministradorCine().generarReporteOperativo(reporte);
        } else {
            reporte.generarReporte();
        }
    }

    public List<String> obtenerEtiquetasGraficoBarras() {
        DiagramaBarra diagrama = (DiagramaBarra) reporteFactory.crearReporteVisual("BARRAS", "Entradas por Película");
        diagrama.procesarDatos();
        return diagrama.getEtiquetas();
    }

    public List<Double> obtenerValoresGraficoBarras() {
        DiagramaBarra diagrama = (DiagramaBarra) reporteFactory.crearReporteVisual("BARRAS", "Entradas por Película");
        diagrama.procesarDatos();
        return diagrama.getValores();
    }

    public List<String> obtenerCategoriasGraficoPastel() {
        DiagramaPastel diagrama = (DiagramaPastel) reporteFactory.crearReporteVisual("PASTEL", "Ocupación");
        diagrama.procesarDatos();
        return diagrama.getCategorias();
    }

    public List<Double> obtenerPorcentajesGraficoPastel() {
        DiagramaPastel diagrama = (DiagramaPastel) reporteFactory.crearReporteVisual("PASTEL", "Ocupación");
        diagrama.procesarDatos();
        return diagrama.getPorcentajes();
    }

    public List<String> obtenerFechasGraficoLineas() {
        GraficoLinea grafico = (GraficoLinea) reporteFactory.crearReporteVisual("LINEAS", "Ventas por Fecha");
        grafico.procesarDatos();
        return grafico.getFechas();
    }

    public List<Double> obtenerMontosGraficoLineas() {
        GraficoLinea grafico = (GraficoLinea) reporteFactory.crearReporteVisual("LINEAS", "Ventas por Fecha");
        grafico.procesarDatos();
        return grafico.getMontosPorFecha();
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}