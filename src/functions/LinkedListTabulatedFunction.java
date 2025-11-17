package functions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LinkedListTabulatedFunction implements TabulatedFunction, java.io.Externalizable {
    private class FunctionNode {
        public FunctionPoint cur;
        public FunctionNode next, prev;

        public FunctionNode() {
            cur = new FunctionPoint();
            next = prev = this;
        }
    }

    private FunctionNode head = new FunctionNode();
    private int length = 0;
    private FunctionNode lastNode = head;
    private int lastIndex = 0;

    public LinkedListTabulatedFunction() {}

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX)
            throw new IllegalArgumentException(" Incorrect Borders");
        if (pointsCount < 2)
            throw new IllegalArgumentException(" Small number of points");
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            addNodeToTail();
            lastNode.cur.setX(leftX + interval * i);
            lastNode.cur.setY(0.0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        this(leftX, rightX, values.length);
        lastNode = head;
        for (int i = 0; i < length; ++i) {
            lastNode = lastNode.next;
            lastNode.cur.setY(values[i]);
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2)
            throw new IllegalArgumentException(" Number of points is two");
        for (int i = 0; i < points.length - 1; ++i) {
            if (Double.compare(points[i].getX(), points[i + 1].getX()) >= 0) { // Исправлено
                throw new IllegalArgumentException(" Invalid X value");
            }
        }
        for (int i = 0; i < points.length; ++i) {
            addNodeToTail().cur = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder() {
        return head.next.cur.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.cur.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        int i = 0;
        while (x > getNodeByIndex(i + 1).cur.getX())
            ++i;
        if (x == lastNode.cur.getX())
            return lastNode.cur.getY();
        double k = (lastNode.cur.getY() - lastNode.prev.cur.getY()) / (lastNode.cur.getX() - lastNode.prev.cur.getX());
        double b = lastNode.cur.getY() - k * lastNode.cur.getX();
        return k * x + b;
    }

    public int getPointsCount() {
        return length;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= length)
            throw new FunctionPointIndexOutOfBoundsException(index);
        return new FunctionPoint(getNodeByIndex(index).cur.getX(), getNodeByIndex(index).cur.getY()); // Возвращаем копию
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 1 || index >= length - 1)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (point.getX() < getNodeByIndex(index - 1).cur.getX() || point.getX() > getNodeByIndex(index + 1).cur.getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        getNodeByIndex(index).cur = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getPoint(index).getX();
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 1 || index >= length - 1)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (x < getNodeByIndex(index - 1).cur.getX() || x > getNodeByIndex(index + 1).cur.getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        getNodeByIndex(index).cur.setX(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getPoint(index).getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= length)
            throw new FunctionPointIndexOutOfBoundsException(index);
        getNodeByIndex(index).cur.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= length)
            throw new FunctionPointIndexOutOfBoundsException(index);
        if (length < 3)
            throw new IllegalStateException(" Number of points is two");
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int i = 0;
        while (i < length && getNodeByIndex(i).cur.getX() < point.getX())
            ++i;
        if (i < length && lastNode.cur.getX() == point.getX())
            throw new InappropriateFunctionPointException(" Invalid X value");
        addNodeByIndex(i);
        lastNode.cur.setX(point.getX());
        lastNode.cur.setY(point.getY());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(length);
        FunctionNode current = head.next;
        for (int i = 0; i < length; i++) {
            out.writeDouble(current.cur.getX());
            out.writeDouble(current.cur.getY());
            current = current.next;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int count = in.readInt();
        length = 0;
        lastNode = head;
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail();
            lastNode.cur = new FunctionPoint(x, y);
        }
    }

    private void getNode(boolean crit, int dist1, int dist2) {
        if (crit) {
            for (int i = 0; i < dist1; ++i)
                lastNode = lastNode.prev;
        } else {
            for (int i = 0; i < dist2; ++i)
                lastNode = lastNode.next;
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (Math.abs(index - lastIndex) > Math.abs(index - length / 2)) {
            lastNode = head;
            getNode(index > length / 2, length - index, index + 1);
        } else
            getNode(index < lastIndex, lastIndex - index, index - lastIndex);
        lastIndex = index;
        return lastNode;
    }

    private void addNode(FunctionNode node) {
        lastNode = new FunctionNode();
        lastNode.prev = node.prev;
        lastNode.next = node;
        node.prev.next = lastNode;
        node.prev = lastNode;
        ++length;
    }

    private FunctionNode addNodeToTail() {
        addNode(head);
        lastIndex = length - 1;
        return lastNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        addNode(node);
        lastIndex = index;
        return lastNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        lastNode = lastNode.next;
        lastNode.prev = node.prev;
        node.prev.next = lastNode;
        node.prev = node.next = node;
        lastIndex = index;
        --length;
        return node;
    }
}