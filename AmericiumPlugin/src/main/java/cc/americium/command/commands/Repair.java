package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Repair implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"repair"};
    }

    @Override
    public String getDescription() {
        return "Repairs held item.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        if (context.getArgs().length != 0) {
            for (ItemStack item : executor.getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                if (item.getType() == Material.AIR) {
                    continue;
                }

                item.setDurability((short) 0);
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Repaired all items in your inventory!");
            return;
        }

        ItemStack item = Utilities.getHeldItem(executor);

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "hold something b4 u shrek urself retARD");
            return;
        }

        item.setDurability((short) 0);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Repaired your " + Utilities.prettyMaterial(item.getType()) + "!");
    }
}
