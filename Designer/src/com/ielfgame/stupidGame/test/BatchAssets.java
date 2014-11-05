package com.ielfgame.stupidGame.test;

import java.io.File;

import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.FileHelper.IteratorFile;

public class BatchAssets {

	public static void main(final String [] args) {
		String path = "D:\\svn-code\\Games\\LordRise\\proj.android\\assets\\";
		FileHelper.travelFiles(path, new IteratorFile() {
			public boolean iterator(File file) {
				if(file.getName().contains(".nvcd") || file.getName().contains("text-base")) {
					System.err.println( file.getName() + " delete " +file.delete() );
					return true;
				} else if(file.getName().equals(".nvc")) {
					file.renameTo(new File(file.getAbsolutePath().replace(".nvc", "nvc")));
					return true;
				} 
				return false;
			}
		});
	}
	
}
