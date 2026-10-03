package praktikum.utils;

import java.util.Random;

public class StringRandomUtils {
    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    public static String getRandomEmail() {
        return getRandomString(8).toLowerCase() + "@" +
                getRandomString(5).toLowerCase() + "." +
                getRandomString(3).toLowerCase();
    }

    public static String getRandomName() {
        return getRandomString(8);
    }

    public static String getRandomPassword() {
        return getRandomString(8);
    }
}
