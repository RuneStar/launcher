package org.runestar.launcher

import org.slf4j.LoggerFactory
import java.awt.Component
import javax.swing.*

class LaunchFrame(project: Project) : JFrame("Launching ${project.title}") {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val textArea = JTextArea(15, 90).apply {
        isEditable = false
    }

    init {
        iconImage = project.iconImage
        add(Box.createVerticalBox().apply {
            add(JLabel("Updating...").apply {
                alignmentX = Component.CENTER_ALIGNMENT
            })
            add(JScrollPane(textArea))
        })
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        pack()
        minimumSize = size
        setLocationRelativeTo(parent)
        isVisible = true
    }

    fun log(s: String) {
        logger.info(s)
        SwingUtilities.invokeLater {
            textArea.append(s)
            textArea.append("\n")
        }
    }
}