package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Americiumspam implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"americiumpam"};
    }

    @Override
    public String getDescription() {
        return "americiumspam broadcast a message in chat a certain amount of times.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

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
                Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.DARK_RED+"゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜████゜゜████゜゜████゜゜█████゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜█゜゜゜█゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜████゜゜████゜゜████゜゜████゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜█゜゜゜█゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜████゜゜█゜゜゜█゜█゜゜゜█゜█████゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜");
                Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.DARK_AQUA+"゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜████゜゜゜███゜゜█████゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜████゜゜゜███゜゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜");
                Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.DARK_GREEN+"゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜███゜゜゜███゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜█゜゜゜█゜█゜゜゜█゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜███゜゜゜███゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜゜");
            }
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully broadcasted message '" + message + Colors.Success + "' " + finalAmount + " times!");
        });
    }
}
