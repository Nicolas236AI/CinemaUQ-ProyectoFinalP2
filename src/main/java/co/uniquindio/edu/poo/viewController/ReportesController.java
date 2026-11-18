package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class ReportesController {

    private final ControlGeneral control = new ControlGeneral();

    @FXML private ComboBox<String> comboTipoReporte;
    @FXML private DatePicker pickerFechaInicio;
    @FXML private DatePicker pickerFechaFin;
    @FXML private TextArea areaReporte;

    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private PieChart graficoPastel;
    @FXML private LineChart<String, Number> graficoLineas;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        comboTipoReporte.setItems(FXCollections.observableArrayList(
                "Ventas totales",
                "Entradas vendidas por película",
                "Ocupación de salas",
                "Productos y combos vendidos",
                "Recargas realizadas",
                "Cancelaciones y reembolsos",
                "Puntos acumulados y redimidos"
        ));
        comboTipoReporte.getSelectionModel().selectFirst();
        generarReporte();
        actualizarGraficos();
    }

    @FXML
    private void generarReporte() {
        try {
            String contenido = control.construirReportePorTipo(
                    comboTipoReporte.getValue(),
                    pickerFechaInicio.getValue(),
                    pickerFechaFin.getValue()
            );
            areaReporte.setText(contenido);
            actualizarMensaje("Reporte generado correctamente.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error en reporte", e.getMessage());
        }
    }

    @FXML
    private void exportarTxt() {
        try {
            control.exportarReportePorTipo(
                    comboTipoReporte.getValue(),
                    pickerFechaInicio.getValue(),
                    pickerFechaFin.getValue(),
                    "TXT"
            );
            actualizarMensaje("Reporte exportado a TXT correctamente.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar TXT", e.getMessage());
        }
    }

    @FXML
    private void exportarPdf() {
        try {
            control.exportarReportePorTipo(
                    comboTipoReporte.getValue(),
                    pickerFechaInicio.getValue(),
                    pickerFechaFin.getValue(),
                    "PDF"
            );
            actualizarMensaje("Reporte exportado a PDF correctamente.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error al exportar PDF", e.getMessage());
        }
    }

    @FXML
    private void actualizarGraficos() {
        graficoBarras.getData().clear();
        XYChart.Series<String, Number> serieBarras = new XYChart.Series<>();
        serieBarras.setName("Entradas");
        List<String> peliculas = control.obtenerEtiquetasGraficoBarras();
        List<Double> entradas = control.obtenerValoresGraficoBarras();
        for (int i = 0; i < peliculas.size(); i++) {
            serieBarras.getData().add(new XYChart.Data<>(peliculas.get(i), entradas.get(i)));
        }
        graficoBarras.getData().add(serieBarras);

        ObservableList<PieChart.Data> datosPastel = FXCollections.observableArrayList();
        List<String> funciones = control.obtenerCategoriasGraficoPastel();
        List<Double> ocupaciones = control.obtenerPorcentajesGraficoPastel();
        for (int i = 0; i < funciones.size(); i++) {
            datosPastel.add(new PieChart.Data(funciones.get(i), ocupaciones.get(i)));
        }
        graficoPastel.setData(datosPastel);

        graficoLineas.getData().clear();
        XYChart.Series<String, Number> serieLineas = new XYChart.Series<>();
        serieLineas.setName("Ventas ($)");
        List<String> fechas = control.obtenerFechasGraficoLineas();
        List<Double> montos = control.obtenerMontosGraficoLineas();
        for (int i = 0; i < fechas.size(); i++) {
            serieLineas.getData().add(new XYChart.Data<>(fechas.get(i), montos.get(i)));
        }
        graficoLineas.getData().add(serieLineas);

        actualizarMensaje("Gráficos actualizados.");
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