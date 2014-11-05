/**
 * ����:2008-9-3
 * ����:zhushijun
 */
package com.ielfgame.stupidGame.shellRunner;


/**
 * @author zhushijun
 * 
 */
public class TestShellRunner {
	/**
	 * ����̨������ �ڵ����ⲿ����󲶻����̨��������� ������̨���쳣��������err.print �������out.print
	 * 
	 * @author zhushijun
	 * 
	 */
	static class InnerHandle implements ConsoleHandle {
		public void onOutRead(String consoleMessage, boolean isErrorOut) {
			if (isErrorOut) {
				System.err.println(consoleMessage);
			} else {
				System.out.println(consoleMessage);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShellRunner runner = new ShellRunner();// ��������ִ̨�ж���
		runner.setConsoleHandle(new InnerHandle());// ���ÿ���̨������
		if (System.getProperty("os.name").startsWith("Windows")) {
			runner.setCmdline("dir");// ���ϵͳ��WINDOWS���á�DIR������
		} else {
			runner.setCmdline("ls -l");// ��WINDOWSϵͳ����"ls -l"
		}
		int t = runner.run();// ִ�п���̨��������ؽ��
		System.out.println("ִ�з��ؽ��=" + t);

		System.out.println("***************************************");
//		runner.setCmdline("xxxxx");// ִ��һ�������ڵ�����
//		runner.run();

//		runner.setCmdline("cd C:/Program Files/CodeAndWeb/TexturePacker/bin");
//		runner.run();
//		runner.setRuningEnv(runProperties); 
//		runner.setRunpath(new File("cd C:\\Program Files\\CodeAndWeb\\TexturePacker\\bin\\"));
		
		
		runner.setCmdline("TexturePacker --format cocos2d  --texture-format pvr2ccz --opt RGBA4444 --size-constraints NPOT  --dither-atkinson-alpha --data 1.plist  --sheet 1.pvr.ccz D:\\transfer\\gyx_UI_DiYiDengLuJieMianDe_123456.png D:\\transfer\\gyx_UI_DiYiDengLuJieMianDe_logo.png");
		runner.run(); 
		
		/*
		 * ��ע�⣺unix/linuxϵͳ�Ĺܵ���|�����ǲ�֧�ֵģ��������벻Ҫ���֡�
		 */
	}
}
