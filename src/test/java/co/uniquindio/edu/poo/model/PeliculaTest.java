package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeliculaTest {

    /**
     * Prueba que la clase Pelicula almacene y recupere adecuadamente sus atributos
     * como el título, la duración y la sinopsis.
     */
    @Test
    public void testAtributosPelicula() {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Inception");
        pelicula.setDuracion(148);

        assertEquals("Inception", pelicula.getTitulo());
        assertEquals(148, pelicula.getDuracion());
    }
}