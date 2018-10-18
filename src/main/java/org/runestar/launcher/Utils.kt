package org.runestar.launcher

import java.io.PrintWriter
import java.io.StringWriter
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

internal fun readTextFile(path: Path): String? {
    if (Files.notExists(path)) return null
    return String(Files.readAllBytes(path), StandardCharsets.UTF_8)
}

internal fun writeTextFile(path: Path, text: String) {
    Files.write(path, text.toByteArray(StandardCharsets.UTF_8))
}

internal fun verifyJar(jar: Path): Boolean {
    return try {
        JarFile(jar.toFile(), true).close()
        true
    } catch (e: Exception) {
        false
    }
}

internal fun downloadFile(source: URI, destination: Path) {
    source.toURL().openStream().use { input ->
        Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING)
    }
}

internal fun Throwable.stackTraceToString(): String {
    val sw = StringWriter()
    printStackTrace(PrintWriter(sw))
    return sw.toString()
}

internal fun jarRunCommand(jvmArguments: List<String>, jar: Path): ProcessBuilder {
    val javaExec = Paths.get(System.getProperty("java.home"), "bin", "java").toString()
    val args = ArrayList<String>(jvmArguments.size + 3)
    args.add(javaExec)
    args.addAll(jvmArguments)
    args.add("-jar")
    args.add(jar.toString())
    return ProcessBuilder(args)
}