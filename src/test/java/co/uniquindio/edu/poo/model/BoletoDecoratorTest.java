package co.uniquindio.edu.poo.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoletoDecoratorTest {

    /**
     * Prueba que se calcule correctamente el costo y la descripción de un boleto base sin decoraciones adicionales.
     */
    @Test
    public void testBoletoBaseCalculoYDescripcion() {
        IBoleto boletoBase = new BoletoBase(20000.0, "Boleto General", null);

        assertEquals(20000.0, boletoBase.calcularCosto(), 0.01);
        assertEquals("Boleto General", boletoBase.obtenerDescripcion());
    }

    /**
     * Prueba el funcionamiento del patrón Decorator al agregar un costo y una descripción adicional al boleto base.
     */
    @Test
    public void testBoletoConAdicionalDecorador() {
        IBoleto boletoBase = new BoletoBase(20000.0, "Boleto General", null);
        IBoleto boletoDecorado = new BoletoConAdicional(boletoBase, 5000.0);

        assertEquals(25000.0, boletoDecorado.calcularCosto(), 0.01);
        assertEquals("Boleto General con servicio adicional", boletoDecorado.obtenerDescripcion());
    }
}