package com.ielfgame.stupidGame.batch;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.utils.FileHelper;

public class TpPlistScaner {

	private final String mDirPath;
	private final String mImageDir;
	private String mCleanDir;
	
	public TpPlistScaner(final String path, final String image) {
		mDirPath = path;
		mImageDir = image;
	}

	public TpPlistScaner(final String path) {
		this(path, null);
	}

	public void setCleanDir(String cleanDir) { 
		mCleanDir = cleanDir;
	} 
	
	//final LinkedList<String> list = FileHelper.getFullPathsDeep(mDirPath, new String[]{".nvcd", ".last"});
	private  final LinkedList<String> getFullPahIds4Plist() {
		final LinkedList<String> list;
		if(mImageDir==null) {
			list = FileHelper.getFullPathsDeep(mDirPath, new String[]{".nvcd", ".last"});
		} else {
			list = FileHelper.getFullPathsDeep(mImageDir, new String[]{".nvcd", ".last"});
		}
		
		return list;
	}
	
//	final LinkedList<String> list = FileHelper.getFullPahIds(mDirPath, new String [] {".png"}, true);
	private  final LinkedList<String> getFullPahIds4Png() {
		final LinkedList<String> list;
		if(mImageDir==null) {
			list = FileHelper.getFullPahIds(mDirPath, new String [] {".png"}, true);
		} else {
			list = FileHelper.getFullPahIds(mImageDir, new String [] {".png"}, true);
		}
		
		return list;
	}
	
	public LinkedList<String> scanPng(final HashMap<String, String> pngToPath) {
		
		final LinkedList<String> warnings = new LinkedList<String>();
//		final LinkedList<String> list = FileHelper.getFullPahIds(mDirPath, new String [] {".png"}, true);
		final LinkedList<String> list = getFullPahIds4Png();
		
		if(pngToPath != null) {
			pngToPath.clear();
		}
		
		final int start = mDirPath.length(); 
		for(final String id : list) { 
			final String simpleName = FileHelper.getSimpleNameWithSuffix(id); 
			final String relativeName = id.substring(start); 
			pngToPath.put(simpleName, relativeName); 
		} 
		
		return warnings;
	} 
	
	public LinkedList<String> scanPlist(final HashMap<String, Set<String>> plistToIdMap, 
			final HashMap<String, String> idToPlistMap, 
			final HashMap<String, String> plistToPvr, 
			final boolean remove) {
		if (plistToIdMap != null) {
			plistToIdMap.clear();
		}
		
		if (idToPlistMap != null) {
			idToPlistMap.clear();
		}
		
		if (plistToPvr != null) {
			plistToPvr.clear();
		}

		final LinkedList<String> warnings = new LinkedList<String>();

//		final LinkedList<String> list = FileHelper.getFullPathsDeep(mDirPath, new String[]{".nvcd", ".last"});
		final LinkedList<String> list = getFullPahIds4Plist();

		final int length = mDirPath.length();

		// read
		for (final String path : list) {
			if (ResJudge.isPlist(path)) {
				final LinkedList<String> resids = readResidsFromPlist(path, mCleanDir == null ? mDirPath : mCleanDir, remove);

				final HashSet<String> set = new HashSet<String>();
				set.addAll(resids);

				final String shortPath;
				if (path.charAt(length) == '\\' || path.charAt(length) == '/') {
					shortPath = path.substring(length + 1).replace(".plist", "").replace("\\", "/");
				} else {
					shortPath = path.substring(length).replace(".plist", "").replace("\\", "/");
				}
				plistToIdMap.put(shortPath, set);

				if (plistToPvr != null) {
					final String[] subs = new String[] { ".pvr.ccz", ".pvr", ".png", ".PNG", ".pkm", };
					String realTexture = shortPath;
					for (int i = 0; i < subs.length; i++) {
						String pvr = shortPath + subs[i];
						final String newPath = FileHelper.replaceDecollators(mDirPath + FileHelper.DECOLLATOR + pvr);
						if (new File(newPath).exists()) {
							realTexture = pvr;
							break;
						}
					}
					plistToPvr.put(shortPath, realTexture);
				}

				for (String res : resids) {
					if (idToPlistMap.containsKey(res)) {
						warnings.add(String.format("%s and %s has the same %s", shortPath, idToPlistMap.get(res), res));
						System.err.println(String.format("Warning: %s and %s has the same %s", shortPath, idToPlistMap.get(res), res));

						idToPlistMap.put(res, shortPath);
					} else {
						idToPlistMap.put(res, shortPath);
					}
				}
			}
		}

		return warnings;
	}
	
	public static LinkedList<String> readResidsFromPlist(final File plsitfile) {
		final LinkedList<String> ret = new LinkedList<String>();
		if(plsitfile != null && plsitfile.exists() && plsitfile.isFile()) {
			final BufferedReader reader = FileHelper.getUTF8Reader(plsitfile);
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					
					final int index = line.indexOf(".png");
					if (index >= 0) {
						final int indexStart = line.indexOf("<key>");
						if (indexStart >= 0) {
							final String resid = line.substring(indexStart + 5, index + 4);
							ret.add(resid);
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static LinkedList<String> readResidsFromPlist(final String plsit, final String dirPath, final boolean withRemoves) {
		final LinkedList<String> ret = new LinkedList<String>();
		
		final File file = new File(plsit); 
		if (file.exists()) {
			final BufferedReader reader = FileHelper.getUTF8Reader(plsit);
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					
					int index = line.indexOf(".png");
					if(index < 0) {
						index = line.indexOf(".jpg");
					}
					
					if (index >= 0) {
						final int indexStart = line.indexOf("<key>");
						if (indexStart >= 0) {
							final String resid = line.substring(indexStart + 5, index + 4);
							final String headResid = line.substring(indexStart + 5, index);

							if (withRemoves && !plsit.startsWith(headResid)) {
								final File f = new File(dirPath + FileHelper.DECOLLATOR + resid);
								if (f.exists()) {
									boolean del = f.delete();
									if (!del) {
										System.err.println("Delete " + f.getAbsolutePath() + " failed!");
									}
								}
							}
							ret.add(resid);
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//
			System.err.println(plsit + " not exist!");
		}

		return ret;
	}
	
//	public static List<String> print() {
//		final LinkedList<String> ret = new LinkedList<String>();
//		return ret;
//	}

	public static void main(final String[] args) {
		// D:\transfer\publish
		 final String dir = "C:\\Users\\zju_wjf\\Documents\\Tencent Files\\313040188\\FileRecv\\vs-cocos\\";

		 final TpPlistScaner scaner = new TpPlistScaner(dir);
		 scaner.scanPng(null);

		// System.err.println("completed!");
	}
}
