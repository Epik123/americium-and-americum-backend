package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;

public class Size implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"size"};
    }

    @Override
    public String getDescription() {
        return "Sends you the size in bytes of a file. (Mainly a dev command)";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "size <./directory/file.jar>");
            return;
        }

        File file;
        try {
            file = new File(args[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (!file.exists()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "File does not exist!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "The size of " + file.getPath() + " appears to be " + file.length() + "!");
    }
}
