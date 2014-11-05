package com.ielfgame.stupidGame.bitmapTool;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.FileHelper.IteratorFile;

public class ScaleImage2 {

	public static void main(String[] args) {
		final String[] paths = { "D:\\pic\\gyx\\特效\\", "D:\\pic\\gyx\\特效2\\" };
		for (int i = 0; i < paths.length; i++) {
			final String path = paths[i];

			FileHelper.travelFiles(path, new IteratorFile() {
				public boolean iterator(File file) {
					final String fullName = file.getAbsolutePath();
					if (ResJudge.isRes(fullName)) {
						try {
							final ImageData data = ImageHelper.read(fullName);
							final ImageData newData = data.scaledTo(Math.round(0.5f * data.width), Math.round(0.5f * data.height));
							ImageHelper.save(newData, fullName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					return false;
				}
			});
		}
		System.err.println("done!");
	}
}
