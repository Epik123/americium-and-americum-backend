package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.UUID;

public class SilentChest implements Command, Listener {
    // This sucks, but allows vanish to enable it :)
    public static final ArrayList<UUID> toggled = new ArrayList<>();
    private final ArrayList<Material> allowedBlocks = new ArrayList<>();

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"silentchest", "sc"};
    }

    @Override
    public String getDescription() {
        return "Quickly changes your gamemode into spectator before you open a chest. (You cannot edit the contents)";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        UUID uuid = executor.getUniqueId();
        if (toggled.contains(uuid)) {
            toggled.remove(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are no longer using silent chest!");
            return;
        }

        toggled.add(uuid);
        sender.sendMessage(Colors.Prefix + Colors.Success + "You are now using silent chest!");
    }

    @Override
    public void onEnable(Z_ z) {
        allowedBlocks.add(Material.CHEST);
        allowedBlocks.add(Material.TRAPPED_CHEST);

        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void chestInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == null || e.getClickedBlock() == null) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clicked = e.getClickedBlock();
        if (!toggled.contains(player.getUniqueId())) {
            return;
        }

        if (!this.allowedBlocks.contains(clicked.getType())) {
            return;
        }

        e.setCancelled(true);

        try {
            Chest chest = (Chest) clicked.getState();
            Inventory inventory = Bukkit.getServer().createInventory(player, chest.getInventory().getSize());
            inventory.setContents(chest.getInventory().getContents());
            player.openInventory(inventory);

            player.sendMessage(Colors.Prefix + Colors.Caution + "Opening chest silently. Cannot edit.");
        } catch (Exception ex) {
            player.sendMessage(Colors.Prefix + Colors.Failure + ex.getMessage());
        }
    }
}
