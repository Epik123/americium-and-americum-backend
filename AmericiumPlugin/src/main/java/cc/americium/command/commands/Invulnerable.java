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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Invulnerable implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"invul", "invulnerable"};
    }

    @Override
    public String getDescription() {
        return "The player cannot be attacked anyone.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        Player selected = executor;
        if (args.length != 0) {
            selected = Bukkit.getPlayer(args[0]);
            if (selected == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        String name = selected.getName().equals(sender.getName()) ? "You are" : selected.getName() + " is";
        if (toggled.contains(selected.getUniqueId())) {
            toggled.remove(selected.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + name + " no longer invulnerable!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + name + " now invulnerable!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (!toggled.contains(e.getEntity().getUniqueId())) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    private void hungerLoseEvent(FoodLevelChangeEvent e) {
        HumanEntity thing = e.getEntity();

        if (thing.getUniqueId() == null) {
            return;
        }

        if (this.toggled.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
