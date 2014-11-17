package com.ielfgame.stupidGame.res;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

import com.ielfgame.stupidGame.ColorFactory;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.bitmapTool.ImageHelper;
import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.DitherFormat;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.ImageFormat;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.TpConfigPanel.TpConfig;
import com.ielfgame.stupidGame.shellRunner.ShellRunner;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.utils.EnumHelper;

public class ResPackerHelper {
	
	public static enum TextureFormat {
		GLT(DitherFormat.FsAlpha, "png", "PNG"), 
		/**
		 * jpg default 75, png default 3
		 */
		JPG(DitherFormat.Fs, "jpg", "JPG"),
		
		PVR4(DitherFormat.FsAlpha, "pvr2ccz", "pvr.ccz"), 
		PVR5(DitherFormat.Fs, "pvr2ccz", "pvr.ccz"), 
		PVR8(DitherFormat.FsAlpha, "pvr2ccz", "pvr.ccz"), 
		
		PNG(DitherFormat.FsAlpha, "png", "PNG");

		private final DitherFormat mDitherFormat;
		private final String mImageformat;
		private final String mSubfix;

		TextureFormat(DitherFormat ditherFormat, String format, String subfix) {
			mDitherFormat = ditherFormat;
			mImageformat = format;
			mSubfix = subfix;
		} 
		public DitherFormat getDitherFormat() { 
			return mDitherFormat;
		}
		public String getImageFormat() {
			return mImageformat;
		}
		public String getSubfix() {
			return mSubfix;
		}
	}
	
	public static class TexturePackerConfig {
		public TextureFormat textureFormat = TextureFormat.GLT;
		public int num = 0;
		/***
		 * 
		 */
		public int compress = 75;
		
		public String toString() {
//			if (num == 0 && textureFormat == TextureFormat.GLT && compress == 75) {
//				return "default";
//			}
			return String.format("%d#%s#%d", num, textureFormat.toString(), compress);
		}
		public void fromString(String line) {
			final String [] items = line.split("#");
			if(items != null && items.length == 3) {
				try {
					num = Integer.valueOf(items[0]);
					textureFormat = EnumHelper.fromString(TextureFormat.class, items[1]);
					compress = Integer.valueOf(items[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/***
	 * panel -> 
	 */
	
	public static boolean isDirHasimages(final String dirPath) {
		if (FileHelper.isDir(dirPath)) {
			final File f = new File(dirPath);

			final boolean[] flag = new boolean[] { false };

			return f.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (flag[0] || ResJudge.isRes(name)) {
						flag[0] = true;
						return true;
					}
					return false;
				}
			}).length > 0;
		}
		return false;
	}

	public static void openExplorer(final String dir) {
		if (!new File(dir).exists()) {
			MessageDialog md = new MessageDialog();
			md.open("Open", String.format("%s has not existed any more!", dir));
			return;
		}

		final ShellRunner sr = new ShellRunner();

		if (FileHelper.IS_WINDOWS) {
			sr.setCmdline(String.format("explorer /select, %s", dir));
		} else {
			sr.setCmdline(String.format("open %s", dir));
		}

		sr.run();
	}

	private final static String LastDirName = ".last";

	public static void writePackerConfig(final String dirPath, final TpConfig tpc) {
		try {
			final LinkedList<String> lines = new LinkedList<String>();
			lines.add(String.format("%s#%d", tpc.format.toString(), tpc.num));
			
			final File f = new File(new File(dirPath), ".tp");
			FileHelper.write(lines, f);
			
//			setFileHidden( f.getAbsolutePath() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TpConfig readPackerConfig(final String dirPath) {
		final File f = new File(new File(dirPath), ".tp");
		if (f.exists() && f.isFile()) {
			final LinkedList<String> lines = FileHelper.read(f);
			if (lines != null && lines.size() > 0) {
				final String line = lines.getFirst();
				final String[] sp = line.split("#");
				if (sp.length >= 2) {
					try {
						final ImageFormat format = EnumHelper.fromString(ImageFormat.class, sp[0]);
						final int num = Integer.valueOf(sp[1]);

						if (format != null) {
							final TpConfig ret = new TpConfig();
							ret.format = format;
							ret.num = num;
							return ret;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	public static void makeLastVersion(final String dirPath) {
		if (FileHelper.isDir(dirPath)) {
			final File dir = new File(dirPath);
			
			final File lastDir = new File(dir, LastDirName);
			if (!lastDir.exists()) {
				lastDir.mkdir();
			}
			
			final LinkedList<String> lastPngs = FileHelper.getFullPahIds(lastDir, new String[] { ".png", ".jpg", ".tp", ".plist", ".ccz", ".PNG", ".pkm" }, false);
			for (final String path : lastPngs) {
				new File(path).delete();
			}

			final LinkedList<String> pngs = FileHelper.getFullPahIds(dir, new String[] { ".png", ".jpg", ".tp", ".plist", ".ccz", ".PNG", ".pkm" }, false);
			for (final String path : pngs) {
				FileHelper.copyFileQuickly(new File(path), lastDir, FileHelper.getSimpleNameWithSuffix(path));
			}
		}
	}

	public static boolean hasChanged(final String dirPath) {
		if (FileHelper.isDir(dirPath)) {

			final File dir = new File(dirPath);

			if (dir.getName().startsWith(".")) {
				return false;
			}

			final File last = new File(dir, LastDirName);

			final LinkedList<String> plists = FileHelper.getFullPahIds(dir, new String[] { ".plist", ".ccz", ".PNG", ".pkm" }, false);
			final int plistNum = plists.size();
			if (plistNum == 0 || plistNum%2==1 ) {
				return true;
			}

			final LinkedList<String> pngs = FileHelper.getFullPahIds(dir, new String[] { ".png", ".jpg", ".tp", ".plist", ".ccz", ".PNG", ".pkm" }, false); 
			final LinkedList<String> lastPngs = FileHelper.getFullPahIds(last, null, false); 
			// plist

			final int pngSize = pngs.size();
			final int lastPngsSize = lastPngs.size();

			if (pngSize != lastPngsSize) {
				return true;
			}

			for (int i = 0; i < pngSize; i++) {
				final String s1 = pngs.get(i);
				final String s2 = lastPngs.get(i);
				if (!FileHelper.getSimpleNameWithSuffix(s1).equals(FileHelper.getSimpleNameWithSuffix(s2))) {
					return true;
				}
				
				if (!FileHelper.compare2files(s1, s2)) {
					return true;
				}
			}

			return false;
		}

		return false;
	}
	
	public static void main(String [] args) {
		final String dir = "D:\\Glee works\\card_product\\new\\doc\\designer\\Resources\\announce\\";
		System.err.println(hasChanged(dir));
	}

	private static String getTexturePackerString(final String name, final ImageFormat format, final LinkedList<String> simplePath) {
		final StringBuilder sb = new StringBuilder();
		for (String path : simplePath) {
			if (FileHelper.IS_WINDOWS) {
				sb.append(" ").append("\"").append(path).append("\"");
			} else {
				sb.append(" ").append(path);
			}
		}
		// TexturePacker --quiet --extrude 1 --scale ScaleInput --format cocos2d
		// --texture-format pvr2ccz --opt FormatInput --size-constraints NPOT
		// DitherFormat --data Output.plist --sheet Output.pvr.ccz Input
		//--shape-debug
		
		final String imageformat = format.getFormat();
		final String subfix = format.getSubfix();
		
//		final String imageformat = "png";
//		final String subfix = "PNG";
		
//		--size-constraints <value>  Restrict sizes
//        POT - Power of 2 (2,4,8,16,32,...)
//		AnySize - Minimum size
//		NPOT - Any size but power of 2
		
		//--disable-rotation
		return String.format("Texturepacker --algorithm MaxRects --maxrects-heuristics Best --trim-mode Trim --extrude 1 --padding 0 --format cocos2d --texture-format %s --opt %s --size-constraints %s  %s --data %s.plist  --sheet %s.%s%s", imageformat, format.toRGBString(), format.getSizeFromat(), format.getDitherFormat().toString(), name, name, subfix, sb.toString());
	}

	private static boolean packer(final ShellRunner sr, final String name, final ImageFormat format, final LinkedList<String> simplePath) {
		assert (simplePath.size() > 0);

		final String cmd = getTexturePackerString(name, format, simplePath);

		sr.setCmdline(cmd);
		
		final boolean ret = sr.run() == 0;
		
		if(!ret) {
			System.err.println(cmd+" falied");
		} else {
			if(format == ImageFormat.GLEE75 || format == ImageFormat.GLEE35) {
				;
				
				final String pngPath = new File(sr.getRunpath(),name + ".PNG").getAbsolutePath();
				final String gtPath = new File(sr.getRunpath(),name + ".glt").getAbsolutePath();
				ImageHelper.createGTfromPng(pngPath, gtPath, format.getCompress());
				
				FileHelper.removeFile(pngPath);
			}
		}

		return ret;
	}

	public static boolean runDirPacker(final ShellRunner sr, final String dirPath, final ImageFormat format, final int num) {
		if (!FileHelper.isDir(dirPath)) {
			return true;
		}

		final LinkedList<String> pathList = FileHelper.getSimplePahIds(dirPath, new String[] { ".png", ".jpg" }, false);
		StringHelper.sortByLastInt(pathList);
		
		if (pathList.size() == 0) {
			return true;
		}
		
		final File dir = new File(dirPath);
		sr.setRunpath(dir);

		final String name = FileHelper.getSimpleName(dirPath);
		int count = 1;

		if (num <= 0) {
			boolean ret = packer(sr, name, format, pathList);
			
			if (!ret) {
				//remove if published
				FileHelper.removeFile(new File(dir, name+".plist"));
				FileHelper.removeFile(new File(dir, name+".PNG"));
				FileHelper.removeFile(new File(dir, name+".pvr.ccz"));
				FileHelper.removeFile(new File(dir, name+".pkm"));
				
				// iteration
				LinkedList<String> testList = new LinkedList<String>(pathList);
				LinkedList<String> remainList = new LinkedList<String>();

				remainList.addFirst(testList.getLast());
				testList.removeLast();

				while (!testList.isEmpty() || !remainList.isEmpty()) {
					ret = packer(sr, name + count, format, testList);
					
					if (!ret) {
						
						final int testListLength = testList.size();

						if (testListLength == 1) {
							final String lastErrMessage = "Error on pack:" + testList.get(0);
							System.err.println(lastErrMessage);
							
							final Display display = PowerMan.getSingleton(MainDesigner.class).getShell().getDisplay();
							
							display.asyncExec(new Runnable() {
								public void run() {
									final StyleRange styleRange = new StyleRange();
									styleRange.foreground = ColorFactory.RED;
									ViewInfoDialog.getSingleton().appendInfo(lastErrMessage, styleRange);
								}
							});
							
							return false;
						} else {
							remainList.addFirst(testList.getLast());
							testList.removeLast();
						}

					} else {
						count++;
						
						testList = remainList;
						remainList = new LinkedList<String>();
					}
				}

				return true;
			}

		} else {
			while (!pathList.isEmpty()) {
				final LinkedList<String> list = new LinkedList<String>();
				for (int i = 0; i < num; i++) {
					if (!pathList.isEmpty()) {
						list.add(pathList.getFirst());
						pathList.removeFirst();
					}
				}
				final boolean ret = packer(sr, name + count, format, list);
				count++;
				if (!ret) {
					return false;
				}
			}
		}

		return false;
	}

	public static void runTexturePackerInDeep(final ShellRunner sr, final File dir, final HashSet<String> changeMap) {
		if (dir.exists() && dir.isDirectory() && !dir.getName().equals(LastDirName)) {
			
			final String path = dir.getAbsolutePath();
			
			if (isDirHasimages(path) && changeMap.contains(path)) { 
				final Boolean flag = (changeMap == null ? false : changeMap.contains(path));
				if (flag) {
					TpConfig tpc = readPackerConfig(path);

					if (tpc == null) {
						tpc = new TpConfig();
					}
					
					cleanDirForPlist(dir, false);

					writePackerConfig(path, tpc);

					runDirPacker(sr, path, tpc.format, tpc.num);
					
					makeLastVersion(path);
				}
			}

			final File lastDir = new File(dir, LastDirName);
			if(!lastDir.isHidden()) {
				setFileHidden( lastDir.getAbsolutePath() );
			}

			final File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				runTexturePackerInDeep(sr, fs[i], changeMap);
			}

		}
	}
	
	private final static void setFileHidden(String path) {
		try {
			Runtime.getRuntime().exec("attrib " + "\"" + path + "\""+ " +H");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final static File[] sortFile(final String dirPath) {
		if (dirPath != null) {

			final File dirFile = new File(dirPath);

			final LinkedList<File> dirList = new LinkedList<File>();
			final LinkedList<File> pngList = new LinkedList<File>();

			dirFile.listFiles(new FilenameFilter() {
				public boolean accept(final File file, final String name) {
					final File f = new File(file, name);
					if (f.isDirectory()) {
						if (!name.startsWith(".")) {
							dirList.add(f);
						}
					} else {
						if (ResJudge.isRes(name)) {
							pngList.add(f);
						}
					}
					return false;
				}
			});
			
			// Collections.sort(dirList);
			// Collections.sort(pngList);
			
			final int dirListSize = dirList.size();
			final int pngListSize = pngList.size();
			final File[] ret = new File[dirListSize + pngListSize];
			dirList.toArray(ret);

			for (int i = 0; i < pngListSize; i++) {
				ret[i + dirListSize] = pngList.get(i);
			}
			return ret;
		}
		return new File[0];
	}
	
	public final static void cleanDirForPlist(final File dir, final boolean deep) {
		if(dir.isDirectory()) {
			FileHelper.removeFile(new File(dir, ".last"));
			
//			final LinkedList<String> list = FileHelper.getFullPahIds(dir, new String[]{".tp",".plist",".ccz", ".PNG", ".pkm"}, false);
			final LinkedList<String> list = FileHelper.getFullPahIds(dir, new String[]{".plist",".ccz", ".PNG", ".pkm"}, false);
			for(String path : list) { 
				FileHelper.removeFile(path); 
			} 
			
			if(deep) {
				final File [] fs = dir.listFiles();
				for(File f : fs) {
					cleanDirForPlist(f, deep);
				}
			}
		}
	}
	
//	public static void main(final String [] args) {
//		final String dir = "D:\\Glee works\\card_product\\new\\doc\\designer\\Resources\\";
//		final File dirFile = new File(dir);
//		
//	}
}
