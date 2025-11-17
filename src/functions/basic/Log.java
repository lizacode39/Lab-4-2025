package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;

    public Log(double n) {
        base = n;
    }

    public Log(Log log) {
        base = log.base;
    }

    public double getLeftDomainBorder() {
        return +0.0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return Math.log(x) / Math.log(base);
    }
}
