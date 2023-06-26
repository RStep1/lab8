package visualization.snake;

public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0);
    
    private final int moveX;
    private final int moveY;

    private Direction(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }


    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
}
