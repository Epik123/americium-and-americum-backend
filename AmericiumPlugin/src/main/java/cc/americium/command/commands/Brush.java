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

public class Brush implements Command, Listener {
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"brush", "br"};
    }

    @Override
    public String getDescription() {
        return "Make a worldedit-ish brush.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "br <material> (radius)");
            return;
        }

        Material block;
        try {
            block = Material.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (!block.isBlock()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Material is not a block, retard.");
            return;
        }

        int radius = 2;

        if (args.length > 1) {
            try {
                radius = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        ItemStack bone = new ItemStack(Material.BONE, 1);
        ItemMeta meta = bone.getItemMeta();
        try {
            meta.setDisplayName(ChatColor.GREEN + block.name().toLowerCase() + " wand " + radius);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        bone.setItemMeta(meta);

        PlayerInventory inventory = executor.getInventory();
        inventory.setItem(inventory.getHeldItemSlot(), bone);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Gave you a " + Utilities.prettyMaterial(block) + " wand with a radius of " + radius + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void interactEvent(PlayerInteractEvent e) throws IOException, InterruptedException {
        if (e.getAction() == null || e.getItem() == null || e.getMaterial() == null) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
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

        if (!itemName.contains(" wand") || held.getType() != Material.BONE) {
            return;
        }

        e.setCancelled(true);

        String[] splits = ChatColor.stripColor(itemName).split(" ");

        if (splits.length != 3) {
            return;
        }

        String materialName = splits[0].toUpperCase();

        int increase = 0;

        try {
            increase = Integer.parseInt(splits[2]);
        } catch (Exception ignored) {
            return;
        }

        Material foundMaterial;
        try {
            foundMaterial = Material.getMaterial(materialName);
        } catch (Exception ignored) {
            return;
        }

        if (foundMaterial == null) {
            return;
        }

        if (!foundMaterial.isBlock()) {
            // Just in case they update the name
            return;
        }

        Block targetBlock = player.getTargetBlock(null, 200);
        Material finalFoundMaterial = foundMaterial;

        Location max = new Location(targetBlock.getWorld(), targetBlock.getLocation().getX() + increase, targetBlock.getLocation().getY() + increase, targetBlock.getLocation().getZ() + increase);
        Location min = new Location(targetBlock.getWorld(), targetBlock.getLocation().getX() - increase, targetBlock.getLocation().getY() - increase, targetBlock.getLocation().getZ() - increase);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> {
            for (int x = Math.max(max.getBlockX(), min.getBlockX()); x >= Math.min(min.getBlockX(), max.getBlockX()); x--) {
                for (int y = Math.max(max.getBlockY(), min.getBlockY()); y >= Math.min(min.getBlockY(), max.getBlockY()); y--) {
                    for (int z = Math.max(max.getBlockZ(), min.getBlockZ()); z >= Math.min(min.getBlockZ(), max.getBlockZ()); z--) {
                        player.getWorld().getBlockAt(x, y, z).setType(finalFoundMaterial);
                    }
                }
            }
        });
    }
}
