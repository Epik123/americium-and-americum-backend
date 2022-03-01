package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Gamemodes implements Command {
    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"gmc", "gms", "gma", "gmsp"};
    }

    @Override
    public String getDescription() {
        return "Sets your/another player's gamemode.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        GameMode gamemode;

        switch (context.getCommand().toLowerCase()) {
            case "gms":
                gamemode = GameMode.SURVIVAL;
                break;
            case "gma":
                gamemode = GameMode.ADVENTURE;
                break;
            case "gmsp":
                gamemode = GameMode.SPECTATOR;
                break;
            default:
                gamemode = GameMode.CREATIVE;
                break;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            String[] args = context.getArgs();
            if (args.length == 0) {
                executor.setGameMode(gamemode);
                sender.sendMessage(Colors.Prefix + Colors.Success + "Set your gamemode to " + gamemode.name().toLowerCase() + "!");
                return;
            }

            Player foundPlayer = Bukkit.getPlayer(args[0]);
            if (foundPlayer == null) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That player was not found!");
                return;
            }

            foundPlayer.setGameMode(gamemode);
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully set " + foundPlayer.getName() + "'s gamemode to " + gamemode.name().toLowerCase() + "!");
        });
    }
}