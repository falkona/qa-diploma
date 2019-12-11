package ru.netology.utils;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Random;

public class DataGenerator {

    public static String name() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    public static String currentYear() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear() - 2000;
        return Integer.toString(year);
    }

    public static String validFutureYear() {
        Random random = new Random();
        int i = random.nextInt(5) + 1;

        LocalDate futureDate = LocalDate.now().plusYears(i);
        int year = futureDate.getYear() - 2000;

        return Integer.toString(year);
    }

    public static String pastYear() {
        Random random = new Random();
        int i = random.nextInt(19) + 1;
        return String.format("%02d", i);
    }

    public static String currentMonth() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        return String.format("%02d", month);
    }

    public static String anyMonth() {
        Random random = new Random();
        int month = random.nextInt(12) + 1;
        return String.format("%02d", month);
    }

    public static String pastMonth() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue() - 1;
        return String.format("%02d", month);
    }

    public static String cvc() {
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        int c = random.nextInt(10);
        return Integer.toString(a).concat(Integer.toString(b)).concat(Integer.toString(c));
    }

    public static String shortCvc() {
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        return Integer.toString(a).concat(Integer.toString(b));
    }

    public static String invalidCardNumber() {
        Faker faker = new Faker();
        Random random = new Random();
        int i = random.nextInt(15) + 1;
        return Long.toString(faker.number().randomNumber(i, true));
    }

    public static String randomCardNumber() {
        Faker faker = new Faker();
        return Long.toString(faker.number().randomNumber(16, true));
    }

    public static String approvedCardNumber() {
        return "4444444444444441";
    }

    public static String declinedCardNumber() {
        Faker faker = new Faker();
        return "4444444444444442";
    }

}
