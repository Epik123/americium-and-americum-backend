package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip implements Command {
    // https://mkyong.com/java/how-to-decompress-files-from-a-zip-file/
    public static void unzipFolder(Path source, Path target) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = zipEntry.getName().endsWith(File.separator);
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }

    // protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        return targetDirResolved.normalize();
    }

    @Override
    public String[] getCommands() {
        return new String[]{"unzip"};
    }

    @Override
    public String getDescription() {
        return "Unzip a zip file. ";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "unzip <path/to/file.zip> <path/to/output>");
            return;
        }

        try {
            unzipFolder(Paths.get(args[0]), Paths.get(args[1]));
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully unzipped " + args[0] + " to " + args[1] + "!");
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }
}
