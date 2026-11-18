package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.Pelicula;
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
import javafx.scene.layout.VBox;

import java.util.Optional;

public class CarteleraController {

    private final ControlGeneral control = new ControlGeneral();
    private final ObservableList<Pelicula> peliculas = FXCollections.observableArrayList();

    @FXML
    private VBox panelFormularioPelicula;

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
    private TextField campoBuscarPelicula;

    @FXML
    private Label mensajeEstado;

    @FXML
    private void initialize() {
        boolean esAdmin = control.esAdministradorActivo();
        panelFormularioPelicula.setVisible(esAdmin);
        panelFormularioPelicula.setManaged(esAdmin);

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
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
    }

    private void cargarPeliculas() {
        peliculas.setAll(control.obtenerPeliculas());
        tablaPeliculas.setItems(peliculas);
        mensajeEstado.setText("Películas en cartelera: " + peliculas.size());
    }

    @FXML
    private void filtrarPeliculas() {
        peliculas.setAll(control.buscarPeliculas(campoBuscarPelicula.getText()));
        mensajeEstado.setText("Resultados encontrados: " + peliculas.size());
    }

    @FXML
    private void registrarPelicula() {
        try {
            int duracion = leerDuracion();
            control.registrarPelicula(
                    campoId.getText().trim(),
                    campoTitulo.getText().trim(),
                    duracion,
                    campoGenero.getText().trim()
            );
            cargarPeliculas();
            limpiarCampos();
            mensajeEstado.setText("Película registrada correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Duración inválida", "La duración debe ser un número entero mayor que cero.");
        } catch (RuntimeException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void actualizarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        try {
            int duracion = leerDuracion();
            control.actualizarPelicula(
                    seleccionada,
                    campoId.getText().trim(),
                    campoTitulo.getText().trim(),
                    duracion,
                    campoGenero.getText().trim()
            );
            tablaPeliculas.refresh();
            cargarPeliculas();
            limpiarCampos();
            mensajeEstado.setText("Película actualizada correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Duración inválida", "La duración debe ser un número entero mayor que cero.");
        } catch (RuntimeException e) {
            mostrarAlerta("Datos inválidos", e.getMessage());
        }
    }

    @FXML
    private void eliminarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin selección", "Selecciona una película para eliminarla.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar película");
        confirmacion.setHeaderText("¿Deseas eliminar esta película?");
        confirmacion.setContentText("Se eliminará: " + seleccionada.getTitulo());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                control.eliminarPelicula(seleccionada);
                cargarPeliculas();
                limpiarCampos();
                mensajeEstado.setText("Película eliminada correctamente.");
            } catch (RuntimeException e) {
                mostrarAlerta("Datos inválidos", e.getMessage());
            }
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
        campoDuracion.setText(String.valueOf(pelicula.getDuracion()));
        campoGenero.setText(pelicula.getGenero());
        mensajeEstado.setText("Película seleccionada: " + pelicula.getTitulo());
    }

    private int leerDuracion() {
        String texto = campoDuracion.getText().trim();
        if (texto.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(texto);
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}