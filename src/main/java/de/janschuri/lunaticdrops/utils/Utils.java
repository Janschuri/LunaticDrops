package de.janschuri.lunaticdrops.utils;

import java.util.List;
import java.util.Random;

public class Utils extends de.janschuri.lunaticlib.common.utils.Utils {

    private static final Random random = new Random();

    public static boolean isLucky(float chance) {
        float randomValue = random.nextFloat(0, 1);
        return randomValue <= chance;
    }

    public static int isLucky(float[] chances) {

        float sum = 0;
        for (float chance : chances) {
            sum += chance;
        }

        if (sum > 1) {
            chances = normalize(chances, sum);
        }

        float randomValue = random.nextFloat();
        float cumulativeSum = 0;

        for (int i = 0; i < chances.length; i++) {
            cumulativeSum += chances[i];
            if (randomValue <= cumulativeSum) {
                return i;
            }
        }

        return -1;
    }

    public static float[] normalize(float[] chances, float sum) {
        float[] normalized = new float[chances.length];
        for (int i = 0; i < chances.length; i++) {
            normalized[i] = chances[i] / sum;
        }
        return normalized;
    }
}
