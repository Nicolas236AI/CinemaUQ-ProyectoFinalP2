package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObserverClienteTest {

    /**
     * Prueba que se puedan agregar observadores correctamente al notificador
     * evitando duplicados o valores nulos.
     */
    @Test
    public void testAgregarObservador() {
        NotificadorCentro notificador = new NotificadorCentro();
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("C001", "María Gómez", "maria@mail.com", cinema);

        notificador.agregarObservador(cliente);
        assertEquals(1, notificador.getObservadores().size());

        notificador.agregarObservador(cliente);
        assertEquals(1, notificador.getObservadores().size());

        notificador.agregarObservador(null);
        assertEquals(1, notificador.getObservadores().size());
    }

    /**
     * Prueba que un observador pueda ser eliminado exitosamente de la lista del notificador.
     */
    @Test
    public void testEliminarObservador() {
        NotificadorCentro notificador = new NotificadorCentro();
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente = new Cliente("C002", "Carlos Ruiz", "carlos@mail.com", cinema);

        notificador.agregarObservador(cliente);
        assertEquals(1, notificador.getObservadores().size());

        notificador.eliminarObservador(cliente);
        assertEquals(0, notificador.getObservadores().size());
    }

    /**
     * Prueba que el envío de notificaciones a los clientes registrados se ejecute
     * correctamente sin lanzar excepciones.
     */
    @Test
    public void testNotificarClientes() {
        NotificadorCentro notificador = new NotificadorCentro();
        Cinema cinema = Cinema.getInstancia();
        Cliente cliente1 = new Cliente("C001", "Ana", "ana@mail.com", cinema);
        Cliente cliente2 = new Cliente("C002", "Pedro", "pedro@mail.com", cinema);

        notificador.agregarObservador(cliente1);
        notificador.agregarObservador(cliente2);

        assertDoesNotThrow(() -> notificador.notificarClientes("Nueva función disponible en cartelera"));
    }
}