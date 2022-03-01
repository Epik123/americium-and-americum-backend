package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class FakeScaffold implements Command, Listener {
    private final HashMap<UUID, Material> toggled = new HashMap<>();
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"fakescaffold"};
    }

    @Override
    public String getDescription() {
        return "Places a specified block whenever a player moves.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "fakescaffold <player> (material)");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        if (toggled.containsKey(player.getUniqueId())) {
            toggled.remove(player.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + player.getName() + " is no longer scaffolding!");
            return;
        }

        Material block = Material.DIRT;
        if (args.length > 1) {
            try {
                block = Material.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            if (!block.isBlock()) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Material is not a block!");
                return;
            }
        }

        toggled.put(player.getUniqueId(), block);
        sender.sendMessage(Colors.Prefix + Colors.Success + player.getName() + " is now scaffolding using " + Utilities.prettyMaterial(block) + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (toggled.containsKey(player.getUniqueId())) {
            Material block = toggled.get(player.getUniqueId());
            Block currentLoc = player.getLocation().add(0, -1, 0).getBlock();
            if(currentLoc.getType() != Material.AIR) {
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> currentLoc.setType(block));
        }
    }
}
