package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reload implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"reload", "rl"};
    }

    @Override
    public String getDescription() {
        return "Reloads the server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Are you sure you want to do this? Type rl confirm to reload.");
            return;
        }

        if (!args[0].equals("confirm")) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Are you sure you want to do this? Type rl confirm to reload.");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Attempting to reload server...");
            Bukkit.getServer().reload();
        });
    }
}
