package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x, y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        x = point.x;
        y = point.y;
    }

    public FunctionPoint() {
        x = y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}