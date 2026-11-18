package co.uniquindio.edu.poo.viewController;

import co.uniquindio.edu.poo.controller.ControlGeneral;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PanelPrincipalController {

    private final ControlGeneral control = new ControlGeneral();

    @FXML
    private BorderPane panelPrincipal;

    @FXML
    private VBox vistaInicio;

    @FXML
    private Label etiquetaUsuarioActivo;

    @FXML
    private Label etiquetaMenuAdmin;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnCartelera;

    @FXML
    private Button btnFunciones;

    @FXML
    private Button btnReservarAsientos;

    @FXML
    private Button btnMiCuenta;

    @FXML
    private Button btnConfiteria;

    @FXML
    private Button btnCompras;

    @FXML
    private Button btnReportes;

    @FXML
    private Button btnAdministracion;

    @FXML
    private Label tituloContenido;

    @FXML
    private Label descripcionContenido;

    @FXML
    private Label statPeliculas;

    @FXML
    private Label statFunciones;

    @FXML
    private Label statCompras;

    @FXML
    private Label statIngresos;

    @FXML
    private Label mensajeEstado;

    @FXML
    private void initialize() {
        configurarPermisosVisuales();
        mostrarInicio();
    }

    private void configurarPermisosVisuales() {
        etiquetaUsuarioActivo.setText(control.obtenerTextoUsuarioActivo());
        boolean esAdmin = control.esAdministradorActivo();

        etiquetaMenuAdmin.setVisible(esAdmin);
        etiquetaMenuAdmin.setManaged(esAdmin);

        btnConfiteria.setVisible(esAdmin);
        btnConfiteria.setManaged(esAdmin);

        btnCompras.setVisible(esAdmin);
        btnCompras.setManaged(esAdmin);

        btnReportes.setVisible(esAdmin);
        btnReportes.setManaged(esAdmin);

        btnAdministracion.setVisible(esAdmin);
        btnAdministracion.setManaged(esAdmin);
    }

    private void actualizarEstadisticasInicio() {
        statPeliculas.setText(String.valueOf(control.contarPeliculas()));
        statFunciones.setText(String.valueOf(control.contarFunciones()));
        statCompras.setText(String.valueOf(control.contarComprasCompletadas()));
        statIngresos.setText(String.format("$%.2f", control.calcularIngresosTotales()));
    }

    @FXML
    private void mostrarInicio() {
        actualizarEstadisticasInicio();
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
    private void mostrarSeleccionAsientos() {
        cargarVista("SeleccionAsientos.fxml");
        mensajeEstado.setText("Sección: Reserva de Entradas y Asientos");
    }

    @FXML
    private void mostrarMiCuenta() {
        cargarVista("MiCuenta.fxml");
        mensajeEstado.setText("Sección: Mi Tarjeta Virtual y Puntos");
    }

    @FXML
    private void mostrarConfiteria() {
        cargarVista("Confiteria.fxml");
        mensajeEstado.setText("Sección: Confitería, Combos y Promociones");
    }

    @FXML
    private void mostrarCompras() {
        cargarVista("Compras.fxml");
        mensajeEstado.setText("Sección: Historial de Compras");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("Reportes.fxml");
        mensajeEstado.setText("Sección: Reportes y Gráficos");
    }

    @FXML
    private void mostrarAdministracion() {
        cargarVista("Administracion.fxml");
        mensajeEstado.setText("Sección: Administración");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        control.cerrarSesion();
        try {
            java.net.URL url = resolverUrlFxml("Inicio.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeEstado.setText("No se pudo volver a la pantalla de inicio.");
        }
    }

    private void cargarVista(String archivo) {
        try {
            java.net.URL url = resolverUrlFxml(archivo);
            FXMLLoader loader = new FXMLLoader(url);
            Parent vista = loader.load();
            panelPrincipal.setCenter(vista);
        } catch (Exception e) {
            e.printStackTrace();
            mensajeEstado.setText("Error cargando " + archivo + ": " + e.getCause());
        }
    }
    private java.net.URL resolverUrlFxml(String archivo) {
        String[] rutas = {
                "/views/" + archivo,
                "/co/uniquindio/edu/poo/views/" + archivo,
                "/co/uniquindio/edu/poo/" + archivo
        };
        for (String ruta : rutas) {
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                return url;
            }
        }
        throw new IllegalStateException("No se encontró el archivo FXML: " + archivo);
    }
}