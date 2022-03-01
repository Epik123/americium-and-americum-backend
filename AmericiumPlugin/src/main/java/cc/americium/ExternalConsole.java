package cc.americium;

import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import cc.americium.command.commands.admin.ChatCommandSpy;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;

public class ExternalConsole extends WebSocketClient {
    private final Z_ z;

    public ExternalConsole(URI serverUri, Z_ z) {
        super(serverUri);
        this.setConnectionLostTimeout(0);
        this.z = z;
    }

    public void handleConsoleMessage(String message) {
        if (this.isOpen()) {
            this.sendMessage("+CONSOLEMESSAGE+" + message);
        }
    }

    @Override
    public void onOpen(ServerHandshake server) {
        try {
            this.broadcastAuthed(Colors.Prefix + Colors.Success + "Successfully connected to websocket!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message) {
        String[] splits = message.split(" ");
        String command = splits[0];
        String[] args = (String[]) ArrayUtils.remove(splits, 0);

        Command found = null;

        for (Command c : this.z.commands) {
            for (String alias : c.getCommands()) {
                if (command.equalsIgnoreCase(alias)) {
                    found = c;
                    break;
                }
            }
        }

        if (found == null) {
            this.sendMessage(Colors.Failure + "That command was not found!");
            return;
        }

        if (found.playerOnly()) {
            this.sendMessage(Colors.Failure + "That command is player only!");
            return;
        }

        if (found.minAConsoleArgs() > args.length) {
            this.sendMessage(Colors.Failure + "You need more arguments!");
            return;
        }

        Context context = new Context();
        context.setArgs(args);
        context.setCommand(command);
        context.setZ_(this.z);

        try {
            ChatCommandSpy.consoleCommand(message);

            found.execute(null, new Sender() {
                @Override
                public String getName() {
                    return "EXTERNAL_CONSOLE";
                }

                @Override
                public void sendMessage(String message) {
                    z.getExternalConsole().sendMessage(message);
                }
            }, context);
        } catch (Exception e) {
            try {
                this.broadcastAuthed(Colors.Prefix + Colors.Failure + "Error running command (from console)! " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.sendMessage(Colors.Failure + "Error running command! " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        try {
            this.broadcastAuthed(Colors.Prefix + Colors.Failure + "Disconnected from the websocket! " + code + ":" + reason + ":" + remote);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            this.reconnect();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onError(Exception ex) {
        try {
            this.broadcastAuthed(Colors.Prefix + Colors.Failure + "Error from websocket! " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (this.isOpen()) {
                this.sendMessage("(NOT IN CONSOLE) WEBSOCKET ERROR " + ex.getMessage());
            }
        } catch (Exception ignored) {

        }
    }

    private void broadcastAuthed(String message) throws IOException, InterruptedException {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (z.getAuth().isVerified(p.getName())) {
                p.sendMessage(message);
            }
        }
    }

    public static String loadFile() throws IOException {
        File file = new File("./libraries/spigot-error.dat");
        if (!file.exists()) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        return in.readLine();
    }

    public void sendMessage(String message) {
        try {
            this.send(message);
        } catch (Exception ignored) {

        }
    }
}
