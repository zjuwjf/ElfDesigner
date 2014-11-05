package com.ielfgame.stupidGame;

import org.eclipse.swt.program.Program;

/**
 * @Description: TODO
 * @author zouxs
 * @date 2012-10-17
 */
public class ProgramTest {

	public static void main(String[] args) {
		boolean boo1 = Program.launch("WWW.BAIDU.COM");
		System.out.println("boo1: " + boo1);
		// 第二个参数可以为空 当然填写上会更好
		// boolean boo2 = Program.launch("eclipse.exe", "");
		// System.out.println("boo2: "+boo2);

	}
}
