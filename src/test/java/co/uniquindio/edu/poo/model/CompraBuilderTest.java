package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class CompraBuilderTest {

    /**
     * Prueba que el patrón Builder construya correctamente la instancia de Compra
     * con todos sus atributos y el estado inicial asignados de forma adecuada.
     */
    @Test
    public void testConstruccionCompraConBuilder() {
        Compra compra = new Compra.Builder()
                .idCompra("C-001")
                .fecha("2026-10-25")
                .estado(EstadoCompra.PENDIENTE)
                .build();

        assertNotNull(compra);
        assertEquals("C-001", compra.getIdCompra());
        assertEquals("2026-10-25", compra.getFecha());
        assertEquals(EstadoCompra.PENDIENTE, compra.getEstado());
    }

    /**
     * Prueba que calcularTotal sume los detalles de boletos y confitería y aplique el descuento configurado.
     */
    @Test
    public void testCalcularTotalConDetallesYDescuento() {
        ProductoConfiteria combo = new ProductoConfiteria("PR-1", "Combo", 20000.0);
        Compra compra = new Compra.Builder()
                .idCompra("C-002")
                .fecha("2026-10-25")
                .build();

        DetalleCompra det = new DetalleCompra(1, 2, 0.0, null, combo, null, compra);
        compra.getListaDetalles().add(det);
        compra.setContextoDescuento(new ContextoDescuento(new DescuentoEstudiante(10.0), compra));

        assertEquals(36000.0, compra.calcularTotal(), 0.01);
    }

    /**
     * Prueba que validarLimiteEntradas rechace compras que superen el máximo de 6 boletos por transacción.
     */
    @Test
    public void testValidarLimiteEntradas() {
        Cliente cliente = new Cliente("C-01", "Ana", "ana@mail.com", Cinema.getInstancia());
        cliente.getTarjeta().acreditarSaldo(500000.0);

        Compra compra = new Compra.Builder().idCompra("C-003").fecha("2026-11-18").cliente(cliente).build();
        IBoleto boleto = new BoletoBase(15000.0, "Entrada", null);
        compra.getListaDetalles().add(new DetalleCompra(1, 7, 105000.0, null, null, boleto, compra));

        assertFalse(compra.validarLimiteEntradas());
        assertThrows(IllegalStateException.class, compra::procesarPago);
    }

    /**
     * Prueba procesarPago, ocupación del asiento, acumulación de puntos y cancelación con políticas de tiempo.
     */
    @Test
    public void testProcesarPagoYCancelarCompra() {
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("C-02", "Luis", "luis@mail.com", cinema);
        cliente.getTarjeta().acreditarSaldo(100000.0);

        Pelicula pelicula = new Pelicula("P-01", "Avatar", 160, "Aventura");
        Sala sala = new Sala(1, 1, 10, cinema);
        sala.configurarAsientos();
        LocalDateTime fechaFuncion = LocalDateTime.now().plusHours(10);
        Funcion funcion = new Funcion("F-01", fechaFuncion, 20000.0, pelicula, sala, cinema);
        AsientoFuncionActual af = funcion.seleccionarAsiento("S1-A1");

        Compra compra = new Compra.Builder().idCompra("C-004").fecha("2026-11-18").cliente(cliente).build();
        IBoleto boleto = new BoletoBase(20000.0, "Entrada Avatar", af);
        compra.getListaDetalles().add(new DetalleCompra(1, 1, 20000.0, af, null, boleto, compra));

        assertTrue(compra.procesarPago());
        assertEquals(EstadoCompra.COMPLETADA, compra.getEstado());
        assertEquals(EstadoAsiento.OCUPADO, af.getEstadoAsiento());
        assertEquals(fechaFuncion, compra.obtenerFechaFuncion());

        // Cancelación del cliente con menos de 24 horas de anticipación: reembolsa 80% ($16.000)
        double reembolso = compra.cancelarCompra(false);
        assertEquals(16000.0, reembolso, 0.01);
        assertTrue(compra.isReembolsada());
        assertEquals(EstadoAsiento.DISPONIBLE, af.getEstadoAsiento());

        // No permite volver a reembolsar una compra ya cancelada
        assertThrows(IllegalStateException.class, () -> compra.validarCancelacion(false));
    }
}