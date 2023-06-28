package visualization;

import commands.InsertCommand;
import controllers.LoginWindowController;
import data.ClientRequest;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;
import processing.Console;


import java.io.IOException;
import user.Listener;
import utility.AlertCaller;
import visualization.snake.Direction;
import visualization.snake.Snake;

public class SnakeGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final Direction START_DIRECTION = Direction.RIGHT;
    private static final double UPDATE_SPEED = 130;

    private GraphicsContext graphicsContext;
    private Snake snake = new Snake();
    private Food food;
    private FoodGenarator foodGenarator = new FoodGenarator(snake.getSnakeBody());
    private GamePainter gamePainter;
    private boolean gameOver;
    private Direction currentDirection = START_DIRECTION;

    public SnakeGame() {
        
    }

    public void start() {
        Stage stage = new Stage();
        stage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        graphicsContext = canvas.getGraphicsContext2D();

        initKeys(scene);

        gamePainter = new GamePainter(graphicsContext, snake, food);

        this.food = foodGenarator.generateFood();
        gamePainter.updateFood(food);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(UPDATE_SPEED), eventAction -> run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initKeys(Scene scene) {
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case RIGHT, D -> {
                    if (currentDirection != Direction.LEFT)
                        currentDirection = Direction.RIGHT;
                }
                case LEFT, A -> {
                    if (currentDirection != Direction.RIGHT)
                        currentDirection = Direction.LEFT;
                }
                case UP, W -> {
                    if (currentDirection != Direction.DOWN)
                        currentDirection = Direction.UP;
                }
                case DOWN, S -> {
                    if (currentDirection != Direction.UP)
                        currentDirection = Direction.DOWN;
                }
            }
        });
    }

    private void run() {
        if (gameOver) {
            gamePainter.drawGameOver();
            return;
        }
        gamePainter.drawBackground();
        gamePainter.drawFood();
        gamePainter.drawSnake();
        gamePainter.drawScore();
        
        snake.moveSnake(currentDirection);

        gameOver();
        eatFood();
    }


    public void gameOver() {
        if (snake.getSnakeHead().getX() < 0 || snake.getSnakeHead().getY() < 0 || 
            snake.getSnakeHead().getX() * SQUARE_SIZE >= WIDTH || snake.getSnakeHead().getY() * SQUARE_SIZE >= HEIGHT) {
            gameOver = true;
        }
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            if (snake.getSnakeHead().getX() == snake.getSnakeBody().get(i).getX() && 
                snake.getSnakeHead().getY() == snake.getSnakeBody().get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void eatFood() {
        if (snake.getSnakeHead().getX() == food.getFoodX() && snake.getSnakeHead().getY() == food.getFoodY()) {
            // snake.getSnakeBody().add(new Point(-1, -1));
            snake.addBodyPart();
            this.food = foodGenarator.generateFood();
            gamePainter.updateFood(food);
            gamePainter.incremetScore();

            ClientRequest clientRequest = new ClientRequest(InsertCommand.getName(), null, food.getVehicleValues(),
                                                        LoginWindowController.getInstance().getUser());
            try {
                Listener.sendRequest(clientRequest);
            } catch (IOException e) {
                AlertCaller.showErrorDialog("Connection lost", "Please check for firewall issues and check if the server is running.");
                Console.println("Connection lost");
            }
        }
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getColumns() {
        return COLUMNS;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }
}
