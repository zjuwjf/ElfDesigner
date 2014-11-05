package com.ielfgame.stupidGame.power;

public class ASuperManSingleton implements ISingleton{ 
	public ASuperManSingleton(){ 
		SuperMan.register(this.getClass(), this);
	} 
}