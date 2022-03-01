package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class CreateMap implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"createmap"};
    }

    @Override
    public String getDescription() {
        return "Create a map for your world or another.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        World world = null;
        if (args.length == 0) {
            world = executor.getWorld();
        } else {
            world = Bukkit.getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That world was not found!");
                return;
            }
        }

        World finalWorld = world;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                MapView map = Bukkit.getServer().createMap(finalWorld);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully created a map with an id of " + map.getId() + " in world " + finalWorld.getName() + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
