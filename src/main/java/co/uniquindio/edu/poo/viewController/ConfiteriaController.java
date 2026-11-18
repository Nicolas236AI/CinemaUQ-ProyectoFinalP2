package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.IElementoConfiteria;
import co.uniquindio.edu.poo.model.ProductoConfiteria;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ConfiteriaController {

    private final ControlGeneral control = new ControlGeneral();
    private final List<IElementoConfiteria> itemsTemporalesCombo = new ArrayList<>();

    @FXML private TextField campoIdProducto;
    @FXML private TextField campoNombreProducto;
    @FXML private TextField campoPrecioProducto;

    @FXML private TextField campoNombreCombo;
    @FXML private TextField campoPrecioCombo;
    @FXML private ComboBox<ProductoConfiteria> comboProductoParaCombo;
    @FXML private ListView<String> listaCatalogoConfiteria;

    @FXML private ComboBox<String> comboTipoPromocion;
    @FXML private TextField campoPorcentajePromocion;
    @FXML private TextField campoPuntosRequeridosPromo;
    @FXML private ListView<String> listaPromocionesActivas;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        comboTipoPromocion.setItems(FXCollections.observableArrayList("Estudiante", "Fidelidad"));
        comboTipoPromocion.getSelectionModel().selectFirst();
        refrescarListas();
    }

    private void refrescarListas() {
        comboProductoParaCombo.setItems(FXCollections.observableArrayList(control.obtenerProductosIndividuales()));
        listaCatalogoConfiteria.setItems(FXCollections.observableArrayList(control.obtenerDescripcionesCatalogoConfiteria()));
        listaPromocionesActivas.setItems(FXCollections.observableArrayList(control.obtenerNombresPromociones()));
    }

    @FXML
    private void registrarProducto() {
        try {
            double precio = Double.parseDouble(campoPrecioProducto.getText().trim());
            control.registrarProductoConfiteria(
                    campoIdProducto.getText().trim(),
                    campoNombreProducto.getText().trim(),
                    precio
            );
            campoIdProducto.clear();
            campoNombreProducto.clear();
            campoPrecioProducto.clear();
            refrescarListas();
            mensajeEstado.setText("Producto de confitería registrado.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido", "Ingresa un precio numérico válido.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error al registrar producto", e.getMessage());
        }
    }

    @FXML
    private void agregarProductoACombo() {
        ProductoConfiteria prod = comboProductoParaCombo.getValue();
        if (prod == null) {
            mostrarAlerta("Selección", "Selecciona un producto para incluir en el combo.");
            return;
        }
        itemsTemporalesCombo.add(prod);
        mensajeEstado.setText("Ítem añadido al combo (" + itemsTemporalesCombo.size() + " elementos).");
    }

    @FXML
    private void registrarCombo() {
        try {
            double precio = Double.parseDouble(campoPrecioCombo.getText().trim());
            control.registrarComboConfiteria(
                    campoNombreCombo.getText().trim(),
                    precio,
                    new ArrayList<>(itemsTemporalesCombo)
            );
            itemsTemporalesCombo.clear();
            campoNombreCombo.clear();
            campoPrecioCombo.clear();
            refrescarListas();
            mensajeEstado.setText("Combo registrado correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio inválido", "Ingresa un precio válido para el combo.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error al crear combo", e.getMessage());
        }
    }

    @FXML
    private void registrarPromocion() {
        try {
            double porcentaje = Double.parseDouble(campoPorcentajePromocion.getText().trim());
            int puntos = campoPuntosRequeridosPromo.getText().trim().isEmpty()
                    ? 0
                    : Integer.parseInt(campoPuntosRequeridosPromo.getText().trim());

            control.registrarPromocion(comboTipoPromocion.getValue(), porcentaje, puntos);
            campoPorcentajePromocion.clear();
            campoPuntosRequeridosPromo.clear();
            refrescarListas();
            mensajeEstado.setText("Promoción registrada y disponible para compras.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Datos numéricos inválidos", "Verifica el porcentaje y los puntos requeridos.");
        } catch (RuntimeException e) {
            mostrarAlerta("Error al registrar promoción", e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}