package utility;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertCaller {

    public static void infoAlert(String info) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(info);
            alert.showAndWait();
        });
    }

    public static void errorAlert(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(error);
            alert.showAndWait();
        });
    }

    public static void showErrorDialog(String message, String contentText) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText(contentText);
            alert.showAndWait();
        });
    }
}