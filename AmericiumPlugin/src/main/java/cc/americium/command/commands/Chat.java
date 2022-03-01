package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"chat"};
    }

    @Override
    public String getDescription() {
        return "Chat something without having to logout.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "chat <message...>");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            context.getZ_().getAuth().setLoggedIn(sender.getName(), false);
            executor.chat(String.join(" ", context.getArgs()));
            context.getZ_().getAuth().setLoggedIn(sender.getName(), true);
        });
    }
}
