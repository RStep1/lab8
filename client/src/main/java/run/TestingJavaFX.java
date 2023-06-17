package run;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.LogManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TestingJavaFX extends Application {
    
    private static final String DATABASE_LOGO_ICON_PATH = "images/database_logo_icon.png";
    private static final String APP_TITLE = "Vehicle Database";
    private static final String LOGIN_WINDOW = "../view/LoginWindow.fxml";
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;
    
    @Override
    public void start(Stage stage) {
        // Stage stage = new Stage();

        // Group root = new Group();

        // Scene scene = sceneSettings(root);

        // stageSettings(stage);

        // stage.setScene(scene);
        // stage.show();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_WINDOW));
            // Parent root = FXMLLoader.load(getClass().getResource(LOGIN_WINDOW));
            Parent root = loader.load();
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

            Image app_icon = new Image(DATABASE_LOGO_ICON_PATH);
            stage.getIcons().add(app_icon);
            stage.setTitle(APP_TITLE);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Scene sceneSettings(Group root) {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.AQUA);
        
        Text text = new Text("HELLLOOOOOOO!");
        text.setX(50);
        text.setY(50);
        text.setFont(Font.font("Verdana", 26));
        text.setFill(Color.ORANGE);
        
        Line line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(500);
        line.setEndY(200);
        line.setStrokeWidth(10);
        line.setStroke(Color.CORAL);
        line.setOpacity(0.5);
        line.setRotate(45);
        
        
        Rectangle rectangle = new Rectangle();
        rectangle.setX(100);
        rectangle.setY(100);
        rectangle.setWidth(100);
        rectangle.setHeight(100);
        rectangle.setRotate(30);
        rectangle.setFill(Color.BLUE);
        rectangle.setStrokeWidth(5);
        rectangle.setStroke(Color.BLACK);
        
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
            200.0, 200.0,
            300.0, 300.0,
            200.0, 300.0
        );
        triangle.setFill(Color.AZURE);
        triangle.setStrokeWidth(3);
        triangle.setStroke(Color.BLACK);

        Circle circle = new Circle(400, 400, 50, Color.YELLOW);
        
        Image image = new Image(DATABASE_LOGO_ICON_PATH);
        ImageView imageView = new ImageView(image);
        imageView.setX(100);
        imageView.setY(200);
        imageView.setFitWidth(1000);


        root.getChildren().add(imageView);
        root.getChildren().add(circle);
        root.getChildren().add(triangle);
        root.getChildren().add(rectangle);
        root.getChildren().add(line);
        root.getChildren().add(text);  

        
        return scene;
    }

    private static void stageSettings(Stage stage) {

        Image app_icon = new Image(DATABASE_LOGO_ICON_PATH);

        stage.getIcons().add(app_icon);
        stage.setTitle(APP_TITLE);

        // stage.setWidth(420);
        // stage.setHeight(420);

        // stage.setFullScreen(true);
        // stage.setFullScreenExitHint("YOU CAN'T ESCAPE unless you press 'q' bottom");
        // stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));

        // stage.setX(50);
        // stage.setY(50);
        // stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}