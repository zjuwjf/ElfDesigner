package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class PreviewPictureTab extends AbstractWorkSpaceTab{

	private Label mLabel;
	public PreviewPictureTab() {
		super("Preview");
	}
	
	public Composite createTab(CTabFolder parent) {
		final Composite com = new Composite(parent, SWT.CENTER);
		com.setLayout(new GridLayout());
		mLabel = new Label(com, SWT.BORDER|SWT.CENTER);
		mLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		return com;
	}
	
	public void setResid(final String filename) {
		try {
			final Display display = mLabel.getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					
					final ImageData image = new ImageData(filename);
					mLabel.setText("size:"+image.width+","+image.height);
					
					if(image.width > mLabel.getSize().x || image.height > mLabel.getSize().y) {
						float scaleX = image.width / mLabel.getSize().x;
						float scaleY = image.height / mLabel.getSize().y;
						
						float scale = Math.max(scaleX, scaleY);
						
						final Image imageView = new Image(display, image.scaledTo(Math.round(image.width/scale), Math.round(image.height/scale)));
						mLabel.setImage(imageView);
					} else {
						final Image imageView = new Image(display, image);
						mLabel.setImage(imageView);
					}
					
					mLabel.setAlignment(SWT.CENTER);
					mLabel.redraw();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		
	}
}
