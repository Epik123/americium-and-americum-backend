package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Top implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"top"};
    }

    @Override
    public String getDescription() {
        return "Teleport you to the top of wherever you are.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        Location loc = executor.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            executor.teleport(executor.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0));
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully teleported you to the highest block!");
        });
    }
}
