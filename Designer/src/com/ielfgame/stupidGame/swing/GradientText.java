package com.ielfgame.stupidGame.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * 
 * @author  zhangtao
 * @msn        zht_dream@hotmail.com
 * @mail    zht_dream@hotmail.com
 * Let's Swing together.
 */
public class GradientText extends JComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        GradientText gText = new GradientText("zht_dream");

        JPanel panel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JScrollPane(new JTree()));
        JTextArea textArea = new JTextArea();
        textArea.setEnabled(false);
        StringBuffer text = new StringBuffer();
        text.append("good good study\n");
        text.append("day day up\n\n");
        text.append("MSN:zht_dream@hotmail.com\n");
        text.append("Mail:zht_dream@hotmail.com\n");
        text.append("                     ----zhangtao");
        textArea.setText(text.toString());
        textArea.setFont(new Font("Dialog", Font.ITALIC, 20));

        splitPane.setRightComponent(new JScrollPane(textArea));
        splitPane.setDividerLocation(100);

        panel.add(gText, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private int y = 0;
    private Font font = new Font("Times", Font.BOLD, 40);
    private String text = null;//new String("MSN:zht_dream@hotmail.com");

    ActionListener anAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(-1);
        }
    };

    public GradientText(String text) {
        this.text = text;
        this.registerKeyboardAction(anAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        putClientProperty("order", true);
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean order = Boolean.parseBoolean(getClientProperty("order").toString());
                if (order) {
                    y = y + getHeight() / 20;
                } else {
                    y = y - getHeight() / 20;
                }
                if (y > getHeight()) {
                    putClientProperty("order", false);
                    y = getHeight();
                }
                if (y < 0) {
                    putClientProperty("order", true);
                    y = 0;
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (text == null || font == null || text.trim().length() == 0) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        Rectangle2D textBounds = textLayout.getBounds();
        double sw = textBounds.getWidth();
        double sh = textBounds.getHeight();
        Dimension componetSize = this.getSize();

        double sx = (componetSize.getWidth() - 5) / sw;
        double sy = (componetSize.getHeight() - 5) / sh;
        AffineTransform transform = new AffineTransform();
        transform.translate(2, (sh - textLayout.getDescent() + 5) * sy);
        transform.scale(sx, sy - 5 / componetSize.getWidth());
        Shape shape = textLayout.getOutline(transform);
        g2d.draw(shape);
        g2d.setClip(shape);
        GradientPaint paint = new GradientPaint(0, 0, Color.BLACK, 0, y, Color.WHITE, true);
        g2d.setPaint(paint);
        g2d.fill(this.getBounds());
        
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.repaint();
    }
}