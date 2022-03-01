package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AllOps implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"allops"};
    }

    @Override
    public String getDescription() {
        return "Shows you all currently opped players.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ArrayList<String> players = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                players.add(p.getName());
            }
        }

        if (players.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "There are currently no opped players on!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Currently opped players are: " + String.join(", ", players));
    }
}
