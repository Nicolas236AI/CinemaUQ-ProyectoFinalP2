package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private Cinema cinema;

    @BeforeEach
    public void setUp() {
        cinema = Cinema.getInstancia();
        cinema.getListaClientes().clear();
        cinema.getListaFunciones().clear();
        cinema.getListaCompras().clear();
        cinema.getSolicitudesRecarga().clear();
    }

    /**
     * Prueba que los constructores de Cliente inicialicen correctamente sus atributos,
     * su tarjeta virtual activa y su cuenta de puntos en cero.
     */
    @Test
    public void testCreacionCliente() {
        Cliente cliente = new Cliente("12345", "Carlos Pérez", "carlos@mail.com", cinema);

        assertNotNull(cliente);
        assertEquals("12345", cliente.getIdCliente());
        assertEquals("Carlos Pérez", cliente.getNombre());
        assertEquals("carlos@mail.com", cliente.getCorreo());
        assertEquals("1234", cliente.getContrasena());
        assertNotNull(cliente.getTarjeta());
        assertNotNull(cliente.getCuentaPuntos());
        assertEquals(0.0, cliente.consultarSaldoTarjeta(), 0.01);
        assertEquals(0, cliente.consultarPuntos());
    }

    /**
     * Prueba que validarDatosRegistro verifique campos obligatorios, formato de correo y longitud de contraseña.
     */
    @Test
    public void testValidarDatosRegistro() {
        Cliente valido = new Cliente("C-01", "Laura Gómez", "laura@mail.com", "abcd", cinema);
        assertDoesNotThrow(valido::validarDatosRegistro);

        Cliente correoInvalido = new Cliente("C-02", "Pedro", "pedromail.com", "abcd", cinema);
        assertThrows(IllegalArgumentException.class, correoInvalido::validarDatosRegistro);

        Cliente claveCorta = new Cliente("C-03", "Ana", "ana@mail.com", "12", cinema);
        assertThrows(IllegalArgumentException.class, claveCorta::validarDatosRegistro);
    }

    /**
     * Prueba que el método autenticar valide correo (sin distinguir mayúsculas) y contraseña exacta.
     */
    @Test
    public void testAutenticarCliente() {
        Cliente cliente = new Cliente("C-01", "Carlos Pérez", "carlos@mail.com", "segura99", cinema);

        assertTrue(cliente.autenticar("CARLOS@MAIL.COM", "segura99"));
        assertFalse(cliente.autenticar("carlos@mail.com", "otra"));
        assertFalse(cliente.autenticar("otro@mail.com", "segura99"));
    }

    /**
     * Prueba que consultarCartelera retorne las funciones programadas en el cine asociado.
     */
    @Test
    public void testConsultarCarteleraCliente() {
        Cliente cliente = new Cliente("C-01", "Carlos Pérez", "carlos@mail.com", cinema);
        assertNotNull(cliente.consultarCartelera());

        Cliente sinCine = new Cliente("C-02", "Sin Cine", "sin@mail.com", null);
        assertThrows(IllegalStateException.class, sinCine::consultarCartelera);
    }

    /**
     * Prueba la solicitud de recarga, consulta de saldo y consulta de movimientos de la tarjeta virtual.
     */
    @Test
    public void testRecargaSaldoYMovimientosTarjeta() {
        Cliente cliente = new Cliente("C-01", "Carlos Pérez", "carlos@mail.com", cinema);
        assertThrows(IllegalArgumentException.class, () -> cliente.solicitarRecargaTarjeta(-5000.0));

        Recarga solicitud = cliente.solicitarRecargaTarjeta(80000.0);
        assertNotNull(solicitud);
        solicitud.ejecutarRecarga();

        assertEquals(80000.0, cliente.consultarSaldoTarjeta(), 0.01);
        assertFalse(cliente.consultarMovimientosTarjeta().isEmpty());
    }

    /**
     * Prueba que realizarCompra y solicitarCancelacionCompra debiten el saldo, acumulen puntos
     * y reembolsen correctamente al cancelar con más de 24 horas de anticipación.
     */
    @Test
    public void testRealizarYSolicitarCancelacionCompra() {
        Cliente cliente = new Cliente("C-01", "Carlos Pérez", "carlos@mail.com", cinema);
        cliente.getTarjeta().acreditarSaldo(100000.0);

        Pelicula pelicula = new Pelicula("P-01", "Interstellar", 169, "Sci-Fi");
        Sala sala = new Sala(1, 1, 10, cinema);
        sala.configurarAsientos();
        Funcion funcion = new Funcion("F-01", LocalDateTime.now().plusDays(3), 20000.0, pelicula, sala, cinema);
        AsientoFuncionActual af = funcion.seleccionarAsiento("S1-A1");

        IBoleto boleto = new BoletoBase(20000.0, "Entrada Interstellar", af);
        Compra compra = new Compra.Builder()
                .idCompra("CMP-100")
                .fecha("2026-11-18")
                .cliente(cliente)
                .build();
        DetalleCompra detalle = new DetalleCompra(1, 1, 20000.0, af, null, boleto, compra);
        compra.getListaDetalles().add(detalle);

        assertTrue(cliente.realizarCompra(compra));
        assertEquals(80000.0, cliente.consultarSaldoTarjeta(), 0.01);
        assertEquals(200, cliente.consultarPuntos());

        double reembolsado = cliente.solicitarCancelacionCompra("CMP-100");
        assertEquals(20000.0, reembolsado, 0.01);
        assertEquals(100000.0, cliente.consultarSaldoTarjeta(), 0.01);
        assertThrows(IllegalArgumentException.class, () -> cliente.solicitarCancelacionCompra("NO-EXISTE"));
    }

    /**
     * Prueba la consulta de puntos vigentes y la redención por beneficios del catálogo.
     */
    @Test
    public void testConsultarYRedimirPuntosPorBeneficio() {
        Cliente cliente = new Cliente("C-01", "Carlos Pérez", "carlos@mail.com", cinema);
        cliente.getCuentaPuntos().acumularPuntos(600);

        assertEquals(600, cliente.consultarPuntos());
        assertDoesNotThrow(() -> cliente.redimirPuntosPorBeneficio("ENTRADA"));
        assertEquals(100, cliente.consultarPuntos());
        assertThrows(IllegalStateException.class, () -> cliente.redimirPuntosPorBeneficio("COMBO"));
    }

    /**
     * Prueba que los métodos actualizar del patrón Observer almacenen las notificaciones recibidas en el cliente.
     */
    @Test
    public void testActualizarObserverCliente() {
        Cliente cliente = new Cliente("12345", "Carlos Pérez", "carlos@mail.com", cinema);

        cliente.actualizar("Nueva película en cartelera");
        cliente.actualizar();

        assertEquals(2, cliente.getNotificaciones().size());
        assertEquals("Nueva película en cartelera", cliente.getNotificaciones().get(0));
    }
}