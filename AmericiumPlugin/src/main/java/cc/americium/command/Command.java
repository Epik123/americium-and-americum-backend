package cc.americium.command;

import cc.americium.Z_;
import org.bukkit.entity.Player;

import java.io.IOException;

public interface Command {
    String[] getCommands();

    String getDescription();

    void execute(Player executor, Sender sender, Context context) throws IOException, InterruptedException;

    default boolean playerOnly() {
        return false;
    }

    // For commands like gargle/mute
    default int minAConsoleArgs() {
        return 0;
    }

    default void onEnable(Z_ z) {

    }

    default void onDisable(Z_ z) {

    }
}

