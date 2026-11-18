package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class CinemaTest {

    private Cinema cinema;

    @BeforeEach
    public void setUp() {
        cinema = Cinema.getInstancia();
        cinema.getListaPeliculas().clear();
        cinema.getListaClientes().clear();
        cinema.getListaFunciones().clear();
        cinema.getListaSalas().clear();
        cinema.getListaConfiteria().clear();
        cinema.getListaCompras().clear();
        cinema.getSolicitudesRecarga().clear();
        cinema.getPromocionesActivas().clear();
        cinema.setNotificadorCentro(new NotificadorCentro());
    }

    /**
     * Prueba que el método getInstancia devuelva una única instancia de Cinema
     * cumpliendo con el patrón Singleton.
     */
    @Test
    public void testInstanciaUnicaSingleton() {
        Cinema cinema1 = Cinema.getInstancia();
        Cinema cinema2 = Cinema.getInstancia();

        assertNotNull(cinema1);
        assertNotNull(cinema2);
        assertSame(cinema1, cinema2);
    }

    /**
     * Prueba que la instancia única de Cinema mantenga sus atributos principales y listas inicializadas.
     */
    @Test
    public void testAtributosCinemaSingleton() {
        assertEquals("C001", cinema.getId());
        assertEquals("CinemaUQ", cinema.getNombre());
        assertNotNull(cinema.getListaClientes());
        assertNotNull(cinema.getListaPeliculas());
        assertNotNull(cinema.getListaFunciones());
    }

    /**
     * Prueba que el método registrarPelicula agregue la película correctamente
     * y lance excepción ante elementos nulos o identificadores duplicados.
     */
    @Test
    public void testRegistrarPelicula() {
        Pelicula pelicula = new Pelicula("P-01", "Matrix", 136, "Ciencia Ficción");

        cinema.registrarPelicula(pelicula);
        assertEquals(1, cinema.getListaPeliculas().size());

        assertThrows(IllegalArgumentException.class, () -> cinema.registrarPelicula(pelicula));
        assertThrows(IllegalArgumentException.class, () -> cinema.registrarPelicula(null));
    }

    /**
     * Prueba que registrarCliente y registrarNuevoCliente incorporen clientes válidos,
     * los suscriban al notificador y rechacen correos duplicados o nulos.
     */
    @Test
    public void testRegistrarClienteYNuevoCliente() {
        Cliente cliente = new Cliente("123", "Laura", "laura@mail.com", "1234", cinema);

        cinema.registrarCliente(cliente);
        assertEquals(1, cinema.getListaClientes().size());
        assertTrue(cinema.getNotificadorCentro().getObservadores().contains(cliente));

        assertThrows(IllegalArgumentException.class, () -> cinema.registrarCliente(cliente));
        assertThrows(IllegalArgumentException.class, () -> cinema.registrarCliente(null));

        Cliente nuevo = cinema.registrarNuevoCliente("124", "Andrés", "andres@mail.com", "5678");
        assertNotNull(nuevo);
        assertEquals(2, cinema.getListaClientes().size());
    }

    /**
     * Prueba que iniciarSesionCliente autentique correctamente por correo y contraseña.
     */
    @Test
    public void testIniciarSesionCliente() {
        cinema.registrarNuevoCliente("125", "Sofía", "sofia@mail.com", "clave123");

        assertNotNull(cinema.iniciarSesionCliente("sofia@mail.com", "clave123"));
        assertNull(cinema.iniciarSesionCliente("sofia@mail.com", "incorrecta"));
        assertNull(cinema.iniciarSesionCliente("inexistente@mail.com", "clave123"));
    }

    /**
     * Prueba que registrarFuncion almacene funciones válidas y evite cruces de horario en la misma sala.
     */
    @Test
    public void testRegistrarFuncionYConsultarCartelera() {
        Pelicula pelicula = new Pelicula("P-01", "Inception", 148, "Sci-Fi");
        Sala sala = new Sala(1, 1, 20, cinema);
        sala.configurarAsientos();
        LocalDateTime fecha = LocalDateTime.now().plusDays(1);

        Funcion funcion = new Funcion("F-01", fecha, 16000.0, pelicula, sala, cinema);
        cinema.registrarFuncion(funcion);

        assertEquals(1, cinema.consultarCartelera().size());
        assertThrows(IllegalArgumentException.class, () -> cinema.registrarFuncion(null));

        Funcion funcionCruzada = new Funcion("F-02", fecha, 18000.0, pelicula, sala, cinema);
        assertThrows(IllegalStateException.class, () -> cinema.registrarFuncion(funcionCruzada));
    }

    /**
     * Prueba el registro de elementos de confitería, solicitudes de recarga, compras y conteo de entradas por película.
     */
    @Test
    public void testConfiteriaRecargasComprasYConteoEntradas() {
        ProductoConfiteria crispetas = new ProductoConfiteria("PR-1", "Crispetas", 12000.0);
        cinema.registrarElementoConfiteria(crispetas);
        assertEquals(1, cinema.getListaConfiteria().size());
        assertThrows(IllegalArgumentException.class, () -> cinema.registrarElementoConfiteria(null));

        Cliente cliente = cinema.registrarNuevoCliente("C-10", "Mateo", "mateo@mail.com", "1234");
        Recarga solicitud = cinema.registrarSolicitudRecarga(cliente, 50000.0);
        assertEquals(1, cinema.getSolicitudesRecarga().size());
        solicitud.ejecutarRecarga();

        Pelicula pelicula = new Pelicula("P-10", "Dune", 155, "Sci-Fi");
        cinema.registrarPelicula(pelicula);
        Sala sala = new Sala(1, 1, 10, cinema);
        sala.configurarAsientos();
        Funcion funcion = new Funcion("F-10", LocalDateTime.now().plusDays(2), 15000.0, pelicula, sala, cinema);
        cinema.registrarFuncion(funcion);

        AsientoFuncionActual af = funcion.seleccionarAsiento("S1-A1");
        IBoleto boleto = new BoletoBase(15000.0, "Entrada Dune", af);
        Compra compra = new Compra.Builder().idCompra("CMP-10").fecha("2026-11-18").cliente(cliente).build();
        DetalleCompra det = new DetalleCompra(1, 1, 15000.0, af, null, boleto, compra);
        compra.getListaDetalles().add(det);

        assertTrue(cliente.realizarCompra(compra));
        assertEquals(1, cinema.getListaCompras().size());
        assertEquals(1, cinema.contarEntradasPorPelicula("P-10"));
    }

    /**
     * Prueba que InicializarSistema precargue películas, salas, funciones, confitería y el cliente demo sin errores.
     */
    @Test
    public void testInicializarSistema() {
        assertDoesNotThrow(() -> cinema.InicializarSistema());
        assertFalse(cinema.getListaPeliculas().isEmpty());
        assertFalse(cinema.getListaSalas().isEmpty());
        assertFalse(cinema.getListaFunciones().isEmpty());
        assertFalse(cinema.getListaConfiteria().isEmpty());
        assertFalse(cinema.getListaClientes().isEmpty());
    }
}