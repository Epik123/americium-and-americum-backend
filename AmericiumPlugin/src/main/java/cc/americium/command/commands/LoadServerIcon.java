package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public class LoadServerIcon implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"loadservericon"};
    }

    @Override
    public String getDescription() {
        return "Load a server icon from a local file.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "loadservericon <./path/to/icon.png>");
            return;
        }

        File file = new File(String.join(" ", args));
        if (!file.exists()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That file was not found!");
            return;
        }

        if (file.isDirectory()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That is a directory. you are braindead.");
            return;
        }

        try {
            Bukkit.loadServerIcon(file);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set server icon to " + file.toPath() + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
