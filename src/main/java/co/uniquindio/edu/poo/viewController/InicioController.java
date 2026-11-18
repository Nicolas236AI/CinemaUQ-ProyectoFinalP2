package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioController {

    private final ControlGeneral control = new ControlGeneral();

    @FXML
    private ComboBox<String> comboRol;

    @FXML
    private TextField campoCorreoLogin;

    @FXML
    private PasswordField campoContrasenaLogin;

    @FXML
    private TextField campoIdRegistro;

    @FXML
    private TextField campoNombreRegistro;

    @FXML
    private TextField campoCorreoRegistro;

    @FXML
    private PasswordField campoContrasenaRegistro;

    @FXML
    private Label mensaje;

    @FXML
    private void initialize() {
        comboRol.setItems(FXCollections.observableArrayList("Cliente", "Administrador"));
        comboRol.getSelectionModel().selectFirst();
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        try {
            control.iniciarSesion(
                    comboRol.getValue(),
                    campoCorreoLogin.getText(),
                    campoContrasenaLogin.getText()
            );
            abrirPanelPrincipal(event);
        } catch (IllegalArgumentException e) {
            mensaje.setText(e.getMessage());
            mostrarAlerta("Acceso denegado", e.getMessage());
        }
    }

    @FXML
    private void ingresarModoDemo(ActionEvent event) {
        control.iniciarModoDemo();
        abrirPanelPrincipal(event);
    }

    @FXML
    private void registrarClienteNuevo(ActionEvent event) {
        try {
            control.registrarNuevoClienteConCuenta(
                    campoIdRegistro.getText(),
                    campoNombreRegistro.getText(),
                    campoCorreoRegistro.getText(),
                    campoContrasenaRegistro.getText()
            );
            abrirPanelPrincipal(event);
        } catch (IllegalArgumentException e) {
            mensaje.setText(e.getMessage());
            mostrarAlerta("Error en el registro", e.getMessage());
        }
    }

    private void abrirPanelPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/uniquindio/edu/poo/PanelPrincipal.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error de navegación", "No se pudo abrir el panel principal.");
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