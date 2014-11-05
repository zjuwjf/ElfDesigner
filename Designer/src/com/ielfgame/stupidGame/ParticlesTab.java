package com.ielfgame.stupidGame;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

public class ParticlesTab extends AbstractWorkSpaceTab{

	public ParticlesTab() {
		super("Particle");
	}
	
	public Composite createTab(CTabFolder parent) {
		return new Composite(parent, 0);
	}
	
	public void update() {
		
	}
}
