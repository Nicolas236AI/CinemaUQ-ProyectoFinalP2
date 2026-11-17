
package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
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
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Vista de funciones. Solo maneja los componentes gráficos;
 * la lógica de negocio se delega en ControlGeneral.
 */
public class FuncionesController {

    private final ControlGeneral control = new ControlGeneral();

    private final ObservableList<Funcion> funciones =
            FXCollections.observableArrayList();

    private final ObservableList<Pelicula> peliculas =
            FXCollections.observableArrayList();

    private final ObservableList<Sala> salas =
            FXCollections.observableArrayList();

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

        peliculas.setAll(control.obtenerPeliculas());
        salas.setAll(control.obtenerSalas());
        funciones.setAll(control.obtenerFunciones());

        comboPeliculas.setItems(peliculas);
        comboSalas.setItems(salas);

        columnaId.setCellValueFactory(new PropertyValueFactory<>("idFuncion"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precioBase"));
        columnaPelicula.setCellValueFactory(new PropertyValueFactory<>("pelicula"));
        columnaSala.setCellValueFactory(new PropertyValueFactory<>("sala"));

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
            LocalDateTime fecha = leerFecha();
            double precio = leerPrecio();

            control.registrarFuncion(
                    campoIdFuncion.getText().trim(),
                    fecha,
                    precio,
                    comboPeliculas.getValue(),
                    comboSalas.getValue()
            );

            cargarFunciones();
            limpiarCampos();

            actualizarMensaje("Función registrada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarFechaInvalida();
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido", "Ingresa un precio numérico válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarFuncion() {

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        try {
            LocalDateTime fecha = leerFecha();
            double precio = leerPrecio();

            control.actualizarFuncion(
                    seleccionada,
                    campoIdFuncion.getText().trim(),
                    fecha,
                    precio,
                    comboPeliculas.getValue(),
                    comboSalas.getValue()
            );

            cargarFunciones();
            limpiarCampos();

            actualizarMensaje("Función actualizada correctamente.");

        } catch (DateTimeParseException e) {
            mostrarFechaInvalida();
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido", "Ingresa un precio numérico válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarFuncion() {

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selección", "Selecciona una función para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar función");
        alerta.setHeaderText("¿Deseas eliminar esta función?");
        alerta.setContentText("ID: " + seleccionada.getIdFuncion());

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                control.eliminarFuncion(seleccionada);

                cargarFunciones();
                limpiarCampos();

                actualizarMensaje("Función eliminada correctamente.");

            } catch (IllegalArgumentException e) {
                mostrarAlerta("Datos inválidos", e.getMessage());
            }
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
        campoFechaHora.setText(control.formatearFecha(funcion.getFechaHora()));
        campoPrecio.setText(String.valueOf(funcion.getPrecioBase()));

        comboPeliculas.setValue(funcion.getPelicula());
        comboSalas.setValue(funcion.getSala());
    }

    /**
     * Convierte el texto del campo de fecha en un LocalDateTime.
     */
    private LocalDateTime leerFecha() {
        return control.parsearFecha(campoFechaHora.getText());
    }

    /**
     * Convierte el texto del campo de precio en un número.
     */
    private double leerPrecio() {
        return Double.parseDouble(campoPrecio.getText().trim());
    }

    private void cargarFunciones() {
        funciones.setAll(control.obtenerFunciones());
        tablaFunciones.refresh();
    }

    private void mostrarFechaInvalida() {
        mostrarAlerta("Fecha inválida",
                "Usa el formato: yyyy-MM-dd HH:mm\nEjemplo: 2026-12-20 18:30");
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
