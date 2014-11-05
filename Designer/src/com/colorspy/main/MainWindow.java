package com.colorspy.main;

import java.awt.Color;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.colorspy.mouse.LowLevelMouseProc;
import com.colorspy.mouse.MOUSEHOOKSTRUCT;
import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.User32.HHOOK;
import com.sun.jna.examples.win32.User32.MSG;
import com.sun.jna.examples.win32.W32API.HMODULE;
import com.sun.jna.examples.win32.W32API.LRESULT;
import com.sun.jna.examples.win32.W32API.WPARAM;

/**
 * 屏幕取色工具
 * 
 * @author pengo(http://www.blogjava.net/pengo/)
 * @version 0.5Beta 2011.01.16
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 20110116L;
	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_LBUTTONUP = 514;
	public static final int WM_RBUTTONDOWN = 516;
	public static final int WM_RBUTTONUP = 517;
	public static final int WM_MBUTTONDOWN = 519;
	public static final int WM_MBUTTONUP = 520;
	private static HHOOK hhk;
	private static LowLevelMouseProc mouseHook;
	private final User32 lib = User32.INSTANCE;
	private JPanel contentPane;
	private JTextField textField_Html;
	private final JTextField textField_Red;
	private final JTextField textField_Green;
	private final JTextField textField_Blue;
	private JLabel cruColor;
	private JLabel selectColor;
	private JTextField textField_X;
	private JTextField textField_Y;
	private boolean isColor = true;

	private Clipboard clipbd = getToolkit().getSystemClipboard();

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// try {
	// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	// new MainWindow();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("UTF-8");
	}
	
	public static String compress(byte [] bytes) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(bytes);
		gzip.flush();
		gzip.finish();
		gzip.close();
		return out.toString("UTF-8");
	}
	
//	public static String uncompress(String str) throws IOException {
//		ByteArrayInputStream out = new ByteArrayInputStream(str.getBytes());
//		GZIPInputStream gzip = new GZIPInputStream(out);
////		gzip.write(bytes);
////		gzip.close();
////		return out.toString("UTF-8");
//		gzip.read(arg0);
//	}

	public static void main(String args[]) {
		final String text = "HELLO";
		final byte [] bytes = {'H','E','L','L','O'};
		
		try {
			final String zip = compress(text);
			System.err.println(zip.length());
			
			final String zip2 = compress(bytes);
			System.err.println(zip2.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lib.UnhookWindowsHookEx(hhk);
				System.exit(0);
			}

		});
		setBounds(100, 100, 298, 158);
		this.setTitle("屏幕取色器-0.5-beta");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resource/edit.png")));

		MyMouseAdapter l = new MyMouseAdapter();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblGreen = new JLabel("绿(G)：");
		lblGreen.setBounds(209, 56, 41, 15);
		contentPane.add(lblGreen);

		JLabel lblBlue = new JLabel("蓝(B)：");
		lblBlue.setBounds(209, 101, 41, 15);
		contentPane.add(lblBlue);

		textField_Red = new JTextField("");
		textField_Red.setBounds(247, 8, 35, 25);
		textField_Red.setEditable(false);
		textField_Red.addMouseListener(l);
		textField_Red.setBorder(BorderFactory.createBevelBorder(1));
		textField_Red.setToolTipText("双击复制文本");
		contentPane.add(textField_Red);

		JLabel lblRed = new JLabel("红(R)：");
		lblRed.setBounds(209, 14, 41, 15);
		contentPane.add(lblRed);

		textField_Green = new JTextField("");
		textField_Green.setBounds(247, 50, 35, 25);
		textField_Green.setEditable(false);
		textField_Green.addMouseListener(l);
		textField_Green.setBorder(BorderFactory.createBevelBorder(1));
		textField_Green.setToolTipText("双击复制文本");
		contentPane.add(textField_Green);

		textField_Blue = new JTextField("");
		textField_Blue.setBounds(247, 95, 35, 25);
		textField_Blue.setEditable(false);
		textField_Blue.addMouseListener(l);
		textField_Blue.setBorder(BorderFactory.createBevelBorder(1));
		textField_Blue.setToolTipText("双击复制文本");
		contentPane.add(textField_Blue);

		JLabel lblHtml = new JLabel("HTML：");
		lblHtml.setBounds(6, 53, 54, 15);
		contentPane.add(lblHtml);

		textField_Html = new JTextField();
		textField_Html.setBounds(47, 48, 70, 25);
		textField_Html.setEditable(false);
		textField_Html.addMouseListener(l);
		textField_Html.setBorder(BorderFactory.createBevelBorder(1));
		textField_Html.setToolTipText("双击复制文本");
		contentPane.add(textField_Html);

		cruColor = new JLabel("");
		cruColor.setBounds(38, 93, 50, 30);
		cruColor.setOpaque(true);
		cruColor.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 3));
		contentPane.add(cruColor);

		selectColor = new JLabel("");
		selectColor.setBounds(130, 93, 50, 30);
		selectColor.setOpaque(true);
		selectColor.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 3));
		contentPane.add(selectColor);

		JLabel label = new JLabel("鼠标位置");
		label.setBounds(6, 12, 55, 18);
		contentPane.add(label);

		textField_X = new JTextField();
		textField_X.setEnabled(false);
		textField_X.setEditable(false);
		textField_X.setBounds(85, 8, 38, 25);
		contentPane.add(textField_X);

		textField_Y = new JTextField();
		textField_Y.setEnabled(false);
		textField_Y.setEditable(false);
		textField_Y.setBounds(145, 8, 38, 25);
		contentPane.add(textField_Y);

		JLabel lblX = new JLabel("X：");
		lblX.setBounds(70, 12, 30, 18);
		contentPane.add(lblX);

		JLabel lblY = new JLabel("Y：");
		lblY.setBounds(130, 12, 30, 18);
		contentPane.add(lblY);

		JLabel label_1 = new JLabel("当前：");
		label_1.setBounds(6, 99, 41, 18);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("选择：");
		label_2.setBounds(98, 99, 38, 18);
		contentPane.add(label_2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		setVisible(true);
		try {
			final Robot robot = new Robot();

			HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
			mouseHook = new LowLevelMouseProc() {
				public LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT info) {
					if (nCode >= 0) {
						int x = info.pt.x;
						int y = info.pt.y;
						textField_X.setText(String.valueOf(x));
						textField_Y.setText(String.valueOf(y));
						Color color = robot.getPixelColor(x, y);
						switch (wParam.intValue()) {
						case MainWindow.WM_MOUSEMOVE:
							cruColor.setBackground(color);
							break;
						case MainWindow.WM_LBUTTONDOWN:
							if (isColor) {
								textField_Red.setText(String.valueOf(color.getRed()));
								textField_Green.setText(String.valueOf(color.getGreen()));
								textField_Blue.setText(String.valueOf(color.getBlue()));
								selectColor.setBackground(color);
								String red = MainWindow.decimalToHex(color.getRed());
								String green = MainWindow.decimalToHex(color.getGreen());
								String blue = MainWindow.decimalToHex(color.getBlue());
								textField_Html.setText("#" + red + green + blue);

							}
							break;
						case MainWindow.WM_MBUTTONDOWN:
							break;
						case MainWindow.WM_MBUTTONUP:
							break;
						}
					}
					return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
				}
			};
			hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);
			int result;
			MSG msg = new MSG();
			result = lib.GetMessage(msg, null, 512, 513);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String decimalToHex(int decimal) {
		String hex = "";

		for (; decimal > 0; decimal /= 16) {
			int remainder = decimal % 16;

			if (remainder > 10) {
				hex = (char) ('A' + remainder - 10) + hex;
			} else {
				hex = remainder + hex;
			}
		}
		if (hex.length() == 0) {
			hex = "00";
		} else if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}

	public class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				Object obj = e.getSource();
				if (obj instanceof JTextField) {
					JTextField text = (JTextField) obj;
					
					String select = text.getText();
					text.setSelectionStart(0);
					text.setSelectionEnd(select.length());
					StringSelection clipString = new StringSelection(select);
					clipbd.setContents(clipString, clipString);
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			isColor = false;
		}

		public void mouseExited(MouseEvent e) {
			isColor = true;
		}

	}

}
