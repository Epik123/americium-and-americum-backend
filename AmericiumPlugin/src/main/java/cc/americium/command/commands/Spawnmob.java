package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Spawnmob implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"spawnmob", "spawn"};
    }

    @Override
    public String getDescription() {
        return "Spawn a certain amount of a mob where you're looking.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "spawn <mob> (amount)");
            return;
        }

        EntityType type;
        try {
            type = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        int finalAmount = amount;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Location looking = executor.getTargetBlock(null, 100).getLocation();
            looking = new Location(looking.getWorld(), looking.getX(), looking.getY() + 1, looking.getZ());
            for (int i = 0; i < finalAmount; i++) {
                try {
                    executor.getWorld().spawnEntity(looking, type);
                } catch (Exception ignored) {

                }
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to spawn " + finalAmount + " of " + type.name().toLowerCase().replaceAll("_", " ") + "!");
        });
    }
}
