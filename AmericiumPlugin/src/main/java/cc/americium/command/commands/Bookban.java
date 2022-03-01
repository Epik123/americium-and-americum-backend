package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Bookban implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"bookban"};
    }

    @Override
    public String getDescription() {
        return "Gives the player a book which will ban them from the server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "bookban <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        int value = 1999;
        if (args.length == 2) {
            try {
                value = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        try {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 64);
            BookMeta meta = (BookMeta) book.getItemMeta();

            String page = Strings.repeat("", value);

            for (int i = 0; i < value; i++) {
                meta.addPage(page);
            }

            meta.setAuthor("");
            meta.setTitle("");
            book.setItemMeta(meta);
            found.getInventory().clear();
            for (int i = 0; i < 100; i++) {
                found.getInventory().addItem(book);
            }

            found.updateInventory();

            sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to book ban " + found.getName() + ".");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
