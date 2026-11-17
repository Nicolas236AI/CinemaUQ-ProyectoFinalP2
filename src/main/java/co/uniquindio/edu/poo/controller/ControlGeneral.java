package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.model.AdaptadorPdf;
import co.uniquindio.edu.poo.model.AdaptadorTxt;
import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Compra;
import co.uniquindio.edu.poo.model.EstadoCompra;
import co.uniquindio.edu.poo.model.Funcion;
import co.uniquindio.edu.poo.model.Pelicula;
import co.uniquindio.edu.poo.model.Sala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador general de la aplicación (capa Controller del MVC).
 * Concentra toda la lógica de negocio que usan las vistas JavaFX:
 * validaciones, reglas del cine y operaciones sobre el modelo.
 * Las vistas no deben acceder directamente a Cinema; lo hacen a través
 * de esta clase. No depende de ningún componente de JavaFX.
 *
 * Ante datos inválidos lanza IllegalArgumentException con un mensaje
 * apto para mostrar al usuario.
 */
public class ControlGeneral {

    private static final String FORMATO_FECHA_TEXTO = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(FORMATO_FECHA_TEXTO);
    private static final String REGEX_CORREO = "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";

    private final Cinema cinema = Cinema.getInstancia();

    // =====================================================
    // FECHAS
    // =====================================================

    /**
     * Convierte un texto con formato yyyy-MM-dd HH:mm en fecha y hora.
     *
     * @throws java.time.format.DateTimeParseException si el texto no cumple el formato.
     */
    public LocalDateTime parsearFecha(String texto) {
        return LocalDateTime.parse(texto == null ? "" : texto.trim(), FORMATO_FECHA);
    }

    public String formatearFecha(LocalDateTime fecha) {
        return fecha.format(FORMATO_FECHA);
    }

    // =====================================================
    // PELÍCULAS
    // =====================================================

    public List<Pelicula> obtenerPeliculas() {
        return cinema.getListaPeliculas();
    }

    public void registrarPelicula(String id, String titulo, int duracion, String genero) {
        validarDatosPelicula(id, titulo, duracion, genero);

        if (existeIdPelicula(id, null)) {
            throw new IllegalArgumentException("Ya existe una película con ese identificador.");
        }

        cinema.registrarPelicula(new Pelicula(id, titulo, duracion, genero));
    }

    public void actualizarPelicula(Pelicula pelicula, String id, String titulo,
                                   int duracion, String genero) {
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
        return cinema.getListaPeliculas().stream()
                .anyMatch(p -> p != excluida
                        && p.getIdPelicula() != null
                        && p.getIdPelicula().equalsIgnoreCase(id));
    }

    // =====================================================
    // FUNCIONES
    // =====================================================

    public List<Funcion> obtenerFunciones() {
        return cinema.getListaFunciones();
    }

    public void registrarFuncion(String id, LocalDateTime fecha, double precio,
                                 Pelicula pelicula, Sala sala) {
        validarDatosFuncion(id, fecha, precio, pelicula, sala);

        if (existeIdFuncion(id, null)) {
            throw new IllegalArgumentException("Ya existe una función con ese ID.");
        }

        cinema.registrarFuncion(new Funcion(id, fecha, precio, pelicula, sala));
    }

    public void actualizarFuncion(Funcion funcion, String id, LocalDateTime fecha,
                                  double precio, Pelicula pelicula, Sala sala) {
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

    public void eliminarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("Selecciona una función para eliminar.");
        }
        cinema.getListaFunciones().remove(funcion);
    }

    private void validarDatosFuncion(String id, LocalDateTime fecha, double precio,
                                     Pelicula pelicula, Sala sala) {
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
        return cinema.getListaFunciones().stream()
                .anyMatch(f -> f != excluida
                        && f.getIdFuncion() != null
                        && f.getIdFuncion().equalsIgnoreCase(id));
    }

    // =====================================================
    // SALAS
    // =====================================================

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
        nueva.configurarAsientos();
        cinema.getListaSalas().add(nueva);
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
            throw new IllegalArgumentException(
                    "No puedes cambiar la capacidad de una sala que tiene funciones programadas.");
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
            throw new IllegalArgumentException(
                    "No puedes eliminar esta sala porque tiene funciones asociadas.");
        }
        cinema.getListaSalas().remove(sala);
    }

    private void validarDatosSala(int id, int numero, int capacidad) {
        if (id <= 0 || numero <= 0 || capacidad <= 0) {
            throw new IllegalArgumentException(
                    "El ID, número y capacidad deben ser mayores que cero.");
        }
    }

    private boolean existeIdSala(int id, Sala excluida) {
        return cinema.getListaSalas().stream()
                .anyMatch(s -> s != excluida && s.getIdSala() == id);
    }

    private boolean existeNumeroSala(int numero, Sala excluida) {
        return cinema.getListaSalas().stream()
                .anyMatch(s -> s != excluida && s.getNumeroSala() == numero);
    }

    private boolean tieneFuncionesAsociadas(Sala sala) {
        return cinema.getListaFunciones().stream()
                .anyMatch(f -> f.getSala() == sala);
    }

    // =====================================================
    // CLIENTES
    // =====================================================

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
            throw new IllegalArgumentException(
                    "No puedes eliminar este cliente porque tiene compras registradas.");
        }
        cinema.getListaClientes().remove(cliente);
    }

    private void validarDatosCliente(String id, String nombre, String correo) {
        if (estaVacio(id) || estaVacio(nombre) || estaVacio(correo)) {
            throw new IllegalArgumentException("Debes completar todos los campos del cliente.");
        }
        if (!correo.matches(REGEX_CORREO)) {
            throw new IllegalArgumentException("Introduce una dirección de correo válida.");
        }
    }

    private boolean existeIdCliente(String id, Cliente excluido) {
        return cinema.getListaClientes().stream()
                .anyMatch(c -> c != excluido
                        && c.getIdCliente() != null
                        && c.getIdCliente().equalsIgnoreCase(id));
    }

    // =====================================================
    // COMPRAS
    // =====================================================

    public List<Compra> obtenerTodasLasCompras() {
        List<Compra> resultado = new ArrayList<>();

        for (Cliente cliente : cinema.getListaClientes()) {
            if (cliente.getListaCompras() != null) {
                resultado.addAll(cliente.getListaCompras());
            }
        }

        return resultado;
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

        if (cliente.getListaCompras() == null) {
            cliente.setListaCompras(new ArrayList<>());
        }

        cliente.getListaCompras().add(compra);
    }

    public void actualizarCompra(Compra compra, String id, String fecha,
                                 double total, Cliente nuevoCliente) {
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
            if (nuevoCliente.getListaCompras() == null) {
                nuevoCliente.setListaCompras(new ArrayList<>());
            }
            nuevoCliente.getListaCompras().add(compra);
        }

        compra.setIdCompra(id);
        compra.setFecha(fecha.trim());
        compra.setTotal(total);
        compra.setCliente(nuevoCliente);
    }

    public void cancelarCompra(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Selecciona una compra para cancelar.");
        }
        if (compra.getEstado() == EstadoCompra.CANCELADA) {
            throw new IllegalArgumentException("Esta compra ya está cancelada.");
        }
        compra.setEstado(EstadoCompra.CANCELADA);
    }

    private void validarDatosCompra(String id, String fecha, double total, Cliente cliente) {
        if (estaVacio(id)) {
            throw new IllegalArgumentException("El identificador de la compra es obligatorio.");
        }
        if (estaVacio(fecha)) {
            throw new IllegalArgumentException("La fecha de la compra es obligatoria.");
        }
        parsearFecha(fecha); // lanza DateTimeParseException si el formato es inválido
        if (!Double.isFinite(total) || total <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor que cero.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Selecciona el cliente de la compra.");
        }
    }

    private boolean existeIdCompra(String id, Compra excluida) {
        return obtenerTodasLasCompras().stream()
                .anyMatch(c -> c != excluida
                        && c.getIdCompra() != null
                        && c.getIdCompra().equalsIgnoreCase(id));
    }

    // =====================================================
    // REPORTES
    // =====================================================

    /**
     * Construye el texto del reporte operativo con la información actual del cine.
     */
    public String construirReporte() {

        List<Compra> compras = obtenerTodasLasCompras();

        int pendientes = 0;
        int completadas = 0;
        int canceladas = 0;
        double ingresos = 0;

        for (Compra compra : compras) {
            if (compra.getEstado() == EstadoCompra.PENDIENTE) {
                pendientes++;
            } else if (compra.getEstado() == EstadoCompra.COMPLETADA) {
                completadas++;
                ingresos += compra.getTotal();
            } else if (compra.getEstado() == EstadoCompra.CANCELADA) {
                canceladas++;
            }
        }

        String fechaGeneracion = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder reporte = new StringBuilder();

        reporte.append("========================================\n");
        reporte.append("             CINEMA UQ\n");
        reporte.append("          REPORTE OPERATIVO\n");
        reporte.append("========================================\n\n");

        reporte.append("Fecha de generación: ").append(fechaGeneracion).append("\n\n");

        reporte.append("--- INFORMACIÓN GENERAL ---\n");
        reporte.append("Películas registradas: ").append(cinema.getListaPeliculas().size()).append("\n");
        reporte.append("Funciones programadas: ").append(cinema.getListaFunciones().size()).append("\n");
        reporte.append("Salas registradas: ").append(cinema.getListaSalas().size()).append("\n");
        reporte.append("Clientes registrados: ").append(cinema.getListaClientes().size()).append("\n");

        reporte.append("\n--- RESUMEN DE COMPRAS ---\n");
        reporte.append("Total de compras: ").append(compras.size()).append("\n");
        reporte.append("Compras pendientes: ").append(pendientes).append("\n");
        reporte.append("Compras completadas: ").append(completadas).append("\n");
        reporte.append("Compras canceladas: ").append(canceladas).append("\n");

        reporte.append("\n--- INFORMACIÓN FINANCIERA ---\n");
        reporte.append("Ingresos de compras completadas: $")
                .append(String.format("%.2f", ingresos)).append("\n");

        reporte.append("\n--- DETALLE DE COMPRAS ---\n");

        if (compras.isEmpty()) {
            reporte.append("No hay compras registradas.\n");
        } else {
            for (Compra compra : compras) {
                reporte.append("\nID: ").append(compra.getIdCompra());
                reporte.append("\nFecha: ").append(compra.getFecha());
                reporte.append("\nCliente: ");

                if (compra.getCliente() != null) {
                    reporte.append(compra.getCliente().getNombre());
                } else {
                    reporte.append("Sin cliente");
                }

                reporte.append("\nTotal: $").append(String.format("%.2f", compra.getTotal()));
                reporte.append("\nEstado: ").append(compra.getEstado());
                reporte.append("\n------------------------------\n");
            }
        }

        reporte.append("\n========================================\n");
        reporte.append("       FIN DEL REPORTE OPERATIVO\n");
        reporte.append("========================================\n");

        return reporte.toString();
    }

    public void exportarReporteTxt() {
        new AdaptadorTxt().exportar(construirReporte());
    }

    public void exportarReportePdf() {
        new AdaptadorPdf().exportar(construirReporte());
    }

    // =====================================================
    // AUXILIARES
    // =====================================================

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}