package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Sala;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

/**
 * Vista de administración (salas y clientes). Solo maneja los componentes
 * gráficos; la lógica de negocio se delega en ControlGeneral.
 */
public class AdministracionController {

    private final ControlGeneral control = new ControlGeneral();

    private ObservableList<Sala> salas;
    private ObservableList<Cliente> clientes;

    // =========================
    // CONTROLES DE SALAS
    // =========================

    @FXML
    private TextField campoIdSala;

    @FXML
    private TextField campoNumeroSala;

    @FXML
    private TextField campoCapacidad;

    @FXML
    private TableView<Sala> tablaSalas;

    @FXML
    private TableColumn<Sala, Integer> columnaIdSala;

    @FXML
    private TableColumn<Sala, Integer> columnaNumeroSala;

    @FXML
    private TableColumn<Sala, Integer> columnaCapacidad;

    // =========================
    // CONTROLES DE CLIENTES
    // =========================

    @FXML
    private TextField campoIdCliente;

    @FXML
    private TextField campoNombreCliente;

    @FXML
    private TextField campoCorreoCliente;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TableColumn<Cliente, String> columnaIdCliente;

    @FXML
    private TableColumn<Cliente, String> columnaNombreCliente;

    @FXML
    private TableColumn<Cliente, String> columnaCorreoCliente;

    // =========================
    // MENSAJE DE ESTADO
    // =========================

    @FXML
    private Label mensajeEstado;

    // =========================
    // INICIALIZACIÓN
    // =========================

    @FXML
    private void initialize() {

        salas = FXCollections.observableArrayList(control.obtenerSalas());
        clientes = FXCollections.observableArrayList(control.obtenerClientes());

        configurarTablaSalas();
        configurarTablaClientes();

        tablaSalas.setItems(salas);
        tablaClientes.setItems(clientes);

        configurarSeleccionSalas();
        configurarSeleccionClientes();

        actualizarMensaje("Administración inicializada correctamente.");
    }

    private void configurarTablaSalas() {
        columnaIdSala.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        columnaNumeroSala.setCellValueFactory(new PropertyValueFactory<>("numeroSala"));
        columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
    }

    private void configurarTablaClientes() {
        columnaIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        columnaNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCorreoCliente.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void configurarSeleccionSalas() {

        tablaSalas.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        campoIdSala.setText(String.valueOf(seleccionada.getIdSala()));
                        campoNumeroSala.setText(String.valueOf(seleccionada.getNumeroSala()));
                        campoCapacidad.setText(String.valueOf(seleccionada.getCapacidad()));
                    }
                });
    }

    private void configurarSeleccionClientes() {

        tablaClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        campoIdCliente.setText(seleccionada.getIdCliente());
                        campoNombreCliente.setText(seleccionada.getNombre());
                        campoCorreoCliente.setText(seleccionada.getCorreo());
                    }
                });
    }

    // =====================================================
    // SALAS
    // =====================================================

    @FXML
    private void registrarSala() {

        try {
            control.registrarSala(
                    leerEntero(campoIdSala),
                    leerEntero(campoNumeroSala),
                    leerEntero(campoCapacidad)
            );

            cargarSalas();
            limpiarCamposSala();

            actualizarMensaje("Sala registrada correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Datos incorrectos",
                    "Introduce números enteros válidos para el ID, número y capacidad.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarSala() {

        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();

        try {
            control.actualizarSala(
                    seleccionada,
                    leerEntero(campoIdSala),
                    leerEntero(campoNumeroSala),
                    leerEntero(campoCapacidad)
            );

            cargarSalas();
            tablaSalas.getSelectionModel().select(seleccionada);

            actualizarMensaje("Sala actualizada correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Datos incorrectos",
                    "Introduce números enteros válidos.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarSala() {

        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona una sala",
                    "Debes seleccionar una sala para eliminarla.");
            return;
        }

        boolean confirmar = confirmarAccion(
                "Eliminar sala",
                "¿Estás seguro de que deseas eliminar la sala "
                        + seleccionada.getNumeroSala() + "?"
        );

        if (!confirmar) {
            return;
        }

        try {
            control.eliminarSala(seleccionada);

            cargarSalas();
            limpiarCamposSala();

            actualizarMensaje("Sala eliminada correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sala en uso", e.getMessage());
        }
    }

    @FXML
    private void limpiarCamposSala() {

        campoIdSala.clear();
        campoNumeroSala.clear();
        campoCapacidad.clear();

        tablaSalas.getSelectionModel().clearSelection();
    }

    private void cargarSalas() {
        salas.setAll(control.obtenerSalas());
        tablaSalas.refresh();
    }

    // =====================================================
    // CLIENTES
    // =====================================================

    @FXML
    private void registrarCliente() {

        try {
            control.registrarCliente(
                    campoIdCliente.getText().trim(),
                    campoNombreCliente.getText().trim(),
                    campoCorreoCliente.getText().trim()
            );

            cargarClientes();
            limpiarCamposCliente();

            actualizarMensaje("Cliente registrado correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarCliente() {

        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        try {
            control.actualizarCliente(
                    seleccionado,
                    campoIdCliente.getText().trim(),
                    campoNombreCliente.getText().trim(),
                    campoCorreoCliente.getText().trim()
            );

            cargarClientes();
            tablaClientes.getSelectionModel().select(seleccionado);

            actualizarMensaje("Cliente actualizado correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarCliente() {

        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un cliente",
                    "Debes seleccionar un cliente para eliminarlo.");
            return;
        }

        boolean confirmar = confirmarAccion(
                "Eliminar cliente",
                "¿Estás seguro de que deseas eliminar al cliente "
                        + seleccionado.getNombre() + "?"
        );

        if (!confirmar) {
            return;
        }

        try {
            control.eliminarCliente(seleccionado);

            cargarClientes();
            limpiarCamposCliente();

            actualizarMensaje("Cliente eliminado correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Cliente con compras", e.getMessage());
        }
    }

    @FXML
    private void limpiarCamposCliente() {

        campoIdCliente.clear();
        campoNombreCliente.clear();
        campoCorreoCliente.clear();

        tablaClientes.getSelectionModel().clearSelection();
    }

    private void cargarClientes() {
        clientes.setAll(control.obtenerClientes());
        tablaClientes.refresh();
    }

    // =====================================================
    // MÉTODOS AUXILIARES DE VISTA
    // =====================================================

    /**
     * Convierte el texto de un campo en entero.
     */
    private int leerEntero(TextField campo) {
        return Integer.parseInt(campo.getText().trim());
    }

    private boolean confirmarAccion(String titulo, String contenido) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);

        Optional<ButtonType> resultado = alerta.showAndWait();

        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {

        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);

        alerta.showAndWait();
    }

    private void actualizarMensaje(String mensaje) {
        mensajeEstado.setText(mensaje);
    }
}
