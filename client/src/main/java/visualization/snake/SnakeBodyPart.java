package visualization.snake;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class SnakeBodyPart {
    private Rectangle rectangle;
    private int startPositionX;
    private int startPositionY;
    
    public SnakeBodyPart(int size, int startPositionX, int startPositionY) {
        this.startPositionX = startPositionX;
        this.startPositionY = startPositionY;
        //-TESTING-
        rectangle = new Rectangle(startPositionX, startPositionY, size, size);
        rectangle.setFill(Color.BLACK);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getStartPositionX() {
        return startPositionX;
    }

    public int getStartPositionY() {
        return startPositionY;
    }
}
