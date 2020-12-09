package com.shcherbakov;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static String name;
    public static String result;

    public static void main(String[] args) {

        System.out.println("Введите пример:");
        Scanner inText = new Scanner(System.in);
        name = inText.nextLine();
        name = name.replace(" ",""); //очищаем от пробелов
        name = name.toUpperCase(Locale.ROOT); //переводим в верхний регистр
        result = Parser.Parsing(name); //отправляем на разбор
        System.out.println(result); //выводим результат
    }
}
