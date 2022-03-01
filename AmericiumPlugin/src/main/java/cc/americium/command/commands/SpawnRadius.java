package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpawnRadius implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"spawnradius"};
    }

    @Override
    public String getDescription() {
        return "Get or set the spawn radius.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "The server's spawn radius is " + Bukkit.getSpawnRadius() + ".");
            return;
        }

        int newRadius = 0;
        try {
            newRadius = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Bukkit.setSpawnRadius(newRadius);

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set the server spawn radius to " + newRadius + "!");
        sender.sendMessage(Colors.Prefix + Colors.Caution + "There is a good chance the server will need to restart for this to update!");
    }
}
