package visualization;

import javafx.scene.image.Image;

public class Food {
    private int foodX;
    private int foodY;
    private Image imageFood;
    private String[] vehicleValues;

    public Food(int foodX, int foodY, Image imageFood, String[] vehicleValues) {
        this.foodX = foodX;
        this.foodY = foodY;
        this.imageFood = imageFood;
        this.vehicleValues = vehicleValues;
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }

    public Image getImageFood() {
        return imageFood;
    }

    public String[] getVehicleValues() {
        return vehicleValues;
    }
    
}
