
package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.model.AdaptadorPdf;
import co.uniquindio.edu.poo.model.AdaptadorTxt;
import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Compra;
import co.uniquindio.edu.poo.model.EstadoCompra;
import co.uniquindio.edu.poo.model.Funcion;
import co.uniquindio.edu.poo.model.Pelicula;
import co.uniquindio.edu.poo.model.Sala;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportesController {

    private final Cinema cinema = Cinema.getInstancia();

    @FXML private Label mensajeEstado;
    @FXML private TextArea areaReporte;

    @FXML
    private void initialize() {
        generarReporte();
    }

    @FXML
    private void generarReporte() {

        String contenido = construirReporte();

        areaReporte.setText(contenido);
        actualizarMensaje("Reporte actualizado correctamente.");
    }

    @FXML
    private void exportarTxt() {

        try {
            String contenido = construirReporte();

            new AdaptadorTxt().exportar(contenido);

            actualizarMensaje(
                    "Se solicitó la exportación del reporte TXT.");

        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar TXT",
                    obtenerMensajeError(e));
        }
    }

    @FXML
    private void exportarPdf() {

        try {
            String contenido = construirReporte();

            new AdaptadorPdf().exportar(contenido);

            actualizarMensaje(
                    "Se solicitó la exportación del reporte PDF.");

        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar PDF",
                    obtenerMensajeError(e));
        }
    }

    private String construirReporte() {

        List<Compra> compras = obtenerTodasLasCompras();

        int peliculas = cinema.getListaPeliculas().size();
        int funciones = cinema.getListaFunciones().size();
        int salas = cinema.getListaSalas().size();
        int clientes = cinema.getListaClientes().size();

        int pendientes = 0;
        int completadas = 0;
        int canceladas = 0;

        double ingresos = 0;

        for (Compra compra : compras) {

            if (compra.getEstado() == EstadoCompra.PENDIENTE) {
                pendientes++;
            } else if (compra.getEstado() == EstadoCompra.COMPLETADA) {
                completadas++;
                ingresos += compra.getTotal();
            } else if (compra.getEstado() == EstadoCompra.CANCELADA) {
                canceladas++;
            }
        }

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String fechaGeneracion =
                LocalDateTime.now().format(formato);

        StringBuilder reporte = new StringBuilder();

        reporte.append("========================================\n");
        reporte.append("             CINEMA UQ\n");
        reporte.append("          REPORTE OPERATIVO\n");
        reporte.append("========================================\n\n");

        reporte.append("Fecha de generación: ")
                .append(fechaGeneracion)
                .append("\n\n");

        reporte.append("--- INFORMACIÓN GENERAL ---\n");
        reporte.append("Películas registradas: ")
                .append(peliculas).append("\n");

        reporte.append("Funciones programadas: ")
                .append(funciones).append("\n");

        reporte.append("Salas registradas: ")
                .append(salas).append("\n");

        reporte.append("Clientes registrados: ")
                .append(clientes).append("\n");

        reporte.append("\n--- RESUMEN DE COMPRAS ---\n");
        reporte.append("Total de compras: ")
                .append(compras.size()).append("\n");

        reporte.append("Compras pendientes: ")
                .append(pendientes).append("\n");

        reporte.append("Compras completadas: ")
                .append(completadas).append("\n");

        reporte.append("Compras canceladas: ")
                .append(canceladas).append("\n");

        reporte.append("\n--- INFORMACIÓN FINANCIERA ---\n");
        reporte.append("Ingresos de compras completadas: $")
                .append(String.format("%.2f", ingresos))
                .append("\n");

        reporte.append("\n--- DETALLE DE COMPRAS ---\n");

        if (compras.isEmpty()) {
            reporte.append("No hay compras registradas.\n");
        } else {
            for (Compra compra : compras) {

                reporte.append("\nID: ")
                        .append(compra.getIdCompra());

                reporte.append("\nFecha: ")
                        .append(compra.getFecha());

                reporte.append("\nCliente: ");

                if (compra.getCliente() != null) {
                    reporte.append(compra.getCliente().getNombre());
                } else {
                    reporte.append("Sin cliente");
                }

                reporte.append("\nTotal: $")
                        .append(String.format("%.2f", compra.getTotal()));

                reporte.append("\nEstado: ")
                        .append(compra.getEstado());

                reporte.append("\n------------------------------\n");
            }
        }

        reporte.append("\n========================================\n");
        reporte.append("       FIN DEL REPORTE OPERATIVO\n");
        reporte.append("========================================\n");

        return reporte.toString();
    }

    private List<Compra> obtenerTodasLasCompras() {

        List<Compra> resultado = new ArrayList<>();

        for (Cliente cliente : cinema.getListaClientes()) {

            if (cliente.getListaCompras() != null) {
                resultado.addAll(cliente.getListaCompras());
            }
        }

        return resultado;
    }

    private String obtenerMensajeError(RuntimeException e) {

        if (e.getMessage() == null || e.getMessage().isBlank()) {
            return "No fue posible exportar el reporte.";
        }

        return e.getMessage();
    }

    private void actualizarMensaje(String mensaje) {
        mensajeEstado.setText(mensaje);
    }

    private void mostrarAlerta(String titulo, String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
