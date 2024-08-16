package com.management.leaderspace.model;

import java.text.DecimalFormat;

public class NumberToWordsService {

    private static final String[] tensNames = {
            "",
            " dix",
            " vingt",
            " trente",
            " quarante",
            " cinquante",
            " soixante",
            " soixante-dix",
            " quatre-vingt",
            " quatre-vingt-dix"
    };

    private static final String[] numNames = {
            "",
            " un",
            " deux",
            " trois",
            " quatre",
            " cinq",
            " six",
            " sept",
            " huit",
            " neuf",
            " dix",
            " onze",
            " douze",
            " treize",
            " quatorze",
            " quinze",
            " seize",
            " dix-sept",
            " dix-huit",
            " dix-neuf"
    };

    private static String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20) {
            current = numNames[number % 100];
            number /= 100;
        } else {
            current = numNames[number % 10];
            number /= 10;

            if (number % 10 == 1 && current.equals(" un")) {
                current = " et" + current;
            }

            current = tensNames[number % 10] + current;
            number /= 10;
        }
        if (number == 0) return current.trim();
        return (number == 1 ? " cent" : numNames[number] + " cent" + (current.trim().isEmpty() ? "s" : " " + current.trim()));
    }

    public String convertToWords(long number) {
        if (number == 0) {
            return "zéro";
        }

        String snumber = Long.toString(number);

        // Pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        int billions = Integer.parseInt(snumber.substring(0, 3));
        int millions = Integer.parseInt(snumber.substring(3, 6));
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertLessThanOneThousand(billions) + " milliard ";
                break;
            default:
                tradBillions = convertLessThanOneThousand(billions) + " milliards ";
                break;
        }

        String result = tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertLessThanOneThousand(millions) + " million ";
                break;
            default:
                tradMillions = convertLessThanOneThousand(millions) + " millions ";
                break;
        }

        result += tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = " mille ";
                break;
            default:
                tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " mille ";
                break;
        }

        result += tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result += tradThousand;

        // Remove extra spaces and fix special cases
        result = result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim();

        // Handle special cases like "cent", "mille", "million", "milliard"
        if (result.endsWith(" cent")) {
            result += "s";
        }
        if (result.endsWith(" mille")) {
            result = result.replace(" mille", "mille");
        }
        result = result.replace("un mille", "mille");

        // Ensure "et" in special cases like 21, 31, 41, etc.
        result = result.replaceAll("vingt un", "vingt et un")
                .replaceAll("trente un", "trente et un")
                .replaceAll("quarante un", "quarante et un")
                .replaceAll("cinquante un", "cinquante et un")
                .replaceAll("soixante un", "soixante et un")
                .replaceAll("quatre-vingt un", "quatre-vingt-un")
                .replaceAll("quatre-vingt onze", "quatre-vingt-onze");

        return result;
    }


}
