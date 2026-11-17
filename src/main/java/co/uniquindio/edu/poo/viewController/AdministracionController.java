package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.Funcion;
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

public class AdministracionController {

    private final Cinema cinema = Cinema.getInstancia();

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

        salas = FXCollections.observableArrayList(
                cinema.getListaSalas()
        );

        clientes = FXCollections.observableArrayList(
                cinema.getListaClientes()
        );

        configurarTablaSalas();
        configurarTablaClientes();

        tablaSalas.setItems(salas);
        tablaClientes.setItems(clientes);

        configurarSeleccionSalas();
        configurarSeleccionClientes();

        actualizarMensaje("Administración inicializada correctamente.");
    }

    private void configurarTablaSalas() {

        columnaIdSala.setCellValueFactory(
                new PropertyValueFactory<>("idSala")
        );

        columnaNumeroSala.setCellValueFactory(
                new PropertyValueFactory<>("numeroSala")
        );

        columnaCapacidad.setCellValueFactory(
                new PropertyValueFactory<>("capacidad")
        );
    }

    private void configurarTablaClientes() {

        columnaIdCliente.setCellValueFactory(
                new PropertyValueFactory<>("idCliente")
        );

        columnaNombreCliente.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        columnaCorreoCliente.setCellValueFactory(
                new PropertyValueFactory<>("correo")
        );
    }

    private void configurarSeleccionSalas() {

        tablaSalas.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        campoIdSala.setText(
                                String.valueOf(seleccionada.getIdSala())
                        );

                        campoNumeroSala.setText(
                                String.valueOf(seleccionada.getNumeroSala())
                        );

                        campoCapacidad.setText(
                                String.valueOf(seleccionada.getCapacidad())
                        );
                    }
                });
    }

    private void configurarSeleccionClientes() {

        tablaClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        campoIdCliente.setText(
                                seleccionada.getIdCliente()
                        );

                        campoNombreCliente.setText(
                                seleccionada.getNombre()
                        );

                        campoCorreoCliente.setText(
                                seleccionada.getCorreo()
                        );
                    }
                });
    }

    // =====================================================
    // CRUD DE SALAS
    // =====================================================

    @FXML
    private void registrarSala() {

        try {

            int id = Integer.parseInt(campoIdSala.getText().trim());
            int numero = Integer.parseInt(campoNumeroSala.getText().trim());
            int capacidad = Integer.parseInt(campoCapacidad.getText().trim());

            if (id <= 0 || numero <= 0 || capacidad <= 0) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Datos inválidos",
                        "El ID, número y capacidad deben ser mayores que cero."
                );
                return;
            }

            if (existeIdSala(id, null)) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "ID duplicado",
                        "Ya existe una sala con ese identificador."
                );
                return;
            }

            if (existeNumeroSala(numero, null)) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Número duplicado",
                        "Ya existe una sala con ese número."
                );
                return;
            }

            Sala nuevaSala = new Sala(
                    id,
                    numero,
                    capacidad,
                    cinema
            );

            nuevaSala.configurarAsientos();

            cinema.getListaSalas().add(nuevaSala);

            cargarSalas();
            limpiarCamposSala();

            actualizarMensaje("Sala registrada correctamente.");

        } catch (NumberFormatException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Datos incorrectos",
                    "Introduce números enteros válidos para el ID, número y capacidad."
            );
        }
    }

    @FXML
    private void actualizarSala() {

        Sala seleccionada = tablaSalas.getSelectionModel()
                .getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona una sala",
                    "Debes seleccionar una sala de la tabla."
            );
            return;
        }

        try {

            int id = Integer.parseInt(campoIdSala.getText().trim());
            int numero = Integer.parseInt(campoNumeroSala.getText().trim());
            int capacidad = Integer.parseInt(campoCapacidad.getText().trim());

            if (id <= 0 || numero <= 0 || capacidad <= 0) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Datos inválidos",
                        "Todos los valores deben ser mayores que cero."
                );
                return;
            }

            if (existeIdSala(id, seleccionada)) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "ID duplicado",
                        "Otra sala ya utiliza ese identificador."
                );
                return;
            }

            if (existeNumeroSala(numero, seleccionada)) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Número duplicado",
                        "Otra sala ya utiliza ese número."
                );
                return;
            }

            boolean tieneFunciones = tieneFuncionesAsociadas(seleccionada);

            if (tieneFunciones && capacidad != seleccionada.getCapacidad()) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Capacidad protegida",
                        "No puedes cambiar la capacidad de una sala que tiene funciones programadas."
                );
                return;
            }

            seleccionada.setIdSala(id);
            seleccionada.setNumeroSala(numero);
            seleccionada.setCapacidad(capacidad);

            if (!tieneFunciones) {
                seleccionada.configurarAsientos();
            }

            cargarSalas();
            tablaSalas.getSelectionModel().select(seleccionada);

            actualizarMensaje("Sala actualizada correctamente.");

        } catch (NumberFormatException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Datos incorrectos",
                    "Introduce números enteros válidos."
            );
        }
    }

    @FXML
    private void eliminarSala() {

        Sala seleccionada = tablaSalas.getSelectionModel()
                .getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona una sala",
                    "Debes seleccionar una sala para eliminarla."
            );
            return;
        }

        if (tieneFuncionesAsociadas(seleccionada)) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Sala en uso",
                    "No puedes eliminar esta sala porque tiene funciones asociadas."
            );
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

        cinema.getListaSalas().remove(seleccionada);

        cargarSalas();
        limpiarCamposSala();

        actualizarMensaje("Sala eliminada correctamente.");
    }

    @FXML
    private void limpiarCamposSala() {

        campoIdSala.clear();
        campoNumeroSala.clear();
        campoCapacidad.clear();

        tablaSalas.getSelectionModel().clearSelection();
    }

    private void cargarSalas() {

        salas.setAll(cinema.getListaSalas());
        tablaSalas.refresh();
    }

    private boolean existeIdSala(int id, Sala excluida) {

        return cinema.getListaSalas().stream()
                .anyMatch(sala ->
                        sala != excluida && sala.getIdSala() == id
                );
    }

    private boolean existeNumeroSala(int numero, Sala excluida) {

        return cinema.getListaSalas().stream()
                .anyMatch(sala ->
                        sala != excluida && sala.getNumeroSala() == numero
                );
    }

    private boolean tieneFuncionesAsociadas(Sala sala) {

        return cinema.getListaFunciones().stream()
                .anyMatch(funcion -> funcion.getSala() == sala);
    }

    // =====================================================
    // CRUD DE CLIENTES
    // =====================================================

    @FXML
    private void registrarCliente() {

        String id = campoIdCliente.getText().trim();
        String nombre = campoNombreCliente.getText().trim();
        String correo = campoCorreoCliente.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Debes completar todos los campos del cliente."
            );
            return;
        }

        if (!correo.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Correo inválido",
                    "Introduce una dirección de correo válida."
            );
            return;
        }

        if (existeIdCliente(id, null)) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Cliente duplicado",
                    "Ya existe un cliente con ese identificador."
            );
            return;
        }

        Cliente nuevoCliente = new Cliente(
                id,
                nombre,
                correo,
                cinema
        );

        cinema.registrarCliente(nuevoCliente);

        cargarClientes();
        limpiarCamposCliente();

        actualizarMensaje("Cliente registrado correctamente.");
    }

    @FXML
    private void actualizarCliente() {

        Cliente seleccionado = tablaClientes.getSelectionModel()
                .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona un cliente",
                    "Debes seleccionar un cliente de la tabla."
            );
            return;
        }

        String id = campoIdCliente.getText().trim();
        String nombre = campoNombreCliente.getText().trim();
        String correo = campoCorreoCliente.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Debes completar todos los campos."
            );
            return;
        }

        if (!correo.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Correo inválido",
                    "Introduce una dirección de correo válida."
            );
            return;
        }

        if (existeIdCliente(id, seleccionado)) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Identificador duplicado",
                    "Otro cliente ya utiliza ese identificador."
            );
            return;
        }

        seleccionado.setIdCliente(id);
        seleccionado.setNombre(nombre);
        seleccionado.setCorreo(correo);

        cargarClientes();
        tablaClientes.getSelectionModel().select(seleccionado);

        actualizarMensaje("Cliente actualizado correctamente.");
    }

    @FXML
    private void eliminarCliente() {

        Cliente seleccionado = tablaClientes.getSelectionModel()
                .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona un cliente",
                    "Debes seleccionar un cliente para eliminarlo."
            );
            return;
        }

        if (seleccionado.getListaCompras() != null
                && !seleccionado.getListaCompras().isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Cliente con compras",
                    "No puedes eliminar este cliente porque tiene compras registradas."
            );
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

        cinema.getListaClientes().remove(seleccionado);

        cargarClientes();
        limpiarCamposCliente();

        actualizarMensaje("Cliente eliminado correctamente.");
    }

    @FXML
    private void limpiarCamposCliente() {

        campoIdCliente.clear();
        campoNombreCliente.clear();
        campoCorreoCliente.clear();

        tablaClientes.getSelectionModel().clearSelection();
    }

    private void cargarClientes() {

        clientes.setAll(cinema.getListaClientes());
        tablaClientes.refresh();
    }

    private boolean existeIdCliente(String id, Cliente excluido) {

        return cinema.getListaClientes().stream()
                .anyMatch(cliente ->
                        cliente != excluido
                                && cliente.getIdCliente().equalsIgnoreCase(id)
                );
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private boolean confirmarAccion(String titulo, String contenido) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);

        Optional<ButtonType> resultado = alerta.showAndWait();

        return resultado.isPresent()
                && resultado.get() == ButtonType.OK;
    }

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String contenido
    ) {

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
