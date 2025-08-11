package fr.twitmund.modSuite.utils.discordWebhook;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.utils.TimeUtil;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.WebhookMessage;
import lombok.Getter;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebHookSender {
    private static String urlWebHook = Main.getDiscordWebHookUrl();

    //todo handle exception

    public static void sendWebhook(MessageType content,
                            String author,
                            String target,
                            String reason) throws Exception {
        if (urlWebHook == null || urlWebHook.isEmpty()) {
            Bukkit.getLogger().warning("Discord webhook URL is not set. Please configure it in the plugin settings.");
            return;
        }
        if (!Main.discordWebhookEnabled) return;

        String headImageLink = GetHeadImage.getHeadImage(target);

        String webhookTitle = WebhookMessage.SANCTION_TITLE.getValue();
        String webhookDescription = WebhookMessage.SANCTION_DESCRIPTION.getValue();
        String title = content.getValue()[0];
        String message = content.getValue()[1];
        DiscordWebHookBuilder builder = new DiscordWebHookBuilder();
        //builder.setContent(content.getValue()[0]);
        //builder.setAvatarUrl(GetHeadImage.getHeadImage(uuidForHead));
        builder.setUsername("ModSuite");
        builder.setTts(false);
        builder.addEmbed(new DiscordWebHookBuilder.EmbedObject()
                .setTitle(webhookTitle)
                .setDescription(webhookDescription)
                .setColor(Color.CYAN)
                .addField(title , message
                        .replace("%author%", author)
                        .replace("%player%", target)
                        .replace("%reason%", reason), true)
                .setFooter("ModSuite - A plugin made by Twitmund",null)
                .setImage(headImageLink));
        builder.execute();

    }

    public void sendWebhook(MessageType content,
                            String author,
                            String target,
                            String reason,
                            TimeUtil duration) throws Exception {
        if (urlWebHook == null || urlWebHook.isEmpty()) {
            Bukkit.getLogger().warning("Discord webhook URL is not set. Please configure it in the plugin settings.");
            return;
        }
        if (!Main.discordWebhookEnabled) return;
        URL url = new URL(urlWebHook);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String[] values = content.getValue();
        String jsonPayload = String.format("{\"content\": \"%s\\n%s\"}", values[0], values[1])
                .replace("%author%", author)
                .replace("%player%", target)
                .replace("%reason%", reason)
                .replace("%duration%", TimeUtil.formatTime(duration.getTime()));

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 204) {
            Main.getInstance().getLogger().severe("Failed to send webhook: " + responseCode);
            throw new RuntimeException("Failed to send webhook: " + responseCode);
        }
    }

}
