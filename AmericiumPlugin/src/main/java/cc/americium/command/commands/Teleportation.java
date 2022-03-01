package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleportation implements Command {
    @Override
    public int minAConsoleArgs() {
        return 2;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"tp", "tphere", "tpall"};
    }

    @Override
    public String getDescription() {
        return "Teleportation commands.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (context.getCommand().equalsIgnoreCase("tphere")) {
            if (args.length == 0) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: tphere <player>");
                return;
            }

            Player found = Bukkit.getPlayer(args[0]);
            if (found == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                found.teleport(executor.getLocation());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported " + found.getName() + " to you!");
            });
            return;
        }

        if (context.getCommand().equalsIgnoreCase("tpall")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.teleport(executor.getLocation());
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported all players to you!");
            });

            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: tp <player> (player) | tp <x> <y> <z>");
            return;
        }

        if (args.length == 1) {
            Player found = Bukkit.getPlayer(args[0]);
            if (found == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                executor.teleport(found.getLocation());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported you to " + found.getName() + "!");
            });
            return;
        }

        if (args.length == 2) {
            Player one = Bukkit.getPlayer(args[0]);
            Player two = Bukkit.getPlayer(args[1]);
            if (one == null || two == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "One of those players could not be found!");
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                one.teleport(two.getLocation());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported " + one.getName() + " to " + two.getName() + "!");
            });
            return;
        }

        if (executor == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "No");
            return;
        }

        float x;
        float y;
        float z;
        try {
            x = Float.parseFloat(args[0]);
            y = Float.parseFloat(args[1]);
            z = Float.parseFloat(args[2]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                Location loc = new Location(executor.getWorld(), x, y, z);

                executor.teleport(loc);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported you to " + Utilities.locationToString(loc) + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
