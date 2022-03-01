package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Save implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"save"};
    }

    @Override
    public String getDescription() {
        return "Save the world you are in to file.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        World found = null;
        if (args.length != 0) {
            found = Bukkit.getWorld(args[0]);
        } else {
            // this.minAConsoleArgs() should prevent this from happening in console!
            found = executor.getWorld();
        }

        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That world was not found!");
            return;
        }

        World finalFound = found;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                finalFound.save();
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully saved world " + finalFound.getName() + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
