package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CinemaTest {

    /**
     * Prueba que el método getInstancia devuelva una instancia única de Cinema
     * cumpliendo con el patrón Singleton (comprobando que dos llamadas apunten al mismo objeto en memoria).
     */
    @Test
    public void testInstanciaUnicaSingleton() {
        Cinema cinema1 = Cinema.getInstancia();
        Cinema cinema2 = Cinema.getInstancia();

        assertNotNull(cinema1);
        assertNotNull(cinema2);
        assertSame(cinema1, cinema2, "Las instancias del Singleton deben ser exactamente la misma en memoria.");
    }

    /**
     * Prueba que la instancia única de Cinema cargue y mantenga correctamente
     * sus atributos principales como el ID y el nombre por defecto.
     */
    @Test
    public void testAtributosCinemaSingleton() {
        Cinema cinema = Cinema.getInstancia();

        assertEquals("C001", cinema.getId());
        assertEquals("CinemaUQ", cinema.getNombre());
        assertNotNull(cinema.getListaClientes());
        assertNotNull(cinema.getListaPeliculas());
        assertNotNull(cinema.getListaFunciones());
    }

    /**
     * Prueba que el método registrarPelicula agregue la película a la lista
     * de forma correcta y evite registrar elementos nulos o duplicados.
     */
    @Test
    public void testRegistrarPelicula() {
        Cinema cinema = Cinema.getInstancia();
        cinema.getListaPeliculas().clear(); // Limpiar lista para la prueba

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Matrix");

        cinema.registrarPelicula(pelicula);
        assertEquals(1, cinema.getListaPeliculas().size());

        // Intentar registrar la misma película duplicada no debería incrementar el tamaño
        cinema.registrarPelicula(pelicula);
        assertEquals(1, cinema.getListaPeliculas().size());

        // Intentar registrar un valor nulo no debería aceptarse
        cinema.registrarPelicula(null);
        assertEquals(1, cinema.getListaPeliculas().size());
    }

    /**
     * Prueba que el método registrarCliente agregue exitosamente un cliente
     * a la lista del cine previniendo duplicados y nulos.
     */
    @Test
    public void testRegistrarCliente() {
        Cinema cinema = Cinema.getInstancia();
        cinema.getListaClientes().clear();

        Cliente cliente = new Cliente("123", "Laura", "laura@mail.com", cinema);

        cinema.registrarCliente(cliente);
        assertEquals(1, cinema.getListaClientes().size());

        // Cliente duplicado
        cinema.registrarCliente(cliente);
        assertEquals(1, cinema.getListaClientes().size());

        // Cliente nulo
        cinema.registrarCliente(null);
        assertEquals(1, cinema.getListaClientes().size());
    }

    /**
     * Prueba que el método registrarFuncion almacene adecuadamente las funciones
     * en la lista correspondiente del cine.
     */
    @Test
    public void testRegistrarFuncion() {
        Cinema cinema = Cinema.getInstancia();
        cinema.getListaFunciones().clear();

        Funcion funcion = new Funcion();
        funcion.setIdFuncion("F-01");

        cinema.registrarFuncion(funcion);
        assertEquals(1, cinema.getListaFunciones().size());

        // Función duplicada
        cinema.registrarFuncion(funcion);
        assertEquals(1, cinema.getListaFunciones().size());

        // Función nula
        cinema.registrarFuncion(null);
        assertEquals(1, cinema.getListaFunciones().size());
    }

    /**
     * Prueba que los métodos de ejecución visual del sistema (InicializarSistema y consultarCartelera)
     * se ejecuten de manera correcta sin lanzar excepciones.
     */
    @Test
    public void testMetodosEjecucionSistema() {
        Cinema cinema = Cinema.getInstancia();

        assertDoesNotThrow(() -> cinema.InicializarSistema());
        assertDoesNotThrow(() -> cinema.consultarCartelera());
        assertNotNull(cinema.toString());
    }
}