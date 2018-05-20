package org.runestar.launcher

import java.awt.Image
import java.nio.file.Path

interface Project {

    val title: String

    val repoName: String

    val directory: Path

    val jarName: String

    val iconImage: Image

    val jarPath: Path get() = directory.resolve("$jarName.jar")

    val jarSourcePath: Path get() = directory.resolve("$jarName.jar.source")

    val jarPrmPath: Path get() = directory.resolve("$jarName.jar.prm")
}