package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Compra;
import co.uniquindio.edu.poo.model.EstadoCompra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeParseException;
import java.util.Optional;

public class ComprasController {

    private final ControlGeneral control = new ControlGeneral();

    private final ObservableList<Compra> compras = FXCollections.observableArrayList();
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @FXML private TextField campoIdCompra;
    @FXML private TextField campoFecha;
    @FXML private TextField campoTotal;

    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<EstadoCompra> comboEstados;
    @FXML private ComboBox<String> comboFiltroEstado;

    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra, String> columnaId;
    @FXML private TableColumn<Compra, String> columnaFecha;
    @FXML private TableColumn<Compra, Double> columnaTotal;
    @FXML private TableColumn<Compra, EstadoCompra> columnaEstado;
    @FXML private TableColumn<Compra, Double> columnaReembolso;
    @FXML private TableColumn<Compra, Cliente> columnaCliente;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        clientes.setAll(control.obtenerClientes());
        comboClientes.setItems(clientes);

        comboEstados.setItems(FXCollections.observableArrayList(EstadoCompra.values()));
        comboFiltroEstado.setItems(FXCollections.observableArrayList("TODAS", "COMPLETADA", "PENDIENTE", "CANCELADA"));
        comboFiltroEstado.getSelectionModel().selectFirst();

        columnaId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaReembolso.setCellValueFactory(new PropertyValueFactory<>("montoReembolsado"));
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        tablaCompras.setItems(compras);

        tablaCompras.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {
                    if (seleccionada != null) {
                        cargarCompra(seleccionada);
                    }
                });

        cargarCompras();
    }

    @FXML
    private void filtrarCompras() {
        compras.setAll(control.filtrarComprasPorEstado(comboFiltroEstado.getValue()));
        actualizarMensaje("Compras mostradas: " + compras.size());
    }

    @FXML
    private void registrarCompra() {
        try {
            control.registrarCompra(
                    campoIdCompra.getText().trim(),
                    campoFecha.getText().trim(),
                    Double.parseDouble(campoTotal.getText().trim()),
                    comboClientes.getValue()
            );
            cargarCompras();
            limpiarCampos();
            actualizarMensaje("Compra registrada correctamente.");
        } catch (DateTimeParseException e) {
            mostrarFechaInvalida();
        } catch (NumberFormatException e) {
            mostrarAlerta("Total inválido", "Ingresa un valor numérico válido.");
        } catch (RuntimeException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarCompra() {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        try {
            control.actualizarCompra(
                    seleccionada,
                    campoIdCompra.getText().trim(),
                    campoFecha.getText().trim(),
                    Double.parseDouble(campoTotal.getText().trim()),
                    comboClientes.getValue()
            );
            cargarCompras();
            limpiarCampos();
            actualizarMensaje("Compra actualizada correctamente.");
        } catch (DateTimeParseException e) {
            mostrarFechaInvalida();
        } catch (NumberFormatException e) {
            mostrarAlerta("Total inválido", "Ingresa un valor numérico válido.");
        } catch (RuntimeException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void cancelarCompra() {
        Compra seleccionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selección", "Selecciona una compra para cancelar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cancelar compra");
        alerta.setHeaderText("¿Deseas cancelar y reembolsar esta compra?");
        alerta.setContentText("Compra: " + seleccionada.getIdCompra());

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                double reembolso = control.cancelarCompraConPolitica(seleccionada, false);
                cargarCompras();
                limpiarCampos();
                actualizarMensaje(String.format("Compra cancelada. Reembolsado: $%.2f", reembolso));
            } catch (RuntimeException e) {
                mostrarAlerta("Información", e.getMessage());
            }
        }
    }

    @FXML
    private void limpiarCampos() {
        campoIdCompra.clear();
        campoFecha.clear();
        campoTotal.clear();
        comboClientes.getSelectionModel().clearSelection();
        comboEstados.getSelectionModel().clearSelection();
        tablaCompras.getSelectionModel().clearSelection();
    }

    private void cargarCompra(Compra compra) {
        campoIdCompra.setText(compra.getIdCompra());
        campoFecha.setText(compra.getFecha());
        campoTotal.setText(String.valueOf(compra.getTotal()));
        comboClientes.setValue(compra.getCliente());
        comboEstados.setValue(compra.getEstado());
    }

    private void cargarCompras() {
        compras.setAll(control.obtenerTodasLasCompras());
        clientes.setAll(control.obtenerClientes());
        tablaCompras.refresh();
        actualizarMensaje("Compras registradas: " + compras.size());
    }

    private void mostrarFechaInvalida() {
        mostrarAlerta("Fecha inválida", "Usa el formato yyyy-MM-dd HH:mm. Ejemplo: 2026-12-20 18:30.");
    }

    private void actualizarMensaje(String mensaje) {
        mensajeEstado.setText(mensaje);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}