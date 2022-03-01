package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ThirtyTwoK implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"32k"};
    }

    @Override
    public String getDescription() {
        return "Enchants your held item with every enchantment possible.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ItemStack item = Utilities.getHeldItem(executor);

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are not holding anything. fucktard.");
            return;
        }

        for (Enchantment e : Enchantment.values()) {
            if (!e.toString().contains("CURSE")) {
                item.addUnsafeEnchantment(e, 32000);
            }
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Applied all enchantments to your " + Utilities.prettyMaterial(item.getType()) + "!");
    }
}
