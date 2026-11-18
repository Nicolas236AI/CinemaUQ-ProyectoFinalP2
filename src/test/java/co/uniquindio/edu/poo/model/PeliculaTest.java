package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeliculaTest {

    /**
     * Prueba que la clase Pelicula almacene y recupere adecuadamente sus atributos
     * como el identificador, título, duración y género.
     */
    @Test
    public void testAtributosPelicula() {
        Pelicula pelicula = new Pelicula("PEL-01", "Inception", 148, "Ciencia Ficción");

        assertEquals("PEL-01", pelicula.getIdPelicula());
        assertEquals("Inception", pelicula.getTitulo());
        assertEquals(148, pelicula.getDuracion());
        assertEquals("Ciencia Ficción", pelicula.getGenero());
    }

    /**
     * Prueba que el método obtenerDetalles retorne la información formateada de la película.
     */
    @Test
    public void testObtenerDetallesPelicula() {
        Pelicula pelicula = new Pelicula("PEL-02", "Interstellar", 169, "Aventura");
        String detalles = pelicula.obtenerDetalles();

        assertNotNull(detalles);
        assertTrue(detalles.contains("Interstellar"));
        assertTrue(detalles.contains("Aventura"));
        assertTrue(detalles.contains("169"));
    }
}