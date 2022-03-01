package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResourcePack implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"resourcepack"};
    }

    @Override
    public String getDescription() {
        return "Request for a user to download a texture pack.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "resourcepack <player | *> <url>");
            return;
        }

        if (args[0].equals("*")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                try {
                    p.setResourcePack(args[1]);
                } catch (Exception e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    // Return here, errors will only be issues with the url so don't show the success message.
                    return;
                }
            }


            sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to request all users download texture pack " + args[1] + "!");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        try {
            found.setResourcePack(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to request user " + found.getName() + " to download texture pack " + args[1] + "!");
    }
}
