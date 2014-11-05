package com.ielfgame.stupidGame.design.hotSwap.flash;

import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.design.hotSwap.SurfaceFlashPanel;


public class FlashWorkSpaceTab extends GLWorkSpaceTab{

	public FlashWorkSpaceTab() {
		super("Flash Edit");
		
		final Surface sf = new SurfaceFlashPanel();
		
		sf.setPhysicWidth(1200);
		sf.setPhysicHeight(450);
		
		this.setSurface(sf);
		
		this.setInterval(120);
	}

	public void update() {
		
	}
	
	public void onSelect(){
		super.onSelect();
//		this.getCTabFolder().setSize(this.getCTabFolder().getSize().x, 400);
	}
}
