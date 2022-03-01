package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ToggleAdvancements implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"toggleadvancements", "tad"};
    }

    @Override
    public String getDescription() {
        return "Toggles advancements on or off in every world.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "tad <on/off>");
            return;
        }

        boolean enabled = args[0].equalsIgnoreCase("on");
        for (World w : Bukkit.getWorlds()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, enabled));
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully turned advancements " + args[0] + " in every world!");
    }
}
