package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoCraft implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean allPrevent = false;

    @Override
    public String[] getCommands() {
        return new String[]{"nocraft"};
    }

    @Override
    public String getDescription() {
        return "Prevent a player or all players from crafting.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "nocraft <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.allPrevent = !this.allPrevent;
            if (!allPrevent) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players can now craft!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players can't craft!");
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
            sender.sendMessage(Colors.Prefix + Colors.Failure + selected.getName() + " can now craft!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + selected.getName() + " can't craft!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onCraft(CraftItemEvent e) {
        if (this.allPrevent) {
            e.setCancelled(true);
            return;
        }

        List<HumanEntity> viewers = e.getViewers();
        if (viewers.size() == 0) {
            return;
        }

        if (viewers.get(0) == null) {
            return;
        }

        Player p = Bukkit.getPlayer(viewers.get(0).getUniqueId());

        if (p == null) {
            return;
        }

        if (toggled.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
