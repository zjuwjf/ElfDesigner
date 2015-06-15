package com.ielfgame.stupidGame.res;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.res.TemplateCreator.ITemplateDealer;
import com.ielfgame.stupidGame.res.TemplateCreator.TemplateStruct;
import com.ielfgame.stupidGame.utils.FileHelper;

public class TFileCreator {

	public static void createLuaT(final String ouputDir) {
		createByModelCpp(ouputDir, "T_lua", "T.lua");
	}

	private static void createByModelCpp(final String ouputDir, final String model, final String filename) {
		final String resAsset = ResManager.getSingleton().getDesignerRootAsset();
		final String imgAsset = ResManager.getSingleton().getDesignerImageAsset();

		final TpPlistScaner scaner = new TpPlistScaner(resAsset, imgAsset);
		
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();

		final HashMap<String, String> name2pathmap = ResManager.getName2PathMap();

		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);

		final LinkedList<String> keys = new LinkedList<String>(name2pathmap.keySet());
		Collections.sort(keys);

		final LinkedList<String> plistkeys = new LinkedList<String>(idToPlistMap.keySet());
		Collections.sort(plistkeys);

		try {
			final String th = model;
			final InputStream hstream = TFileCreator.class.getResourceAsStream(th);
			final LinkedList<String> hList = FileHelper.read(hstream);

			TemplateCreator.create(new File(new File(ouputDir), filename).getAbsolutePath(), hList, new ITemplateDealer() {
				public String replaceKey(String key) {
					if (key.equals("<XML_DATE>")) {
//						return new Date().toString() + " By " + FileHelper.getUserName();
						return "";
					}
					return key;
				}

				public String dealWithTemplate(TemplateStruct temlate) {
					final StringBuilder sb = new StringBuilder();
					
					if (temlate.name.equals("fieldsDefine")) {
						for (final String key : keys) {
							for (final String line : temlate.lines) {
								final String realLine = line.replace("<RES_ID>", key).replace("<RES_ID_NAME>", getResidVarName(key));
								sb.append(realLine);
							}
							sb.append("\n");
						}
					} else if (temlate.name.equals("fieldsInit")) {

						for (final String key : plistkeys) {
							for (final String line : temlate.lines) {
								final String plistPath = (idToPlistMap.get(key) + ".plist").replace("\\", "/");
								final String realLine = line.replace("<RES_ID>", key).replace("<RES_PLIST_ID>", plistPath);
								sb.append(realLine);
							}
							sb.append("\n");
						}
					}

					return sb.toString();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createByModelH(final String ouputDir, final String model, final String filename) {
		final String resAsset = ResManager.getSingleton().getDesignerRootAsset();
		final String imgAsset = ResManager.getSingleton().getDesignerImageAsset();
		
		final TpPlistScaner scaner = new TpPlistScaner(resAsset, imgAsset);

		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();

		final HashMap<String, String> name2pathmap = ResManager.getName2PathMap();

		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);

		final LinkedList<String> keys = new LinkedList<String>(name2pathmap.keySet());
		Collections.sort(keys);

		final LinkedList<String> plistkeys = new LinkedList<String>(idToPlistMap.keySet());
		Collections.sort(plistkeys);

		try {
			final String th = model;
			final InputStream hstream = TFileCreator.class.getResourceAsStream(th);
			final LinkedList<String> hList = FileHelper.read(hstream);

			TemplateCreator.create(new File(new File(ouputDir), filename).getAbsolutePath(), hList, new ITemplateDealer() {
				public String replaceKey(String key) {
					if (key.equals("<XML_DATE>")) {
//						return new Date().toString() + " By " + FileHelper.getUserName();
						return "";
					}
					return key;
				}
				
				public String dealWithTemplate(TemplateStruct temlate) {

					final StringBuilder sb = new StringBuilder();

					for (final String key : keys) {
						for (final String line : temlate.lines) {
							sb.append(line.replace("<RES_ID_NAME>", getResidVarName(key)));
						}
						sb.append("\n");
					}

					return sb.toString();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createCPlusPlusT(final String ouputDir) {
		createByModelH(ouputDir, "T_h", "T.h");
		createByModelCpp(ouputDir, "T_cpp", "T.cpp");
	}

	private static String getResidVarName(String string) {
		return FileHelper.getSimpleNameNoSuffix(string);
	}
}
