
package co.uniquindio.edu.poo.viewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PanelPrincipalController {

    @FXML
    private BorderPane panelPrincipal;

    @FXML
    private VBox vistaInicio;

    @FXML
    private Label mensajeEstado;

    @FXML
    private void initialize() {
        mostrarInicio();
    }

    @FXML
    private void mostrarInicio() {
        panelPrincipal.setCenter(vistaInicio);
        mensajeEstado.setText("Panel principal");
    }

    @FXML
    private void mostrarCartelera() {
        cargarVista("Cartelera.fxml");
        mensajeEstado.setText("Sección: Cartelera");
    }

    @FXML
    private void mostrarFunciones() {
        cargarVista("Funciones.fxml");
        mensajeEstado.setText("Sección: Funciones");
    }

    @FXML
    private void mostrarCompras() {
        cargarVista("Compras.fxml");
        mensajeEstado.setText("Sección: Compras");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("Reportes.fxml");
        mensajeEstado.setText("Sección: Reportes");
    }

    @FXML
    private void mostrarAdministracion() {
        cargarVista("Administracion.fxml");
        mensajeEstado.setText("Sección: Administración");
    }

    private void cargarVista(String archivo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/co/uniquindio/edu/poo/" + archivo
                    )
            );

            Parent vista = loader.load();
            panelPrincipal.setCenter(vista);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            mensajeEstado.setText(
                    "No se pudo cargar la sección: " + archivo
            );
        }
    }
}
