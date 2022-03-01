package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Weather implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"weather"};
    }

    @Override
    public String getDescription() {
        return "Set the weather of the world you are in.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "weather <clear / thunder / rain>");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String first = args[0];
            World world = executor.getWorld();

            if (first.equalsIgnoreCase("clear")) {
                world.setStorm(false);
                world.setThundering(false);
            } else if (first.equalsIgnoreCase("thunder")) {
                world.setStorm(true);
                world.setThundering(true);
            } else if (first.equalsIgnoreCase("rain")) {
                world.setThundering(false);
                world.setStorm(true);
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "weather <clear / thunder / rain>");
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set weather to " + first.toLowerCase() + "!");
        });
    }
}
