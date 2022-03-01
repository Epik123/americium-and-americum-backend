package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Clear implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"clear", "ci"};
    }

    @Override
    public String getDescription() {
        return "Clears your inventory or another player's inventory.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        Player selected = executor;
        if (args.length != 0) {
            selected = Bukkit.getPlayer(args[0]);
            if (selected == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        Player finalSelected = selected;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            finalSelected.getInventory().clear();
            String name = finalSelected.getName().equals(sender.getName()) ? "your" : finalSelected.getName() + "'s";
            sender.sendMessage(Colors.Prefix + Colors.Success + "Clearing " + name + " inventory!");
        });
    }
}
