package cc.americium.command.commands.admin;

import cc.americium.Authentication;
import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

public class AllowAllAuth implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"allowallauth"};
    }

    @Override
    public String getDescription() {
        return "Toggle every player who joins being authorized. " + Colors.Caution + "ADMIN ONLY!";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        Authentication auth = context.getZ_().getAuth();
        auth.setAllAuth(!auth.isAllAuth());

        if (auth.isAllAuth()) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "All players that join will now be verified!");
            return;
        }
        sender.sendMessage(Colors.Prefix + Colors.Success + "All players that join will no longer be verified!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (auth.isLoggedIn(p.getName()) && !auth.isVerified(p.getName())) {
                p.sendMessage(Colors.Prefix + Colors.Caution + "Your trial period has been ended by an admin.");
                auth.setLoggedIn(p.getName(), false);
            }
        }
    }
}
