package com.ielfgame.stupidGame.swing;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestHtml {

    public TestHtml() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("<html>this is something I want people to <span style='color:#00FF00;'>NOTICE</span></html>");

        // JLabel label = new JLabel("<html>this is something I want people to <font color=\"#00FF00\">NOTICE</font></html>");//will be shown on single line

        frame.add(label);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TestHtml();
            }
        });
    }
}
