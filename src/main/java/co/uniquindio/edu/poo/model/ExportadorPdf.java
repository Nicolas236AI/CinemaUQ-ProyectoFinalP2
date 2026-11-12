package co.uniquindio.edu.poo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorPdf {

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