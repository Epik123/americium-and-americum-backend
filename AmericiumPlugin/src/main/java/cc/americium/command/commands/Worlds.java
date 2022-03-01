package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Worlds implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"worlds"};
    }

    @Override
    public String getDescription() {
        return "Shows you a list of all worlds on the server.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        ArrayList<String> worldNames = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            worldNames.add(w.getName());
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "The current worlds appear to be: " + String.join(", ", worldNames) + ".");
    }
}
