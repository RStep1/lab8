package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DatabaseWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button databaseQuitButton;

    @FXML
    void quit(ActionEvent event) {
         System.out.println("logout");
    }

    @FXML
    void initialize() {

    }

}
