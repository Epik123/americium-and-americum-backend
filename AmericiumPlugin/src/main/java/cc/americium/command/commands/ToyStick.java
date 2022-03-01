package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
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

public class ToyStick implements Command, Listener {
    private final String itemName = ChatColor.GREEN + "Toy Stick ";
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"toystick"};
    }

    @Override
    public String getDescription() {
        return "Gives you a toy stick, where you click tnt is placed.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        float amount = 5;

        if (args.length != 0) {
            try {
                amount = Float.parseFloat(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }

        ItemStack stick = new ItemStack(Material.STICK, 1);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName(itemName + amount);
        stick.setItemMeta(meta);

        PlayerInventory inventory = executor.getInventory();
        inventory.setItem(inventory.getHeldItemSlot(), stick);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Gave you a toy stick!");
    }


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

        if (!itemName.startsWith(this.itemName) || held.getType() != Material.STICK) {
            return;
        }

        itemName = itemName.replace(this.itemName, "");

        float power = 4;
        try {
            power = Float.parseFloat(itemName);
        } catch (Exception ignored) {

        }

        e.setCancelled(true);

        Block targetBlock = player.getTargetBlock(null, 500);

        try {
            targetBlock.setType(Material.AIR);
        } catch (Exception ignored) {

        }

        targetBlock.getWorld().createExplosion(targetBlock.getLocation(), power);
    }

}