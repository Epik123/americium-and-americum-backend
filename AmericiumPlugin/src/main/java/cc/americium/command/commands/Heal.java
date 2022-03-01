package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Heal implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"heal", "feed"};
    }

    @Override
    public String getDescription() {
        return "Heals/feeds you or another player.";
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
            finalSelected.setFoodLevel(20);

            if (context.getCommand().equalsIgnoreCase("feed")) {
                sender.sendMessage(Colors.Prefix + Colors.Success + "Fed " + name + "!");
                return;
            }

            finalSelected.setHealth(20);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Healed " + name + "!");
        });
    }
}
