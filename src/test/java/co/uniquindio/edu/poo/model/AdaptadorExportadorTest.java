package co.uniquindio.edu.poo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdaptadorExportadorTest {

    /**
     * Prueba que el adaptador de texto (AdaptadorTxt) implemente correctamente
     * la interfaz IExportador y ejecute la exportación sin lanzar excepciones.
     */
    @Test
    public void testAdaptadorTxtExportar() {
        IExportador exportadorTxt = new AdaptadorTxt();
        assertNotNull(exportadorTxt);
        assertDoesNotThrow(() -> exportadorTxt.exportar("Contenido de prueba para TXT"));
    }

    /**
     * Prueba que el adaptador de PDF (AdaptadorPdf) implemente correctamente
     * la interfaz IExportador y ejecute la exportación de manera exitosa.
     */
    @Test
    public void testAdaptadorPdfExportar() {
        IExportador exportadorPdf = new AdaptadorPdf();
        assertNotNull(exportadorPdf);
        assertDoesNotThrow(() -> exportadorPdf.exportar("Contenido de prueba para PDF"));
    }
}