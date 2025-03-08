package de.janschuri.lunaticdrops.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Random;

public class Utils extends de.janschuri.lunaticlib.common.utils.Utils {

    private static final Random random = new Random();

    public static boolean isLucky(double chance) {
        float randomValue = random.nextFloat(0, 1);
        return randomValue <= chance;
    }

    public static int isLucky(double[] chances) {

        float sum = 0;
        for (double chance : chances) {
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

    public static double[] normalize(double[] chances, float sum) {
        double[] normalized = new double[chances.length];
        for (int i = 0; i < chances.length; i++) {
            normalized[i] = chances[i] / sum;
        }
        return normalized;
    }

    public static String formatChance(double chance) {
        // format to last existing decimal place
        String chanceString = String.valueOf(chance*100);
        if (chanceString.length() > 12) {
            chanceString = chanceString.substring(0, 5);
        }

        //cut off trailing zeros

        if (chanceString.contains(".")) {
            chanceString = chanceString.replaceAll("0*$", "");
            chanceString = chanceString.replaceAll("\\.$", "");
        }

        return chanceString + " %";
    }

    public static Double parseEquation(String equation) {

        try {
            Expression expression = new ExpressionBuilder(equation).build();
            return expression.evaluate();
        } catch (Exception e) {
            return null;
        }
    }

}
