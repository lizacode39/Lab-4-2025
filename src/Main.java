import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        // 1. Создание аналитических функций Sin и Cos
        Sin sinFunc = new Sin();
        Cos cosFunc = new Cos();

        System.out.println("Аналитические функции");
        System.out.println("Границы области определения для Sin: [" +
                sinFunc.getLeftDomainBorder() + ", " + sinFunc.getRightDomainBorder() + "]");
        System.out.println("Границы области определения для Cos: [" +
                cosFunc.getLeftDomainBorder() + ", " + cosFunc.getRightDomainBorder() + "]");

        System.out.println("\nЗначения аналитических функций на отрезке [0, π]");
        System.out.printf("%-8s %-15s %-15s%n", "x", "Sin(x)", "Cos(x)");
        System.out.println("--------------------------------------------------");

        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinValue = sinFunc.getFunctionValue(x);
            double cosValue = cosFunc.getFunctionValue(x);
            System.out.printf("%-8.1f %-15.10f %-15.10f%n", x, sinValue, cosValue);
        }

        //2. Создание табулированных функций
        System.out.println("\nТабулированные функции (10 точек на [0, π])");
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 10);

        System.out.printf("%-8s %-15s %-15s%n", "x", "TabSin(x)", "TabCos(x)");
        System.out.println("--------------------------------------------------");

        for (double x = 0; x <= Math.PI; x += 0.1) {
            double tabSinValue = tabulatedSin.getFunctionValue(x);
            double tabCosValue = tabulatedCos.getFunctionValue(x);
            System.out.printf("%-8.1f %-15.10f %-15.10f%n", x, tabSinValue, tabCosValue);
        }

        //3. Сумма квадратов
        System.out.println("\nСумма квадратов табулированных функций (sin² + cos²)");
        Function squaredSin = new Power(tabulatedSin, 2);
        Function squaredCos = new Power(tabulatedCos, 2);
        Function sumOfSquares = new Sum(squaredSin, squaredCos);

        System.out.printf("%-8s %-15s%n", "x", "sin²(x) + cos²(x)");
        System.out.println("--------------------------------------------------");

        for (double x = 0; x <= Math.PI; x += 0.1) {
            double result = sumOfSquares.getFunctionValue(x);
            System.out.printf("%-8.1f %-15.10f%n", x, result);
        }

        //4. Экспонента: табуляция, запись и чтение
        System.out.println("\nЭкспонента (табулированная функция)");
        Exp expFunc = new Exp();
        TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(expFunc, 0, 10, 11);

        System.out.println("Оригинальная табулированная экспонента:");
        printFunctionPoints(tabulatedExp);

        String expTextFile = "exp_output.txt";
        try {
            // Запись в текстовый файл
            try (FileWriter writer = new FileWriter(expTextFile)) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
            }

            // Чтение из текстового файла
            TabulatedFunction restoredExp;
            try (FileReader reader = new FileReader(expTextFile)) {
                restoredExp = TabulatedFunctions.readTabulatedFunction(reader);
            }

            System.out.println("\nЭкспонента после записи и чтения из текстового файла:");
            printFunctionPoints(restoredExp);

        } catch (IOException e) {
            System.err.println("Ошибка при работе с текстовым файлом экспоненты: " + e.getMessage());
        }

        // 5. Логарифм: табуляция, запись и чтение (бинарный)
        System.out.println("\nЛогарифм (табулированная функция)");
        Log logFunc = new Log(Math.E);
        TabulatedFunction tabulatedLog = TabulatedFunctions.tabulate(logFunc, 0.1, 10, 11); // Начинаем с 0.1, чтобы избежать ln(0)

        System.out.println("Оригинальная табулированная логарифмическая функция:");
        printFunctionPoints(tabulatedLog);

        String logBinaryFile = "log_output.bin";
        try {
            // Запись в бинарный файл
            try (FileOutputStream out = new FileOutputStream(logBinaryFile)) {
                TabulatedFunctions.outputTabulatedFunction(tabulatedLog, out);
            }

            // Чтение из бинарного файла
            TabulatedFunction restoredLog;
            try (FileInputStream in = new FileInputStream(logBinaryFile)) {
                restoredLog = TabulatedFunctions.inputTabulatedFunction(in);
            }

            System.out.println("\nЛогарифм после записи и чтения из бинарного файла:");
            printFunctionPoints(restoredLog);

        } catch (IOException e) {
            System.err.println("Ошибка при работе с бинарным файлом логарифма: " + e.getMessage());
        }

        // 6. Сериализация
        System.out.println("\nСериализация ArrayTabulatedFunction");
        TabulatedFunction serializableFunc = new ArrayTabulatedFunction(0, 10, 5);

        String serialFile = "serialized_func.ser";
        try {
            // Сериализация
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serialFile))) {
                oos.writeObject(serializableFunc);
            }

            // Десериализация
            TabulatedFunction loadedFunc;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serialFile))) {
                loadedFunc = (TabulatedFunction) ois.readObject();
            }

            System.out.println("Сериализация и десериализация ArrayTabulatedFunction успешна:");
            printFunctionPoints(loadedFunc);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при сериализации: " + e.getMessage());
        }
    }

    //Вспомогательный метод для печати точек табулированной функции
    private static void printFunctionPoints(TabulatedFunction func) {
        System.out.printf("%-15s %-15s%n", "X", "Y");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.printf("%-15.6f %-15.6f%n", func.getPointX(i), func.getPointY(i));
        }
    }
}