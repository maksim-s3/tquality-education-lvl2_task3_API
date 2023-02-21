package utils;

import com.github.javafaker.Faker;

public class Random {
    private static final int MIN = 10;
    private static final int MAX = 100;
    private static Faker faker = new Faker();

    public static String getRandomText(int min, int max) {
        String text = faker.internet().password(min, max);
        return text;
    }

    public static String getRandomText() {
        return getRandomText(MIN, MAX);
    }
}
