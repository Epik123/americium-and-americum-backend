package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpamInterval implements Command {
    private final HashMap<String, Integer> repeatingTasks = new HashMap<>();

    @Override
    public String[] getCommands() {
        return new String[]{"spaminterval"};
    }

    @Override
    public String getDescription() {
        return "Spam a message every certain amount of ticks until stopped.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if (!this.repeatingTasks.containsKey(sender.getName()) && args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "spaminterval <ticks> <message...>");
            return;
        }

        if (this.repeatingTasks.containsKey(sender.getName())) {
            try {
                Bukkit.getScheduler().cancelTask(this.repeatingTasks.get(sender.getName()));
                this.repeatingTasks.remove(sender.getName());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully cleared your spam message!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
            return;
        }

        int interval;
        try {
            interval = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        String[] messageArgs = (String[]) ArrayUtils.remove(args, 0);
        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", messageArgs));

        int task;

        try {
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(context.getZ_(), () -> Bukkit.broadcastMessage(message), 0, interval);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        repeatingTasks.put(sender.getName(), task);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully started spamming every " + interval + " ticks with message '" + message + Colors.Success + "'!");

    }
}
