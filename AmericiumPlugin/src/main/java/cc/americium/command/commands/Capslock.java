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

import java.util.ArrayList;
import java.util.UUID;

public class Capslock implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public String[] getCommands() {
        return new String[]{"capslock"};
    }

    @Override
    public String getDescription() {
        return "Whenever the player speaks in chat, they speak in all capitals.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: capslock <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
            return;
        }

        if (toggled.contains(found.getUniqueId())) {
            toggled.remove(found.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Un-capslocked " + found.getName() + "!");
            return;
        }

        toggled.add(found.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + "Capslocked " + found.getName() + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent e) {
        if (toggled.contains(e.getPlayer().getUniqueId())) {
            e.setMessage(e.getMessage().toUpperCase());
        }
    }
}
