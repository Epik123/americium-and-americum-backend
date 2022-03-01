package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class TimeFucker implements Command {
    private final ArrayList<UUID> toggled = new ArrayList<>();
    private boolean fuckAll = false;
    private boolean day = false;

    @Override
    public String[] getCommands() {
        return new String[]{"timefucker"};
    }

    @Override
    public String getDescription() {
        return "Change a player time constantly. ouch.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "timefucker <player | *>");
            return;
        }

        if (args[0].equals("*")) {
            this.fuckAll = !this.fuckAll;

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.resetPlayerTime();
            }

            if (!fuckAll) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "All players are no longer time fucked!");
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Success + "All players are now time fucked!");
            }

            return;
        }

        Player selected = Bukkit.getPlayer(args[0]);
        if (selected == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        selected.resetPlayerTime();

        if (toggled.contains(selected.getUniqueId())) {
            toggled.remove(selected.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + selected.getName() + " is no longer time fucked!");
            return;
        }

        toggled.add(selected.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + selected.getName() + " is now time fucked!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getScheduler().scheduleSyncRepeatingTask(z, this::tickFucker, 0, 20);
    }

    private void tickFucker() {
        if (this.toggled.size() == 0 && !this.fuckAll) {
            return;
        }

        day = !day;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (this.toggled.contains(p.getUniqueId()) || this.fuckAll) {
                fuck(p);
            }
        }
    }

    private void fuck(Player p) {
        if (day) {
            p.setPlayerTime(1000, false);
        } else {
            p.setPlayerTime(13000, false);
        }
    }
}
