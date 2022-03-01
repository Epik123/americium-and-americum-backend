package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Enderchest implements Command, Listener {
    // Modifier/Original Player
//    private final HashMap<UUID, UUID> savePlayer = new HashMap<>();
//    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"echest", "enderchest"};
    }

    @Override
    public String getDescription() {
        return "Open/modify a player's ender chest.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        Player found = executor;
        if (args.length != 0) {
            found = Bukkit.getPlayer(args[0]);
            if (found == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player is not online!");
                return;
            }
        }

        Player finalFound = found;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String name = finalFound.getName().equals(sender.getName()) ? "your" : finalFound.getName() + "'s";
            executor.openInventory(finalFound.getEnderChest());
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully opened " + name + " enderchest!");
        });
    }

    // Echests seem to save automatically.
/*
    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void inventoryExit(InventoryCloseEvent e) {
        UUID uuid;
        try {
            uuid = e.getPlayer().getUniqueId();
            if (uuid == null) {
                return;
            }
        } catch (Exception ignored) {
            return;
        }

        if (!this.savePlayer.containsKey(uuid)) {
            return;
        }

        Player otherPlayer = Bukkit.getPlayer(this.savePlayer.get(uuid));
        Player player = Bukkit.getPlayer(uuid);

        this.savePlayer.remove(uuid);

        if (player == null) {
            return;
        }

        if (otherPlayer == null) {
            player.sendMessage(Colors.Prefix + Colors.Failure + "That player is offline! Could not update their enderchest!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> {
            try {
                otherPlayer.getEnderChest().setContents(e.getInventory().getContents());
                player.sendMessage(Colors.Prefix + Colors.Success + "Successfully updated " + otherPlayer.getName() + "'s inventory!");
            } catch (Exception ex) {
                player.sendMessage(Colors.Prefix + Colors.Failure + ex.getMessage());
            }
        });
    }
*/
}
