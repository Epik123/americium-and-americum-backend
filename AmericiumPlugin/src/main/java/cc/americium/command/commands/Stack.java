package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Stack implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"stack"};
    }

    @Override
    public String getDescription() {
        return "Stacks the item you are holding to 64.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        ItemStack item = Utilities.getHeldItem(executor);

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are not holding anything. fucktard.");
            return;
        }

        int amount = 64;
        if (args.length != 0) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        item.setAmount(amount);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Stacked your " + item.getType().name().toLowerCase().replace("_", " ") + " to " + amount + "!");
    }
}
