package com.ielfgame.stupidGame.config;

import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.lua.ConvertXMLToLua.LuaTempleType;

public class LuaTempleConfig  extends ElfDataXML{
	public LuaTempleType LuaTemple = LuaTempleType.GleeControllerTemple;
	public boolean DoNotShowAgain = false;
}
