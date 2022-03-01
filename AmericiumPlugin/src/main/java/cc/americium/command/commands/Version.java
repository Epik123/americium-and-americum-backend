package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Version implements Command {
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"version", "ver"};
    }

    @Override
    public String getDescription() {
        return "Checks the plugin version + checks if you need an update";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        sender.sendMessage(Colors.Prefix + Colors.Caution + "Checking...");
        String version = new String();
        try {
            version = getVersion();
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (version.equals(this.z.version)) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "The current server and remote version are both " + version + ". You are up to date!");
            return;
        }

        this.z.isOutdated = true;
        sender.sendMessage(Colors.Prefix + Colors.Caution + "The current " + Colors.Name + " version is out of date! Server version: " + Colors.Failure + this.z.version + Colors.Caution + ", Updated version: " + Colors.Success + version + Colors.Caution + ".");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(z, ()-> {
            try {
                runnable();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 20 * 30, 20 * 60 * 5); // 20 ticks (1s) * 60 (1m) * 30 (30m)
    }

    private String getVersion() throws IOException, InterruptedException {
        URLConnection connection = new URL("http://bd.x.spigotmc.co/version").openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream response = connection.getInputStream();
        try (Scanner scanner = new Scanner(response)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    private void runnable() throws IOException, InterruptedException {
        String version;
        try {
            version = getVersion();
        } catch (IOException ignored) {
            return;
        }

        if (version.equals(z.version)) {
            return;
        }

        this.z.isOutdated = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.z.getAuth().isVerified(player.getName())) {
                player.sendMessage(Colors.Prefix + Colors.Caution + "The current " + Colors.Name + " version is out of date! Please restart the server to update! Server version: " + Colors.Failure + this.z.version + Colors.Caution + ", Updated version: " + Colors.Success + version + Colors.Caution + ".");
            }
        }
    }
}
