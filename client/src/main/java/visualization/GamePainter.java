package visualization;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import visualization.snake.Snake;
import javafx.scene.canvas.GraphicsContext;

public class GamePainter {
    private static final int SQUARE_SIZE = SnakeGame.getWidth() / SnakeGame.getRows();
    private static final String DEFAULT_FONT = "Digital-7";
    private GraphicsContext graphicsContext;
    private int score = 0;
    private Snake snake;
    private Food food;
    
    public GamePainter(GraphicsContext graphicsContext, Snake snake, Food food) {
        this.graphicsContext = graphicsContext;
        this.snake = snake;
        this.food = food;
    }

    public void drawBackground() {
        for (int i = 0; i < SnakeGame.getRows(); i++) {
            for (int j = 0; j < SnakeGame.getColumns(); j++) {
                if ((i + j) % 2 == 0) {
                    graphicsContext.setFill(Color.web("AAD751"));
                } else {
                    graphicsContext.setFill(Color.web("A2D149"));
                }
                graphicsContext.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    public void drawFood() {
        Image foodImage = food.getImageFood();
        int foodX = food.getFoodX();
        int foodY = food.getFoodY();
        graphicsContext.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void drawSnake() {
        graphicsContext.setFill(Color.web("4674E9"));
        graphicsContext.fillRoundRect(snake.getSnakeHead().getX() * SQUARE_SIZE, snake.getSnakeHead().getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            graphicsContext.fillRoundRect(snake.getSnakeBody().get(i).getX() * SQUARE_SIZE, snake.getSnakeBody().get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1, 20, 20);
        }
    }

    public void drawScore() {
        this.graphicsContext.setFill(Color.WHITE);
        this.graphicsContext.setFont(new Font(DEFAULT_FONT, 35));
        this.graphicsContext.fillText("Score: " + score, 10, 35);
    }

    public void drawGameOver() {
        graphicsContext.setFill(Color.RED);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 70));
        graphicsContext.fillText("Game Over", SnakeGame.getWidth() / 3.5, SnakeGame.getHeight() / 2);
    }

    public void incremetScore() {
        this.score++;
    }

    public void updateFood(Food food) {
        this.food = food;
    }
}
