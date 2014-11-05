package com.ielfgame.stupidGame.packer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.batch.ElfBatch;
import com.ielfgame.stupidGame.batch.ElfBatch.BatchInput;
import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.dialog.CollectDialog;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.enumTypes.BatchOp;
import com.ielfgame.stupidGame.fileBar.CopyPanel;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

public class PackHelper {
	//
	public static void pack(final String dir) {
		// search-plist_ids
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();

		final TpPlistScaner pScaner = new TpPlistScaner(dir);
		final LinkedList<String> warnings = pScaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);
		// has repeat ?
		if (!warnings.isEmpty()) {
			final CollectDialog warnDialog = new CollectDialog("Plist-Warnings", false);
			warnDialog.open(warnings);
		}

		// 2.search xml_ids
		final LinkedList<String> paths = FileHelper.getFullPahIds(dir, new String[] { ".xml", ".cocos" }, true);
		final Set<String> allSet = new HashSet<String>();
		for (String xml : paths) { 
			final Set<String> set0 = XMLVersionManage.getAllResids(xml);
			final Set<String> set1 = new HashSet<String>();
			for(String p : set0) {
				if(new File(p).exists()) {
					set1.add(p);
				} else { 
					final String real = p.substring(p.lastIndexOf("#")+1);
					if(idToPlistMap.containsKey(real)) {
						//found in plist
						set1.add("No Worries!"+p+" is in plist");
					} else { 
						set1.add("check "+p+" in "+xml+"!");
					} 
				} 
			}
			allSet.addAll(set1);
		}
		
		final ArrayList<String> allArray = new ArrayList<String>(allSet);
		Collections.sort(allArray);
		final CollectDialog allDialog = new CollectDialog("All Resids", false);
		allDialog.open(allArray);

		final Set<String> allCompressidSet = new HashSet<String>();
		final Set<String> toBeRemove = new HashSet<String>();
		final HashMap<String, String> idToPlistMap2 = new HashMap<String, String>(idToPlistMap);

		for (String id : allSet) {
			final String compressId = TransferRes.exportCompressPath(id, true);
			if (idToPlistMap.containsKey(compressId)) {
				toBeRemove.add(id);
				idToPlistMap2.remove(compressId);
			} else {
				allCompressidSet.add(compressId);
			}
		}
		allSet.removeAll(toBeRemove);

		if (!idToPlistMap2.isEmpty()) {
			final CollectDialog warnDialog = new CollectDialog("Plist-UnusedResids", false);
			final ArrayList<String> unuseds = new ArrayList<String>();
			for (String key : idToPlistMap2.keySet()) {
				final String text = idToPlistMap2.get(key) + "->" + key;
				unuseds.add(text);
			} 
			Collections.sort(unuseds);
			warnDialog.open(unuseds);
		} else { 
			MessageDialog scanDialog = new MessageDialog();
			if (!scanDialog.open("", "Plist scan completed , no warnings found!")) {
				return;
			}
		}
		
		MessageDialog continueDialog = new MessageDialog();
		if (!continueDialog.open("", "continue to delete old transfered-pngs ?")) {
			return;
		}
		// unused
		final LinkedList<String> toBeRemovedPngs = new LinkedList<String>();
		final LinkedList<String> allPngs = FileHelper.getFullPahIds(dir, new String[] { ".png", ".jpg" }, true);
		for (String png : allPngs) {
			final String simplePng = FileHelper.getSimpleNameNoSuffix(png);
			if (!allCompressidSet.contains(simplePng) || !idToPlistMap.containsKey(simplePng)) {
				toBeRemovedPngs.add(png);
			}
		}
		for (String png : toBeRemovedPngs) {
			FileHelper.removeFile(png);
		}

		{
			MessageDialog scanDialog = new MessageDialog();
			if (!scanDialog.open("", "Deleting unused transfered-pngs completed , no warnings found!")) {
				return;
			}
		}

		continueDialog = new MessageDialog();
		if (!continueDialog.open("", "continue to transfer unPlistResids?")) {
			return;
		}
		
		// transfer-UnPlistIds
		CopyPanel copyPanel = new CopyPanel();
		final LinkedList<String> notfoundList = copyPanel.open("Transfer-UnPlistResids", allSet);

		if (!notfoundList.isEmpty()) {
			final CollectDialog unfoundDialog = new CollectDialog("Unfound-Resids", false);
			unfoundDialog.open(notfoundList);
		} else {
			MessageDialog scanDialog = new MessageDialog();
			if (!scanDialog.open("", "Transfer-UnPlistResids completed , no warnings found!")) {
				return;
			}
		}

		continueDialog = new MessageDialog();
		if (!continueDialog.open("", "continue to do TP_PLIST ?")) {
			return;
		}

		final BatchInput batchInput = new BatchInput();
		batchInput.source = dir;
		batchInput.batchOp = BatchOp.COCOS_PLIST;
		ElfBatch.batch(batchInput);
	}

	public static void main(String[] args) {
		final String dir = FileHelper.getUserDir();
		final File f = new File(dir);
		final File[] fs = f.listFiles();
		for (File file : fs) {
			System.err.println(file.getAbsolutePath() + " dir:" + file.isDirectory() + " file:" + file.isFile());
		}
	}
}
