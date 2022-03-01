package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffDetector implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"staffdetector", "sd"};
    }

    @Override
    public String getDescription() {
        return "Toggles on/off alerting you if an opped player is near you.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        UUID uuid = executor.getUniqueId();
        if (toggled.contains(uuid)) {
            toggled.remove(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You will no longer be notified of staff near you.");
            return;
        }

        toggled.add(uuid);
        sender.sendMessage(Colors.Prefix + Colors.Success + "You will now be notified of staff near you.");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getScheduler().scheduleSyncRepeatingTask(z, () -> {
            try {
                this.runnable(z);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 20 * 5);
    }

    private void runnable(Z_ z) throws IOException, InterruptedException {
        for (UUID toggledUUID : toggled) {
            Player player = Bukkit.getPlayer(toggledUUID);
            if (player == null) {
                continue;
            }

            List<Entity> entities = player.getNearbyEntities(100, 100, 100);
            for (Entity e : entities) {
                if (e.getType() != EntityType.PLAYER) {
                    continue;
                }

                Player foundPlayer = Bukkit.getPlayer(e.getUniqueId());
                if (foundPlayer == null) {
                    continue;
                }

                if (foundPlayer.isOp() && !z.getAuth().isVerified(foundPlayer.getName())) {
                    player.sendMessage(Colors.Prefix + Colors.Caution + "(Staff Detector) " + foundPlayer.getName() + " is near you!");
                }
            }
        }
    }
}
