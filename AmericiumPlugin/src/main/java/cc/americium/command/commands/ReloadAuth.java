package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ReloadAuth implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"reloadauth"};
    }

    @Override
    public String getDescription() {
        return "Reload the auth list by sending another request.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        context.getZ_().refreshAuth(executor.getName());
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully reloaded current auth list!");
    }
}