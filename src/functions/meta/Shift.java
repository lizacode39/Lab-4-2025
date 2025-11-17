package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function function;
    private double kX, kY;

    public Shift(Function fun, double kX, double kY) {
        function = fun;
        this.kX = kX;
        this.kY = kY;
    }

    public double getLeftDomainBorder() {
        return kX + function.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return kX + function.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return kY + function.getFunctionValue(kX + x);
    }
}
