package org.koteyka.mytablist.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final String patternColorString = "#[0-9A-Fa-f]{6}";
    private static final Pattern patternColor = Pattern.compile(patternColorString);

    private static final String patternGradientString = "<gr\\s*(#[0-9A-Fa-f]{6}):(#[0-9A-Fa-f]{6})>(.*?)</gr>";
    private static final Pattern patternGradient = Pattern.compile(patternGradientString);

    public static int[] hexToRgb(String hex) {
        int red = Integer.parseInt(hex.substring(1, 3), 16);
        int green = Integer.parseInt(hex.substring(3, 5), 16);
        int blue = Integer.parseInt(hex.substring(5, 7), 16);
        return new int[] {red, green, blue};
    }

    public static String setColorAmpersand(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String setColorsHex(String text) {
        Matcher matcher = patternColor.matcher(text);
        while (matcher.find()) {
            String colorHex = matcher.group();
            text = text.replace(colorHex, "" + ChatColor.of(colorHex));
        }
        return text;
    }

    public static String setGradient(String text) {
        Matcher matcher = patternGradient.matcher(text);
        while (matcher.find()) {
            String matchedText = matcher.group();
            String colorStart = matcher.group(1);
            String colorEnd = matcher.group(2);
            String letters = matcher.group(3);

            StringBuilder result = new StringBuilder();

            // Convert HEX to RGB
            int[] startRGB = hexToRgb(colorStart);
            int[] endRGB = hexToRgb(colorEnd);

            for (int i = 0; i < letters.length(); i++) {

                double ratio = (double) i / (letters.length() - 1);
                int red = (int) (startRGB[0] * (1 - ratio) + endRGB[0] * ratio);
                int green = (int) (startRGB[1] * (1 - ratio) + endRGB[1] * ratio);
                int blue = (int) (startRGB[2] * (1 - ratio) + endRGB[2] * ratio);

                String hexColor = String.format("#%02X%02X%02X", red, green, blue);

                result.append(ChatColor.of(hexColor));
                result.append(letters.charAt(i));
            }

            text = text.replace(matchedText, result.toString());
        }
        return text;
    }

    public static String applyStyle(String text) {
        String textGradient = setGradient(text);
        String coloredHex = setColorsHex(textGradient);
        return setColorAmpersand(coloredHex);
    }
}
