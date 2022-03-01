package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Stop implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"stop"};
    }

    @Override
    public String getDescription() {
        return "Stops the server. Now THIS needs explaining!";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String confirm = "";
        String[] args = context.getArgs();
        if (args.length != 0) {
            confirm = args[0];
        }

        if (!confirm.equals("confirm")) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Are you sure you want to do this? This will stop the server. To confirm, use 'stop confirm'.");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            sender.sendMessage(Colors.Prefix + Colors.Success + "Attempting to stop the server!");
            Bukkit.getServer().shutdown();
        });
    }
}
