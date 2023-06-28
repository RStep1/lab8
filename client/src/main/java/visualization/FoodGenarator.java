package visualization;

import java.util.ArrayList;
import java.util.List;

import data.TableRowVehicle;
import java.awt.Point;
import javafx.scene.image.Image;
import run.MainLauncher;

public class FoodGenarator {
    private static final String[] FOODS_IMAGE = new String[]{MainLauncher.getDatabaseLogoIcon()};
    private List<Point> snakeBody;

    public FoodGenarator(List<Point> snakeBody) {
        this.snakeBody = snakeBody;
    }

    public Food generateFood() {
        String[] vehicleValues = generateVehicleObject();
        boolean isCorrectSpawn;
        int foodX, foodY;
        Image foodImage;
        while (true) {
            isCorrectSpawn = true;
            foodX = (int) (Math.random() * SnakeGame.getRows());
            foodY = (int) (Math.random() * SnakeGame.getColumns());

            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    isCorrectSpawn = false;
                    break;
                }
            }
            if (!isCorrectSpawn)
                continue;
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
            break;
        }
        return new Food(foodX, foodY, foodImage, vehicleValues);
    }

    private String[] generateVehicleObject() {
        String[] vehicleValues = new String[7];
        

        return vehicleValues;
    }
}
