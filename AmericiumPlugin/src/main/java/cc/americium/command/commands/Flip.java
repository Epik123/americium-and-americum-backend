package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Flip implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"flip"};
    }

    @Override
    public String getDescription() {
        return "Flip a player around.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "flip <player | *>");
            return;
        }

        if (args[0].equalsIgnoreCase("*")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    this.flip(p);
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully flipped all players!");
            });
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            this.flip(found);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully flipped " + found.getName() + "!");
        });
    }

    private void flip(Player p) {
        Location loc = p.getLocation();
        loc.setYaw(loc.getYaw() - 180);

        p.teleport(loc);
    }
}
