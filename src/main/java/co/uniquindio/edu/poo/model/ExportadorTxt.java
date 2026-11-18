package co.uniquindio.edu.poo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Se encarga de exportar la información de los reportes
 * mediante archivos de texto con formato TXT.
 */
public class ExportadorTxt {

    /**
     * Escribe la información recibida en un archivo llamado reporte.txt.
     * También controla posibles errores durante la escritura.
     *
     * @param datos contenido que se desea exportar.
     */
    public void exportarTxt(String datos) {
        try {
            Path ruta = Path.of("reporte.txt");
            Files.writeString(ruta, datos);
            System.out.println("Reporte TXT exportado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al exportar el reporte TXT: " + e.getMessage());
        }
    }

}