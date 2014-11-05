package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class ChatPaneMsgBox extends JPanel {

	boolean ij = true;
	private String msg = "";
	private JLabel jl;

	public ChatPaneMsgBox() {
	}

	public ChatPaneMsgBox(String str, boolean bj) {
		ij = bj;
		msg = str;
		msgpnl();
	}

	private void msgpnl() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		if (ij) {
			jl = new JLabel(msg);

			jl.setOpaque(true);
			jl.setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 10, 7, ij));

			jl.setBackground(new Color(123, 104, 238));
			jl.setAlignmentX(0);
		} else {
			jl = new JLabel(msg, SwingConstants.RIGHT);

			jl.setOpaque(true);
			jl.setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 10, 7, ij));

			jl.setBackground(new Color(224, 102, 255));
			jl.setAlignmentX(1);
		}
		this.add(jl);
		System.out.print("\nThis executed");
	}
}
