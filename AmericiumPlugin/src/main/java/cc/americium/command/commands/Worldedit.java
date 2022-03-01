package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

// Maybe write a better fucking global data manager between classes
public class Worldedit implements Command, Listener {
    private final HashMap<UUID, Location> pos1 = new HashMap<>();
    private final HashMap<UUID, Location> pos2 = new HashMap<>();
    Material stoneAxe;
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"wand", "pos1", "pos2", "set", "air"};
    }

    @Override
    public String getDescription() {
        return "Gives you a wand, sets positions, and sets the current selection.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        switch (context.getCommand().toLowerCase()) {
            case "pos1":
                pos1.put(executor.getUniqueId(), executor.getLocation());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set your first position to " + Utilities.locationToString(executor.getLocation()) + "!");
                return;
            case "pos2":
                pos2.put(executor.getUniqueId(), executor.getLocation());
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set your second position to " + Utilities.locationToString(executor.getLocation()) + "!");
                return;
            case "wand":
                ItemStack wand = new ItemStack(stoneAxe, 1);
                ItemMeta meta = wand.getItemMeta();
                try {
                    meta.setDisplayName(ChatColor.GREEN + "Wand");
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    return;
                }

                wand.setItemMeta(meta);
                PlayerInventory inv = executor.getInventory();
                inv.setItem(inv.getHeldItemSlot(), wand);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully gave you a wand!");
                return;
            case "air":
                args = new String[]{"air"};
        }

        Material foundMaterial;

        // Remaining command is set
        if (args.length == 0) {
            ItemStack heldItem = Utilities.getHeldItem(executor);

            if (heldItem == null) {
                return;
            }

            if (heldItem.getType() == Material.AIR) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "set <block>, or hold an item");
                return;
            }

            foundMaterial = heldItem.getType();

        } else {
            try {
                foundMaterial = Material.getMaterial(args[0].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        if (foundMaterial == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Null material.");
            return;
        }

        if (!foundMaterial.isBlock()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That material is not a block!");
            return;
        }

        Location loc1;
        Location loc2;
        try {
            loc1 = pos1.get(executor.getUniqueId());
            loc2 = pos2.get(executor.getUniqueId());
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (loc1 == null || loc2 == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You haven't set one of your positions!");
            return;
        }

        Location max = loc1;
        Location min = loc2;

        Material finalFoundMaterial = foundMaterial;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            int totalBlocks = 0;
            for (int x = Math.max(max.getBlockX(), min.getBlockX()); x >= Math.min(min.getBlockX(), max.getBlockX()); x--) {
                for (int y = Math.max(max.getBlockY(), min.getBlockY()); y >= Math.min(min.getBlockY(), max.getBlockY()); y--) {
                    for (int z = Math.max(max.getBlockZ(), min.getBlockZ()); z >= Math.min(min.getBlockZ(), max.getBlockZ()); z--) {
                        executor.getWorld().getBlockAt(x, y, z).setType(finalFoundMaterial);
                        totalBlocks++;
                    }
                }
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set " + totalBlocks + " blocks to be " + Utilities.prettyMaterial(finalFoundMaterial) + "!");
        });
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);

        try {
            // In versions below 1.13, the api might have named it differently
            stoneAxe = Material.getMaterial("STONE_AXE");
        } catch (Exception ignored) {
            stoneAxe = Material.DIRT; // Fallback
        }
    }

    @EventHandler
    private void interactEvent(PlayerInteractEvent e) throws IOException, InterruptedException {
        if (e.getClickedBlock() == null || e.getAction() == null || e.getItem() == null || e.getMaterial() == null) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block clicked = e.getClickedBlock();
        if (clicked == null) {
            return;
        }

        Player player = e.getPlayer();
        if (!z.getAuth().isVerified(player.getName())) {
            return;
        }

        ItemStack held = e.getItem();
        if (held == null) {
            return;
        }

        if (held.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = held.getItemMeta();
        if (meta == null) {
            return;
        }

        String itemName = meta.getDisplayName();

        if (itemName == null || held.getType() == null) {
            return;
        }

        if (!itemName.equals(ChatColor.GREEN + "Wand") || held.getType() != stoneAxe) {
            return;
        }

        e.setCancelled(true);

        boolean isPos1 = e.getAction() == Action.LEFT_CLICK_BLOCK;
        if (isPos1) {
            pos1.put(player.getUniqueId(), clicked.getLocation());
        } else {
            pos2.put(player.getUniqueId(), clicked.getLocation());
        }

        player.sendMessage(Colors.Prefix + Colors.Success + "Set your " + (isPos1 ? "first position" : "second position") + " to " + Utilities.locationToString(clicked.getLocation()) + "!");
    }
}
