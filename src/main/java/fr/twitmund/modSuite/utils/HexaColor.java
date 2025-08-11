package fr.twitmund.modSuite.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexaColor {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Convert a collection of strings to a single string with hex color codes formatted.
     *
     * @param message The collection of strings to format.
     * @return A single string with all messages formatted with hex colors.
     */
    public static String format(String message) {
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
}
