package fr.twitmund.modSuite.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.twitmund.modSuite.Main;

public class MojangAPI {

    /**
     * Retrieves the UUID of a Minecraft player by their username using the Mojang API.
     * If the player is not found, it returns a default UUID.
     *
     * @param username The Minecraft username to look up.
     * @return The UUID of the player as a String.
     * @throws Exception If an error occurs while contacting the Mojang API.
     */
    public static String getUUIDFromUsername(String username) throws Exception {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 204) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            if (jsonResponse.has("errorMessage") && jsonResponse.get("errorMessage").getAsString().contains("Couldn't find any profile with name")) {
                return "22a25a17-8368-35c0-9320-59efa78a84e6";
            }
        } else if (responseCode != 200) {
            Main.getInstance().getLogger().severe("Failed to contact official Mojang API: " + responseCode);
            return "22a25a17-8368-35c0-9320-59efa78a84e6";
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        return jsonResponse.get("id").getAsString();
    }
}