
package co.uniquindio.edu.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de la pantalla inicial de CinemaUQ.
 */
public class InicioController {

    @FXML
    private Label mensaje;

    @FXML
    private void iniciarSesion() {
        mensaje.setText("¡Bienvenido a CinemaUQ!");
    }
}
