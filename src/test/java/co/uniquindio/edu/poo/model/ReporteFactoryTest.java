package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReporteFactoryTest {

    private final IReporteFactory fabrica = new ReporteFactory();

    /**
     * Prueba que la fábrica cree reportes visuales del tipo solicitado.
     */
    @Test
    public void testCrearReportesVisuales() {
        assertInstanceOf(DiagramaBarra.class, fabrica.crearReporteVisual("BARRAS", "Ventas"));
        assertInstanceOf(DiagramaPastel.class, fabrica.crearReporteVisual("pastel", "Ocupación"));
        assertInstanceOf(GraficoLinea.class, fabrica.crearReporteVisual("LINEAS", "Recargas"));
    }

    /**
     * Prueba que la fábrica cree reportes exportables y rechace formatos inválidos.
     */
    @Test
    public void testCrearReportesExportables() {
        ReporteExportable r = fabrica.crearReporteExportable("Ventas", "TXT");
        assertEquals("Ventas", r.getNombre());
        assertNotNull(fabrica.crearReporteExportable("Ventas", "PDF"));
        assertThrows(IllegalArgumentException.class, () -> fabrica.crearReporteExportable("Ventas", "XLS"));
        assertThrows(IllegalArgumentException.class, () -> fabrica.crearReporteVisual("DONA", "X"));
    }

    /**
     * Prueba que el gráfico de líneas genere su reporte sin errores.
     */
    @Test
    public void testGraficoLineaGenerarReporte() {
        GraficoLinea g = new GraficoLinea(800, 600, "Ventas por mes");
        assertDoesNotThrow(g::generarReporte);
        assertEquals("Ventas por mes", g.getTituloGrafico());
    }
}