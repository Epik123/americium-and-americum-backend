package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// This is an extension to the setname command, it would just over-complicate things to keep it in 1 class.
public class ResetName implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"resetname"};
    }

    @Override
    public String getDescription() {
        return "Reset the name of yourself, another player, or all players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        Player selected = null;
        if (args.length == 0) {
            selected = executor;
        }

        boolean allPlayers = false;
        if (args.length != 0) {
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

        Player finalSelected = selected;
        boolean finalAllPlayers = allPlayers;

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            if (finalAllPlayers) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        p.setPlayerListName(null);
                        p.setDisplayName(null);
                        p.setCustomName(null);
                    } catch (Exception e) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                        return;
                    }
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully reset all names!");
                return;
            }

            String sendName = finalSelected.getName().equals(sender.getName()) ? "your" : finalSelected.getName() + "'s";

            try {
                finalSelected.setPlayerListName(null);
                finalSelected.setDisplayName(null);
                finalSelected.setCustomName(null);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully reset " + sendName + " name!");
        });
    }
}
