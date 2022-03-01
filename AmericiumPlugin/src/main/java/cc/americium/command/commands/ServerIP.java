package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

public class ServerIP implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"serverip", "server"};
    }

    @Override
    public String getDescription() {
        return "Sends you the server's ip and port.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        sender.sendMessage(Colors.Prefix + Colors.Success + "The server appears to be running on " + Utilities.serverIP() + "!");
    }
}
