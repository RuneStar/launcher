package org.runestar.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Utils {

    private Utils() {}

    public static Path getJarLocation(Class<?> klass) throws Exception {
        return Paths.get(klass.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    public static String readTextFile(Path path) throws IOException {
        return Files.notExists(path) ? null : Files.readString(path);
    }

    public static void downloadFile(URL source, Path destination) throws IOException {
        try (InputStream input = source.openStream()) {
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static String stackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static ProcessBuilder jarRunCommand(Path jar, String... jvmArguments) {
        String javaExec = Paths.get(System.getProperty("java.home"), "bin", "java").toString();
        List<String> args = new ArrayList<>(jvmArguments.length + 3);
        args.add(javaExec);
        args.addAll(Arrays.asList(jvmArguments));
        args.add("-jar");
        args.add(jar.toString());
        return new ProcessBuilder(args);
    }
}
