
package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Vista de reportes. Solo maneja los componentes gráficos;
 * la construcción y exportación del reporte se delegan en ControlGeneral.
 */
public class ReportesController {

    private final ControlGeneral control = new ControlGeneral();

    @FXML private Label mensajeEstado;
    @FXML private TextArea areaReporte;

    @FXML
    private void initialize() {
        generarReporte();
    }

    @FXML
    private void generarReporte() {
        areaReporte.setText(control.construirReporte());
        actualizarMensaje("Reporte actualizado correctamente.");
    }

    @FXML
    private void exportarTxt() {
        try {
            control.exportarReporteTxt();
            actualizarMensaje("Se solicitó la exportación del reporte TXT.");

        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar TXT", obtenerMensajeError(e));
        }
    }

    @FXML
    private void exportarPdf() {
        try {
            control.exportarReportePdf();
            actualizarMensaje("Se solicitó la exportación del reporte PDF.");

        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar PDF", obtenerMensajeError(e));
        }
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
