package com.ielfgame.stupidGame.shellRunner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

public class ShellRunner {
	private Process process;// �����еĽ��.

	private String cmdline = null;

	private Properties runProperties = null; // ���л���

	private File dir = null; // ����·��

	private int lastExitValue = Integer.MAX_VALUE;
	
	private boolean isWindows = System.getProperty("os.name").startsWith("Windows");

	ConsoleHandle consoleHandle;

	private Thread threadin;

	private Thread threaderr;

	private String windowsComander = "cmd.exe /c ";

	/**
	 * @param cmdline
	 *            ����������
	 */
	public ShellRunner(String cmdline) {
		this();
		setCmdline(cmdline);
	}

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public ShellRunner() {
		consoleHandle = new BaseOutConsoleHandle();
	}

	/**
	 * 
	 * ͬ�����������ⲿ����, ����
	 * 
	 * @return �����ⲿ����˳�״̬��.
	 */
	public int run() {
		if (processIsRunning()) {
			throw new IllegalStateException("Can not start command  when processor is running");
		}

		String runCommands = getRunCommand();
		try {
			process = Runtime.getRuntime().exec(runCommands, getEnvString(), getRunpath());
		} catch (IOException e1) {
			throw new RuntimeException("call System Process Error :" + e1.getMessage(), e1);
		}
		threadin = new ReadThread(process.getInputStream(), "[INPUT]", false);
		threaderr = new ReadThread(process.getErrorStream(), "[ERROR]", true);
		threadin.start();
		threaderr.start();
		int rcode = 0; 
		
		try {
			rcode = process.waitFor();
		} catch (InterruptedException e) {
			stopProcess(); 
			rcode = process.exitValue();
		} finally { 
			process = null;
		} 

		lastExitValue = rcode;
		try {
			threadin.join();
			threaderr.join();
		} catch (InterruptedException e) {
			// �����жϴ���
		}
		return rcode; 
	}

	/**
	 * @return ��ȡ�����ַ�
	 */
	public String[] getEnvString() {
		if (this.runProperties == null)
			return null;
		String[] result = new String[runProperties.size()];

		int i = 0;
		for (Enumeration<?> en = runProperties.keys(); en.hasMoreElements();) {
			String element = (String) en.nextElement();
			result[i++] = element + "=" + runProperties.getProperty(element);
		}
		return result;
	}

	/**
	 * 
	 * @return ��ȡ��ǰ���е�ָ��.
	 */
	public String getRunCommand() {
		return cmdline;
	}

	/**
	 * ���̵�����ܵ�������һ����Ϣ.
	 * 
	 * @param messageCode
	 * @throws IOException
	 */
	public void appendMessage(byte[] messageCode) throws IOException {
		if (process != null) {
			process.getOutputStream().write(messageCode);
		} else {
			throw new IOException("Process is not start or has been destoryed.");
		}
	}

	/**
	 * д��һ����Ϣ.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void appendMessageLn(String message) throws IOException {
		appendMessage((message + System.getProperty("line.separator")).getBytes());
	}

	/**
	 * ֹͣ���
	 */
	public void stopProcess() {
		Process r = process;
		if (r != null) {
			r.destroy();
		}

		if (threadin != null) {
			threadin.interrupt();
		}

		if (threaderr != null) {
			threaderr.interrupt();
		}

	}

	/**
	 * @return ����Ƿ�������
	 */
	public boolean processIsRunning() {
		return process != null;
	}

	/**
	 * @return ���� runProperties��
	 */
	public Properties getRuningEnv() {
		return runProperties;
	}
	
	/**
	 * @param runProperties
	 *            Ҫ���õ� runProperties��
	 */
	public void setRuningEnv(Properties runProperties) {
		this.runProperties = runProperties;
	}

	/**
	 * @return ���� dir��
	 */
	public File getRunpath() {
		return dir;
	}

	/**
	 * @param dir
	 *            Ҫ���õ� dir��
	 */
	public void setRunpath(File dir) {
		this.dir = dir;
	}

	static final class BaseOutConsoleHandle implements ConsoleHandle {
		public void onOutRead(String message, boolean isErrorOut) {
			PrintStream out = isErrorOut ? System.err : System.out;
			out.println(message);
		}
	}

	/**
	 * ��׼���Handle
	 * 
	 */
	public static final ConsoleHandle STARDARD_OUT_HANDLE = new BaseOutConsoleHandle();

	class ReadThread extends Thread {

		private final InputStream inputStream;

		private final String cName;

		private final boolean isErrorOut;

		ReadThread(InputStream iStream, String cName, boolean isErrorOut) {
			this.inputStream = iStream;
			this.cName = cName;
			this.isErrorOut = isErrorOut;
		} 

		/**
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			BufferedInputStream stream = new BufferedInputStream(this.inputStream);
			Reader reader = new InputStreamReader(stream);
			BufferedReader reader2 = new BufferedReader(reader);

			String readSize = null;
			try {
				while ((readSize = reader2.readLine()) != null && !isInterrupted()) {
					consoleHandle.onOutRead(readSize, isErrorOut);
				}
			} catch (IOException e) {
				throw new RuntimeException(cName + "=" + e.getMessage(), e);
			}
		}
	}

	/**
	 * @return ���� lastExitValue��
	 */
	public int getLastExitValue() {
		return lastExitValue;
	}

	/**
	 * @param cmdline
	 *            Ҫ���õ� cmdline��
	 */
	public void setCmdline(String cmdline) {
		String cmd = cmdline;
		if (this.processIsRunning()) {
			throw new IllegalStateException("Can not set cmd line when processor is running");
		}

		if (isWindows) {
			if ((cmd.length() > 4) && (cmd.substring(0, 4).equalsIgnoreCase("cmd ") || cmd.substring(0, 4).equalsIgnoreCase("cmd."))) {
				// nothing
			} else {
				cmd = getWindowsCommandMode() + cmd;
			}
		}
		this.cmdline = cmd;
	}

	/**
	 * @return �趨Windows�����µ�
	 */
	public String getWindowsCommandMode() {
		return windowsComander;
	}

	/**
	 * @param args
	 *            ���Թ��
	 */
	public static void main(String[] args) { 
		ShellRunner runner = new ShellRunner(); 
		runner.setCmdline("dir c:"); 
		runner.run(); 
		System.out.print("code : " + runner.getLastExitValue()); 
	}

	/**
	 * @param consoleHandle
	 *            Ҫ���õ� ����̨���� Handle
	 */
	public void setConsoleHandle(ConsoleHandle consoleHandle) {
		this.consoleHandle = consoleHandle;
	}

	/**
	 * @param windowsComanderMode
	 *            Ҫ���õ� windowsComanderMode��
	 */
	public void setWindowsComanderMode(String windowsComanderMode) {
		this.windowsComander = windowsComanderMode;
	}
}
