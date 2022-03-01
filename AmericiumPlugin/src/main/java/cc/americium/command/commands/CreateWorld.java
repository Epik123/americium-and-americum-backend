package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class CreateWorld implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"createworld"};
    }

    @Override
    public String getDescription() {
        return "Create a world with various options.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "createworld <name>");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Attempting to create world " + args[0] + ". Please wait.");

            try {
                WorldCreator worldCreator = new WorldCreator(args[0]);
                World created = Bukkit.getServer().createWorld(worldCreator);

                sender.sendMessage(Colors.Prefix + Colors.Success + "Created world " + created.getName() + " with seed of " + created.getSeed() + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
