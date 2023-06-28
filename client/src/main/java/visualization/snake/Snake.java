package visualization.snake;

import java.util.ArrayList;
import java.util.List;

import visualization.SnakeGame;

import java.awt.Point;

public class Snake {
    private static final int START_POSITION_X = 5;
    private static final int START_POSITION_Y = SnakeGame.getRows() / 2;
    private List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead = new Point();

    public Snake() {
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(START_POSITION_X, START_POSITION_Y));
        }
        snakeHead = snakeBody.get(0);
    }

    public void moveSnake(Direction currentDirection) {
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        snakeHead.x += currentDirection.getMoveX();
        snakeHead.y += currentDirection.getMoveY();
    }


    public void addBodyPart() {
        snakeBody.add(new Point(-1, -1));
    }

    public List<Point> getSnakeBody() {
        return snakeBody;
    }

    public Point getSnakeHead() {
        return snakeHead;
    }

}
