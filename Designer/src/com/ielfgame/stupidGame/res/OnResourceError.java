package com.ielfgame.stupidGame.res;

import java.io.BufferedWriter;
import java.io.File;

import com.ielfgame.stupidGame.utils.FileHelper;

public class OnResourceError implements IOnResourceError {

	private BufferedWriter mWriter = null;

	public OnResourceError() {
		final File f = new File(new File(FileHelper.getUserDir()), "ResourceError.txt");
		if (!f.exists()) {
			mWriter = FileHelper.getWriter(f);
		}
	}

	public void onConfict(String name, final String path1, final String path2) {
		if (mWriter != null) {
			try {
				mWriter.write(String.format("Confict: %s;%s\n", path1, path2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onIlleagl(String name, String path) {
		if (mWriter != null) {
			try {
				mWriter.write(String.format("Illeagl: %s\n", path));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void flush() {
		if (mWriter != null) {
			try {
				mWriter.flush();
				mWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
