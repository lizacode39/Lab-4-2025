package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function first, second;

    public Composition(Function firstFunction, Function secondFunction) {
        first = firstFunction;
        second = secondFunction;
    }

    public double getLeftDomainBorder() {
        return first.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return first.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return first.getFunctionValue(second.getFunctionValue(x));
    }
}
