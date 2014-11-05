package com.ielfgame.stupidGame.lua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class SearchCCObject {
	
	public static class FileHelper {

		public static final String DECOLLATOR;
		public static final boolean IS_WINDOWS;

		public enum FileType {
			FILE, DIR, NO
		}

		public static FileType getFileType(final String path) {
			if (path != null) {
				final File f = new File(path);
				if (f.exists()) {
					if (f.isFile()) {
						return FileType.FILE;
					} else if (f.isDirectory()) {
						return FileType.DIR;
					}
				}
			}
			return FileType.NO;
		}

		static {
			final String sys = System.getProperty("os.name");
			System.err.println("System " + sys);
			IS_WINDOWS = sys.contains("Windows");
			if (IS_WINDOWS) {
				DECOLLATOR = "\\";
			} else {
				DECOLLATOR = "/";
			}
		}

		public static String replaceDecollators(String path) {
			if (IS_WINDOWS) {
				return path.replace("/", "\\");
			} else {
				return path.replace("\\", "/");
			} 
		} 

		public static void removeFile(final String file) {
			final File f = new File(file);
			final boolean ret = f.delete();
			if (!ret) { 
				System.err.println("delete " + file + " failed!"); 
			} 
		} 

		// no suffix
		public static String getSimpleNameNoSuffix(final String path) {
			if (path != null) {
				String ret = getSimpleName(path);
				return ret.substring(0, ret.indexOf("."));
			}
			return null;
		}
		
		public static String getSimpleNameWithSuffix(final String path) {
			if (path != null) {
				String ret = getSimpleName(path);
				return ret;
			}
			return null;
		}

		public static LinkedList<String> getFullPahIds(final File file, final String subs[], final boolean deep) {
			final LinkedList<String> ret = new LinkedList<String>();
			if (file.exists()) {
				if (file.isDirectory()) {
					final File[] fs = file.listFiles();
					if (deep) {
						for (File f : fs) {
							ret.addAll(getFullPahIds(f, subs, deep));
						}
					} else {
						for (File f : fs) {
							if (!f.isDirectory()) {
								ret.addAll(getFullPahIds(f, subs, deep));
							}
						}
					}
				} else if (file.isFile()) {
					final String name = file.getAbsolutePath();
					if (subs != null) {
						for (String sub : subs) {
							if (name.endsWith(sub)) {
								ret.add(name);
								break;
							}
						}
					} else {
						ret.add(name);
					}
				}
			}

			return ret;
		}

		public static LinkedList<String> getSimplePahIds(final String dir, final String[] subs, final boolean deep) {
			final File file = new File(dir);
			return getSimplePahIds(file, subs, deep);
		}

		public static LinkedList<String> getSimplePahIds(final File file, final String[] subs, final boolean deep) {
			final LinkedList<String> ret = new LinkedList<String>();
			if (file.exists()) {
				if (file.isDirectory()) {
					final File[] fs = file.listFiles();
					if (deep) {
						for (File f : fs) {
							ret.addAll(getFullPahIds(f, subs, deep));
						}
					} else {
						for (File f : fs) {
							if (!f.isDirectory()) {
								ret.addAll(getFullPahIds(f, subs, deep));
							}
						}
					}
				} else if (file.isFile()) {
					final String name = file.getName();
					if (subs != null) {
						for (String sub : subs) {
							if (name.endsWith(sub)) {
								ret.add(name);
								break;
							}
						}
					} else {
						ret.add(name);
					}
				}
			}

			return ret;
		}

		public static LinkedList<String> getFullPahIds(final String dir, final String[] subs, final boolean deep) {
			final File file = new File(dir);
			return getFullPahIds(file, subs, deep);
		}

		public static void main(String[] args) {
			final String dir = "D:\\LordRiseRes";
			final LinkedList<String> ret = getFullPathsDeep(dir, new String[] { "release", ".nvcd", "text-base" });
			for (String f : ret) {
				System.err.println(f);
			}
		}

		public static String getUserDir() {
			return System.getProperty("user.dir");
		}

		// @Deprecated
		public static String[] getFullPaths(final String dir) {
			boolean end = dir.endsWith(DECOLLATOR);
			final File f = new File(dir);
			if (f.exists() && f.isDirectory()) {
				final String[] ret = f.list();
				for (int i = 0; i < ret.length; i++) {
					if (end) {
						ret[i] = dir + ret[i];
					} else {
						ret[i] = dir + DECOLLATOR + ret[i];
					}
				}

				return ret;
			}
			return null;
		}

		public static String[] getSimplePaths(final String dir) {
			final File f = new File(dir);
			if (f.exists() && f.isDirectory()) {
				final String[] ret = f.list();
				return ret;
			}
			return new String[0];
		}

		public static LinkedList<String> getFullPathsDeep(final String dir) {
			return getFullPathsDeep(dir, null);
		}

		public static LinkedList<String> getFullPathsDeep(final String dir, final String[] except) {
			final File f = new File(dir);

			final String simpleName = f.getName();

			final LinkedList<String> ret = new LinkedList<String>();
			if (except != null && simpleName != null) {
				for (String exc : except) {
					if (simpleName.equals(exc)) {
						return ret;
					}
				}
			}

			if (f.exists()) {
				if (f.isDirectory()) {
					final String[] paths = getFullPaths(dir);
					for (String path : paths) {
						ret.addAll(getFullPathsDeep(path, except));
					}
				} else {
					ret.add(f.getAbsolutePath());
				}
			}
			return ret;
		}

		public static String getSimpleName(final String path) {
			if (path != null) {
				int index = path.lastIndexOf("\\"); 
				if (index >= 0) {
					return path.substring(index + 1);
				} else {
					index = path.lastIndexOf("/");
					if (index >= 0) {
						return path.substring(index + 1);
					} 
				}
			}
			return path;
		}

		public static BufferedReader getReader(final String path) {
			return getReader(new File(path));
		}

		public static BufferedReader getReader(final File file) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reader;
		}

		public static BufferedReader getReader(final Class<?> _class, final String resName) {
			final InputStream stream = _class.getResourceAsStream(resName);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			return reader;
		}

		public static BufferedWriter getWriter(final String path) {
			return getWriter(new File(path));
		}

		public static BufferedWriter getWriter(final File file) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return writer;
		}

		public static String readBufferedReader(BufferedReader br) throws IOException {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}

		public static LinkedList<String> read(final File file) {
			final LinkedList<String> ret = new LinkedList<String>();
			final BufferedReader reader = getReader(file);
			if (reader != null) {
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						ret.add(line);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return ret;
		}

		public static void write(final List<String> lines, final File file) {
			final BufferedWriter write = getWriter(file);
			if (write != null) {
				try {
					for (String line : lines) {
						write.write(line + "\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						write.flush();
						write.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public static String readInputStream(InputStream in) throws IOException {
			return readBufferedReader(new BufferedReader(new InputStreamReader(in)));
		}

		public static String removeFileSubfix(String path) {
			if (path.lastIndexOf('.') >= 0) {
				return path.substring(0, path.lastIndexOf('.'));
			} else {
				return path;
			}
		}

		public static String fileSubfix(String path) {
			if (path.lastIndexOf('.') > 0) {
				return path.substring(path.lastIndexOf('.') + 1);
			} else {
				return "";
			}
		}

		public static String removeSubfix(String path) {
			final int index = path.lastIndexOf('.');
			if (index > 0) {
				return path.substring(0, index);
			} else {
				return path;
			}
		}

		public static String removeAllSubfix(String path) {
			final int index = path.indexOf('.');
			if (index > 0) {
				return path.substring(0, index);
			} else {
				return path;
			}
		}

		public static String getDirPath(final String path) {
			return path.substring(0, path.lastIndexOf(FileHelper.DECOLLATOR));
		}

		public static String replaceFileSubfix(String path, String replaceSubfix) {
			if (path.lastIndexOf('.') >= 0) {
				return path.substring(0, path.lastIndexOf('.') + 1) + replaceSubfix;
			} else {
				return path;
			}
		}

//		public static interface IStringMatch {
//			public boolean match(final String str1, final String str2);
//		}

		public static void removeFile(final String name, final IFileMatch match) {
			final File file = new File(name);
			if (match.isFileMatch(file)) { 
				boolean ret = file.delete();
				System.err.println("remove : " + file.getAbsolutePath() + " " + ret);
			} else if (file.isDirectory()) {
				final String[] list = file.list();
				for (String l : list) {
					removeFile(name + DECOLLATOR + l, match);
				}
			}
		}

		public static interface IteratorFile {
			public boolean iterator(final File file);
		}

		public static void travelFiles(final String path, final IteratorFile it) {
			final File file = new File(path);
			if (file.exists()) {
				if (!it.iterator(file)) {
					final String[] list = file.list();
					if (list != null) {
						for (String f : list) {
							travelFiles(path + DECOLLATOR + f, it);
						}
					}
				}
			}
		}

		public static void replaceFileKey(final String path, final ArrayList<String> keys, final ArrayList<String> replaces) {
			final BufferedReader br = getReader(path);
			final LinkedList<String> allLines = new LinkedList<String>();
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					int size = keys.size();
					for (int i = 0; i < size; i++) {
						line = line.replace(keys.get(i), replaces.get(i));
					}
					allLines.add(line);
				}

				br.close();

				try {
					final BufferedWriter bw = getWriter(path);
					for (String text : allLines) {
						bw.write(text + "\n");
					}
					bw.flush();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static String num2Str(final int num, final int bits) {
			return String.format("%s%d", chars(bits - num2Bits(num), '0'), num);
		}

		public static int num2Bits(final int num) {
			return String.format("%d", num).length();
		}

		public static String chars(final int num, final char c) {
			final char[] cs = new char[num];
			Arrays.fill(cs, c);
			return String.valueOf(cs);
		}

		static boolean isSubfix(String path, String[] subfix) {
			if (subfix != null && path != null) {
				final String sub = fileSubfix(path);
				for (final String s : subfix) {
					if (sub.endsWith(s)) {
						return true;
					}
				}
			}

			if (subfix == null) {
				return true;
			}

			return false;
		}
		
		public static interface IFileMatch {
			public boolean isFileMatch(File file);
		}
	}

	
	public static void main(String[] args) {
		final String path = FileHelper.getUserDir();
		final HashMap<String, String> map = new HashMap<String, String>();
		LinkedList<String> pkgs = FileHelper.getFullPahIds(path, new String[] { ".pkg" }, false);
		for (final String pkg : pkgs) {
			LinkedList<String> lines = FileHelper.read(new File(pkg));
			for (String line : lines) {
				final String [] ret = getClassNames(line);
				if(ret != null) { 
					map.put(ret[0], ret[1]); 
				}
			}
		}
		
		final LinkedList<String> ccObjects = new LinkedList<String>();
		
		final Set<String> set = map.keySet();
		for(String key : set) {
			if(isCCObject(key, map)) {
				ccObjects.add(key);
			}
		}
		Collections.sort(ccObjects);
		
		for(String cc : ccObjects) {
			System.err.println("\t\""+cc+"\",");
		}
	}
	
	static String [] split(String line, String delim) {
		if(line != null) {
			final LinkedList<String> list = new LinkedList<String>();
			 StringTokenizer st = new StringTokenizer(line, delim);
			 while( st.hasMoreElements()) {
				 list.add(st.nextToken());
			 }
			 final String [] ret = new String[list.size()];
			 list.toArray(ret);
			 return ret;
		}
		return null;
	}

	static String[] CCObjectTypes = { "CCObject", "CCAction", "CCImage", "CCFiniteTimeAction", "CCActionInstant", "CCCallFunc", "CCCallFuncN", "CCFlipX", "CCFlipY", "CCHide", "CCPlace", "CCReuseGrid", "CCShow", "CCStopGrid", "CCToggleVisibility", "CCActionInterval",
			"CCAccelAmplitude", "CCAccelDeccelAmplitude", "CCActionCamera", "CCOrbitCamera", "CCCardinalSplineTo", "CCCardinalSplineBy", "CCCatmullRomTo", "CCCatmullRomBy", "CCActionEase", "CCEaseBackIn", "CCEaseBackInOut", "CCEaseBackOut", "CCEaseBounce", "CCEaseElastic",
			"CCEaseExponentialIn", "CCEaseExponentialInOut", "CCEaseExponentialOut", "CCEaseRateAction", "CCEaseSineIn", "CCEaseSineInOut", "CCEaseSineOut", "CCAnimate", "CCBezierBy", "CCBezierTo", "CCBlink", "CCDeccelAmplitude", "CCDelayTime", "CCFadeIn", "CCFadeOut",
			"CCFadeTo", "CCGridAction", "CCJumpBy", "CCJumpTo", "CCMoveTo", "CCMoveBy", "CCProgressFromTo", "CCProgressTo", "CCRepeat", "CCRepeatForever", "CCReverseTime", "CCRotateBy", "CCRotateTo", "CCScaleTo", "CCScaleBy", "CCSequence", "CCSkewTo", "CCSkewBy", "CCSpawn",
			"CCTintBy", "CCTintTo", "CCActionManager", "CCAnimation", "CCAnimationCache", "CCArray", "CCAsyncObject", "CCAutoreleasePool", "CCBMFontConfiguration", "CCCamera", "CCConfiguration", "CCData", "CCDirector", "CCDisplayLinkDirector", "CCEvent", "CCGrabber", "CCGrid3D",
			"CCTiledGrid3D", "CCKeypadDispatcher", "CCKeypadHandler", "CCDictionary", "CCNode", "CCAtlasNode", "CCLabelAtlas", "CCTileMapAtlas", "CCLayer", "CCLayerColor", "CCLayerGradient", "CCLayerMultiplex", "CCMenu", "CCMenuItem", "CCMenuItemLabel", "CCMenuItemAtlasFont",
			"CCMenuItemFont", "CCMenuItemSprite", "CCMenuItemImage", "CCMenuItemToggle", "CCMotionStreak", "CCParallaxNode", "CCParticleSystem", "CCParticleBatchNode", "CCParticleSystemQuad", "CCProgressTimer", "CCRenderTexture", "CCRibbon", "CCScene", "CCTransitionScene",
			"CCTransitionCrossFade", "CCTransitionFade", "CCTransitionFadeTR", "CCTransitionFadeBL", "CCTransitionFadeDown", "CCTransitionFadeUp", "CCTransitionJumpZoom", "CCTransitionMoveInL", "CCTransitionMoveInB", "CCTransitionMoveInR", "CCTransitionMoveInT",
			"CCTransitionPageTurn", "CCTransitionRotoZoom", "CCTransitionSceneOriented", "CCTransitionFlipAngular", "CCTransitionFlipX", "CCTransitionFlipY", "CCTransitionZoomFlipAngular", "CCTransitionZoomFlipX", "CCTransitionZoomFlipY", "CCTransitionShrinkGrow",
			"CCTransitionSlideInL", "CCTransitionSlideInB", "CCTransitionSlideInR", "CCTransitionSlideInT", "CCTransitionSplitCols", "CCTransitionSplitRows", "CCTransitionTurnOffTiles", "CCTransitionProgress", "CCTransitionProgressRadialCCW", "CCTransitionProgressRadialCW",
			"CCTransitionProgressHorizontal", "CCTransitionProgressVertical", "CCTransitionProgressInOut", "CCTransitionProgressOutIn", "CCSprite", "CCLabelTTF", "CCTextFieldTTF", "CCSpriteBatchNode", "CCLabelBMFont", "CCTMXLayer", "CCTMXTiledMap", "CCPointObject",
			"CCProjectionProtocol", "CCRibbonSegment", "CCScheduler", "CCSet", "CCSpriteFrame", "CCSpriteFrameCache", "CCString", "CCTexture2D", "CCTextureAtlas", "CCTextureCache", "CCTexturePVR", "CCTimer", "CCTMXLayerInfo", "CCTMXMapInfo", "CCTMXObjectGroup",
			"CCTMXTilesetInfo", "CCTouch", "CCTouchDispatcher", "CCTouchHandler", "CCParticleFire", "CCParticleFireworks", "CCParticleSun", "CCParticleGalaxy", "CCParticleFlower", "CCParticleMeteor", "CCParticleSpiral", "CCParticleExplosion", "CCParticleSmoke", "CCParticleSnow",
			"CCParticleRain", "CCScale9Sprite", "CCControl", "CCControlButton", "CCControlColourPicker", "CCControlPotentiometer", "CCControlSlider", "CCControlStepper", "CCControlSwitch", "CCEditBox", "CCInteger",
	};

	static boolean isCCObject(String className, HashMap<String, String> map) { 
		while(map.containsKey(className)) {
			className = map.get(className);
		};
		
		if(className != null) {
			for(String type : CCObjectTypes) {
				if(type.equals(className)) {
					return true;
				}
			}
		}
		return false;
	}

	static String[] getClassNames(final String line) {
		if (line != null) {
			if (line.contains("class")) {
				final String regex = " :{";
				final String[] ls = split(line, regex);
				if (ls == null) {
					System.err.println("warning :" + line);
				} else {
					if (ls.length == 2) {
						return new String[] { ls[1], null };
					} else if (ls.length == 4) {
						return new String[] { ls[1], ls[3] };
					} else {
						System.err.println("warning :" + line + " "+ls.length);
						for(String l : ls) {
							System.err.println(l);
						}
					}
				}
			}
		}
		return null;
	}
}
