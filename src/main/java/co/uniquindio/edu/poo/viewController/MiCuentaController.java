package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class MiCuentaController {

    private final ControlGeneral control = new ControlGeneral();
    private final ObservableList<Compra> misCompras = FXCollections.observableArrayList();

    @FXML private ComboBox<Cliente> comboClienteCuenta;
    @FXML private Label etiquetaIdTarjeta;
    @FXML private Label etiquetaSaldoTarjeta;
    @FXML private TextField campoMontoRecarga;
    @FXML private ListView<String> listaMovimientosTarjeta;

    @FXML private Label etiquetaPuntosVigentes;
    @FXML private ListView<String> listaLotesPuntos;

    @FXML private TableView<Compra> tablaMisCompras;
    @FXML private TableColumn<Compra, String> colMiCompraId;
    @FXML private TableColumn<Compra, String> colMiCompraFecha;
    @FXML private TableColumn<Compra, Double> colMiCompraTotal;
    @FXML private TableColumn<Compra, EstadoCompra> colMiCompraEstado;
    @FXML private TableColumn<Compra, Double> colMiCompraReembolso;

    @FXML private Label mensajeEstado;

    @FXML
    private void initialize() {
        comboClienteCuenta.setItems(FXCollections.observableArrayList(control.obtenerClientes()));
        if (control.getClienteActivo() != null) {
            comboClienteCuenta.setValue(control.getClienteActivo());
            comboClienteCuenta.setDisable(true);
        } else if (!comboClienteCuenta.getItems().isEmpty()) {
            comboClienteCuenta.getSelectionModel().selectFirst();
        }

        colMiCompraId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colMiCompraFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMiCompraTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colMiCompraEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colMiCompraReembolso.setCellValueFactory(new PropertyValueFactory<>("montoReembolsado"));
        tablaMisCompras.setItems(misCompras);

        comboClienteCuenta.setOnAction(e -> refrescarDatosCuenta());
        refrescarDatosCuenta();
    }

    private void refrescarDatosCuenta() {
        Cliente cliente = comboClienteCuenta.getValue();
        if (cliente == null) {
            return;
        }
        etiquetaIdTarjeta.setText(control.obtenerIdTarjetaCliente(cliente));
        etiquetaSaldoTarjeta.setText(String.format("$%.2f", control.obtenerSaldoTarjetaCliente(cliente)));
        listaMovimientosTarjeta.setItems(FXCollections.observableArrayList(control.obtenerMovimientosCliente(cliente)));

        etiquetaPuntosVigentes.setText(control.obtenerPuntosVigentesCliente(cliente) + " pts");
        listaLotesPuntos.setItems(FXCollections.observableArrayList(control.obtenerDetalleLotesPuntosCliente(cliente)));

        misCompras.setAll(control.obtenerComprasDeCliente(cliente));
        tablaMisCompras.refresh();
    }

    @FXML
    private void solicitarRecarga() {
        try {
            double monto = Double.parseDouble(campoMontoRecarga.getText().trim());
            control.solicitarRecargaTarjeta(comboClienteCuenta.getValue(), monto);
            campoMontoRecarga.clear();
            mensajeEstado.setText("Solicitud de recarga enviada. Pendiente de aprobación del administrador.");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Solicitud enviada",
                    "Tu solicitud de recarga quedó registrada y será acreditada al aprobarse.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Monto inválido", "Ingresa un valor numérico positivo.");
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error en solicitud", e.getMessage());
        }
    }

    @FXML
    private void redimirEntrada() {
        ejecutarRedencion("ENTRADA");
    }

    @FXML
    private void redimirProducto() {
        ejecutarRedencion("PRODUCTO");
    }

    @FXML
    private void redimirCombo() {
        ejecutarRedencion("COMBO");
    }

    private void ejecutarRedencion(String tipoBeneficio) {
        try {
            control.redimirPuntosCliente(comboClienteCuenta.getValue(), tipoBeneficio);
            refrescarDatosCuenta();
            mensajeEstado.setText("Beneficio redimido exitosamente: " + tipoBeneficio);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Redención exitosa",
                    "Has redimido tus puntos por: " + tipoBeneficio);
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Puntos insuficientes", e.getMessage());
        }
    }

    @FXML
    private void cancelarCompraCliente() {
        Compra seleccionada = tablaMisCompras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Selecciona una compra de tu historial para cancelar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar compra");
        confirmacion.setHeaderText("¿Deseas cancelar la compra " + seleccionada.getIdCompra() + "?");
        confirmacion.setContentText("Se aplicará la política de reembolso vigente según el tiempo restante para la función.");

        Optional<ButtonType> res = confirmacion.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                double reembolso = control.cancelarCompraConPolitica(seleccionada, false);
                refrescarDatosCuenta();
                mensajeEstado.setText(String.format("Compra cancelada. Reembolso acreditado: $%.2f", reembolso));
            } catch (RuntimeException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Cancelación no permitida", e.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}