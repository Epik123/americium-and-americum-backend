package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Push implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"push"};
    }

    @Override
    public String getDescription() {
        return "Pushes the specified player";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "push <player> <amount>");
            return;
        }

        Player selected = Bukkit.getPlayer(args[0]);
        if (selected == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        int amount = 1;
        if(args.length > 1) {amount = Integer.parseInt(args[1]);}

        push(selected, amount);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Pushed " + selected.getName());
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private void push(Player p, Integer a){
        if (p.getPlayer() == null) {
            return;
        }

        p.setVelocity(p.getVelocity().setY(0));
        p.setVelocity(p.getLocation().getDirection().multiply(-3.1 * a));
        p.setVelocity(p.getVelocity().setY(1.12 * a));
    }
}
