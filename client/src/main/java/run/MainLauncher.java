package run;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;
import user.Listener;


public class MainLauncher extends Application {
    private static final String DATABASE_LOGO_ICON_PATH = "images/database_logo_icon.png";
    private static final String APP_TITLE = "Vehicle Database";
    private static final String LOGIN_WINDOW_PATH = "../view/LoginWindow.fxml";
    private static final String DATABASE_WINDOW_PATH = "../view/DatabaseWindow.fxml";
    private static final String VISUALIZATION_WINDOW_PATH = "../view/VisualizationWindow.fxml";
    private static final double SCENE_WIDTH = 1400;
    private static final double SCENE_HEIGHT = 750;
    private static Stage primaryStageObjcect;
    // private static String host;
    // private static int port;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStageObjcect = primaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_WINDOW_PATH));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle(APP_TITLE);
            primaryStage.getIcons().add(new Image(DATABASE_LOGO_ICON_PATH));
            Scene mainScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            primaryStage.setScene(mainScene);
            primaryStage.show();
            // primaryStage.setOnCloseRequest(event -> Platform.exit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // host = args[0];
        // port = Integer.parseInt(args[1]);
        launch();
    }

    // public static String getHost() {
    //     return host;
    // }

    // public static int getPort() {
    //     return port;
    // }

    public static String getLoginWindowPahth() {
        return LOGIN_WINDOW_PATH;
    }

    public static String getDatabaseWindowPath() {
        return DATABASE_WINDOW_PATH;
    }
    public static String getVisualizationWindowPath() {
        return VISUALIZATION_WINDOW_PATH;
    }

    public static Stage getPrimaryStage() {
        return primaryStageObjcect;
    }

    public static double getSceneWidth() {
        return SCENE_WIDTH;
    }

    public static double getSceneHeight() {
        return SCENE_HEIGHT;
    }

    public static String getDatabaseLogoIcon() {
        return DATABASE_LOGO_ICON_PATH;
    }
    
}
