package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Coords implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"coords"};
    }

    @Override
    public String getDescription() {
        return "Shows the coords of a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: coords <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        Location loc = found.getLocation();
        if (loc.getWorld() == null) {
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + found.getName() + " is in the " + loc.getWorld().getName() + " and is at " + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()) + "!");
    }
}
