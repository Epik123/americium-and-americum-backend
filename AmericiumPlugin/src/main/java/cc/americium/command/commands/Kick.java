package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class Kick implements Command {
    private final String[] errors = {
            "java.lang.NullPointerException: group",
            "Internal Exception: java.lang.NullPointerException",
            "Disconnected",
            "Timed out",
            "Internal Exception: java.io.IOException: Connection reset by peer",
            "Internal Exception: io.netty.handler.codec.DecoderException: badly compressed packet - size of 2692183 is larger than protocol maximum of 2097152"
    };

    @Override
    public String[] getCommands() {
        return new String[]{"kick"};
    }

    @Override
    public String getDescription() {
        return "Kicks a player with a random error message.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: kick <player>");
            return;
        }

        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
            return;
        }

        final String message = errors[new Random().nextInt(errors.length)];
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            player.kickPlayer(message);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Attempted to kick " + player.getName() + " with reason of '" + message + "'!");
        });
    }
}
