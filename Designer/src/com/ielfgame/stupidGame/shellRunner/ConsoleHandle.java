/*
 * 
 * 
 * 
 * �������� 2006-9-4
 * $Id: ConsoleHandle.java,v 1.1 2008/02/29 01:57:07 skysss Exp $
 */
package com.ielfgame.stupidGame.shellRunner;

/**
 * @author Skysss ��������� Handle
 */
public interface ConsoleHandle {
	/**
	 * @param consoleMessage
	 *            ����������Ϣ
	 * @param isErrorOut
	 *            �Ƿ��� System Error ��� .
	 */
	void onOutRead(String consoleMessage, boolean isErrorOut);
}