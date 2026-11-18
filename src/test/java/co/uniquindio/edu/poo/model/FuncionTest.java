package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionTest {

    /**
     * Prueba que la clase Funcion asigne fecha/hora e inicialice la disponibilidad
     * de todos los asientos físicos de la sala asignada.
     */
    @Test
    public void testInicializacionYFechaHoraFuncion() {
        Cinema cinema = Cinema.getInstancia();
        Pelicula pelicula = new Pelicula("P-01", "Oppenheimer", 180, "Drama");
        Sala sala = new Sala(1, 1, 15, cinema);
        sala.configurarAsientos();
        LocalDateTime tiempoPrueba = LocalDateTime.of(2026, 12, 10, 18, 30);

        Funcion funcion = new Funcion("F-01", tiempoPrueba, 18000.0, pelicula, sala, cinema);

        assertEquals(tiempoPrueba, funcion.getFechaHora());
        assertEquals(15, funcion.getListaAsientosFuncionActual().size());
    }

    /**
     * Prueba que verificarFuncion valide todos los datos obligatorios antes de programar la función.
     */
    @Test
    public void testVerificarFuncion() {
        Funcion incompleta = new Funcion();
        assertThrows(IllegalArgumentException.class, incompleta::verificarFuncion);

        Sala sala = new Sala(1, 1, 10, Cinema.getInstancia());
        sala.configurarAsientos();
        Funcion valida = new Funcion("F-02", LocalDateTime.now().plusDays(1), 15000.0,
                new Pelicula("P-02", "Tenet", 150, "Acción"), sala);
        assertDoesNotThrow(valida::verificarFuncion);
    }

    /**
     * Prueba la búsqueda, bloqueo temporal (seleccionarAsiento) y rechazo de asientos ya seleccionados o inexistentes.
     */
    @Test
    public void testObtenerYSeleccionarAsiento() {
        Sala sala = new Sala(1, 1, 10, Cinema.getInstancia());
        sala.configurarAsientos();
        Funcion funcion = new Funcion("F-03", LocalDateTime.now().plusDays(1), 16000.0,
                new Pelicula("P-03", "Arrival", 116, "Sci-Fi"), sala);

        AsientoFuncionActual seleccionado = funcion.seleccionarAsiento("S1-A1");
        assertEquals(EstadoAsiento.SELECCIONADO, seleccionado.getEstadoAsiento());
        assertEquals(seleccionado, funcion.obtenerAsientoFuncion("S1-A1"));

        assertThrows(IllegalStateException.class, () -> funcion.seleccionarAsiento("S1-A1"));
        assertThrows(IllegalArgumentException.class, () -> funcion.obtenerAsientoFuncion("S1-Z99"));
    }

    /**
     * Prueba el cálculo de porcentaje de ocupación y el reembolso del 100% cuando el cine cancela la función.
     */
    @Test
    public void testPorcentajeOcupacionYCancelarFuncionPorCine() {
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("C-20", "Valeria", "valeria@mail.com", cinema);
        cliente.getTarjeta().acreditarSaldo(50000.0);

        Sala sala = new Sala(1, 1, 10, cinema);
        sala.configurarAsientos();
        Funcion funcion = new Funcion("F-04", LocalDateTime.now().plusDays(2), 20000.0,
                new Pelicula("P-04", "The Batman", 176, "Acción"), sala, cinema);

        AsientoFuncionActual af = funcion.seleccionarAsiento("S1-A2");
        Compra compra = new Compra.Builder().idCompra("CMP-20").fecha("2026-11-18").cliente(cliente).build();
        IBoleto boleto = new BoletoBase(20000.0, "Entrada Batman", af);
        DetalleCompra det = new DetalleCompra(1, 1, 20000.0, af, null, boleto, compra);
        af.setDetalleCompra(det);
        compra.getListaDetalles().add(det);

        assertTrue(cliente.realizarCompra(compra));
        assertEquals(10.0, funcion.calcularPorcentajeOcupacion(), 0.01);

        funcion.cancelarFuncionPorCine();
        assertEquals(EstadoCompra.CANCELADA, compra.getEstado());
        assertEquals(20000.0, compra.getMontoReembolsado(), 0.01);
        assertEquals(50000.0, cliente.consultarSaldoTarjeta(), 0.01);
    }
}