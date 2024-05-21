package com.password_manager.Utils;

import java.util.Random;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.scene.paint.Color;

public class ColorGenerator {
    public static Color generateRandomColor(String seed) {
        String hashedSeed = sha512(seed);

        // Extract and calculate hue
        int hueComponent = Integer.parseInt(hashedSeed.substring(0, 2), 16);
        double hue = hueComponent * 360.0 / 255.0;

        // Extract and calculate saturation
        int saturationComponent = Integer.parseInt(hashedSeed.substring(2, 4), 16);
        double saturation = saturationComponent * ((70.0 - 40.0) / 255.0) + 40.0;

        // Fixed Lightness
        double lightness = 45.0;

        return Color.hsb(hue, saturation * 0.01, lightness * 0.01);
    }

    private static String sha512(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    } 
}
