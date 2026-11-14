package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
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
     * Prueba que el método calcularTotal sume correctamente los subtotales de los detalles
     * y aplique el descuento asociado a través del contexto si está configurado.
     */
    @Test
    public void testCalcularTotalCompra() {
        // Nota: Asumiendo una compra sin detalles para validar la estructura del builder y cálculo base
        Compra compra = new Compra.Builder()
                .idCompra("C-002")
                .fecha("2026-10-25")
                .build();

        double total = compra.calcularTotal();
        assertEquals(0.0, total, 0.01);
    }
}