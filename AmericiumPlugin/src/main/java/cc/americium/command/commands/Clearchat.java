package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Clearchat implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"clearchat"};
    }

    @Override
    public String getDescription() {
        return "Sends every player a LOT of blank messages.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(StringUtils.repeat(" \n", 100));
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully cleared chat!");
    }
}
