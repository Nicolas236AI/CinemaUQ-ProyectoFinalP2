package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionTest {

    /**
     * Prueba que la clase Funcion asigne y permita consultar de forma correcta
     * la fecha y hora de la función del cine.
     */
    @Test
    public void testAsignacionFechaHoraFuncion() {
        Funcion funcion = new Funcion();
        LocalDateTime tiempoPrueba = LocalDateTime.of(2026, 10, 25, 18, 30);
        funcion.setFechaHora(tiempoPrueba);

        assertEquals(tiempoPrueba, funcion.getFechaHora());
        assertNotNull(funcion);
    }
}