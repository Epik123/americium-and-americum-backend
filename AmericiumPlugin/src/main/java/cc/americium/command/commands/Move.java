package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Move implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"mv"};
    }

    @Override
    public String getDescription() {
        return "Move a file/directory to a new folder.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "mv <path/to/dir> <new/path> | <path/to/file> <new/path>");
            return;
        }

        try {
            File fileTwo = new File(args[1]);
            fileTwo.mkdirs();

            Files.move(new File(args[0]).toPath(), fileTwo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully moved " + args[0] + " to " + fileTwo + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
