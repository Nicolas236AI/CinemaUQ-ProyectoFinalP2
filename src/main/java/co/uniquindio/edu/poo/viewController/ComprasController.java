package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.model.Cinema;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComprasController {

    private final Cinema cinema = Cinema.getInstancia();

    private final ObservableList<Compra> compras =
            FXCollections.observableArrayList();

    private final ObservableList<Cliente> clientes =
            FXCollections.observableArrayList();

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML private TextField campoIdCompra;
    @FXML private TextField campoFecha;
    @FXML private TextField campoTotal;

    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<EstadoCompra> comboEstados;

    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra, String> columnaId;
    @FXML private TableColumn<Compra, String> columnaFecha;
    @FXML private TableColumn<Compra, Double> columnaTotal;
    @FXML private TableColumn<Compra, EstadoCompra> columnaEstado;
    @FXML private TableColumn<Compra, Cliente> columnaCliente;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {

        clientes.setAll(cinema.getListaClientes());
        comboClientes.setItems(clientes);

        comboEstados.setItems(
                FXCollections.observableArrayList(EstadoCompra.values())
        );

        columnaId.setCellValueFactory(
                new PropertyValueFactory<>("idCompra"));

        columnaFecha.setCellValueFactory(
                new PropertyValueFactory<>("fecha"));

        columnaTotal.setCellValueFactory(
                new PropertyValueFactory<>("total"));

        columnaEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        columnaCliente.setCellValueFactory(
                new PropertyValueFactory<>("cliente"));

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
    private void registrarCompra() {

        try {
            String id = campoIdCompra.getText().trim();

            if (id.isEmpty()) {
                mostrarAlerta("Validación",
                        "Ingresa el identificador de la compra.");
                return;
            }

            if (existeId(id, null)) {
                mostrarAlerta("Validación",
                        "Ya existe una compra con ese identificador.");
                return;
            }

            String fecha = validarFecha();
            double total = validarTotal();
            Cliente cliente = comboClientes.getValue();

            if (cliente == null) {
                mostrarAlerta("Validación",
                        "Selecciona el cliente de la compra.");
                return;
            }

            Compra compra = new Compra.Builder()
                    .idCompra(id)
                    .fecha(fecha)
                    .total(total)
                    .estado(EstadoCompra.PENDIENTE)
                    .cliente(cliente)
                    .build();

            if (cliente.getListaCompras() == null) {
                cliente.setListaCompras(new ArrayList<>());
            }

            cliente.getListaCompras().add(compra);

            cargarCompras();
            limpiarCampos();

            actualizarMensaje("Compra registrada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarAlerta("Fecha inválida",
                    "Usa el formato yyyy-MM-dd HH:mm. Ejemplo: 2026-12-20 18:30.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Total inválido",
                    "Ingresa un valor numérico válido.");
        }
    }

    @FXML
    private void actualizarCompra() {

        Compra seleccionada =
                tablaCompras.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selección",
                    "Selecciona una compra de la tabla.");
            return;
        }

        if (seleccionada.getEstado() != EstadoCompra.PENDIENTE) {
            mostrarAlerta("Operación no permitida",
                    "Solo se pueden editar compras pendientes.");
            return;
        }

        try {
            String id = campoIdCompra.getText().trim();

            if (id.isEmpty()) {
                mostrarAlerta("Validación",
                        "El identificador es obligatorio.");
                return;
            }

            if (existeId(id, seleccionada)) {
                mostrarAlerta("Validación",
                        "Ya existe otra compra con ese identificador.");
                return;
            }

            String fecha = validarFecha();
            double total = validarTotal();
            Cliente nuevoCliente = comboClientes.getValue();

            if (nuevoCliente == null) {
                mostrarAlerta("Validación",
                        "Selecciona un cliente.");
                return;
            }

            Cliente clienteAnterior = seleccionada.getCliente();

            if (clienteAnterior != nuevoCliente) {

                if (clienteAnterior != null
                        && clienteAnterior.getListaCompras() != null) {
                    clienteAnterior.getListaCompras().remove(seleccionada);
                }

                if (nuevoCliente.getListaCompras() == null) {
                    nuevoCliente.setListaCompras(new ArrayList<>());
                }

                nuevoCliente.getListaCompras().add(seleccionada);
            }

            seleccionada.setIdCompra(id);
            seleccionada.setFecha(fecha);
            seleccionada.setTotal(total);
            seleccionada.setCliente(nuevoCliente);

            cargarCompras();
            limpiarCampos();

            actualizarMensaje("Compra actualizada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarAlerta("Fecha inválida",
                    "Usa el formato yyyy-MM-dd HH:mm.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Total inválido",
                    "Ingresa un valor numérico válido.");
        }
    }

    @FXML
    private void cancelarCompra() {

        Compra seleccionada =
                tablaCompras.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selección",
                    "Selecciona una compra para cancelar.");
            return;
        }

        if (seleccionada.getEstado() == EstadoCompra.CANCELADA) {
            mostrarAlerta("Información",
                    "Esta compra ya está cancelada.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cancelar compra");
        alerta.setHeaderText("¿Deseas cancelar esta compra?");
        alerta.setContentText("Compra: " + seleccionada.getIdCompra());

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent()
                && resultado.get() == ButtonType.OK) {

            seleccionada.setEstado(EstadoCompra.CANCELADA);

            cargarCompras();
            limpiarCampos();

            actualizarMensaje("Compra cancelada correctamente.");
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

    private String validarFecha() {

        String fecha = campoFecha.getText().trim();

        if (fecha.isEmpty()) {
            throw new DateTimeParseException(
                    "Fecha vacía", fecha, 0);
        }

        LocalDateTime.parse(fecha, formatoFecha);

        return fecha;
    }

    private double validarTotal() {

        double total = Double.parseDouble(
                campoTotal.getText().trim());

        if (!Double.isFinite(total) || total <= 0) {
            throw new IllegalArgumentException(
                    "El total debe ser mayor que cero.");
        }

        return total;
    }

    private boolean existeId(String id, Compra actual) {

        return obtenerTodasLasCompras().stream().anyMatch(compra ->
                compra != actual
                        && compra.getIdCompra() != null
                        && compra.getIdCompra().equalsIgnoreCase(id));
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

    private void cargarCompras() {

        compras.setAll(obtenerTodasLasCompras());
        clientes.setAll(cinema.getListaClientes());

        tablaCompras.refresh();

        actualizarMensaje("Compras registradas: " + compras.size());
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
