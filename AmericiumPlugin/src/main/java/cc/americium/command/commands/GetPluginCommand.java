package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GetPluginCommand implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"getplugincommand"};
    }

    @Override
    public String getDescription() {
        return "Get plugin which registered command.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "getplugincommand <command>");
            return;
        }

        PluginCommand found = Bukkit.getPluginCommand(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That command wasn't found!");
            return;
        }

        Plugin plugin = found.getPlugin();

        if (plugin == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Plugin is null!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Command " + found.getName() + " appears to be registered by " + plugin.getName() + "!");
    }
}
