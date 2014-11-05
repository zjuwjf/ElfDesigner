package com.colorspy.mouse;

import com.sun.jna.Structure;
import com.sun.jna.examples.win32.User32;

public class MOUSEHOOKSTRUCT extends Structure {
	public static class ByReference extends MOUSEHOOKSTRUCT implements Structure.ByReference {
	};

	public User32.POINT pt;
	public User32.HWND hwnd;
	public int wHitTestCode;
	public User32.ULONG_PTR dwExtraInfo;
}
