package com.colorspy.mouse;

import com.sun.jna.examples.win32.User32.HOOKPROC;
import com.sun.jna.examples.win32.W32API.LRESULT;
import com.sun.jna.examples.win32.W32API.WPARAM;

public interface LowLevelMouseProc extends HOOKPROC {
    LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
}
