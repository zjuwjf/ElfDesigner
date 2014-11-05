package com.ielfgame.stupidGame.trans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

public class TransferRes {

	// move a to b
	// heads
	//

	public static String exportCompressPath(final String oldPath, boolean convertJpg2Png) {
		// final String[] pathHeads =
		// PowerMan.getSingleton(TransferConfig.class).pathHeads;
		final String[] pathHeads = new String[0];
		String newResName = oldPath;

		for (String head : pathHeads) {
			if (newResName.startsWith(head)) {
				newResName = newResName.replace(head, "");
				break;
			} else {
				final int length = head.length();
				if (length > 0) {
					char last = head.charAt(length - 1);
					if (last == '\\' || last == '/') {
						if (newResName.equals(head.substring(0, length - 1))) {
							return "";
						}
					}
				}
			}
		}

		newResName = newResName.replace(":\\", "_");
		newResName = newResName.replace("\\", "_");
		newResName = newResName.replace("/", "_");

		if (convertJpg2Png) {
			newResName = newResName.replace(".jpg", ".png");
		}

		newResName = SpellHelper.getUpEname(newResName);

		return newResName;
	}

	final static HashMap<String, String> sSearchPathMap = new HashMap<String, String>();

	public static void clearPathMap() {
		sSearchPathMap.clear();
	}

	public static String importCompressPath(String newPath) {
		if (newPath == null) {
			return newPath;
		}

		newPath = newPath.substring(newPath.lastIndexOf("#") + 1);

		if (newPath.contains("\\") || newPath.contains("/")) {
			return newPath;
		}

		String ret = sSearchPathMap.get(newPath);

		final DataModel model = PowerMan.getSingleton(DataModel.class);
		if (ret == null) {
			if (model != null) {
				// final String[] pathHeads =
				// PowerMan.getSingleton(TransferConfig.class).pathHeads;
				final String[] pathHeads = new String[0];
				for (final String head : pathHeads) {
					final File dir = new File(head);
					if (dir.exists() && dir.isDirectory()) {
						ret = search(dir, newPath);
						if (ret != null) {
							sSearchPathMap.put(newPath, ret);
							return ret;
						}
					}
				}
			}
			ret = newPath;
			sSearchPathMap.put(newPath, ret);
		}

		return ret;
	}

	final static String search(final File file, final String newPath) {
		final String path = file.getAbsolutePath();
		final String express = exportCompressPath(path, true);
		if (file.isFile()) {
			if (newPath.equals(express)) {
				return path;
			}
		} else if (file.isDirectory()) {
			if (newPath.startsWith(express)) {
				final File[] fs = file.listFiles();
				for (File f : fs) {
					final String ret = search(f, newPath);
					if (ret != null) {
						return ret;
					}
				}
			}
		}
		return null;
	}

	public static String importPath(final String oldPath) {
		String newPath = oldPath;
		return newPath;
	}

	public static boolean copyRes(String resPath, final List<String> notfoundList) {
		return copyRes(resPath, null, false, notfoundList);
	}

	public static boolean copyRes(String resPath, final String dstDir, boolean convertJpg2Png, final List<String> notfoundList) {
		if (ResJudge.isRes(resPath)) {
			if (resPath.endsWith(".jpg") && convertJpg2Png) {
				try {
					ImageLoader loader = new ImageLoader();
					final ImageData[] imageDatas = loader.load(resPath);

					loader.data = imageDatas;
					loader.save(dstDir + FileHelper.DECOLLATOR + exportCompressPath(resPath, convertJpg2Png), SWT.IMAGE_PNG);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				final long size = copyFileQuickly(new File(resPath), new File(dstDir), exportCompressPath(resPath, convertJpg2Png));
				if (size > 0) {
					return true;
				} else {
					// fail
				}
			}
		} else {
			final long size = copyFileQuickly(new File(resPath), new File(dstDir), exportCompressPath(resPath, convertJpg2Png));
			if (size > 0) {
				return true;
			} else {
				// fail
			}
		}

		System.err.println("OnSaving: " + resPath + " not found!");
		if (notfoundList != null) {
			notfoundList.add(resPath);
		}
		return false;
	}

	public static long copyFileQuickly(File srcFile, File destDir, String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			// System.err.println("copyFileQuickly src file did not existed!");
			copySizes = -1;
		} else if (newFileName == null) {
			System.err.println("new file name null");
			copySizes = -1;
		} else {
			if (!destDir.exists()) {
				// System.err.println("copyFileQuickly dst Dir did not existed!");
				destDir.mkdirs();
			}

			try {
				final String src = srcFile.getAbsolutePath();
				final String dst = destDir.getAbsolutePath() + FileHelper.DECOLLATOR + newFileName;

				if (src.equals(dst)) {
					return 1;
				}

				final File destFile = new File(destDir, newFileName);

				FileChannel fcin = new FileInputStream(srcFile).getChannel();
				FileChannel fcout = new FileOutputStream(destFile).getChannel();
				long size = fcin.size();
				fcin.transferTo(0, fcin.size(), fcout);
				fcin.close();
				fcout.close();
				copySizes = size;
				// System.err.println("copyFileQuickly :"
				// +destDir.getAbsolutePath() + FileHelper.DECOLLATOR +
				// newFileName);
				// overlap
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copySizes;
	}

	public static void main(String[] args) {
		final String dir = "D:\\svn-code\\Games\\libs\\cocos2dx\\extensions";
		final LinkedList<String> names = FileHelper.getFullPathsDeep(dir);
		for (String name : names) {
			if (name.endsWith(".cpp") || name.endsWith(".c")) {
				System.err.println("extensions/" + name.substring(dir.length() + 1).replace("\\", "/") + "\\");
			}
		}

		// final String dir = "D:\\svn-code\\Games\\libs\\cocos2dx";
		// final LinkedList<String> names = FileHelper.getFullPathsDeep(dir);
		//
		// final Set<String> set = new HashSet<String>();
		// for(String name : names) {
		// final String nameDir = FileHelper.getDirPath(name);
		// if(!dir.equals(nameDir) && !nameDir.contains("platform") &&
		// !nameDir.contains("kazmath")) {
		// set.add(nameDir.substring(dir.length()+1));
		// }
		// }
		// final LinkedList<String> list = new LinkedList<String>(set);
		// Collections.sort(list);
		// for(String name : list) {
		// System.err.println("$(LOCAL_PATH)/"+name.replace("\\", "/") + " \\");
		// }
	}
}
