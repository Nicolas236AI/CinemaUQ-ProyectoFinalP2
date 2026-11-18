package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Recarga;
import co.uniquindio.edu.poo.model.Sala;
import co.uniquindio.edu.poo.model.Tarjeta;
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

public class AdministracionController {

    private final ControlGeneral control = new ControlGeneral();

    private ObservableList<Sala> salas;
    private ObservableList<Cliente> clientes;
    private ObservableList<Recarga> recargasPendientes;

    @FXML private TextField campoIdSala;
    @FXML private TextField campoNumeroSala;
    @FXML private TextField campoCapacidad;
    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, Integer> columnaIdSala;
    @FXML private TableColumn<Sala, Integer> columnaNumeroSala;
    @FXML private TableColumn<Sala, Integer> columnaCapacidad;

    @FXML private TextField campoIdCliente;
    @FXML private TextField campoNombreCliente;
    @FXML private TextField campoCorreoCliente;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> columnaIdCliente;
    @FXML private TableColumn<Cliente, String> columnaNombreCliente;
    @FXML private TableColumn<Cliente, String> columnaCorreoCliente;

    @FXML private TableView<Recarga> tablaRecargas;
    @FXML private TableColumn<Recarga, String> columnaIdRecarga;
    @FXML private TableColumn<Recarga, String> columnaFechaRecarga;
    @FXML private TableColumn<Recarga, Double> columnaMontoRecarga;
    @FXML private TableColumn<Recarga, Tarjeta> columnaTarjetaRecarga;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        salas = FXCollections.observableArrayList(control.obtenerSalas());
        clientes = FXCollections.observableArrayList(control.obtenerClientes());
        recargasPendientes = FXCollections.observableArrayList(control.obtenerSolicitudesRecarga());

        columnaIdSala.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        columnaNumeroSala.setCellValueFactory(new PropertyValueFactory<>("numeroSala"));
        columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

        columnaIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        columnaNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCorreoCliente.setCellValueFactory(new PropertyValueFactory<>("correo"));

        columnaIdRecarga.setCellValueFactory(new PropertyValueFactory<>("idRecarga"));
        columnaFechaRecarga.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaMontoRecarga.setCellValueFactory(new PropertyValueFactory<>("monto"));
        columnaTarjetaRecarga.setCellValueFactory(new PropertyValueFactory<>("tarjeta"));

        tablaSalas.setItems(salas);
        tablaClientes.setItems(clientes);
        tablaRecargas.setItems(recargasPendientes);

        tablaSalas.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            if (sel != null) {
                campoIdSala.setText(String.valueOf(sel.getIdSala()));
                campoNumeroSala.setText(String.valueOf(sel.getNumeroSala()));
                campoCapacidad.setText(String.valueOf(sel.getCapacidad()));
            }
        });

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            if (sel != null) {
                campoIdCliente.setText(sel.getIdCliente());
                campoNombreCliente.setText(sel.getNombre());
                campoCorreoCliente.setText(sel.getCorreo());
            }
        });

        actualizarMensaje("Administración inicializada correctamente.");
    }

    @FXML
    private void registrarSala() {
        try {
            control.registrarSala(
                    Integer.parseInt(campoIdSala.getText().trim()),
                    Integer.parseInt(campoNumeroSala.getText().trim()),
                    Integer.parseInt(campoCapacidad.getText().trim())
            );
            cargarSalas();
            limpiarCamposSala();
            actualizarMensaje("Sala registrada correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Datos incorrectos", "Introduce números enteros válidos.");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarSala() {
        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        try {
            control.actualizarSala(
                    seleccionada,
                    Integer.parseInt(campoIdSala.getText().trim()),
                    Integer.parseInt(campoNumeroSala.getText().trim()),
                    Integer.parseInt(campoCapacidad.getText().trim())
            );
            cargarSalas();
            actualizarMensaje("Sala actualizada correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Datos incorrectos", "Introduce números enteros válidos.");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarSala() {
        Sala seleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona una sala", "Debes seleccionar una sala para eliminarla.");
            return;
        }
        if (confirmarAccion("Eliminar sala", "¿Deseas eliminar la sala " + seleccionada.getNumeroSala() + "?")) {
            try {
                control.eliminarSala(seleccionada);
                cargarSalas();
                limpiarCamposSala();
                actualizarMensaje("Sala eliminada correctamente.");
            } catch (RuntimeException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Sala en uso", e.getMessage());
            }
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
        } catch (RuntimeException e) {
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
            actualizarMensaje("Cliente actualizado correctamente.");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecciona un cliente", "Debes seleccionar un cliente para eliminarlo.");
            return;
        }
        if (confirmarAccion("Eliminar cliente", "¿Deseas eliminar al cliente " + seleccionado.getNombre() + "?")) {
            try {
                control.eliminarCliente(seleccionado);
                cargarClientes();
                limpiarCamposCliente();
                actualizarMensaje("Cliente eliminado correctamente.");
            } catch (RuntimeException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Cliente con compras", e.getMessage());
            }
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

    @FXML
    private void aprobarRecargaSeleccionada() {
        Recarga seleccionada = tablaRecargas.getSelectionModel().getSelectedItem();
        try {
            control.aprobarRecarga(seleccionada);
            recargasPendientes.setAll(control.obtenerSolicitudesRecarga());
            tablaRecargas.refresh();
            actualizarMensaje("Recarga aprobada y acreditada en la tarjeta virtual.");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aprobación de recarga", e.getMessage());
        }
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