package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SavePlayers implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"saveplayers"};
    }

    @Override
    public String getDescription() {
        return "Writes loaded players to disk.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Bukkit.savePlayers();
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully saved players!");
        });
    }
}
