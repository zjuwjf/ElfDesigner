/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ielfgame.stupidGame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

/**
 * Manages icons for the application.
 * This is necessary as we could easily end up creating thousands of icons
 * bearing the same image.
 */
public class IconCache {
	// Stock images
	public final int
		shellIcon = 0,
		iconClosedDrive = 1,
		iconClosedFolder = 2,
		iconFile = 3,
		iconOpenDrive = 4,
		iconOpenFolder = 5,
		iconSetAsScence = 6,
		iconPlay = 7,
		iconPause = 8,
		iconAddAction = 9,
		iconRemoveAction = 10,
		iconCenter = 11,
		iconSave = 12,
		iconRemoveAll = 13,
		iconValue1 = 14,
		iconValue2 = 15,
		iconSelectAll = 16,
		iconDeselectAll = 17,
		iconCartton = 18,
		iconSelect = 19,
		iconPic = 20,
		iconOpenTpFolder = 21,
		iconClosedTpFolder = 22,
		iconWarning = 23
		;
	public final String[] stockImageLocations = {
		"generic_example.gif",
		"icon_ClosedDrive.gif",
		"icon_ClosedFolder.gif",
		"icon_File.gif",
		"icon_OpenDrive.gif",
		"icon_OpenFolder.gif",
		"eyes.png",
		"play.gif",
		"pause.gif",
		"add_action.gif",
		"remove_action.gif",
		"walkable.png",
		"fileSave.png",
		"Cancel.png",
		"value1.gif",
		"value2.gif",
		"select_all.gif",
		"deselect_all.gif",
		"cartoon.gif",
		"select.gif",
		"pic.gif",
		"icon_OpenTpFolder.gif",
		"icon_ClosedTpFolder.gif",
		"warning.gif"
	};
	public Image stockImages[];
	
	// Stock cursors
	public final int
		cursorDefault = 0,
		cursorWait = 1;
	public Cursor stockCursors[];
	// Cached icons
	@SuppressWarnings("rawtypes")
	private Hashtable iconCache; /* map Program to Image */
	
	public IconCache() {
	}
	/**
	 * Loads the resources
	 * 
	 * @param display the display
	 */
	@SuppressWarnings("rawtypes")
	public void initResources(Display display) {
		if (stockImages == null) {
			stockImages = new Image[stockImageLocations.length];
				
			for (int i = 0; i < stockImageLocations.length; ++i) {
				Image image = createStockImage(display, stockImageLocations[i]);
				if (image == null) {
					freeResources();
					throw new IllegalStateException(ResJudge.getResourceString("error.CouldNotLoadResources"));
				}
				stockImages[i] = image;
			}
		}	
		if (stockCursors == null) {
			stockCursors = new Cursor[] {
				null,
				display.getSystemCursor(SWT.CURSOR_WAIT)
			};
		}
		iconCache = new Hashtable();
	}
	/**
	 * Frees the resources
	 */
	public void freeResources() {
		if (stockImages != null) {
			for (int i = 0; i < stockImages.length; ++i) {
				final Image image = stockImages[i];
				if (image != null) image.dispose();
			}
			stockImages = null;
		}
		if (iconCache != null) {
			for (@SuppressWarnings("rawtypes")
			Enumeration it = iconCache.elements(); it.hasMoreElements(); ) {
				Image image = (Image) it.nextElement();
				image.dispose();
			}
		}
		stockCursors = null;
	}
	/**
	 * Creates a stock image
	 * 
	 * @param display the display
	 * @param path the relative path to the icon
	 */
	private Image createStockImage(Display display, String path) {
		
		InputStream stream = IconCache.class.getResourceAsStream(path);
		if(stream == null) {
			System.err.println("error "+path);
			return null;
		}
		ImageData imageData = new ImageData (stream);
		ImageData mask = imageData.getTransparencyMask ();
		Image result = new Image (display, imageData, mask);
		try {
			stream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		return result;
	}
	/**
	 * Gets an image for a file associated with a given program
	 *
	 * @param program the Program
	 */
	@SuppressWarnings("unchecked")
	public Image getIconFromProgram(Program program) {
		Image image = (Image) iconCache.get(program);
		if (image == null) {
			ImageData imageData = program.getImageData();
			if (imageData != null) {
				image = new Image(null, imageData, imageData.getTransparencyMask());
				iconCache.put(program, image);
			} else {
				image = stockImages[iconFile];
			}
		}
		return image;
	}
}
