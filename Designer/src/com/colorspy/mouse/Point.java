package com.colorspy.mouse;

import com.sun.jna.Structure;

public class Point extends Structure {
	public class ByReference extends Point implements Structure.ByReference {
	};

	public com.sun.jna.NativeLong x;
	public com.sun.jna.NativeLong y;
}
