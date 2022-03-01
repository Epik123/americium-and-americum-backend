package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Time implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"time"};
    }

    @Override
    public String getDescription() {
        return "Set the time of the world you are currently in.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "time <night / day / long value>");
            return;
        }

        long value;
        switch (args[0].toLowerCase().trim()) {
            case "day":
                value = 0L;
                break;
            case "night":
                value = 14000L;
                break;
            default:
                try {
                    value = Long.parseLong(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "Got " + args[0].toLowerCase().trim());
                    return;
                }
        }

        long finalValue = value;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                executor.getWorld().setTime(finalValue);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set time value to " + finalValue + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
