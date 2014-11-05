package com.ielfgame.stupidGame;

import org.eclipse.swt.widgets.Display;

public class UpdateThread extends Thread{
	
	final Display display;
	
	public UpdateThread(Display display) {
		this.display = display;
	}
	
	public void run() {
		while(true) { 
			try {
				//update UI
				
				//update GL
				
			} catch (Exception e) {
			} 
		} 
	} 
}
