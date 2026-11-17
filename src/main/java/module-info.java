module co.uniquindio.edu.poo {

    requires javafx.controls;
    requires javafx.fxml;

    exports co.uniquindio.edu.poo;

    opens co.uniquindio.edu.poo.controller to javafx.fxml;
    opens co.uniquindio.edu.poo.viewController to javafx.fxml;
    opens co.uniquindio.edu.poo.model;

}