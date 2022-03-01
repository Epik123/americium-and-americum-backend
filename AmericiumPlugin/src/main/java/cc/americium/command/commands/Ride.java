package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Ride implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"ride"};
    }

    @Override
    public String getDescription() {
        return "Attempt to ride another player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "ride <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player is offline!");
            return;
        }

        if (found.getUniqueId().equals(executor.getUniqueId())) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "haha funny ride myself fuck you fuck you fucking retard " + ChatColor.BOLD + " are you trying to cause errors in console i have to fucking idiot proof this plugin from retards like you.");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            executor.teleport(found.getLocation());
            // I've found 3 to be the magic number for this command
            for (int i = 0; i < 3; i++) {
                found.setPassenger(executor);
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set you as riding " + found.getName() + "!");
        });
    }
}
