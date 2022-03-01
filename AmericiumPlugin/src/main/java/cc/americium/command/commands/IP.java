package cc.americium.command.commands;

import cc.americium.Authentication;
import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

public class IP implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"ip"};
    }

    @Override
    public String getDescription() {
        return "Shows you a player's IP address.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        String command = context.getCommand();
        if (command.equalsIgnoreCase("ip")) {
            String[] args = context.getArgs();
            if (args.length == 0) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "ip <player>");
                return;
            }

            Player foundPlayer = Bukkit.getPlayer(args[0]);
            if (foundPlayer == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }

            Authentication auth = context.getZ_().getAuth();
            if (auth.isVerified(foundPlayer.getName()) && !sender.getName().equals(foundPlayer.getName()) && !auth.isAdmin(sender.getName())) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player is verified! You cannot view their IP!");
                return;
            }

            try {
                sender.sendMessage(Colors.Prefix + Colors.Success + foundPlayer.getName() + "'s IP address is: '" + foundPlayer.getAddress().getAddress().toString().replaceAll("/", "") + "'!");
            } catch (NullPointerException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        }
    }
}
