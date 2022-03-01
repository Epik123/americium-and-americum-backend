package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class Powertool implements Command, Listener {
    private final String prefix = ChatColor.GREEN + "Powertool: ";
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"pt"};
    }

    @Override
    public String getDescription() {
        return "Set a command to whatever item you are holding.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "pt </command with args>");
            return;
        }

        String command = String.join(" ", context.getArgs());

        ItemStack heldItem = Utilities.getHeldItem(executor);

        if (heldItem == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You aren't holding anything fucktard.");
            return;
        }

        ItemMeta meta = heldItem.getItemMeta();
        meta.setDisplayName(this.prefix + command);
        heldItem.setItemMeta(meta);

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully added command '" + command + "' to your " + Utilities.prettyMaterial(heldItem.getType()) + "!");
    }

    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void playerInteract(PlayerInteractEvent e) throws IOException, InterruptedException {
        if (e.getAction() == null || e.getItem() == null || e.getMaterial() == null) {
            return;
        }

        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        Player p = e.getPlayer();
        if (!z.getAuth().isVerified(p.getName())) {
            return;
        }

        ItemMeta meta = e.getItem().getItemMeta();
        if (meta == null) {
            return;
        }

        if (!meta.hasDisplayName()) {
            return;
        }

        String name = meta.getDisplayName();
        if (!name.startsWith(prefix)) {
            return;
        }

        e.setCancelled(true);
        // On survival right click, can be a ghost block
        p.updateInventory();

        name = name.replace(prefix, "");
        p.chat(name);
    }
}
