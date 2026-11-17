
package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.model.Cinema;
import co.uniquindio.edu.poo.model.Pelicula;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class CarteleraController {

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private TableColumn<Pelicula, String> columnaId;

    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;

    @FXML
    private TableColumn<Pelicula, Integer> columnaDuracion;

    @FXML
    private TableColumn<Pelicula, String> columnaGenero;

    @FXML
    private TextField campoId;

    @FXML
    private TextField campoTitulo;

    @FXML
    private TextField campoDuracion;

    @FXML
    private TextField campoGenero;

    @FXML
    private javafx.scene.control.Label mensajeEstado;

    private final Cinema cinema = Cinema.getInstancia();

    private final ObservableList<Pelicula> peliculas =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarTabla();
        cargarPeliculas();

        tablaPeliculas.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {
                    if (seleccionada != null) {
                        mostrarDatosSeleccionados(seleccionada);
                    }
                });
    }

    private void configurarTabla() {
        columnaId.setCellValueFactory(
                new PropertyValueFactory<>("idPelicula")
        );

        columnaTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );

        columnaDuracion.setCellValueFactory(
                new PropertyValueFactory<>("duracion")
        );

        columnaGenero.setCellValueFactory(
                new PropertyValueFactory<>("genero")
        );
    }

    private void cargarPeliculas() {
        peliculas.setAll(cinema.getListaPeliculas());
        tablaPeliculas.setItems(peliculas);

        actualizarMensaje();
    }

    @FXML
    private void registrarPelicula() {
        String id = campoId.getText().trim();
        String titulo = campoTitulo.getText().trim();
        String duracionTexto = campoDuracion.getText().trim();
        String genero = campoGenero.getText().trim();

        if (id.isEmpty() || titulo.isEmpty()
                || duracionTexto.isEmpty() || genero.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Completa todos los campos para registrar la película."
            );
            return;
        }

        int duracion;

        try {
            duracion = Integer.parseInt(duracionTexto);

            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Duración inválida",
                    "La duración debe ser un número entero mayor que cero."
            );
            return;
        }

        if (existeId(id)) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Identificación duplicada",
                    "Ya existe una película con ese identificador."
            );
            return;
        }

        Pelicula nueva = new Pelicula(id, titulo, duracion, genero);

        cinema.registrarPelicula(nueva);

        cargarPeliculas();
        limpiarCampos();

        mensajeEstado.setText("Película registrada correctamente.");
    }

    @FXML
    private void actualizarPelicula() {
        Pelicula seleccionada =
                tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Selecciona una película de la tabla para actualizarla."
            );
            return;
        }

        String id = campoId.getText().trim();
        String titulo = campoTitulo.getText().trim();
        String duracionTexto = campoDuracion.getText().trim();
        String genero = campoGenero.getText().trim();

        if (id.isEmpty() || titulo.isEmpty()
                || duracionTexto.isEmpty() || genero.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Completa todos los campos antes de actualizar."
            );
            return;
        }

        int duracion;

        try {
            duracion = Integer.parseInt(duracionTexto);

            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Duración inválida",
                    "La duración debe ser un número entero mayor que cero."
            );
            return;
        }

        if (!seleccionada.getIdPelicula().equalsIgnoreCase(id)
                && existeId(id)) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Identificación duplicada",
                    "Otra película ya utiliza ese identificador."
            );
            return;
        }

        seleccionada.setIdPelicula(id);
        seleccionada.setTitulo(titulo);
        seleccionada.setDuracion(duracion);
        seleccionada.setGenero(genero);

        tablaPeliculas.refresh();
        cargarPeliculas();
        limpiarCampos();

        mensajeEstado.setText("Película actualizada correctamente.");
    }

    @FXML
    private void eliminarPelicula() {
        Pelicula seleccionada =
                tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Selecciona una película para eliminarla."
            );
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar película");
        confirmacion.setHeaderText("¿Deseas eliminar esta película?");
        confirmacion.setContentText(
                "Se eliminará: " + seleccionada.getTitulo()
        );

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent()
                && resultado.get() == ButtonType.OK) {

            cinema.getListaPeliculas().remove(seleccionada);

            cargarPeliculas();
            limpiarCampos();

            mensajeEstado.setText("Película eliminada correctamente.");
        }
    }

    @FXML
    private void limpiarCampos() {
        campoId.clear();
        campoTitulo.clear();
        campoDuracion.clear();
        campoGenero.clear();

        tablaPeliculas.getSelectionModel().clearSelection();
    }

    private void mostrarDatosSeleccionados(Pelicula pelicula) {
        campoId.setText(pelicula.getIdPelicula());
        campoTitulo.setText(pelicula.getTitulo());
        campoDuracion.setText(
                String.valueOf(pelicula.getDuracion())
        );
        campoGenero.setText(pelicula.getGenero());

        mensajeEstado.setText(
                "Película seleccionada: " + pelicula.getTitulo()
        );
    }

    private boolean existeId(String id) {
        return cinema.getListaPeliculas().stream()
                .anyMatch(p -> p.getIdPelicula() != null
                        && p.getIdPelicula().equalsIgnoreCase(id));
    }

    private void actualizarMensaje() {
        int cantidad = peliculas.size();

        mensajeEstado.setText(
                "Películas registradas: " + cantidad
        );
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
}
