package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Spam implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"spam"};
    }

    @Override
    public String getDescription() {
        return "Spam broadcast a message in chat a certain amount of times.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "spam <amount> <message...>");
            return;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        String[] argsWithoutFirst = (String[]) ArrayUtils.remove(args, 0);
        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", argsWithoutFirst));
        int finalAmount = amount;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            for (int i = 0; i < finalAmount; i++) {
                Bukkit.broadcastMessage(message);
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully broadcasted message '" + message + Colors.Success + "' " + finalAmount + " times!");
        });
    }
}
