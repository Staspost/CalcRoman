package com.shcherbakov;

class Parser {
    private static String numberOneRoman="";
    private static String numberTwoRoman="";
    private static String numberOneArabian="";
    private static String numberTwoArabian="";
    private static int numberOne;
    private static int numberTwo;
    private static String sign;
    private static String[] arrText;
    private static boolean flagSign = false;
    private static double result;

    protected static String Parsing(String calculation) //на входе получаем введенную пользователем строку
    {
        calculation = calculation.trim(); //очищаем от пробелов в начале и конце строки
        int count = calculation.length(); //подсчитываем длину строки

        //проверка на корректность места расположения оператора
        try {
            CheckCorrectSignPosition(calculation, count);
        } catch (CheckException e) {
            System.out.println(e.getMessage());
            Runtime.getRuntime().exit(1);
        }
        //разбираем строку в массив и проверяем каждый символ по отдельности
        arrText = calculation.split("");
        for (int i = 0; i < count; i++) {
            try {
                CheckCorrect(arrText[i]);
            } catch (CheckException e) {
                System.out.println(e.getMessage());
                Runtime.getRuntime().exit(1);
            }
        }

        //проверка на корректность чисел (не смешаны ли римские и арабские цифры в разных частях выражения)
        try {
            CheckMixArabianAndRoman(numberOneRoman, numberOneArabian, numberTwoRoman, numberTwoArabian);
        } catch (CheckException e) {
            System.out.println(e.getMessage());
            Runtime.getRuntime().exit(1);
        }

        //проверка на корректность чисел (не смешаны ли римские и арабские цифры в левой части выражения)
        try {
            CheckCorrectNumberOne(numberOneRoman, numberOneArabian);
        } catch (CheckException e) {
            System.out.println(e.getMessage());
            Runtime.getRuntime().exit(1);
        }

        //проверка на корректность чисел (не смешаны ли римские и арабские цифры в правой части выражения)
        try {
            CheckCorrectNumberTwo(numberTwoRoman, numberTwoArabian);
        } catch (CheckException e) {
            System.out.println(e.getMessage());
            Runtime.getRuntime().exit(1);
        }

        //поскольку только одна из двух переменных содержит в себе строку после предыдущей проверки, проверяем какая именно и конвертируем в число
        if (numberOneRoman.length()>0) numberOne = ConvertRomanToArabian(numberOneRoman); //конвертируем римские числа в арабские
        if (numberTwoRoman.length()>0) numberTwo = ConvertRomanToArabian(numberTwoRoman);
        if (numberOneArabian.length()>0) numberOne = Integer.parseInt(numberOneArabian); //получаем число из строки
        if (numberTwoArabian.length()>0) numberTwo = Integer.parseInt(numberTwoArabian);

        // проверка на размер числа
        try {
            CheckSize(numberOne, numberTwo);
        } catch (CheckException e) {
            System.out.println(e.getMessage());
            Runtime.getRuntime().exit(1);
        }

        //если все проверки прошли, запускаем расчет
        result = Сalculation(numberOne, numberTwo, sign);
        String resultStr;
        //проверяем, были ли цифры римскими, если да, то конвертируем результат в римские цифры
        if (numberOneRoman.length()>0 && numberTwoRoman.length()>0)
        {
            int res = (int) result; //конвертируем результат в целое, для упрощения
            resultStr = Rim.ConvertArabianToRoman(res);
        }
        else resultStr = Double.toString(result);

        return resultStr;
    }



    //проверяем корректность расположение оператора
    private static void CheckCorrectSignPosition(String calculation, int count) throws CheckException
    {
        //для каждого из возможных операторов определяем его позицию в строке, чтобы понять, один ли он в строке
        int positionDivide = calculation.indexOf("/");
        int positionMultiply = calculation.indexOf("*");
        int positionPlus = calculation.indexOf("+");
        int positionMinus = calculation.indexOf("-");
        int countDivide = calculation.lastIndexOf("/");
        int countMultiply = calculation.lastIndexOf("*");
        int countPlus = calculation.lastIndexOf("+");
        int countMinus = calculation.lastIndexOf("-");

        //проверяем, что в строке только один оператор
        int x=0;
        int y=0;
        if (positionDivide==-1) x++;
        else if (positionDivide!=countDivide) y++;
        if (positionMultiply==-1) x++;
        else if (positionMultiply!=countMultiply) y++;
        if (positionPlus==-1) x++;
        else if (positionPlus!=countPlus) y++;
        if (positionMinus==-1) x++;
        else if (positionMinus!=countMinus) y++;
        if (countDivide==-1) x++;
        if (countMultiply==-1) x++;
        if (countPlus==-1) x++;
        if (countMinus==-1) x++;
        if (x != 6 || y > 0)
        {
            throw new CheckException("Неверный формат данных: не допускается использование более одного оператора.");
        }
        if (positionDivide == 0 || positionMultiply == 0 || positionPlus == 0 || positionMinus == 0)
        {
            throw new CheckException("Неверный формат данных: оператор не может находиться в начале строки.");
        }
        if (positionDivide == count-1 || positionMultiply == count-1 || positionPlus == count-1 || positionMinus == count-1)
        {
            throw new CheckException("Неверный формат данных: оператор не может находиться в конце строки.");
        }
        if (positionDivide == -1 && positionMultiply == -1 && positionPlus == -1 && positionMinus == -1)
        {
            throw new CheckException("Неверный формат данных: оператор не может отсутствовать.");
        }
    }

    //проверяем на корректность каждый отдельный символ
    private static void CheckCorrect(String symbol)  throws CheckException
    {
        if ((symbol.equals("0") || symbol.equals("1") || symbol.equals("2") || symbol.equals("3") || symbol.equals("4") || symbol.equals("5") || symbol.equals("6") || symbol.equals("7") || symbol.equals("8") || symbol.equals("9")) && flagSign == false)
        {
            AddArabian(symbol, flagSign);
        }
        else if ((symbol.equals("0") || symbol.equals("1") || symbol.equals("2") || symbol.equals("3") || symbol.equals("4") || symbol.equals("5") || symbol.equals("6") || symbol.equals("7") || symbol.equals("8") || symbol.equals("9")) && flagSign == true)
        {
            AddArabian(symbol, flagSign);
        }
        else if ((symbol.equals("*") || symbol.equals("/") || symbol.equals("+") || symbol.equals("-")) && flagSign == false)
        {
            sign = symbol;
            flagSign = true;
        }
        else if ((symbol.equals("I") || symbol.equals("V") || symbol.equals("X")) && flagSign == false)
        {
            AddRoman(symbol, flagSign);
        }
        else if ((symbol.equals("I") || symbol.equals("V") || symbol.equals("X")) && flagSign == true)
        {
            AddRoman(symbol, flagSign);
        }
        else
        {
            throw new CheckException("Неверный формат данных: обнаружен недопустимый символ '" + symbol + "'.");
        }
    }

    //собираем арабские цифры в строку
    private static void AddArabian(String symbol, boolean flagSign)
    {
        if (flagSign) numberTwoArabian = numberTwoArabian + symbol;
        else numberOneArabian = numberOneArabian + symbol;
    }

    //собираем римские цифры в строку
    private static void AddRoman(String symbol, boolean flagSign)
    {
        if (flagSign) numberTwoRoman = numberTwoRoman + symbol;
        else numberOneRoman = numberOneRoman + symbol;
    }

    //проверка на корректность чисел (не смешаны ли римские и арабские цифры в разных частях выражения)
    private static void CheckMixArabianAndRoman(String numberOneRoman, String numberOneArabian, String numberTwoRoman, String numberTwoArabian) throws CheckException
    {
        if ((numberOneRoman.length()>0 && numberTwoArabian.length()>0) || (numberTwoRoman.length()>0 && numberOneArabian.length()>0))
        {
            throw new CheckException("Неверный формат данных: обнаружено смешение римских и арабских цифр в разных частях выражения.");
        }
    }

    //проверка на корректность чисел (не смешаны ли римские и арабские цифры в левой части выражения)
    private static void CheckCorrectNumberOne(String numberOneRoman, String numberOneArabian) throws CheckException
    {
        if (numberOneRoman.length()>0 && numberOneArabian.length()>0) {
            throw new CheckException("Неверный формат данных: обнаружено смешение римских и арабских цифр в левой части выражения.");
        }
    }

    //проверка на корректность чисел (не смешаны ли римские и арабские цифры в левой части выражения)
    private static void CheckCorrectNumberTwo(String numberTwoRoman, String numberTwoArabian)  throws CheckException
    {
        if (numberTwoRoman.length()>0 && numberTwoArabian.length()>0)
        {
            throw new CheckException("Неверный формат данных: обнаружено смешение римских и арабских цифр в правой части выражения.");
        }
    }

    //проверяем размер чисел
    private static void CheckSize(int numberOne, int numberTwo) throws CheckException
    {
        if (numberOne>10 || numberTwo>10)
        {
            throw new CheckException("Введенное число превышает допустимое значение '10'.");
        }
        if (numberOne<1 || numberTwo<1)
        {
            throw new CheckException("Введенное число ниже допустимого значения '1'.");
        }
    }

    private static double Сalculation(double numberOne, double numberTwo, String sign) {
        switch (sign)
        {
            case "+":
                result = numberOne + numberTwo;
                break;
            case "-":
                result = numberOne - numberTwo;
                break;
            case "*":
                result = numberOne * numberTwo;
                break;
            case "/":
                result = numberOne / numberTwo;
                break;
        }
        return result;
    }

    private static int ConvertRomanToArabian(String numberRoman) {
        int num;
            switch (numberRoman)
            {
                case "I":
                  num = 1;
                    break;
                case "II":
                    num = 2;
                break;
                case "III":
                    num = 3;
                break;
                case "IV":
                    num = 4;
                break;
                case "V":
                    num = 5;
                break;
                case "VI":
                    num = 6;
                break;
                case "VII":
                    num = 7;
                break;
                case "VIII":
                    num = 8;
                break;
                case "IX":
                    num = 9;
                break;
                case "X":
                    num = 10;
                break;
                default:
                    System.out.println("Введенное число '" + numberRoman + "' превышает допустимое значение.");
                    num = 100; //условное число, сигнализирующее о превышении размерности числа
                    break;
            }
            return num;
    }







    public static class CheckException extends Exception {

        public CheckException(String message) {
            super(message);
        }
    }
}
