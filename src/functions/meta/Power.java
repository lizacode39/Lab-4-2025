package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function function;
    private double degree;

    public Power(Function fun, double degree) {
        function = fun;
        this.degree = degree;
    }

    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return function.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return Math.pow(function.getFunctionValue(x), degree);
    }
}
