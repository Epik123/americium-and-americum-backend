package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Hologram implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"hologram", "hd"};
    }

    @Override
    public String getDescription() {
        return "Summon a hologram with a custom name, at your current location.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "hd <name>");
            return;
        }

        String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", context.getArgs()));

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                ArmorStand armorStand = (ArmorStand) executor.getWorld().spawnEntity(executor.getLocation().add(0, 1, 1), EntityType.ARMOR_STAND);
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.setCustomName(name);
                armorStand.setCustomNameVisible(true);
                armorStand.setMarker(true);
                armorStand.setInvulnerable(true);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully created a hologram with a name of '" + name + Colors.Success + "' where you are standing!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
