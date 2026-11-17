
package co.uniquindio.edu.poo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/co/uniquindio/edu/poo/PanelPrincipal.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 750);

        stage.setTitle("CinemaUQ - Sistema de Gestión de Cine");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setResizable(true);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
