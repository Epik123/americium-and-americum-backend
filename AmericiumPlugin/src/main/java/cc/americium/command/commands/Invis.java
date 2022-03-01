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
import java.util.UUID;

public class Invis implements Command {
    private final ArrayList<UUID> invisList = new ArrayList<>();

    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"invis"};
    }

    @Override
    public String getDescription() {
        return "Gives/removes perm invis.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        Player selected = executor;
        if (args.length != 0) {
            selected = Bukkit.getPlayer(args[0]);
            if (selected == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        Player finalSelected = selected;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String name = finalSelected.getName().equals(sender.getName()) ? "you" : finalSelected.getName();
            if (invisList.contains(finalSelected.getUniqueId())) {
                invisList.remove(finalSelected.getUniqueId());
                finalSelected.removePotionEffect(PotionEffectType.INVISIBILITY);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Removing invisibility from " + name + "!");
                return;
            }

            invisList.add(finalSelected.getUniqueId());
            finalSelected.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false));
            sender.sendMessage(Colors.Prefix + Colors.Success + "Giving invisibility to " + name + "!");
        });
    }
}
