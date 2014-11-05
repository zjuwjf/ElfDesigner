package com.ielfgame.stupidGame.texturePacker.pvr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.config.ElfConfig;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.dialog.CollectDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.dialog.ProgressDialog;
import com.ielfgame.stupidGame.dialog.ProgressDialog.IProgress;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.DitherFormat;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.ImageFormat;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.SetTexturePackerPanelRet;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.texturePacker.pvr.PVRTexture.PVRTextureFormat;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.FileHelper.IFileMatch;
import com.ielfgame.stupidGame.utils.FileHelper.IteratorFile;

public class TPTest {

	public static PVRTextureFormat readPVRFormat(final String pvr) throws IllegalArgumentException, IOException {
		final PVRCCZTexture pvrccz = new PVRCCZTexture() {
			protected InputStream onGetInputStream() throws IOException {
				return new FileInputStream(new File(pvr));
			}
		};

		return pvrccz.getPVRTextureHeader().getPVRTextureFormat();
		// return null;
	}
	
	public enum Mode {
		Debug, Release
	}

	public static class ResIOS2AndroidData extends ElfDataXML {
		public String IOS_DEBUG_PATH_REF_DIR;
		public String IOS_RELEASE_PATH_REF_DIR;
		
		public String Android_DEBUG_PATH_REF_DIR;
		public String Android_RELEASE_PATH_REF_DIR;
		
		public Mode mode = Mode.Debug;
		
		public float scale = 1;

		public String[] AndroidProjects;
		public String AndroidSo_REF_FILE_so;

		public String[] AndroidProjectLibs;
	}
	
	public static void copyToAsset() {
		final MultiLineDialog dialog = new MultiLineDialog();
		final ResIOS2AndroidData data = PowerMan.getSingleton(ResIOS2AndroidData.class);
		final Object[] ret = dialog.open(data);
		if (ret != null) {
			data.setValues(ret);
			// save
			ElfConfig.exportElfConfig();
			
			final ProgressDialog pd = new ProgressDialog();
			
			pd.setListener(new IProgress() {
				final Thread thread = new Thread() {
					public void run() {
						copyToAsset(
								data.mode == Mode.Release ? data.IOS_RELEASE_PATH_REF_DIR : data.IOS_DEBUG_PATH_REF_DIR, 
								data.mode == Mode.Release ? data.Android_RELEASE_PATH_REF_DIR:data.Android_DEBUG_PATH_REF_DIR,  
								data.scale, data.AndroidProjects, data.AndroidSo_REF_FILE_so, data.AndroidProjectLibs, new IConvertProjectIOS2AndroidListener() {
							public void onStart() {
								
							} 
							
							public void onFinished() {
								pd.close();
							} 

							public void onProgress(String contextLeft, String contextRight, int index, int size) {
								pd.setProgress(contextLeft, contextRight, index, size);
							}
						});
					}
				};

				public void onStart() {
					thread.start();
				}

				public void onClose() {

				}
			});

			pd.open("Copy To Android Asset");
		}
	}

	public static void convertProjectIOS2Android() {
		final MultiLineDialog dialog = new MultiLineDialog();
		final ResIOS2AndroidData data = PowerMan.getSingleton(ResIOS2AndroidData.class);
		final Object[] ret = dialog.open(data);
		if (ret != null) {
			data.setValues(ret);
			// save
			ElfConfig.exportElfConfig();

			final ProgressDialog pd = new ProgressDialog();

			pd.setListener(new IProgress() {
				final Thread thread = new Thread() {
					public void run() {
						convertProjectResIOS2Android(
								data.mode == Mode.Release ? data.IOS_RELEASE_PATH_REF_DIR : data.IOS_DEBUG_PATH_REF_DIR, 
								data.mode == Mode.Release ? data.Android_RELEASE_PATH_REF_DIR:data.Android_DEBUG_PATH_REF_DIR, 
										data.scale, data.AndroidProjects, data.AndroidSo_REF_FILE_so, data.AndroidProjectLibs, new IConvertProjectIOS2AndroidListener() {
							public void onStart() {
							}

							public void onFinished() {
								pd.close();
							}

							public void onProgress(String contextLeft, String contextRight, int index, int size) {
								pd.setProgress(contextLeft, contextRight, index, size);
							}
						});
					}
				};

				public void onStart() {
					thread.start();
				}

				public void onClose() {

				}
			});

			pd.open("Res IOS To Android");
		}
	}

	// 1. modifier time
	// 2. output warnings
	// 3.
	
	public interface IConvertProjectIOS2AndroidListener {
		public void onStart();

		public void onFinished();

		public void onProgress(String contextLeft, String contextRight, int index, int size);
	}

	public static void copyToAsset(final String source, final String destination, final float scale, final String[] projects, final String soPath, final String[] soLibs, final IConvertProjectIOS2AndroidListener listener) {
		// // remove
		// FileHelper.removeFile(destination, new IFileMatch() {
		// public boolean isFileMatch(File file) {
		// if (file.isFile()) {
		// final String name = file.getName();
		// if (name.equals(".nvc") || name.equals("nvc")) {
		// return true;
		// }
		// }
		// return false;
		// }
		// });
		
		if (listener != null) {
			listener.onStart();
		}

		// copy to assets
		if (listener != null) {
			listener.onProgress("coping res to projects", "", 40, 100);
		}
		
		final String [] folders = {"GameRes", "var"};
		
		if (projects != null) {
			for (String project : projects) {
				for(String folder : folders) {
					final String gameRes = project + FileHelper.DECOLLATOR + "assets\\"+folder;
					FileHelper.removeFile(gameRes);
					FileHelper.copyDeep(destination+"\\"+folder, gameRes, null);
					// rename .nvc
					if (listener != null) {
						listener.onProgress("renaming .nvc files ", "", 80, 100);
					}
					FileHelper.travelFiles(gameRes, new IteratorFile() {
						public boolean iterator(final File file) {
							final String name = file.getName();
							if (name.endsWith(".nvc")) {
								final String fullname = file.getAbsolutePath();
								file.renameTo(new File(fullname.replace(".nvc", "nvc")));
							}
							return false;
						}
					});
				} 
			}
		}

		// copy .so
		if (listener != null) {
			listener.onProgress("coping .so to libs", "", 100, 100);
		} 
		if (soLibs != null) { 
			for (String lib : soLibs) {
				final String libs = lib + FileHelper.DECOLLATOR;
				FileHelper.copyDeep(soPath, libs + FileHelper.getSimpleName(soPath), null);
			}
		}

		if (listener != null) {
			listener.onFinished();
		}
	}

	public static void convertProjectResIOS2Android(final String source, final String destination, final float scale, final String[] projects, final String soPath, final String[] soLibs, final IConvertProjectIOS2AndroidListener listener) {
		// new DataModel();
		// ElfConfig.importElfConfig();
		// ConfigImExport.importConfigs();

		// 1.convert pvr.ccz to android
		final TpPlistScaner scaner = new TpPlistScaner(source);
		// scaner.setCleanDir(cleanDir);

		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();

		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);

		if (listener != null) {
			listener.onStart();
		}

		final LinkedList<String> errorList = new LinkedList<String>();

		final Set<String> plistSet = plistToIdMap.keySet();

		final int plistSize = plistSet.size();
		int index = 0;

		for (final String plist : plistSet) {
			index++;
			final Set<String> idSet = plistToIdMap.get(plist);
			System.err.println("plist:" + plist);
			
			if (listener != null) { 
				listener.onProgress("plist:", plist, index, plistSize);
			} 
			
			// check modifier time
			final String newSource = source + FileHelper.DECOLLATOR + plist + ".plist";
			final String newDestination = destination + FileHelper.DECOLLATOR + plist + ".plist";

			final File sourceFile = new File(newSource);
			final File destinationFile = new File(newDestination);
			if (destinationFile.exists() && sourceFile.exists()) {
				final long sourceTime = sourceFile.lastModified();
				final long destTime = destinationFile.lastModified();
				if (destTime > sourceTime) {
					// no need to texture packer again
					System.err.println("destTime > sourceTime :" + plist );
					continue;
				}
			}
			
			//
			
			boolean continueFlag = false;
			// 1. translate
			for (String id : idSet) {
				// System.err.println("\tid:" + id);
				final String id2 = PlatformHelper.toCurrentPath(id);

				if (!TransferRes.copyRes(id2, null)) {
					System.err.println("\t id :" + id + " not found!");
					errorList.add(plist);
					continueFlag = true;
					continue;
				}
			}

			if (continueFlag) {
				continue;
			}

			// 2. texture packer
			final SetTexturePackerPanelRet ret = new SetTexturePackerPanelRet();
			ret.name = plist.substring(plist.lastIndexOf("/") + 1);
			ret.autoRemove = true;
			ret.scale = scale;
			ret.single = true;
			final String pvrPath = source + FileHelper.DECOLLATOR + plistToPvr.get(plist);

			try {
				final PVRTextureFormat format = readPVRFormat(pvrPath);
				switch (format) {
				case RGB_565:
					ret.ditherFormat = DitherFormat.Fs;
					ret.imageFormat = ImageFormat.RGB565;
					break;
				case RGBA_4444:
					ret.ditherFormat = DitherFormat.FsAlpha;
					ret.imageFormat = ImageFormat.RGBA4444;
					break;
				case RGBA_8888:
					ret.ditherFormat = DitherFormat.Fs;
					ret.imageFormat = ImageFormat.RGBA8888;
					break;
				default:
					System.err.println("Error PVR Not Support Format :" + format);
					System.err.println("Error PVR path :" + pvrPath);

					errorList.add(plist);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error PVR:" + pvrPath);
				errorList.add(plist);
				continue;
			}

//			final String newDestinationDir = newDestination.substring(0, newDestination.lastIndexOf("/"));
//			if (MainNodeMenu.texturepacker(ret, idSet, false, true, null, newDestinationDir)) {
//				errorList.add(plist);
//				continue;
//			}
		}

		// error plist
		if (!errorList.isEmpty()) {
			PowerMan.runOnUI(new Runnable() {
				public void run() {
					final CollectDialog errorDialog = new CollectDialog("Error Plist", false);
					errorDialog.open(errorList);
				}
			});
		}

		// copy everything but .plist, .pvr,
		if (listener != null) {
			listener.onProgress("coping files except *.plist, *.pvr", "", 20, 100);
		}
		FileHelper.copyDeepBut(source, destination, new String[] { "plist", "pvr", "ccz", "pvr.ccz", ".PNG" }, new IFileMatch() {
			public boolean isFileMatch(File file) {
				final String name = file.getName();
				if (name.equals(".nvcd") || name.equals(".svn") || name.equals(".cocos")) {
					return false;
				}
				return true;
			}
		});

		// create unzip lua
		if (listener != null) {
			listener.onProgress("creating unzip-lua file ", "", 60, 100);
		}
		createLua(destination + "/", destination + "/GameRes/script/platform/android/unzipPaths.lua");

		if (listener != null) {
			listener.onFinished();
		}
	}

	// ".plist", ".pvr.ccz"
	static final String[] sUnzipSubs = { ".cocos.zip", ".lua", "nvc", };

	// GameRes/nova
	public static void createLua(final String assetsPath, final String luaPath) {
		final ArrayList<String> pathList = getUnzipPaht(new File(assetsPath));

		final ArrayList<String> unZipSrc = new ArrayList<String>();
		final ArrayList<String> unZipDst = new ArrayList<String>();

		for (String path : pathList) {
			String src = path.substring(assetsPath.length());
			src = src.replace(".nvc", "nvc");
			String dst = src.replace("nvc", ".nvc");

			unZipSrc.add(src);
			unZipDst.add(dst);
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(luaPath));

			writer.write("\n");

			writer.write("local function getUnzipPaths()");
			writer.write("\n");

			writer.write("\tlocal _table = {}");
			writer.write("\n");

			final int size = unZipSrc.size();
			for (int i = 0; i < size; i++) {
				writer.write(String.format("\t_table[%d] = {src = '%s',\tdst = '%s'}\n", (i + 1), unZipSrc.get(i), unZipDst.get(i)));
			}
			writer.write("\n");
			writer.write(String.format("\t_table.size = %d\n", size));
			writer.write("\n");
			writer.write("\treturn _table\nend");
			writer.write("\n");

			writer.write("\n");
			writer.write("return getUnzipPaths()");

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<String> getUnzipPaht(final File dir) {
		final ArrayList<String> ret = new ArrayList<String>();

		if (dir.isDirectory()) {
			final File[] files = dir.listFiles();
			for (File f : files) {
				ret.addAll(getUnzipPaht(f));
			}
		} else {
			if (dir.isFile()) {
				final String path = dir.getAbsolutePath().replace("\\", "/");
				// src = src.replace("\\", "/");
				for (String sub : sUnzipSubs) {
					if (path.endsWith(sub)) {
						ret.add(path);
						break;
					}
				}
			}
		}
		return ret;
	}

}
