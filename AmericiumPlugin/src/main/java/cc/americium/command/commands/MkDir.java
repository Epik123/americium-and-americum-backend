package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;

public class MkDir implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"mkdir", "mkdirs"};
    }

    @Override
    public String getDescription() {
        return "Create a directory if it doesn't already exist.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "mkdir <path | ./path/that/you/want/>");
            return;
        }

        File file = new File(String.join(" ", context.getArgs()));
        if (file.exists()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "A file/dir by that name already exists.");
            return;
        }

        boolean created = file.mkdirs();
        if (!created) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Failed to create dir for some reason!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully created " + file.getPath() + "!");
    }
}
