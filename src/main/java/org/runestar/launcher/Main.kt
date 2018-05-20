@file:JvmName("Main")

package org.runestar.launcher

import org.kohsuke.github.GitHub
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile
import javax.swing.UIManager

fun main(args: Array<String>) {
    val project: Project = RuneStar()
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    val frame = LaunchFrame(project)

    try {
        updateJar(frame, project)
        launchJar(frame, project.jarPath, getArguments(frame, project))
    } finally {
        Thread.sleep(2000)
        frame.dispose()
    }
}

private fun updateJar(frame: LaunchFrame, project: Project) {
    val sourcePath = project.jarSourcePath
    val jarPath = project.jarPath

    frame.log("Reading source URL from '$sourcePath'")
    val sourceUrl = readTextFile(sourcePath)?.let { URI(it) }
    frame.log("Source URL: $sourceUrl")

    frame.log("Connecting to GitHub")
    val github = GitHub.connectAnonymously()

    frame.log("Using repository '${project.repoName}'")
    val repo = github.getRepository(project.repoName)

    frame.log("Finding latest release")
    val latestRelease = repo.listReleases().first()
    val assets = latestRelease.assets
    if (assets.size != 1) {
        frame.log("Release must only have one asset")
        throw IllegalStateException()
    }
    val asset = assets.first()
    val downloadUrl = URI(asset.browserDownloadUrl)
    frame.log("Latest URL: $downloadUrl")

    if (sourceUrl == null || sourceUrl != downloadUrl || !verifyJar(jarPath)) {
        frame.log("Downloading '$downloadUrl' to '$jarPath'")
        downloadFile(downloadUrl, jarPath)

        frame.log("Writing '$downloadUrl' to '$sourcePath'")
        writeTextFile(sourcePath, downloadUrl.toString())
    } else {
        frame.log("'$jarPath' is up to date")
    }
}

private fun getArguments(frame: LaunchFrame, project: Project): List<String> {
    val prmFile = project.jarPrmPath
    val jar = project.jarPath
    if (Files.notExists(prmFile)) {
        frame.log("Extracting '.prm' from '$jar' to '$prmFile'")
        ZipFile(jar.toFile()).use { zf ->
            zf.getInputStream(zf.getEntry("client.prm")).use { input ->
                Files.copy(input, prmFile)
            }
        }
    }
    frame.log("Reading arguments from '$prmFile'")
    return Files.readAllLines(prmFile, StandardCharsets.UTF_8)
            .filter { !it.startsWith('#') && it.isNotBlank() }
            .map { it.trim() }
}

private fun launchJar(frame: LaunchFrame, jar: Path, jarArgs: List<String>) {
    frame.log("Found arguments: $jarArgs")
    val command = listOf("java", *jarArgs.toTypedArray(), "-jar", jar.toString())
    frame.log("Using command: ${command.joinToString(" ")}")
    val processBuilder = ProcessBuilder(command)
    processBuilder.start()
    frame.log("Launched")
}