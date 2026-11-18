package co.uniquindio.edu.poo;

import co.uniquindio.edu.poo.model.Cinema;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Cinema.getInstancia().InicializarSistema();

        URL fxmlUrl = App.class.getResource("/co/uniquindio/edu/poo/Inicio.fxml");
        if (fxmlUrl == null) {
            fxmlUrl = App.class.getResource("/co/uniquindio/edu/poo/views/Inicio.fxml");
        }
        if (fxmlUrl == null) {
            throw new IOException("No se encontró Inicio.fxml en resources.");
        }

        Parent root = new FXMLLoader(fxmlUrl).load();
        stage.setTitle("CinemaUQ - Inicio de Sesión");
        stage.setScene(new Scene(root, 1100, 700));
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}