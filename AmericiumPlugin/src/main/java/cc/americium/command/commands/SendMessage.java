package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SendMessage implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"sendmessage"};
    }

    @Override
    public String getDescription() {
        return "Send a message to a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "sendmessage <player> <message...>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        String[] newArgs = (String[]) ArrayUtils.remove(args, 0);
        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", newArgs));
        found.sendMessage(message);

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully sent message '" + message + Colors.Success + "' to " + found.getName() + "!");
    }
}
