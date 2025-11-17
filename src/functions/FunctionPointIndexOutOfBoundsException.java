package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

    public FunctionPointIndexOutOfBoundsException() {
        super();
    }

    public FunctionPointIndexOutOfBoundsException(String s) {
        super(s);
    }

    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Index out of range: " + index);
    }

    public FunctionPointIndexOutOfBoundsException(long index) {
        super("Index out of range: " + index);
    }

}
