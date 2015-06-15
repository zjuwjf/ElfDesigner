package com.ielfgame.stupidGame.res;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.crypto.XXTEA;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.zip.ZipUtils;

public class ResManager {
	
//	private static final String IMAGE = "image";
	private static final String BMFONT = "bmfont";
	private static final String XML = "xml";
	private static final String SWF = "swf";
	private static final String LANGUAGE = "language";
	private static final String CONFIG = "config";
	private static final String TMP = "tmp";
	private static final String ZIP = "zip";
//	private static final String RAW = "raw";
	private static final String FONT = "font";
	
//	private static final String IMAGE_Andriod = "image-android";
//	private static final String RAW_Andriod = "raw-android";
	
	private static final String getImageStr() {
//		if( PowerMan.getSingleton(ProjectSetting.class).plantform ) {
//			return IMAGE_Andriod;
//		} else {
//			return IMAGE;
//		}
		return PowerMan.getSingleton(ProjectSetting.class).plantform.image;
	}
	
	private static final String getRawStr() {
//		if( PowerMan.getSingleton(ProjectSetting.class).IsAndroid ) {
//			return RAW_Andriod;
//		} else {
//			return RAW;
//		}
		return PowerMan.getSingleton(ProjectSetting.class).plantform.raw;
	}
	
	/***
	 * 1.commit ?
	 */
	private final static String appendPath(final String... paths) {
		File f = null;
		for (String path : paths) {
			if (path != null) {
				if (f == null) {
					f = new File(path);
				} else {
					f = new File(f, path);
				}
			}
		}
		if (f == null) {
			return null;
		} else {
			return f.getAbsolutePath();
		}
	}

	public String getDesignerRootAsset() {
		return PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR;
	}
	
	public String getDesignerSWFAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, SWF);
	}
	
	public String getDesignerBMFontAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, BMFONT);
	}
	
	public String getDesignerImageAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, getImageStr());
	}

	public String getDesignerXMLAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, XML);
	}

	public String getDesignerZipAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, ZIP);
	}

	public String getDesignerTmpAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, TMP);
	}

	public String getDesignerFontAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, FONT);
	}

	public String getDesignerLanguageAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, LANGUAGE);
	}

	public String getDesignerLanguagePlistAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, LANGUAGE, PowerMan.getSingleton(ProjectSetting.class).language_plist);
	}

	public String getDesignerRawAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, getRawStr());
	}

	public String getDesignerConfigAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).Designer_Resource_REF_DIR, CONFIG);
	}

	// ------------------------------------------------------------------------------------------
	public boolean getXCodeRootAssetSupported() {
		try {
			return new File(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR).isAbsolute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getXCodeRootAsset() {
		return PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR;
	}

	public String getXCodeImageAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, "image");
	}

	public String getXCodeXMLAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, XML);
	}

	public String getXCodeZipAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, ZIP);
	}

	public String getXCodeFontAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, FONT);
	}

	public String getXCodeLanguageAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, LANGUAGE);
	}

	public String getXCodeLanguagePlistAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, LANGUAGE, PowerMan.getSingleton(ProjectSetting.class).language_plist);
	}

	public String getXCodeRawAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, "raw");
	}

	public String getXCodeConfigAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, CONFIG);
	}
	
	//getDesignerBMFontAsset
	public String getXCodeBMFontAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, BMFONT);
	}

	public String getXCodeScriptAsset() {
		return appendPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR, PowerMan.getSingleton(ProjectSetting.class).script);
	}

	public String getXCodeClassesAsset() {
		return FileHelper.getDirPath(PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR) + FileHelper.DECOLLATOR + "Classes";
	}

	private final HashMap<String, String> mMap = new HashMap<String, String>();
	
	// private final HashMap<String, Set<String>> mPlistToIdMap = new
	// HashMap<String, Set<String>>();
	// private final HashMap<String, String> mIdToPlistMap = new HashMap<String,
	// String>();
	// private final HashMap<String, String> mPlistToPvr = new HashMap<String,
	// String>();

	private final static HashMap<String, String> sName2PathMap = new HashMap<String, String>();
	private final static HashMap<String, String> sOuterName2PathMap = new HashMap<String, String>();
	private final static HashMap<String, File[]> sDir2ListfileMap = new HashMap<String, File[]>();
	private final static HashSet<String> sChangedDirSet = new HashSet<String>();
	private final static HashSet<String> sPngDirSet = new HashSet<String>();

//	private static boolean sQuiet;
//	private final static String sPatternString = "^[a-zA-Z][a-zA-Z0-9_]{0,250}\\.(jpg|png)$";
//	private final static Pattern sPattern = Pattern.compile(sPatternString);
//	private static boolean isMatch(String input) {
//		if (input == null) {
//			return false;
//		} else {
//			final Matcher matcher = sPattern.matcher(input);
//			return matcher.matches();
//		}
//	}

	public static HashMap<String, String> getName2PathMap() {
		synchronized (sLock) {
			// return new HashMap<String, String>(sName2PathMap);
			return sName2PathMap;
		}
	}
	
	public static HashMap<String, String> getOuterName2PathMap() {
		synchronized (sLock) {
			// return new HashMap<String, String>(sName2PathMap);
			return sOuterName2PathMap;
		}
	}

	public static HashSet<String> getChangedDirSet() {
		synchronized (sLock) {
			// return new HashSet<String>(sChangedDirSet);
			return sChangedDirSet;
		}
	}

	public static HashSet<String> getPngDirSet() {
		synchronized (sLock) {
			// return new HashSet<String>(sPngDirSet);
			return sPngDirSet;
		}
	}

	public static HashMap<String, File[]> getDir2ListfileMap() {
		synchronized (sLock) {
			// return new HashMap<String, File[]>(sDir2ListfileMap);
			return sDir2ListfileMap;
		}
	}
	
	private final static void updateName2PathWithExcept(final File file, final HashMap<String, String> name2pathMap, final IOnResourceError oberver, final File except) {
		final String name = file.getName();
		if (file.isDirectory()) {
			if (!name.startsWith(".")) {
				final File[] fs = file.listFiles();
				for (final File chlid : fs) {
					if(!chlid.equals(except)) {
						updateName2Path(chlid, name2pathMap, oberver);
					}
				}
			}
		} else if (name.endsWith(".png") || name.endsWith(".jpg")) {
			name2pathMap.put(name, file.getAbsolutePath());
		}
	}
	/***
	 * 
	 * name2path ?
	 * 
	 */
	private final static void updateName2Path(final File file, final HashMap<String, String> name2pathMap, final IOnResourceError oberver) {
		final String name = file.getName();
		if (file.isDirectory()) {
			if (!name.startsWith(".")) {
				final File[] fs = file.listFiles();
				for (final File chlid : fs) {
					updateName2Path(chlid, name2pathMap, oberver);
				}
			}
		} else if (name.endsWith(".png") || name.endsWith(".jpg")) {
			// if(path.endsWith(".png") || path.endsWith(".jpg")){
//			final String shortname = name.substring(0, name.length()-4);

			// if (!sQuiet) {
			// if (!isMatch(name)) {
			// if (oberver != null) {
			// oberver.onIlleagl(name, path);
			// }
			// } else {
			// final String oldPath = name2pathMap.get(name);
			// if (oldPath != null && !oldPath.equals(path)) {
			// // do something
			// if (oberver != null) {
			// oberver.onConfict(name, oldPath, path);
			// }
			// }
			// }
			// }
			name2pathMap.put(name, file.getAbsolutePath());
		}
	}

//	private final static void updateName2Path(final String path, final HashMap<String, String> name2pathMap, final IOnResourceError oberver) {
//		final File f = new File(path);
//		if (f.isDirectory()) {
//			final String name = f.getName();
//			if (!name.startsWith(".")) {
//				final File[] fs = f.listFiles();
//				for (final File file : fs) {
//					updateName2Path(file.getAbsolutePath(), name2pathMap, oberver);
//				}
//			}
//		} else if (path.endsWith(".png") || path.endsWith(".jpg")) {
//			// if(path.endsWith(".png") || path.endsWith(".jpg")){
//			final String name = FileHelper.getSimpleName(path);
//
//			// if (!sQuiet) {
//			// if (!isMatch(name)) {
//			// if (oberver != null) {
//			// oberver.onIlleagl(name, path);
//			// }
//			// } else {
//			// final String oldPath = name2pathMap.get(name);
//			// if (oldPath != null && !oldPath.equals(path)) {
//			// // do something
//			// if (oberver != null) {
//			// oberver.onConfict(name, oldPath, path);
//			// }
//			// }
//			// }
//			// }
//
//			name2pathMap.put(name, path);
//		}
//	}

	private final static void updateDirChangedMap(final String path, final HashSet<String> changedMap) {
		final File dir = new File(path);
		if (dir.isFile()) {
			return;
		}

		if (ResPackerHelper.hasChanged(path)) {
			changedMap.add(path);
		}

		if (dir.isDirectory()) {
			final File[] fs = dir.listFiles();
			for (final File f : fs) {
				updateDirChangedMap(f.getAbsolutePath(), changedMap);
			}
		}
	}

	private final static void updateDir2FileListInfo(final String dir, final HashMap<String, File[]> map) {
		final File[] fs = ResPackerHelper.sortFile(dir);
		map.put(dir, fs);
		for (final File f : fs) {
			if (f.isDirectory()) {
				updateDir2FileListInfo(f.getAbsolutePath(), map);
			}
		}
	}

	private final static void updateDirIsPNGDir(final String dir, final HashSet<String> map) {
		if (ResPackerHelper.isDirHasimages(dir)) {
			map.add(dir);
		}
		final File[] fs = new File(dir).listFiles();
		if (fs != null) {
			for (final File f : fs) {
				if (f.isDirectory()) {
					updateDirIsPNGDir(f.getAbsolutePath(), map);
				}
			}
		}
	}

	private final static Object sLock = new Object();
	private final static Object sBoolLock = new Object();
	private static boolean sForceUpdate = false;
	
	public static boolean getForceUpdate () {
		synchronized (sBoolLock) {
			return sForceUpdate;
		}
	}
	
	public static void setForceUpdate (boolean b) {
		synchronized (sBoolLock) {
			sForceUpdate = b;
		}
	}

	// public static void init() {
	// final HashMap<String, String> name2PathMap = new HashMap<String,
	// String>();
	// final HashMap<String, File[]> dir2ListfileMap = new HashMap<String,
	// File[]>();
	// final HashSet<String> changedDirSet = new HashSet<String>();
	// final HashSet<String> pngDirSet = new HashSet<String>();
	//
	// final String resAssert =
	// ResManager.getSingleton().getDesignerImageAsset();
	//
	// updateName2Path(resAssert, name2PathMap, null);
	//
	// synchronized (sLock) {
	// sName2PathMap.clear();
	// sName2PathMap.putAll(name2PathMap);
	// }
	//
	// updateDir2FileListInfo(resAssert, dir2ListfileMap);
	// synchronized (sLock) {
	// sDir2ListfileMap.clear();
	// sDir2ListfileMap.putAll(dir2ListfileMap);
	// }
	//
	// updateDirChangedMap(resAssert, changedDirSet);
	// synchronized (sLock) {
	// sChangedDirSet.clear();
	// sChangedDirSet.addAll(changedDirSet);
	// }
	//
	// updateDirIsPNGDir(resAssert, pngDirSet);
	// synchronized (sLock) {
	// sPngDirSet.clear();
	// sPngDirSet.addAll(pngDirSet);
	// }
	// }

	/***
	 * final HashSet<String> changedMap = new HashSet<String>();
	 * updateHasChangedMap(mResAsset, changedMap);
	 * 
	 * final HashMap<String, File[]> dir2listfile = new HashMap<String,
	 * File[]>(); updateFileInfo(mResAsset, dir2listfile);
	 * 
	 * final HashSet<String> pngMap = new HashSet<String>();
	 * updatePNGDir(mResAsset, pngMap);
	 */
	static {
		new Thread() {
			public void run() {

				while (true) {
					/***
					 * Texture-region ???
					 */
					final Shell shell = PowerMan.getSingleton(MainDesigner.class).getShell();
					if (shell != null && shell.isDisposed()) {
						break;
					}
					
					updateImageResource();
					
					for(int i=0; i<10; i++) {
						//
						if( getForceUpdate() ) {
							setForceUpdate(false);
							break;
						} else {
							try {
								Thread.sleep( 1000 );
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				System.err.println("Observer Resource Finished!");
			}
		}.start();
	}

	private static void updateImageResource() {
		final String resAssert = ResManager.getSingleton().getDesignerImageAsset();
		{
//			final String outResAssert = ResManager.getSingleton().getDesignerRootAsset();
			final HashMap<String, String> name2PathMap = new HashMap<String, String>();
			final OnResourceError iOnResourceError = new OnResourceError();
			
			final File imageFile = new File(resAssert);
			updateName2Path(imageFile, name2PathMap, iOnResourceError);
			
			synchronized (sLock) {
				sName2PathMap.clear();
				sName2PathMap.putAll(name2PathMap);
			} 
			
			//Outer
			final String outResAssert = ResManager.getSingleton().getDesignerRootAsset();
			final HashMap<String, String> name2PathMap2 = new HashMap<String, String>();
			updateName2PathWithExcept(new File(outResAssert), name2PathMap2, iOnResourceError, imageFile);
			
			iOnResourceError.flush();
			
			synchronized (sLock) {
				sOuterName2PathMap.clear();
				sOuterName2PathMap.putAll(name2PathMap2);
			} 
		}

		{
			final HashMap<String, File[]> dir2ListfileMap = new HashMap<String, File[]>();
			updateDir2FileListInfo(resAssert, dir2ListfileMap);
			synchronized (sLock) {
				sDir2ListfileMap.clear();
				sDir2ListfileMap.putAll(dir2ListfileMap);
			}
		}

		{
			final HashSet<String> changedDirSet = new HashSet<String>();
			updateDirChangedMap(resAssert, changedDirSet);
			synchronized (sLock) {
				sChangedDirSet.clear();
				sChangedDirSet.addAll(changedDirSet);
			}
		}

		{
			final HashSet<String> pngDirSet = new HashSet<String>();
			updateDirIsPNGDir(resAssert, pngDirSet);
			synchronized (sLock) {
				sPngDirSet.clear();
				sPngDirSet.addAll(pngDirSet);
			}
		}
	}

	private ResManager() {
	}

	private static final ResManager sResManagerSingleton = new ResManager();

	public static ResManager getSingleton() {
		return sResManagerSingleton;
	}

	// private void refresh() {
	// mMap.clear();
	//
	// final LinkedList<String> list =
	// FileHelper.getFullPahIds(getDesignerImageAsset(), new String[] { ".png",
	// ".jpg" }, true);
	// for (final String path : list) {
	// final String simple = FileHelper.getSimpleNameWithSuffix(path);
	// mMap.put(simple, path);
	// }
	//
	// final TpPlistScaner scanner = new TpPlistScaner(getDesignerImageAsset());
	//
	// mPlistToIdMap.clear();
	// mIdToPlistMap.clear();
	// mPlistToPvr.clear();
	//
	// scanner.scanPlist(mPlistToIdMap, mIdToPlistMap, mPlistToPvr, false);
	// }

	public String key2value(String key, String defaultReturn) {
		// assert key is a simple name
		key = FileHelper.getSimpleNameWithSuffix(key);

		final String inmap = mMap.get(key);
		if (inmap != null) {
			return inmap;
		}

		return defaultReturn;
	}

	public String key2value(String key) {
		return key2value(key, null);
	}

	private void publishImage() {
		// plist to image
		// png to image root
		final String source = getDesignerImageAsset();
		final String dest = getXCodeImageAsset();

		final HashMap<String, String> name2path = new HashMap<String, String>(getName2PathMap());
		final TpPlistScaner scaner = new TpPlistScaner(source);

		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();

		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);

		final File destDir = new File(dest);
		FileHelper.removeFile(destDir);
		
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		compareArtAndCodePackFile(new File(source), destDir);
		compareArtAndCodeUnpackFile(dest, name2path, idToPlistMap);
	}
	
	private void publishImageWithXXTea(final String key) {
		// plist to image
		// png to image root
		publishImage();
		
		final String dest = getXCodeImageAsset();
		final LinkedList<String> allPngs = FileHelper.getFullPahIds(dest, new String[]{".png", ".PNG", ".jpg", ".pvr.ccz"}, true);
		for(final String pngPath : allPngs) {
			final String tmp = pngPath + ".xxtmp";
			XXTEA.encrypt(pngPath, tmp, key);
			FileHelper.removeFile(pngPath);
			
			final File f = new File(tmp);
			f.renameTo(new File(pngPath));
		}
		
		final LinkedList<String> allPngs2 = FileHelper.getFullPahIds(dest, new String[]{".pkm"}, true);
		for(final String pngPath : allPngs2) {
			try {
				final String tmp1 = pngPath + ".xxtmp";
				final String tmp2 = pngPath + ".xxtmp2";
				
				ZipUtils.zip(pngPath, tmp1);
				XXTEA.encrypt(tmp1, tmp2, key);
				
				FileHelper.removeFile(pngPath);
				FileHelper.removeFile(tmp1);
				
				final File f = new File(tmp2);
				f.renameTo(new File(pngPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final static void copyPlistPvrQuickly(final File source, final File dest) {
		final String name = source.getName();
		if (source.isFile()) {
			if (name.endsWith(".plist") || name.endsWith(".ccz") || name.endsWith(".PNG") || name.endsWith(".pkm")) {
				FileHelper.copyFileQuickly(source, dest.getParentFile(), dest.getName());
			}
		} else if (source.isDirectory()) {
			if (!name.startsWith(".")) {
				dest.mkdirs();
				final File[] fs = source.listFiles();
				for (final File f : fs) {
					copyPlistPvrQuickly(f, new File(dest, f.getName()));
				}
			}
		}
	}

	private final static void compareArtAndCodeUnpackFile(final String codePath, final HashMap<String, String> name2path, final HashMap<String, String> name2plist) {
		final LinkedList<String> list = FileHelper.getFullPahIds(codePath, new String[] { ".png", ".jpg" }, false);

		final Set<String> nameSet = name2path.keySet();
		final Set<String> nameInplistSet = name2plist.keySet();

		for (final String fullpath : list) {
			final String name = FileHelper.getSimpleName(fullpath);

			if (nameInplistSet.contains(name)) {
				FileHelper.removeFile(fullpath);
			} else if (!nameSet.contains(name)) {
				FileHelper.removeFile(fullpath);
			}
		}

		for (String name : nameSet) {
			if (!nameInplistSet.contains(name)) {
				final String fullpath = name2path.get(name);
				if (!FileHelper.compare2files(fullpath, new File(new File(codePath), name).getAbsolutePath())) {
					FileHelper.copyFileQuickly(new File(fullpath), new File(codePath), name);
				}
			}
		}
	}

	private final static File[] getFileList(final File file) {
		if (file != null) {
			final File[] fs = file.listFiles();
			if (fs != null && fs.length > 0) {
				final ArrayList<File> array = new ArrayList<File>();
				for (File f : fs) {
					if (f.isDirectory() && f.getName().startsWith(".")) {
					} else {
						array.add(f);
					}
				}
				final File[] ret = new File[array.size()];
				array.toArray(ret);

				return ret;
			}
		}

		return new File[0];
	}

	private final static void compareArtAndCodePackFile(final File artFile, final File codeFile) {
		if (artFile.exists() && !codeFile.exists()) {
			copyPlistPvrQuickly(artFile, codeFile);
		} else if (!artFile.exists() && codeFile.exists()) {
			FileHelper.removeFile(codeFile);
		} else if (artFile.isFile() && codeFile.isFile()) {
			if (!FileHelper.compare2files(artFile.getAbsolutePath(), codeFile.getAbsolutePath())) {
				copyPlistPvrQuickly(artFile, codeFile);
			}
		} else if (artFile.isDirectory()) {
			if (artFile.getName().startsWith(".")) {
				// do nothing
				return;
			}

			if (!codeFile.isDirectory()) {
				codeFile.mkdirs();
			}

			if (!codeFile.isDirectory()) {
				System.err.println(codeFile.getAbsolutePath() + "Is A File, Directory expect!");
				return;
			}

			final File[] artFs = getFileList(artFile);
			final File[] codeFs = getFileList(codeFile);

			final HashSet<String> artSet = new HashSet<String>();
			for (File f : artFs) {
				final String name = f.getName();
				artSet.add(name);
			}

			final HashSet<String> sameSet = new HashSet<String>();
			for (File f : codeFs) {
				final String name = f.getName();
				if (artSet.contains(name)) {
					sameSet.add(name);
				}
			}

			for (File f : artFs) {
				final String name = f.getName();
				if (sameSet.contains(name)) {
					compareArtAndCodePackFile(new File(artFile, name), new File(codeFile, name));
				} else {
					copyPlistPvrQuickly(new File(artFile, name), new File(codeFile, name));
				}
			}

			for (File f : codeFs) {
				final String name = f.getName();
				if (!sameSet.contains(name)) {
					FileHelper.removeFile(new File(codeFile, name));
				}
			}
		}
	}

	private final static void publishCPlusPlusT() {
		TFileCreator.createCPlusPlusT(ResManager.getSingleton().getXCodeClassesAsset());
		
		TFileCreator.createLuaT(ResManager.getSingleton().getXCodeScriptAsset());
	}

	private final static void rsyncDirs(final File dir1, final File dir2) {
		if (dir1.isFile()) {
			if (dir2.isDirectory()) {
				FileHelper.removeFile(dir2);
			}

			if (!FileHelper.compare2files(dir1.getAbsolutePath(), dir2.getAbsolutePath())) {
				FileHelper.copyQuickly(dir1, dir2);
			}
		} else if (dir1.isDirectory()) {
			if (dir1.getName().startsWith(".")) {
				return;
			}

			if (dir2.isFile()) {
				System.err.println("Could Not rsyncDirs:" + dir2.getAbsolutePath() + " Is File!");
				return;
			}

			if (!dir2.exists()) {
				dir2.mkdirs();
			}

			final File[] fs1 = dir1.listFiles();
			final File[] fs2 = dir2.listFiles();

			final HashSet<String> set1 = new HashSet<String>();
			for (File f : fs1) {
				set1.add(f.getName());
			}

			final HashSet<String> sameSet = new HashSet<String>();
			for (File f : fs2) {
				final String name = f.getName();
				if (set1.contains(name)) {
					sameSet.add(name);
					rsyncDirs(new File(dir1, name), new File(dir2, name));
				} else {
					FileHelper.removeFile(f);
				}
			}

			for (File f : fs1) {
				final String name = f.getName();
				if (!sameSet.contains(name)) {
					FileHelper.copyQuickly(new File(dir1, name), new File(dir2, name));
				}
			}
		}
	}

	private final static void publishOthers() {

		final String[] sources = { ResManager.getSingleton().getDesignerConfigAsset(), ResManager.getSingleton().getDesignerRawAsset(), ResManager.getSingleton().getDesignerFontAsset() };
		final String[] dests = { ResManager.getSingleton().getXCodeConfigAsset(), ResManager.getSingleton().getXCodeRawAsset(), ResManager.getSingleton().getXCodeFontAsset(), };

		for (int i = 0; i < sources.length; i++) {
			final String source = sources[i];
			final String dest = dests[i];

			final File destFile = new File(dest);

			if (destFile.isAbsolute()) {
				rsyncDirs(new File(source), destFile);
			}
		}
	}
	
	private final static void publishBMFont() {

		final String[] sources = { ResManager.getSingleton().getDesignerBMFontAsset()};
		final String[] dests = { ResManager.getSingleton().getXCodeBMFontAsset()};

		for (int i = 0; i < sources.length; i++) {
			final String source = sources[i];
			final String dest = dests[i];

			final File destFile = new File(dest);

			if (destFile.isAbsolute()) {
				rsyncDirs(new File(source), destFile);
			}
		}
	}
	
	private final static void publishBMFontWithXXTea(final String key) {
		publishBMFont();
		
		final String dest = ResManager.getSingleton().getXCodeBMFontAsset();
		final LinkedList<String> allPngs = FileHelper.getFullPahIds(dest, new String[]{".png", ".PNG", ".jpg", ".pvr.ccz"}, true);
		for(final String pngPath : allPngs) {
			final String tmp = pngPath + ".xxtmp";
			XXTEA.encrypt(pngPath, tmp, key);
			FileHelper.removeFile(pngPath);
			
			final File f = new File(tmp);
			f.renameTo(new File(pngPath));
		}
	}
	
	/***
	 * 
	 */

	public static void publishLanguage() {
		String mode = PowerMan.getSingleton(ProjectSetting.class).langEditMode;
		if (mode.equals("Mode1")) {
			return;
		}

		final String[] sources = { ResManager.getSingleton().getDesignerLanguageAsset() };
		final String[] dests = { ResManager.getSingleton().getXCodeLanguageAsset() };

		for (int i = 0; i < sources.length; i++) {
			final String source = sources[i];
			final String dest = dests[i];

			final File destFile = new File(dest);

			if (destFile.isAbsolute()) {
				rsyncDirs(new File(source), destFile);
			}

		}
	}
	
	public void publishWithXXTea() {
		final ViewInfoDialog vid = ViewInfoDialog.getSingleton();
		vid.appendInfo("Publish running...", null);

		if (!FileHelper.isDir(getDesignerImageAsset())) {
			vid.appendInfo(new StringBuilder("Source:").append(getDesignerImageAsset()).append(" is not a directory!").toString(), null);
			return;
		}

		if (!FileHelper.isDir(getXCodeImageAsset())) {
			new File(getXCodeImageAsset()).mkdirs();

			if (!FileHelper.isDir(getXCodeImageAsset())) {
				vid.appendInfo(new StringBuilder("Destination:").append(getXCodeImageAsset()).append(" is not a directory!").toString(), null);
				return;
			}
		}

		new Thread() {
			public void run() {
				try {
					publishImageWithXXTea("code");
					publishLanguage();
					publishOthers();
					publishCPlusPlusT();
					publishBMFontWithXXTea("code");
				} catch (Exception e) {
					e.printStackTrace();
				}
				vid.appendInfo("Publish Completed!", null);
			}
		}.start();
	}

	public void publish() {
		final ViewInfoDialog vid = ViewInfoDialog.getSingleton();
		vid.appendInfo("Publish running...", null);

		if (!FileHelper.isDir(getDesignerImageAsset())) {
			vid.appendInfo(new StringBuilder("Source:").append(getDesignerImageAsset()).append(" is not a directory!").toString(), null);
			return;
		}

		if (!FileHelper.isDir(getXCodeImageAsset())) {
			new File(getXCodeImageAsset()).mkdirs();

			if (!FileHelper.isDir(getXCodeImageAsset())) {
				vid.appendInfo(new StringBuilder("Destination:").append(getXCodeImageAsset()).append(" is not a directory!").toString(), null);
				return;
			}
		}

		new Thread() {
			public void run() {
				try {
					publishImage();

					publishLanguage();

					publishOthers();
					
					publishCPlusPlusT();
					
					publishBMFont();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				vid.appendInfo("Publish Completed!", null);
			}
		}.start();
	}

	/***
	 * 不能有重复名称 不能含有空格
	 */
	// public void check() {
	// if (!FileHelper.isDir(getDesignerImageAsset())) {
	// System.err.println(new
	// StringBuilder("Source:").append(getDesignerImageAsset()).append(" is not a directory!"));
	// return;
	// }
	//
	// final LinkedList<String> simpleList =
	// FileHelper.getSimplePahIds(getDesignerImageAsset(), new String[] {
	// ".png", ".jpg" }, true);
	//
	// final LinkedList<String> fullList =
	// FileHelper.getFullPahIds(getDesignerImageAsset(), new String[] { ".png",
	// ".jpg" }, true);
	//
	// final HashSet<String> set = new HashSet<String>();
	//
	// final HashSet<String> conflictSet = new HashSet<String>();
	//
	// for (final String name : simpleList) {
	// if (set.contains(name)) {
	// conflictSet.add(name);
	// } else {
	// set.add(name);
	// }
	// }
	//
	// for (final String conflict : conflictSet) {
	// for (final String fullname : fullList) {
	// final String simplename = FileHelper.getSimpleName(fullname);
	//
	// if (simplename.equals(conflict)) {
	// System.err.println("conflict :" +
	// fullname.substring(getDesignerImageAsset().length()));
	// }
	// }
	// }
	// }

}
