package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Speed implements Command {
    @Override
    public int minAConsoleArgs() {
        return 2;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"speed"};
    }

    @Override
    public String getDescription() {
        return "Set your speed or another player's speed.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "speed <level> (player)");
            return;
        }

        float speed;
        try {
            speed = Float.parseFloat(args[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Player selected = executor;
        if (args.length > 1) {
            selected = Bukkit.getPlayer(args[1]);
            if (selected == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        speed = speed / 10;

        Player finalSelected = selected;
        float finalSpeed = speed;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String name = finalSelected.getName().equals(sender.getName()) ? "your" : finalSelected.getName() + "'s";
            try {
                finalSelected.setWalkSpeed(finalSpeed);
                finalSelected.setFlySpeed((float) (finalSpeed - 0.1));
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set " + name + " walking & flying speed to " + args[0] + "!");
        });
    }
}
