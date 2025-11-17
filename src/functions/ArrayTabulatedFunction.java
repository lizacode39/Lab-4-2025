package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] points;
    private int pointsCount;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX)
            throw new IllegalArgumentException(" Incorrect Borders");
        if (pointsCount < 2)
            throw new IllegalArgumentException(" Small number of points");
        points = new FunctionPoint[pointsCount + 10];
        this.pointsCount = pointsCount;
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            points[i] = new FunctionPoint(leftX + interval * i, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        this(leftX, rightX, values.length);
        for (int i = 0; i < pointsCount; ++i)
            points[i].setY(values[i]);
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2)
            throw new IllegalArgumentException(" Number of points is two");
        for (int i = 0; i < points.length - 1; ++i)
            if (points[i].getX() >= points[i + 1].getX())
                throw new IllegalArgumentException(" Invalid X value");
        pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];
        System.arraycopy(points, 0, this.points, 0, pointsCount);
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || x > this.getRightDomainBorder())
            return Double.NaN;
        int i = 0;
        while (x > points[i + 1].getX())
            ++i;
        if (x == points[i + 1].getX())
            return points[i + 1].getY();
        double k = (points[i + 1].getY() - points[i].getY()) / (points[i + 1].getX() - points[i].getX());
        double b = points[i].getY() - k * points[i].getX();
        return k * x + b;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException(index);
        return points[index];
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 1 || index >= pointsCount - 1)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (point.getX() < points[index - 1].getX() || point.getX() > points[index + 1].getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getPoint(index).getX();
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 1 || index >= pointsCount - 1)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (x < points[index - 1].getX() || x > points[index + 1].getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        points[index].setX(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getPoint(index).getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException(index);
        points[index].setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (pointsCount < 3)
            throw new IllegalStateException(" Number of points is two");
        for (int i = index; i < pointsCount; ++i)
            points[i] = pointsCount == (i + 1) ? null : points[i + 1];
        --pointsCount;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int i = 0;
        while (i < pointsCount && points[i].getX() < point.getX())
            ++i;
        if (i < pointsCount && points[i].getX() == point.getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        if (points.length == pointsCount) {
            FunctionPoint[] newpoints = new FunctionPoint[pointsCount + 10];
            System.arraycopy(points, 0, newpoints, 0, pointsCount);
            points = newpoints;
        }
        for (int q = pointsCount; i <= (q - 1); --q)
            points[q] = points[q - 1];
        points[i] = point;
        ++pointsCount;
    }
}
