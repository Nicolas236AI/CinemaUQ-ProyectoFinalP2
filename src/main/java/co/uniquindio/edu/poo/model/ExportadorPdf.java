package co.uniquindio.edu.poo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Se encarga de guardar la información de los reportes
 * destinada a la exportación en formato PDF.
 */
public class ExportadorPdf {

    /**
     * Escribe la información recibida en un archivo con extensión PDF
     * y controla posibles errores durante la escritura.
     *
     * @param datos contenido que se desea exportar.
     */
    public void exportarPdf(String datos) {
        try {
            Path ruta = Path.of("reporte.pdf");
            Files.writeString(ruta, datos);
            System.out.println("Reporte PDF exportado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al exportar el reporte PDF: " + e.getMessage());
        }
    }

}