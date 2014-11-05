package com.ielfgame.stupidGame.power;


public abstract class APowerManSingleton implements ISingleton{ 
	public APowerManSingleton(){ 
		PowerMan.register(this.getClass(), this);
	} 
}