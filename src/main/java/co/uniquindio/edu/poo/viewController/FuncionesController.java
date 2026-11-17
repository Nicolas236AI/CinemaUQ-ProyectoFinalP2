
package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Funcion;
import co.uniquindio.edu.poo.model.Pelicula;
import co.uniquindio.edu.poo.model.Sala;

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
import java.util.Optional;

public class FuncionesController {

    private final Cinema cinema = Cinema.getInstancia();

    private final ObservableList<Funcion> funciones =
            FXCollections.observableArrayList();

    private final ObservableList<Pelicula> peliculas =
            FXCollections.observableArrayList();

    private final ObservableList<Sala> salas =
            FXCollections.observableArrayList();

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML private TextField campoIdFuncion;
    @FXML private TextField campoFechaHora;
    @FXML private TextField campoPrecio;

    @FXML private ComboBox<Pelicula> comboPeliculas;
    @FXML private ComboBox<Sala> comboSalas;

    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> columnaId;
    @FXML private TableColumn<Funcion, LocalDateTime> columnaFecha;
    @FXML private TableColumn<Funcion, Double> columnaPrecio;
    @FXML private TableColumn<Funcion, Pelicula> columnaPelicula;
    @FXML private TableColumn<Funcion, Sala> columnaSala;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {

        peliculas.setAll(cinema.getListaPeliculas());
        salas.setAll(cinema.getListaSalas());
        funciones.setAll(cinema.getListaFunciones());

        comboPeliculas.setItems(peliculas);
        comboSalas.setItems(salas);

        columnaId.setCellValueFactory(
                new PropertyValueFactory<>("idFuncion"));

        columnaFecha.setCellValueFactory(
                new PropertyValueFactory<>("fechaHora"));

        columnaPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precioBase"));

        columnaPelicula.setCellValueFactory(
                new PropertyValueFactory<>("pelicula"));

        columnaSala.setCellValueFactory(
                new PropertyValueFactory<>("sala"));

        tablaFunciones.setItems(funciones);

        tablaFunciones.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {
                    if (seleccionada != null) {
                        cargarFuncion(seleccionada);
                    }
                });

        actualizarMensaje("Funciones registradas: " + funciones.size());
    }

    @FXML
    private void registrarFuncion() {

        try {
            String id = campoIdFuncion.getText().trim();

            if (id.isEmpty()) {
                mostrarAlerta("Validación", "Ingresa el ID de la función.");
                return;
            }

            if (existeId(id, null)) {
                mostrarAlerta("Validación", "Ya existe una función con ese ID.");
                return;
            }

            LocalDateTime fecha = obtenerFecha();
            double precio = obtenerPrecio();

            Pelicula pelicula = comboPeliculas.getValue();
            Sala sala = comboSalas.getValue();

            if (pelicula == null || sala == null) {
                mostrarAlerta("Validación",
                        "Debes seleccionar una película y una sala.");
                return;
            }

            Funcion funcion = new Funcion(
                    id, fecha, precio, pelicula, sala);

            cinema.registrarFuncion(funcion);

            cargarFunciones();
            limpiarCampos();

            actualizarMensaje("Función registrada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarAlerta("Fecha inválida",
                    "Usa el formato: yyyy-MM-dd HH:mm\nEjemplo: 2026-12-20 18:30");
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido",
                    "Ingresa un precio numérico válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarFuncion() {

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selección",
                    "Selecciona una función de la tabla.");
            return;
        }

        try {
            String id = campoIdFuncion.getText().trim();

            if (id.isEmpty()) {
                mostrarAlerta("Validación",
                        "El ID de la función es obligatorio.");
                return;
            }

            if (existeId(id, seleccionada)) {
                mostrarAlerta("Validación",
                        "Ya existe otra función con ese ID.");
                return;
            }

            LocalDateTime fecha = obtenerFecha();
            double precio = obtenerPrecio();

            Pelicula pelicula = comboPeliculas.getValue();
            Sala sala = comboSalas.getValue();

            if (pelicula == null || sala == null) {
                mostrarAlerta("Validación",
                        "Selecciona una película y una sala.");
                return;
            }

            seleccionada.setIdFuncion(id);
            seleccionada.setFechaHora(fecha);
            seleccionada.setPrecioBase(precio);
            seleccionada.setPelicula(pelicula);
            seleccionada.setSala(sala);

            cargarFunciones();
            limpiarCampos();

            actualizarMensaje("Función actualizada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarAlerta("Fecha inválida",
                    "Usa el formato: yyyy-MM-dd HH:mm");
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido",
                    "Ingresa un precio numérico válido.");
        }
    }

    @FXML
    private void eliminarFuncion() {

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selección",
                    "Selecciona una función para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar función");
        alerta.setHeaderText("¿Deseas eliminar esta función?");
        alerta.setContentText("ID: " + seleccionada.getIdFuncion());

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent()
                && resultado.get() == ButtonType.OK) {

            cinema.getListaFunciones().remove(seleccionada);

            cargarFunciones();
            limpiarCampos();

            actualizarMensaje("Función eliminada correctamente.");
        }
    }

    @FXML
    private void limpiarCampos() {

        campoIdFuncion.clear();
        campoFechaHora.clear();
        campoPrecio.clear();

        comboPeliculas.getSelectionModel().clearSelection();
        comboSalas.getSelectionModel().clearSelection();

        tablaFunciones.getSelectionModel().clearSelection();
    }

    private void cargarFuncion(Funcion funcion) {

        campoIdFuncion.setText(funcion.getIdFuncion());

        campoFechaHora.setText(
                funcion.getFechaHora().format(formatoFecha));

        campoPrecio.setText(
                String.valueOf(funcion.getPrecioBase()));

        comboPeliculas.setValue(funcion.getPelicula());
        comboSalas.setValue(funcion.getSala());
    }

    private LocalDateTime obtenerFecha() {

        String texto = campoFechaHora.getText().trim();

        if (texto.isEmpty()) {
            throw new DateTimeParseException(
                    "Fecha vacía", texto, 0);
        }

        return LocalDateTime.parse(texto, formatoFecha);
    }

    private double obtenerPrecio() {

        double precio = Double.parseDouble(
                campoPrecio.getText().trim());

        if (!Double.isFinite(precio) || precio <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero.");
        }

        return precio;
    }

    private boolean existeId(String id, Funcion actual) {

        return funciones.stream().anyMatch(funcion ->
                funcion != actual
                        && funcion.getIdFuncion() != null
                        && funcion.getIdFuncion().equalsIgnoreCase(id));
    }

    private void cargarFunciones() {

        funciones.setAll(cinema.getListaFunciones());
        tablaFunciones.refresh();
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
