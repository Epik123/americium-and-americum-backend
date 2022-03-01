package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

public class Logout implements Command {
    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"logout"};
    }

    @Override
    public String getDescription() {
        return "Logs you out. Idiot.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        context.getZ_().getAuth().setLoggedIn(sender.getName(), false);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully logged you out!");
    }
}
