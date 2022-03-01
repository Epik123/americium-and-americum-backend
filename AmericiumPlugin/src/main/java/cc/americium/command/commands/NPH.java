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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.UUID;

public class NPH implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public String[] getCommands() {
        return new String[]{"nph"};
    }

    @Override
    public String getDescription() {
        return "The player cannot attack anyone.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: nph <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
            return;
        }

        if (toggled.contains(found.getUniqueId())) {
            toggled.remove(found.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + found.getName() + " can now attack normally!");
            return;
        }

        toggled.add(found.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + found.getName() + " can no longer hit anyone!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }

        if (!toggled.contains(e.getDamager().getUniqueId())) {
            return;
        }

        e.setCancelled(true);
    }
}
