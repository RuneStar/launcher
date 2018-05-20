package org.runestar.launcher

import java.awt.Image
import java.lang.invoke.MethodHandles
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon

class RuneStar : Project {

    override val title: String get() = "RuneStar"

    override val repoName: String get() = "RuneStar/client"

    override val directory: Path = Paths.get(System.getProperty("user.home"), "RuneStar")

    override val jarName: String get() = "client"

    override val iconImage: Image = ImageIO.read(javaClass.getResource("icon.png"))
}