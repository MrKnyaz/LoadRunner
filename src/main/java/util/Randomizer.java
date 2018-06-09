package util;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    public static int randomValue(int min, int max) {
        int value = ThreadLocalRandom.current().nextInt(min, max + 1);
        return value;
    }

    public static boolean throwTheCoin(double chance) {
        double result = ThreadLocalRandom.current().nextDouble() * 100;
        return result <= chance;
    }

}
