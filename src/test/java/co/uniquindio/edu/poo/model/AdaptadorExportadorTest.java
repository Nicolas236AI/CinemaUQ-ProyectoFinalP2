package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class AdaptadorExportadorTest {

    /**
     * Prueba que el adaptador de texto (AdaptadorTxt) implemente correctamente
     * la interfaz IExportador, ejecute la exportación sin lanzar excepciones
     * y genere el archivo reporte.txt con el contenido esperado.
     */
    @Test
    public void testAdaptadorTxtExportar() throws Exception {
        IExportador exportadorTxt = new AdaptadorTxt();
        assertNotNull(exportadorTxt);
        assertDoesNotThrow(() -> exportadorTxt.exportar("Contenido de prueba para TXT"));
        assertTrue(Files.exists(Path.of("reporte.txt")));
    }

    /**
     * Prueba que el adaptador de PDF (AdaptadorPdf) implemente correctamente
     * la interfaz IExportador, ejecute la exportación de manera exitosa
     * y genere el archivo reporte.pdf.
     */
    @Test
    public void testAdaptadorPdfExportar() throws Exception {
        IExportador exportadorPdf = new AdaptadorPdf();
        assertNotNull(exportadorPdf);
        assertDoesNotThrow(() -> exportadorPdf.exportar("Contenido de prueba para PDF"));
        assertTrue(Files.exists(Path.of("reporte.pdf")));
    }
}