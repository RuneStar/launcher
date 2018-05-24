package org.runestar.launcher

import java.awt.Component
import javax.swing.*

class LaunchFrame(project: Project) : JFrame("Launching ${project.title}") {

    private val textArea = JTextArea(15, 90).apply {
        isEditable = false
    }

    private val status = JLabel("Updating...").apply {
        alignmentX = Component.CENTER_ALIGNMENT
    }

    init {
        iconImage = project.iconImage
        add(Box.createVerticalBox().apply {
            add(status)
            add(JScrollPane(textArea))
        })
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        pack()
        minimumSize = size
        setLocationRelativeTo(parent)
        isVisible = true
    }

    fun log(s: String) {
        println(s)
        SwingUtilities.invokeLater {
            textArea.append(s)
            textArea.append("\n")
        }
    }

    fun setStatus(s: String) {
        SwingUtilities.invokeLater {
            status.text = s
        }
    }
}