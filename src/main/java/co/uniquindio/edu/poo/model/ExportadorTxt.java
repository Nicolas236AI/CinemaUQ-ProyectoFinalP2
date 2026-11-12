package co.uniquindio.edu.poo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorTxt {

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