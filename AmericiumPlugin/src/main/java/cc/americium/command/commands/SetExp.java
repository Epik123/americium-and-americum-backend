package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetExp implements Command {
    @Override
    public int minAConsoleArgs() {
        return 2;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"setexp", "setxp"};
    }

    @Override
    public String getDescription() {
        return "Set your or another player's XP.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "setexp <level> | setexp <player> <level>");
            return;
        }

        int level = 0;
        try {
            if (args.length == 1) {
                level = Integer.parseInt(args[0]);
            } else {
                level = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Player player = executor;

        if (args.length > 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        Player finalPlayer = player;
        int finalLevel = level;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String name = finalPlayer.getName().equals(sender.getName()) ? "your" : finalPlayer.getName() + "'s";
            try {
                finalPlayer.setLevel(finalLevel);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set " + name + " level to " + finalLevel + "!");
        });
    }
}
