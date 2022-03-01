package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Fly implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"fly"};
    }

    @Override
    public String getDescription() {
        return "Toggles your or another player's flight.";
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
            String name = finalSelected.getName().equals(sender.getName()) ? "you" : finalSelected.getName();
            if (finalSelected.getAllowFlight()) {
                finalSelected.setAllowFlight(false);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Toggling flight off for " + name + "!");
                return;
            }

            finalSelected.setAllowFlight(true);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Toggling flight on for " + name + "!");
        });
    }
}
