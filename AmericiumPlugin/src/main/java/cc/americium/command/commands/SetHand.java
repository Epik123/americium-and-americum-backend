package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SetHand implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"sethand"};
    }

    @Override
    public String getDescription() {
        return "Sets your current hand to any material.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "sethand <material>");
            return;
        }

        Material item;
        try {
            item = Material.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            PlayerInventory inventory = executor.getInventory();
            inventory.setItem(inventory.getHeldItemSlot(), new ItemStack(item, 1));

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set your hand to " + Utilities.prettyMaterial(item) + "!");
        });

    }
}
