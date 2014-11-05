package elfEngine.opengl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.ImageData;

import _Res.code.InnerRes;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.ColorAssist;

public class GLManage {

	//
	public static void clearAllUnFoundResid() {
	}

	// @Deprecated
	public final static void checkIn(Texture tr) {
	}
	
	// @Deprecated
	public final static void checkOut(Texture tr) {
	}

	public final static void clearUnusedTextureRegion() {
		final DataModel model = PowerMan.getSingleton(DataModel.class);
		if (model != null) {
			final ElfNode screen = model.getRootScreen();
			if (screen != null) {
				for (int mode : Texture.IMAGE_MODES) {
					final HashMap<String, TextureRegion> map = getMap(0, mode);
					final Set<String> keys = new HashSet<String>(map.keySet());
					final Set<String> allResids = screen.getLegalResids(false);
					for (String key : keys) {
						if (!allResids.contains(key)) {
							final TextureRegion tr = map.get(key);
							if (tr != null) {
								tr.texture.invalid();
								map.remove(key);
							}
						}
					}
				}
			} else {
				clearByGLId(0);
			}
		} else {
			clearByGLId(0);
		}
		// sUnFoundResids.clear();
	}

	private final static HashMap<Integer, HashMap<String, TextureRegion>> sTextureMaps = new HashMap<Integer, HashMap<String, TextureRegion>>();

	final static HashMap<String, TextureRegion> getMap(int GLid, int imageMode) {
		final int hashCode = GLid * 1024 + imageMode;
		HashMap<String, TextureRegion> map = sTextureMaps.get(hashCode);

		if (map == null) {
			map = new HashMap<String, TextureRegion>();
			sTextureMaps.put(hashCode, map);
		}

		return map;
	}

	public static void unloadTextureRegion(String key, int GLid, int imageMode) {
		final TextureRegion tr = getMap(GLid, imageMode).get(key);
		if (tr != null) {
			tr.texture.invalid();
			getMap(GLid, imageMode).remove(key);
		}
	}

	public static void unloadTextureRegions(String key, int GLid) {
		for (int mode : Texture.IMAGE_MODES) {
			final TextureRegion tr = getMap(GLid, mode).get(key);
			if (tr != null) {
				tr.texture.invalid();
				getMap(GLid, mode).remove(key);
			}
		}
	}
	
	private static String mapPath(String originKey) {
		if(originKey != null && originKey.startsWith("@")) {
			originKey = originKey.substring(1);
		}
		
		final HashMap<String, String> name2path = ResManager.getName2PathMap();
		String key = name2path.get(originKey);

		final HashMap<String, String> outerName2path = ResManager.getOuterName2PathMap();
		if (key == null) {
			key = outerName2path.get(originKey);
		}
		
		if (key == null) {
			key = originKey;
		}
		
		return key;
	}

	public static TextureRegion loadTextureRegion(String originKey, int GLid, int mode) {
		final String key = mapPath(originKey);

		TextureRegion tr = getMap(GLid, mode).get(key);
		if (tr == null) {
			if (ResJudge.isRes(key)) {
				final File f = new File(key);
				if (f.exists() && f.isFile()) {
					final Texture texture = new Texture(key);
					texture.setImageMode(mode);

					if (texture.load()) {
						tr = new TextureRegion(texture);
						getMap(GLid, mode).put(key, tr);
					} else {
						return null;
					}
				} else {
					//from package
					if(key != null && key.startsWith("__")) {
						try {
							final InputStream is = InnerRes.getImageInputStream(key);
							final Texture texture = new Texture(is);
							
							texture.setImageMode(mode);

							if (texture.load()) {
								tr = new TextureRegion(texture);
								getMap(GLid, mode).put(key, tr);
							} else {
								return null;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				return null;
			}
		} else {
			if (!tr.texture.isGLBindIdValid()) {
				tr.texture.load();
			}
		}
		return tr;
	}

	public static TextureRegion loadTextureRegion(final String key, int GLid) {
		return loadTextureRegion(key, GLid, Texture.IMAGE_UNDEFINED);
	}

	public static void draw(String key, int GLid) {
		draw(key, GLid, Texture.IMAGE_UNDEFINED);
	}

	public static void draw(String key, int GLid, ColorAssist display, ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt) {
		draw(key, GLid, Texture.IMAGE_UNDEFINED, display, rb, lb, lt, rt);
	}

	public static void draw(String key, int GLid, int mode) {
		final TextureRegion tr = loadTextureRegion(key, GLid, mode);
		if (tr != null) {
			tr.draw();
		} else {
			// Redirect.outPrintln("No TextureRegion "+key+" To Draw!");
		}
	}

	public static void draw(String key, int GLid, int mode, ColorAssist display, ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt) {
		final TextureRegion tr = loadTextureRegion(key, GLid, mode);
		if (tr != null) {
			tr.draw(display, rb, lb, lt, rt);
		} else {
			// Redirect.outPrintln("No TextureRegion "+key+" To Draw!");
		}
	}

	// ElfColor display, ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt

	public static void clear() {
		// GLid 0-1
		for (int id = 0; id < 2; id++) {
			// mode
			for (int mode : Texture.IMAGE_MODES) {
				final HashMap<String, TextureRegion> map = getMap(id, mode);
				final Set<String> keySet0 = map.keySet();
				for (final String key : keySet0) {
					final TextureRegion tr = map.get(key);
					if (tr != null) {
						try {
							tr.texture.invalid();
						} catch (Exception e) {
						}
					}
				}

				map.clear();
			}
		}

		ElfNode.resetStatic();
		
		clearSizeMap();
	}

	public static void clearByGLId(final int GLid) {
		for (int mode : Texture.IMAGE_MODES) {
			final HashMap<String, TextureRegion> textureRegions = getMap(GLid, mode);
			final Set<String> keySet = textureRegions.keySet();
			for (final String key : keySet) {
				final TextureRegion tr = textureRegions.get(key);
				if (tr != null) {
					try {
						tr.texture.invalid();
					} catch (Exception e) {
					}
				}
			}

			textureRegions.clear();
		}
	}

	public static final void clearSizeMap() {
		mResSizeMap.clear();
	}

	public static final ElfPointf getSize(String name) {
		final String path = mapPath( name);

		if (mResSizeMap.containsKey(path)) {
			return mResSizeMap.get(path);
		}

		if (ResJudge.isRes(path)) {
			final File file = new File(path);
			
			if (!file.exists()) {
				if(path.startsWith("__")) {
					try {
						final InputStream inputStream = InnerRes.getImageInputStream(path);
						if(inputStream != null) {
							final ImageData source = new ImageData(inputStream);
							ElfPointf size = new ElfPointf(source.width, source.height);
							mResSizeMap.put(path, size);
							
							inputStream.close();
							return size;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				return null;
			}

			try {
				final FileInputStream inputStream = new FileInputStream(file);
				final ImageData source = new ImageData(inputStream);
				ElfPointf size = new ElfPointf(source.width, source.height);
				mResSizeMap.put(path, size);

				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return size;
			} catch (FileNotFoundException e) {
			}
		}
		return null;
	}

	public static final float getWidth(String name) {
		final String path = mapPath( name);

		if (mResSizeMap.containsKey(path)) {
			return mResSizeMap.get(path).x;
		}

		if (ResJudge.isRes(path)) {
			final File file = new File(path);
			if (!file.exists()) {
				return 0;
			}

			try {
				final FileInputStream inputStream = new FileInputStream(file);
				final ImageData source = new ImageData(inputStream);
				ElfPointf size = new ElfPointf(source.width, source.height);
				mResSizeMap.put(path, size);

				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return size.x;
			} catch (FileNotFoundException e) {
			}
		}
		return 0;
	}

	public static final float getHeight(String name) {
		final String path = mapPath( name);

		if (mResSizeMap.containsKey(path)) {
			return mResSizeMap.get(path).y;
		}

		if (ResJudge.isRes(path)) {
			final File file = new File(path);
			if (!file.exists()) {
				return 0;
			}

			try {
				final FileInputStream inputStream = new FileInputStream(file);
				final ImageData source = new ImageData(inputStream);
				ElfPointf size = new ElfPointf(source.width, source.height);
				mResSizeMap.put(path, size);

				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return size.y;
			} catch (FileNotFoundException e) {
			}
		}
		return 0;
	}

	private final static HashMap<String, ElfPointf> mResSizeMap = new HashMap<String, ElfPointf>();
}
