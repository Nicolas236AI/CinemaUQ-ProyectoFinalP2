package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ReporteFactoryTest {

    private final IReporteFactory fabrica = new ReporteFactory();

    /**
     * Prueba que la fábrica cree reportes visuales del tipo solicitado (BARRAS, PASTEL, LINEAS)
     * y ejecute su Template Method generarReporte() procesando los datos del cine.
     */
    @Test
    public void testCrearYGenerarReportesVisuales() {
        ReporteVisual barras = fabrica.crearReporteVisual("BARRAS", "Entradas por Película");
        assertInstanceOf(DiagramaBarra.class, barras);
        assertDoesNotThrow(barras::generarReporte);
        assertNotNull(barras.obtenerDatos());

        ReporteVisual pastel = fabrica.crearReporteVisual("pastel", "Ocupación");
        assertInstanceOf(DiagramaPastel.class, pastel);
        assertDoesNotThrow(pastel::generarReporte);
        assertNotNull(pastel.obtenerDatos());

        ReporteVisual lineas = fabrica.crearReporteVisual("LINEAS", "Ventas por Fecha");
        assertInstanceOf(GraficoLinea.class, lineas);
        assertDoesNotThrow(lineas::generarReporte);
        assertNotNull(lineas.obtenerDatos());
    }

    /**
     * Prueba que la fábrica cree reportes exportables en TXT y PDF y rechace formatos o tipos inválidos.
     */
    @Test
    public void testCrearReportesExportablesYValidarErrores() {
        ReporteExportable rTxt = fabrica.crearReporteExportable("Ventas totales", "TXT");
        assertEquals("Ventas totales", rTxt.getNombre());
        assertEquals("TXT", rTxt.getFormato());
        assertDoesNotThrow(rTxt::generarReporte);

        ReporteExportable rPdf = fabrica.crearReporteExportable("Ocupación por sala", "PDF");
        assertEquals("PDF", rPdf.getFormato());
        assertDoesNotThrow(rPdf::generarReporte);

        assertThrows(IllegalArgumentException.class, () -> fabrica.crearReporteExportable("Ventas", "XLS"));
        assertThrows(IllegalArgumentException.class, () -> fabrica.crearReporteVisual("DONA", "X"));
    }

    /**
     * Prueba los distintos reportes operativos de ReporteExportable y el filtro por rango de fechas.
     */
    @Test
    public void testReportesOperativosConRangoFechas() {
        String[] tipos = {
                "Ventas totales",
                "Entradas vendidas por película",
                "Ocupación por sala",
                "Productos y combos vendidos",
                "Recargas de tarjetas",
                "Cancelaciones y reembolsos",
                "Puntos acumulados y vigentes"
        };

        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now().plusDays(1);

        for (String tipo : tipos) {
            ReporteExportable reporte = new ReporteExportable(tipo, "TXT", inicio, fin);
            reporte.procesarDatos();
            assertNotNull(reporte.obtenerDatos());
            assertTrue(reporte.obtenerDatos().contains("CINEMAUQ"));
        }

        ReporteExportable rangoInvalido = new ReporteExportable("Ventas totales", "TXT", fin, inicio);
        assertThrows(IllegalArgumentException.class, rangoInvalido::procesarDatos);
    }
}