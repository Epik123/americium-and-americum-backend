package cc.americium.command.commands.admin;

import cc.americium.Authentication;
import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class VPN implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"checkwebsite", "cw"};
    }
    
    @Override
    public String getDescription() {
        return "Checks if a player/ip is using a vpn, as well as see where they're from.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "vpn <player/ip>");
            return;
        }

        String ip = args[0];
        Player player = Bukkit.getPlayer(args[0]);
        if (player != null) {
            ip = player.getAddress().getAddress().toString().replaceAll("/", "");
        }

        Authentication auth = context.getZ_().getAuth();
        if (player != null) {
            if (auth.isVerified(player.getName()) && !sender.getName().equals(player.getName()) && !auth.isAdmin(sender.getName())) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player is verified! You cant lookup their IP!");
                return;
            }
        }

        Reader reader;
        try {
            URL url = new URL("https://bromine.spigotmc.co/");
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        String sb = Utilities.getStringFromBuffer(reader);
        if (sb.length() == 0) {
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Results for " + ip + ":");
        sender.sendMessage(Colors.Caution + sb);
    }
}
