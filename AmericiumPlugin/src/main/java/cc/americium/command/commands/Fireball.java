package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Fireball implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"fireball"};
    }

    @Override
    public String getDescription() {
        return "Launch a fireball where you are looking.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                org.bukkit.entity.Fireball fireball = executor.getWorld().spawn(executor.getEyeLocation(), org.bukkit.entity.Fireball.class);
                Vector higherSpeed = new Vector(2, 2, 2);
                Vector playerDirection = executor.getLocation().getDirection();
                Vector finalVector = higherSpeed.multiply(playerDirection);
                fireball.setVelocity(finalVector);
                fireball.setDirection(playerDirection);
                fireball.setShooter(executor);

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully launched a fireball!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
