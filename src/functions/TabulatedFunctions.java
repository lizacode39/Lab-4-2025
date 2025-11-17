package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
        if (function.getLeftDomainBorder() > leftX || function.getRightDomainBorder() < rightX)
            throw new IllegalArgumentException(" Incorrect Borders");
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i)
            points[i] = new FunctionPoint(leftX + interval * i, function.getFunctionValue(leftX + interval * i));
        return new LinkedListTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        try {
            DataOutputStream dataOut = new DataOutputStream(out);
            dataOut.writeInt(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); ++i) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }
        } finally {
            out.close();
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        try {
            DataInputStream dataIn = new DataInputStream(in);
            FunctionPoint[] points = new FunctionPoint[dataIn.readInt()];
            for (int i = 0; i < points.length; ++i)
                points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
            return new LinkedListTabulatedFunction(points);
        } finally {
            in.close();
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        try {
            BufferedWriter buffOut = new BufferedWriter(out);
            String str = String.valueOf(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); ++i)
                str += " " + String.valueOf(function.getPointX(i)) + " " + String.valueOf(function.getPointY(i));
            buffOut.write(str);
            buffOut.flush();
        } finally {
            out.close();
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.nextToken();
            int pointsCount = (int) tokenizer.nval;
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i) {
                tokenizer.nextToken();
                double x = tokenizer.nval;
                boolean crit;
                if (crit = tokenizer.nextToken() == '-')
                    tokenizer.nextToken();
                double y = tokenizer.nval;
                if (tokenizer.sval != null && tokenizer.sval.compareTo("Infinity") == 0)
                    y = crit ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                points[i] = new FunctionPoint(x, y);
            }
            return new LinkedListTabulatedFunction(points);
        } finally {
            in.close();
        }
    }
}
