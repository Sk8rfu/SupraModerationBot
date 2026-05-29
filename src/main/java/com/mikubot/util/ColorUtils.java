package com.mikubot.utils;

import java.awt.*;

public class ColorUtils {

    public static Color parseColor(String input) {
        try {
            input = input.toLowerCase();

            // HEX (#ff00aa)
            if (input.startsWith("#")) {
                return Color.decode(input);
            }

            // RGB (255 0 128)
            if (input.matches("\\d{1,3} \\d{1,3} \\d{1,3}")) {
                String[] parts = input.split(" ");
                int r = Integer.parseInt(parts[0]);
                int g = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);

                if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255)
                    return null;

                return new Color(r, g, b);
            }

            // Имена на цветове
            return switch (input) {
                case "red" -> Color.RED;
                case "blue" -> Color.BLUE;
                case "green" -> Color.GREEN;
                case "yellow" -> Color.YELLOW;
                case "black" -> Color.BLACK;
                case "white" -> Color.WHITE;
                case "cyan" -> Color.CYAN;
                case "magenta" -> Color.MAGENTA;
                case "orange" -> Color.ORANGE;
                case "pink" -> Color.PINK;
                case "gray", "grey" -> Color.GRAY;

                // Допълнителни цветове
                case "lightgray", "lightgrey" -> Color.LIGHT_GRAY;
                case "darkgray", "darkgrey" -> Color.DARK_GRAY;

                case "navy" -> new Color(0, 0, 128);
                case "teal" -> new Color(0, 128, 128);
                case "lime" -> new Color(0, 255, 0);
                case "maroon" -> new Color(128, 0, 0);
                case "olive" -> new Color(128, 128, 0);
                case "purple" -> new Color(128, 0, 128);
                case "brown" -> new Color(150, 75, 0);

                case "gold" -> new Color(255, 215, 0);
                case "silver" -> new Color(192, 192, 192);

                case "violet" -> new Color(148, 0, 211);
                case "indigo" -> new Color(75, 0, 130);

                case "turquoise" -> new Color(64, 224, 208);
                case "aqua" -> new Color(0, 255, 255);

                case "beige" -> new Color(245, 245, 220);
                case "tan" -> new Color(210, 180, 140);
                case "chocolate" -> new Color(210, 105, 30);

                default -> null;
            };

        } catch (Exception e) {
            return null;
        }
    }
}
