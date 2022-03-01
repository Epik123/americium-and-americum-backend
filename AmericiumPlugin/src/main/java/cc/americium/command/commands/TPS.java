package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TPS implements Command {
    private final long[] TICKS = new long[600];
    private final long LAST_TICK = 0L;
    private int TICK_COUNT = 0;

    @Override
    public String[] getCommands() {
        return new String[]{"tps"};
    }

    @Override
    public String getDescription() {
        return "Get server tps";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        try {
            double unRoundedTPS = getTPS();
            double tps = Math.round(unRoundedTPS * 100.0D) / 100.0D;

            ChatColor color;
            if (tps > 17) {
                color = ChatColor.GREEN;
            } else if (tps > 13) {
                color = ChatColor.GOLD;
            } else {
                color = ChatColor.RED;
            }

            if (tps > 20) {
                tps = tps - (tps - 20);
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "The server's TPS appears to be " + color + tps + Colors.Success + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }

    @Override
    public void onEnable(Z_ z) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(z, this::runSchedule, 0, 1);
    }

    private void runSchedule() {
        TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();
        TICK_COUNT += 1;
    }

    private double getTPS() {
        int ticks = 100;
        if (TICK_COUNT < ticks) {
            return 20.0D;
        }

        int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[target];

        return ticks / (elapsed / 1000.0D);
    }
}
