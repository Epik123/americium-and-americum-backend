package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class Players implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"players"};
    }

    @Override
    public String getDescription() {
        return "List all online players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        ArrayList<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (context.getZ_().getAuth().isVerified(p.getName())) {
                list.add(Colors.Success + p.getName());
            } else {
                list.add(Colors.Caution + p.getName());
            }
        }

        if (list.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "There are no players currently online!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Current players: " + String.join(Colors.Success + ", ", list));
    }
}
