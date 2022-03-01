package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Equip implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"equip"};
    }

    @Override
    public String getDescription() {
        return "Set the held item to a current slot.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "equip <helmet|chestplate|leggings|boots>");
            return;
        }

        ItemStack item = Utilities.getHeldItem(executor);
        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are not holding anything. fucktard.");
            return;
        }

        PlayerInventory inv = executor.getInventory();

        switch (args[0].toLowerCase()) {
            case "helmet":
                inv.setHelmet(item);
                break;
            case "chestplate":
                inv.setChestplate(item);
                break;
            case "leggings":
                inv.setLeggings(item);
                break;
            case "boots":
                inv.setBoots(item);
                break;
            default:
                sender.sendMessage(Colors.Prefix + Colors.Failure + "equip <helmet|chestplate|leggings|boots>");
                return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully equipped your " + Utilities.prettyMaterial(item.getType()) + " to slot " + args[0] + "!");
        inv.setItem(inv.getHeldItemSlot(), null);
    }
}
