package data;

import java.io.Serializable;

/**
 * Contains two coordinate variables for Vehicle class.
 */
public class Coordinates implements Serializable {
    private float x;
    private double y;
    private static final int ACCURACY = 5;

    public Coordinates(float x, double y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public static int getAccuracy() {
        return ACCURACY;
    }

    @Override
    public String toString() {
        return String.format("(%." + ACCURACY + "f; %." + ACCURACY + "f)", x, y);
    }

}
