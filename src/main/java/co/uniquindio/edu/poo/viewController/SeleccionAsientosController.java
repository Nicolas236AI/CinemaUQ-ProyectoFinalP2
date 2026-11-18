package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import co.uniquindio.edu.poo.model.AsientoFuncionActual;
import co.uniquindio.edu.poo.model.Cliente;
import co.uniquindio.edu.poo.model.EstadoAsiento;
import co.uniquindio.edu.poo.model.Funcion;
import co.uniquindio.edu.poo.model.IElementoConfiteria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class SeleccionAsientosController {

    private final ControlGeneral control = new ControlGeneral();

    private final List<AsientoFuncionActual> asientosSeleccionados = new ArrayList<>();
    private final List<IElementoConfiteria> confiteriaSeleccionada = new ArrayList<>();
    private final ObservableList<String> resumenVisualItems = FXCollections.observableArrayList();

    @FXML private ComboBox<Cliente> comboClienteCompra;
    @FXML private ComboBox<Funcion> comboFuncionCompra;
    @FXML private GridPane gridAsientos;

    @FXML private CheckBox checkBoletoAdicional;
    @FXML private ComboBox<IElementoConfiteria> comboConfiteria;
    @FXML private Spinner<Integer> spinnerCantidadConfiteria;
    @FXML private ListView<String> listaResumenCarrito;
    @FXML private ComboBox<String> comboPromocion;

    @FXML private Label etiquetaAsientosSeleccionados;
    @FXML private Label etiquetaTotalPagar;
    @FXML private Label etiquetaPuntosGanados;
    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        comboClienteCompra.setItems(FXCollections.observableArrayList(control.obtenerClientes()));
        if (control.getClienteActivo() != null) {
            comboClienteCompra.setValue(control.getClienteActivo());
        } else if (!comboClienteCompra.getItems().isEmpty()) {
            comboClienteCompra.getSelectionModel().selectFirst();
        }

        comboFuncionCompra.setItems(FXCollections.observableArrayList(control.obtenerFunciones()));
        comboConfiteria.setItems(FXCollections.observableArrayList(control.obtenerCatalogoConfiteria()));
        spinnerCantidadConfiteria.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));

        comboPromocion.setItems(FXCollections.observableArrayList(control.obtenerNombresPromociones()));
        comboPromocion.getSelectionModel().selectFirst();

        listaResumenCarrito.setItems(resumenVisualItems);

        comboFuncionCompra.setOnAction(e -> {
            limpiarSeleccion();
            dibujarMapaSala(comboFuncionCompra.getValue());
        });

        checkBoletoAdicional.setOnAction(e -> actualizarResumenVisual());
        comboPromocion.setOnAction(e -> actualizarResumenVisual());
        comboClienteCompra.setOnAction(e -> actualizarResumenVisual());

        if (!comboFuncionCompra.getItems().isEmpty()) {
            comboFuncionCompra.getSelectionModel().selectFirst();
            dibujarMapaSala(comboFuncionCompra.getValue());
        }
    }

    private void dibujarMapaSala(Funcion funcion) {
        gridAsientos.getChildren().clear();
        if (funcion == null) {
            return;
        }
        List<AsientoFuncionActual> lista = control.obtenerAsientosDeFuncion(funcion);
        int columnas = 10;
        for (int i = 0; i < lista.size(); i++) {
            AsientoFuncionActual af = lista.get(i);
            Button btnAsiento = crearBotonAsiento(af);
            gridAsientos.add(btnAsiento, i % columnas, i / columnas);
        }
    }

    private Button crearBotonAsiento(AsientoFuncionActual af) {
        String etiqueta = af.getAsiento().getFila() + af.getAsiento().getNumero();
        if (af.getAsiento().isAccesible()) {
            etiqueta += " ♿";
        }
        Button boton = new Button(etiqueta);
        boton.setPrefSize(66, 36);
        aplicarEstiloAsiento(boton, af);

        boton.setOnAction(e -> alternarAsiento(boton, af));
        return boton;
    }

    private void alternarAsiento(Button boton, AsientoFuncionActual af) {
        try {
            if (asientosSeleccionados.contains(af)) {
                control.liberarAsientoTemporal(af);
                asientosSeleccionados.remove(af);
            } else {
                control.reservarAsientoTemporal(af, asientosSeleccionados.size());
                asientosSeleccionados.add(af);
            }
            aplicarEstiloAsiento(boton, af);
            actualizarResumenVisual();
        } catch (RuntimeException ex) {
            mostrarAlerta("Selección de asiento", ex.getMessage());
        }
    }

    private void aplicarEstiloAsiento(Button boton, AsientoFuncionActual af) {
        if (asientosSeleccionados.contains(af)) {
            boton.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: #111827; -fx-font-weight: bold;");
        } else if (af.getEstadoAsiento() == EstadoAsiento.OCUPADO) {
            boton.setStyle("-fx-background-color: #4B5563; -fx-text-fill: #9CA3AF;");
            boton.setDisable(true);
        } else if (af.getAsiento().isAccesible()) {
            boton.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            boton.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void agregarConfiteriaAlCarrito() {
        IElementoConfiteria item = comboConfiteria.getValue();
        int cantidad = spinnerCantidadConfiteria.getValue();
        if (item == null) {
            mostrarAlerta("Confitería", "Selecciona un producto o combo del catálogo.");
            return;
        }
        for (int i = 0; i < cantidad; i++) {
            confiteriaSeleccionada.add(item);
        }
        actualizarResumenVisual();
    }

    private void actualizarResumenVisual() {
        resumenVisualItems.clear();
        for (AsientoFuncionActual af : asientosSeleccionados) {
            resumenVisualItems.add("Boleto " + af.getAsiento().getIdAsiento()
                    + (checkBoletoAdicional.isSelected() ? " (+Adicional)" : ""));
        }
        for (IElementoConfiteria item : confiteriaSeleccionada) {
            resumenVisualItems.add(item.obtenerNombre() + " - $" + item.obtenerPrecio());
        }

        etiquetaAsientosSeleccionados.setText(
                asientosSeleccionados.isEmpty() ? "Asientos: Ninguno" : "Asientos seleccionados: " + asientosSeleccionados.size()
        );

        double total = control.calcularTotalPreliminar(
                comboClienteCompra.getValue(),
                comboFuncionCompra.getValue(),
                asientosSeleccionados.size(),
                checkBoletoAdicional.isSelected(),
                confiteriaSeleccionada,
                comboPromocion.getValue()
        );

        etiquetaTotalPagar.setText(String.format("Total a pagar: $%.2f", total));
        etiquetaPuntosGanados.setText("Puntos a acumular: " + control.calcularPuntosEstimados(total) + " pts");
    }

    @FXML
    private void confirmarYPagarCompra() {
        try {
            control.procesarCompraCompleta(
                    comboClienteCompra.getValue(),
                    comboFuncionCompra.getValue(),
                    new ArrayList<>(asientosSeleccionados),
                    checkBoletoAdicional.isSelected(),
                    new ArrayList<>(confiteriaSeleccionada),
                    comboPromocion.getValue()
            );
            asientosSeleccionados.clear();
            confiteriaSeleccionada.clear();
            dibujarMapaSala(comboFuncionCompra.getValue());
            actualizarResumenVisual();
            mensajeEstado.setText("¡Compra confirmada y pagada exitosamente con tarjeta virtual!");
            mostrarInformacion("Compra exitosa", "Tu compra ha sido procesada y tus puntos fueron acreditados.");
        } catch (RuntimeException ex) {
            mostrarAlerta("No se pudo completar la compra", ex.getMessage());
        }
    }

    @FXML
    private void limpiarSeleccion() {
        for (AsientoFuncionActual af : asientosSeleccionados) {
            control.liberarAsientoTemporal(af);
        }
        asientosSeleccionados.clear();
        confiteriaSeleccionada.clear();
        dibujarMapaSala(comboFuncionCompra.getValue());
        actualizarResumenVisual();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}