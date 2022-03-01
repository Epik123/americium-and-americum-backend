package cc.americium.command.commands;

import cc.americium.Authentication;
import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class ListLoggedIn implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"listloggedin"};
    }

    @Override
    public String getDescription() {
        return "Shows you all verified/logged in players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        ArrayList<String> loggedIn = new ArrayList<>();
        ArrayList<String> verified = new ArrayList<>();

        Authentication auth = context.getZ_().getAuth();

        for (Player p : Bukkit.getOnlinePlayers()) {
            String name = p.getName();
            if (auth.isLoggedIn(name)) {
                loggedIn.add(name);
                continue;
            }

            if (auth.isVerified(name)) {
                verified.add(name);
            }
        }

        if (loggedIn.size() == 0 && verified.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "There are no logged in/verified players!");
            return;
        }

        if (loggedIn.size() != 0) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "Logged in players: " + String.join(", ", loggedIn));
        }

        if (verified.size() != 0) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "Verified players: " + String.join(", ", verified));
        }
    }
}
