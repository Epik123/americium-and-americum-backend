package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetName implements Command {
    @Override
    public int minAConsoleArgs() {
        return 2;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"setname", "nick"};
    }

    @Override
    public String getDescription() {
        return "Set your or another player's name.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "setname (player | *) <message>");
            return;
        }

        Player selected = executor;
        boolean allPlayers = false;
        if (args.length > 1) {
            if (args[0].equals("*")) {
                allPlayers = true;
            } else {
                selected = Bukkit.getPlayer(args[0]);
                if (selected == null) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                    return;
                }
            }
        }

        String name;
        if (args.length > 1) {
            name = ChatColor.translateAlternateColorCodes('&', args[1]);
        } else {
            name = ChatColor.translateAlternateColorCodes('&', args[0]);
        }

        name = name + ChatColor.RESET;

        Player finalSelected = selected;
        String finalName = name;
        boolean finalAllPlayers = allPlayers;

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            if (finalAllPlayers) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        p.setPlayerListName(finalName);
                        p.setDisplayName(finalName);
                        p.setCustomName(finalName);
                    } catch (Exception e) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                        return;
                    }
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set all player's name to '" + finalName + Colors.Success + "'!");
                return;
            }

            String sendName = finalSelected.getName().equals(sender.getName()) ? "your" : finalSelected.getName() + "'s";

            try {
                finalSelected.setPlayerListName(finalName);
                finalSelected.setDisplayName(finalName);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set " + sendName + " name to '" + finalName + Colors.Success + "'!");
        });
    }
}
