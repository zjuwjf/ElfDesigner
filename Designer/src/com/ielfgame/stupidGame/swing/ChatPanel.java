package com.ielfgame.stupidGame.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ChatPanel {

	private JFrame jf = new JFrame("Testing msgpanel");
	private JScrollPane scroll = new JScrollPane();
	private JPanel jp;
	private ChatPaneMsgBox mb;
	private ChatPaneMsgBox mb2;
	private JPanel cp = new JPanel();
	private JButton jb = new JButton("  ME !!! ");
	private JButton jb1 = new JButton("  YOU !!! ");

	public ChatPanel() {
		jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		mb = new ChatPaneMsgBox("<html><body style='width: 200px; padding: 5px;'>" + "<h1>Do U C Me?</h1>" + "Here is a long string that will wrap.  " + "The effect we want is a multi-line label.", true);
		addmsg(mb);
		mb2 = new ChatPaneMsgBox("Hello 2", false);
		addmsg(mb2);
		scroll.setViewportView(jp);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mb = new ChatPaneMsgBox("<html><body style='width: 200px; padding: 5px;'>" + "<h1>Do U C Me?</h1>" + "Here is a long string that will wrap.  " + "The effect we want is a multi-line label.", true);
				addmsg(mb);
				Rectangle rect = jp.getBounds();
				Rectangle r2 = scroll.getViewport().getVisibleRect();
				jp.scrollRectToVisible(new Rectangle((int) rect.getWidth(), (int) rect.getHeight(), (int) r2.getWidth(), (int) r2.getHeight()));
			}
		});
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mb2 = new ChatPaneMsgBox("Hello 2", false);
				addmsg(mb2);
				Rectangle rect = jp.getBounds();
				Rectangle r2 = scroll.getViewport().getVisibleRect();
				jp.scrollRectToVisible(new Rectangle((int) rect.getWidth(), (int) rect.getHeight(), (int) r2.getWidth(), (int) r2.getHeight()));
			}
		});
		cp.add(jb);
		cp.add(jb1);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(scroll);
		jf.add(cp, BorderLayout.SOUTH);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}

	private void addmsg(Component co) {
		jp.add(co);
		jp.revalidate();
		jp.repaint();
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ChatPanel();
			}
		});
	}
}