package cc.americium;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

public class ZipDirectoryVisitor extends SimpleFileVisitor<Path> {
    private final Path dirToZip;
    private final List<ZipEntry> zipEntries; // files and folders inside source folder as zip entries

    public ZipDirectoryVisitor(Path dirToZip) throws IOException {
        this.dirToZip = dirToZip;
        zipEntries = new ArrayList<>();
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        // According to zip standard backslashes
        // should not be used in zip entries
        String zipFile = dirToZip.getParent().relativize(path).toString().replace("\\", "/");
        ZipEntry entry = new ZipEntry(zipFile);
        zipEntries.add(entry);
        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        String zipDir = dirToZip.getParent().relativize(path).toString().replace("\\", "/");
        // Zip directory entries should end with a forward slash
        ZipEntry entry = new ZipEntry(zipDir + "/");
        zipEntries.add(entry);
        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        System.err.format("Could not visit file %s while creating a file list from file tree", path);
        return FileVisitResult.TERMINATE;
    }

    public List<ZipEntry> getZipEntries() {
        return zipEntries;
    }
}