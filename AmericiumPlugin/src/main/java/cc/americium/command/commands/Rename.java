package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Rename implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"rename"};
    }

    @Override
    public String getDescription() {
        return "Renames the current item you are holding, supports colors.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: rename <name>");
            return;
        }

        ItemStack item = Utilities.getHeldItem(executor);

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are not holding anything. fucktard.");
            return;
        }

        String newName = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        meta.setDisplayName(newName);
        item.setItemMeta(meta);

        sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to rename " + Utilities.prettyMaterial(item.getType()) + " to '" + newName + Colors.Success + "'!");
    }
}
