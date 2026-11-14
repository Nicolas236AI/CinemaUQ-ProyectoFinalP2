package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    /**
     * Prueba que el constructor de la clase Cliente inicialice correctamente
     * sus atributos principales como identificación, nombre y correo.
     */
    @Test
    public void testCreacionCliente() {
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("12345", "Carlos Pérez", "carlos@mail.com", cinema);

        assertNotNull(cliente);
        assertEquals("12345", cliente.getIdCliente());
        assertEquals("Carlos Pérez", cliente.getNombre());
        assertEquals("carlos@mail.com", cliente.getCorreo());
    }

    /**
     * Prueba que el método de actualización del patrón Observer en la clase Cliente
     * procese y reciba las notificaciones correctamente sin errores.
     */
    @Test
    public void testActualizarObserverCliente() {
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("12345", "Carlos Pérez", "carlos@mail.com", cinema);

        assertDoesNotThrow(() -> cliente.actualizar("Nueva película en cartelera"));
    }
}