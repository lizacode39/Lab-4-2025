package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function first, second;
    private boolean intersection;

    public Mult(Function firstFunction, Function secondFunction) {
        first = firstFunction;
        second = secondFunction;
        intersection = first.getLeftDomainBorder() > second.getRightDomainBorder()
        || second.getLeftDomainBorder() > first.getRightDomainBorder();
    }

    public double getLeftDomainBorder() {
        return intersection ? Double.NaN : Math.max(first.getLeftDomainBorder(), second.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return intersection ? Double.NaN : Math.min(first.getRightDomainBorder(), second.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        return intersection ? Double.NaN : (first.getFunctionValue(x) * second.getFunctionValue(x));
    }
}
