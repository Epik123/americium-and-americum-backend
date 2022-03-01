package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class Blame implements Command, Listener {
    // <Victim, Blamed>
    private final HashMap<String, String> names = new HashMap<>();

    @Override
    public String[] getCommands() {
        return new String[]{"blame"};
    }

    @Override
    public String getDescription() {
        return "Blame a player for killing another player or all players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "blame <victim/*> <blamed>");
            return;
        }

        Player blamed = Bukkit.getPlayer(args[1]);
        if (blamed == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The blamed player could not be found!");
            return;
        }

        if (args[0].equals("*")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId() == blamed.getUniqueId()) {
                    continue;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                    names.put(p.getName(), blamed.getName());
                    p.setHealth(0);
                });
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully blamed " + blamed.getName() + " for killing everyone!");
            return;
        }

        Player victim = Bukkit.getPlayer(args[0]);
        if (victim == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The victim could not be found!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            names.put(victim.getName(), blamed.getName());
            victim.setHealth(0);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully blamed " + blamed.getName() + " for killing " + victim.getName() + "!");
        });
    }

    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.LOWEST) // Allow transformations
    private void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        String name = e.getEntity().getName();

        if (names.containsKey(name)) {
            e.setDeathMessage(name + " was killed by " + names.get(name) + ".");
            names.remove(name);
        }
    }
}
