package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Help implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"help", "allcommands"};
    }

    @Override
    public String getDescription() {
        return "Show all commands, a certain command, or pages of commands with descriptions.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException {
        String[] args = context.getArgs();
        Z_ z = context.getZ_();
        Command[] listCommands = z.commands;

        if (z.getAuth().isAdmin(sender.getName())) {
            listCommands = Utilities.getAdminCommands(z);
        }

        ArrayList<Command> commands = new ArrayList<>(Arrays.asList(listCommands));

        if (executor == null) {
            commands.removeIf(Command::playerOnly);
        }

        if (args.length == 0 && context.getCommand().equalsIgnoreCase("allcommands")) {
            ArrayList<String> sb = new ArrayList<>();
            for (Command c : commands) {
                if (c.getCommands().length == 0) {
                    continue;
                }

                for (String command : c.getCommands()) {
                    sb.add(ChatColor.RED + command);
                }
            }

            sender.sendMessage(Colors.Prefix + ChatColor.BLUE + "If you need help with a specific command please type 'help <command>'.");
            sender.sendMessage(String.join(ChatColor.GRAY + ", ", sb) + ChatColor.GRAY + ".");
            return;
        }

        int page = 1;

        if (args.length != 0) {
            for (Command c : commands) {
                for (String cc : c.getCommands()) {
                    if (cc.equalsIgnoreCase(context.getArgs()[0])) {
                        String descriptionSuffix = "";
                        if (executor == null && c.minAConsoleArgs() != 0) {
                            descriptionSuffix += " MINIMUM OF " + c.minAConsoleArgs() + " ARGUMENT(S)";
                        }

                        sender.sendMessage(ChatColor.GRAY + String.join(", ", c.getCommands()) + ": " + ChatColor.RED + c.getDescription() + descriptionSuffix);
                        return;
                    }
                }
            }

            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // If this fails, they most likely tried to help a non-existent command
                sender.sendMessage(Colors.Prefix + Colors.Failure + "That command was not found!");
                return;
            }
        }

        if (page < 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Page must not be negative!");
            return;
        }

        ArrayList<Command> currentCommands = new ArrayList<>();
        int currentPageAmount = 0;
        int currentPage = 1;
        int allPages = 1;

        for (Command command : commands) {
            currentPageAmount++;
            if (currentPage == page) {
                currentCommands.add(command);
            }

            if (currentPageAmount > 5) {
                currentPage++;
//                if (currentPage > page) {
//                    break;
//                }
                allPages++;

                currentPageAmount = 0;
            }
        }

        if (currentCommands.size() == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Could not find that page!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Page " + page + "/" + allPages + ".");

        for (Command command : currentCommands) {
            StringBuilder descriptionSuffix = new StringBuilder();
            if (executor == null && command.minAConsoleArgs() != 0) {
                descriptionSuffix.append(" MINIMUM OF ").append(command.minAConsoleArgs()).append(" ARGUMENT(S)");
            }

            sender.sendMessage(ChatColor.GRAY + String.join(", ", command.getCommands()) + ": " + ChatColor.RED + command.getDescription() + descriptionSuffix);
        }
    }
}
