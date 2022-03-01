package cc.americium.command.commands.admin;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class UploadAllCommands implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"uploadallcommands"};
    }

    @Override
    public String getDescription() {
        return "Upload all servers commands as a txt file. " + Colors.Caution + "ADMIN ONLY!";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ArrayList<String> commands = new ArrayList<>();
        for (Command c : context.getZ_().commands) {
            commands.add(String.join(", ", c.getCommands()) + ": " + c.getDescription());
        }

        Reader reader;
        try {
            reader = Utilities.uploadFile(String.join("\n", commands), context.getZ_(), "text");
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        String sb = Utilities.getStringFromBuffer(reader);

        if (sb.length() == 0) {
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
    }
}
