package com.ielfgame.stupidGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ielfgame.stupidGame.utils.FileHelper;

public class CheckRun {

	private static final String lib_windows_head = "/_Lib/windows/";
	private static String[] lib_windows = { "jinput-dx8.dll", "jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll" };

	private static final String lib_mac_head = "/_Lib/macosx/";
	private static String[] lib_mac = { "libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib" };

	public static void check() {
		final File dir = new File(new File(System.getProperty("user.dir")), "lib");
		if (!dir.isDirectory()) {
			dir.deleteOnExit();
			dir.mkdir();
		}

		final String head;
		final String [] array;
		if (FileHelper.IS_WINDOWS) {
			head = lib_windows_head;
			array = lib_windows;
		} else {
			head = lib_mac_head;
			array = lib_mac;
		}
		
		for (final String name : array) {
			final File out = new File(dir, name);
			if (!out.exists()) {
				try {
					final InputStream is = CheckRun.class.getResourceAsStream(head + name);
					final FileOutputStream foptS = new FileOutputStream(out);
					final OutputStream optS = (OutputStream) foptS;

					int c;
					while ((c = is.read()) != -1) {
						optS.write(c);
					}
					optS.flush();
					optS.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		System.setProperty("org.lwjgl.librarypath", dir.getAbsolutePath());
	}
}
