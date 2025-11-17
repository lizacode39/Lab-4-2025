package functions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ArrayTabulatedFunction implements TabulatedFunction, java.io.Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;

    public ArrayTabulatedFunction() {
        this.points = null;
        this.pointsCount = 0;
    }

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
        for (int i = 0; i < points.length - 1; ++i) {
            if (Double.compare(points[i].getX(), points[i + 1].getX()) >= 0) {
                throw new IllegalArgumentException(" Invalid X value");
            }
        }
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i].getX(), points[i].getY());
        }
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
        return new FunctionPoint(points[index].getX(), points[index].getY()); // Возвращаем копию
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
        for (int i = index; i < pointsCount - 1; ++i) {
            points[i] = points[i + 1];
        }
        points[pointsCount - 1] = null;
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
        for (int q = pointsCount; q > i; --q) {
            points[q] = points[q - 1];
        }
        points[i] = point;
        ++pointsCount;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }
}