package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.ExternalConsole;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

public class WebsocketStatus implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"status"};
    }

    @Override
    public String getDescription() {
        return "Get the current status of the websocket.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ExternalConsole ec = context.getZ_().getExternalConsole();
        if (ec == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The websocket is null!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Caution + "The websocket is" + (ec.isOpen() ? "" : " not") + " connected to " + ec.getURI() + "!");
    }
}
