package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class UnloadWorld implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"unloadworld"};
    }

    @Override
    public String getDescription() {
        return "Unload a world, with saving or not.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "unloadworld <world> (save: true/false)");
            return;
        }

        World found = Bukkit.getWorld(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That world was not found!");
            return;
        }

        boolean save = true;
        if (args.length > 1) {
            try {
                save = Boolean.parseBoolean(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        boolean finalSave = save;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                boolean didUnload = Bukkit.unloadWorld(found, finalSave);
                if (didUnload) {
                    sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully unloaded world " + found.getName() + " with saving " + finalSave + "!");
                } else {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "Something went wrong unloading " + found.getName() + "!");
                }

            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
