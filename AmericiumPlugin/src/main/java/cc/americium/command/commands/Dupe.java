package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Dupe implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"dupe"};
    }

    @Override
    public String getDescription() {
        return "Copy all items in your inventory and drop them where you are standing.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        executor.getInventory().forEach(item ->
                Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                    if (item == null) {
                        return;
                    }

                    if (item.getType() == Material.AIR) {
                        return;
                    }

                    executor.getWorld().dropItem(executor.getLocation(), item);
                })
        );

        sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to drop your items on the ground!");
    }
}
