package org.runestar.launcher;

import javax.swing.*;

public final class LaunchFrame extends JFrame {

    private final JTextArea textArea;

    private final JLabel status;

    public LaunchFrame() {
        super("Launching " + RuneStar.TITLE);
        JTextArea ta = new JTextArea(15, 90);
        ta.setEditable(false);
        textArea = ta;
        JLabel label = new JLabel("Updating...");
        label.setAlignmentX(0.5F);
        status = label;
        setIconImage(RuneStar.ICON);
        Box box = Box.createVerticalBox();
        box.add(status);
        box.add((new JScrollPane(textArea)));
        add(box);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    public void log(String s) {
        System.out.println(s);
        SwingUtilities.invokeLater(() -> {
                textArea.append(s);
                textArea.append("\n");
        });
    }

    public void setStatus(String s) {
        SwingUtilities.invokeLater(() -> status.setText(s));
    }
}
