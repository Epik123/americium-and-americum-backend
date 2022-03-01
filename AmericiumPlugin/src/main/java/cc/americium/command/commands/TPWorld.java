package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TPWorld implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"tpworld"};
    }

    @Override
    public String getDescription() {
        return "Teleport to a world at certain coords.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "tpworld <world> (x) (y) (z)");
            return;
        }

        World found = Bukkit.getWorld(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That world was not found!");
            return;
        }

        int x = 0;
        int y = 0;
        int z = 0;
        if (args.length > 1) {
            try {
                x = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage() + " -- bad x");
                return;
            }
        }

        if (args.length > 2) {
            try {
                y = Integer.parseInt(args[2]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage() + " -- bad y");
                return;
            }
        }

        if (args.length > 3) {
            try {
                z = Integer.parseInt(args[3]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage() + " -- bad z");
                return;
            }
        }

        int finalY = y;
        int finalX = x;
        int finalZ = z;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                Location loc = new Location(found, finalX, finalY, finalZ);
                executor.teleport(loc);

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported you to " + Utilities.locationToString(loc) + " in " + found.getName() + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
