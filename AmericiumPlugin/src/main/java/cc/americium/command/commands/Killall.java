package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Killall implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"killall"};
    }

    @Override
    public String getDescription() {
        return "Kill an entity/all in a certain radius.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "killall <entity / all> (radius)");
            return;
        }

        EntityType type = null;
        boolean killAll = false;

        if (args[0].equalsIgnoreCase("all")) {
            killAll = true;
        } else {
            try {
                if (args[0].equalsIgnoreCase("item")) {
                    type = EntityType.DROPPED_ITEM;
                } else {
                    type = EntityType.valueOf(args[0].toUpperCase());
                }
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        int radius = 100;
        if (args.length > 1) {
            try {
                radius = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        int finalRadius = radius;
        boolean finalKillAll = killAll;
        EntityType finalType = type;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            int killed = 0;

            for (Entity entity : executor.getNearbyEntities(finalRadius, finalRadius, finalRadius)) {
                if (finalKillAll) {
                    entity.remove();
                    killed++;
                } else if (finalType == entity.getType()) {
                    entity.remove();
                    killed++;
                }
            }

            sender.sendMessage(
                    Colors.Prefix + Colors.Success + "Successfully killed " + killed + " of " +
                            (finalKillAll ? "entities" : finalType.name().toLowerCase().replace("_", " "))
                            + "s in a radius of " + finalRadius + "!"
            );
        });
    }
}
