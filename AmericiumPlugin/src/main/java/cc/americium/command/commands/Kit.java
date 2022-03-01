package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import cc.americium.customs.KitManager;
import cc.americium.customs.SerializeInventory;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class Kit implements Command {

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"kit"};
    }

    @Override
    public String getDescription() {
        return "Access the cloud kit network.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException {

        MongoCollection<Document> kit_collection = KitManager.getKitCollection();
        String[] args = context.getArgs();
        if (args.length < 1) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You need to specify an action! Actions: load | list | save | delete");
            return;
        }

        if (args[0].equals("list")) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "=== " + ChatColor.GOLD + "Kits" + Colors.Success + " ===");
            for (Document kit : KitManager.getKits(kit_collection)) {
                sender.sendMessage(Colors.Prefix + Colors.Success + "Kit name: " + ChatColor.WHITE + kit.getString("kit_name") + Colors.Success + " | Created by: " + ChatColor.WHITE + kit.getString("creator"));
            }
            sender.sendMessage(Colors.Prefix + Colors.Success + "=== " + ChatColor.GOLD + "Kits" + Colors.Success + " ===");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "No name is specified");
            return;
        }

        switch (args[0]) {
            case "save":
                KitManager.saveKit(kit_collection, SerializeInventory.playerInventoryToBase64(executor.getInventory()), sender.getName(), args[1], executor.getUniqueId().toString());

                sender.sendMessage(Colors.Prefix + Colors.Success + "Your current inventory has been saved as a kit!");
                break;
            case "load":
                String kit = KitManager.getKit(kit_collection, args[1]);

                if (kit == null) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "That kit does not exist");
                    return;
                }

                ItemStack[] contents = SerializeInventory.itemStackArrayFromBase64(kit);

                executor.getInventory().setContents(contents);
                sender.sendMessage(Colors.Prefix + Colors.Success + args[1] + " has been loaded on your player!");
                break;
            case "delete":
                if (KitManager.deleteKit(kit_collection, args[1], executor.getUniqueId().toString())) {
                    sender.sendMessage(Colors.Prefix + Colors.Success + args[1] + " has been removed");
                } else {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + args[1] + " was not able to be deleted! Maybe you dont own it, or you spelled the name wrong!");
                }

                break;
            default:
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That operation does not exist");
        }
    }
}
