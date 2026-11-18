package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class StrategyDescuentoTest {

    /**
     * Prueba que el cálculo del descuento para estudiantes se aplique correctamente utilizando el porcentaje configurado.
     */
    @Test
    public void testCalcularDescuentoEstudiante() {
        IEstrategiaDescuento estrategia = new DescuentoEstudiante(15.0);
        ContextoDescuento contexto = new ContextoDescuento(estrategia, null);

        double descuento = contexto.aplicarEstrategia(50000.0);

        assertEquals(7500.0, descuento, 0.01);
    }

    /**
     * Prueba que el cálculo del descuento por fidelidad se aplique correctamente basándose en el porcentaje configurado.
     */
    @Test
    public void testCalcularDescuentoFidelidad() {
        IEstrategiaDescuento estrategia = new DescuentoFidelidad(100, 20.0);
        ContextoDescuento contexto = new ContextoDescuento(estrategia, null);

        double descuento = contexto.aplicarEstrategia(60000.0);

        assertEquals(12000.0, descuento, 0.01);
    }

    /**
     * Prueba el cambio dinámico de estrategia de descuento dentro del contexto para verificar que se adapte correctamente.
     */
    @Test
    public void testCambioDinamicoDeEstrategia() {
        IEstrategiaDescuento estrategiaInicial = new DescuentoEstudiante(10.0);
        ContextoDescuento contexto = new ContextoDescuento(estrategiaInicial, null);

        assertEquals(5000.0, contexto.aplicarEstrategia(50000.0), 0.01);

        contexto.setEstrategia(new DescuentoFidelidad(150, 25.0));
        assertEquals(12500.0, contexto.aplicarEstrategia(50000.0), 0.01);
    }

    /**
     * Prueba la acumulación, vigencia a un año y redención FIFO en CuentaPuntos.
     */
    @Test
    public void testVigenciaYRedencionCuentaPuntos() {
        Cliente cliente = new Cliente("C-99", "Sofía", "sofia@mail.com", Cinema.getInstancia());
        CuentaPuntos cuenta = cliente.getCuentaPuntos();

        // Lote vencido (hace más de 1 año)
        cuenta.acumularPuntosConFecha(400, LocalDate.now().minusYears(1).minusDays(5));
        assertEquals(0, cuenta.getSaldoPuntos());

        // Lote vigente
        cuenta.acumularPuntosConFecha(300, LocalDate.now().minusMonths(2));
        assertEquals(300, cuenta.getSaldoPuntos());
        assertTrue(cuenta.redimirPuntos(250));
        assertEquals(50, cuenta.getSaldoPuntos());
    }
}