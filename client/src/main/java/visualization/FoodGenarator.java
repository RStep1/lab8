package visualization;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import data.FuelType;
import data.VehicleType;

import java.awt.Point;
import javafx.scene.image.Image;
import run.MainLauncher;

public class FoodGenarator {
    private static final String[] FOODS_IMAGE = new String[]{MainLauncher.getDatabaseLogoIcon()};
    private List<Point> snakeBody;
    private static final int MAX_NAME_LENGTH = 12;
    private static final int MIN_NAME_LENGTH = 5;

    public FoodGenarator(List<Point> snakeBody) {
        this.snakeBody = snakeBody;
    }

    public Food generateFood() {
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
        String[] vehicleValues = generateVehicleObject(foodX, foodY);
        return new Food(foodX, foodY, foodImage, vehicleValues);
    }

    private String[] generateVehicleObject(int foodX, int foodY) {
        String[] vehicleValues = new String[7];
        vehicleValues[0] = generateVehicleName();//name
        vehicleValues[1] = Integer.toString(foodX);//x
        vehicleValues[2] = Integer.toString(foodY);//y
        vehicleValues[3] = Integer.toString((int) Math.floor(Math.random() * (Integer.MAX_VALUE - 1) + 1)); //engine power
        vehicleValues[4] = Long.toString((long) Math.floor(Math.random() * (Long.MAX_VALUE - 1) + 1)); //distance travelled
        vehicleValues[5] = Integer.toString((int) Math.floor(Math.random() * (VehicleType.values().length) + 1)); // vehicle type
        vehicleValues[6] = Integer.toString((int) Math.floor(Math.random() * (FuelType.values().length) + 1)); //fuel type
        return vehicleValues;
    }

    private String generateVehicleName() {
        int length = (int) Math.random() * MAX_NAME_LENGTH + MIN_NAME_LENGTH;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString;
    }
}
