package com.ielfgame.stupidGame.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.dialog.CollectDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.enumTypes.BatchOp;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.fileBar.CopyPanel;
import com.ielfgame.stupidGame.imExport.ImExport;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.packer.PackHelper;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLConfig;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

public class ElfBatch {

	public static class BatchInput extends ElfDataDisplay {
		// 1. source
		// 2. element label
		// 3. Attribute Label
		// 4. value1
		// 5. value2
		public BatchOp batchOp = BatchOp.COCOS_PLIST;
//		public String source = PowerMan.getSingleton(DataModel.class).getTransferDir();
		public String source = null;
		public String [] key; 
		
		public String toString() { 
			return String.format("Op:%s, Source:%s, key:%s", batchOp.toString(), source, Stringified.toText(key, false));
		} 
	}

	public static void importBatchs() {
		//
	} 

	public static void exportBatchs() {
		//
	}

	public static Menu createToolMenu(final Shell mShell, final Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(LanguageManager.get("Tools"));
		header.setMenu(menu);
		MenuItem item = null;

		item = new MenuItem(menu, SWT.PUSH);
		item.setText(LanguageManager.get("Batch"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					final MultiLineDialog dialog = new MultiLineDialog();

					dialog.setTitle(sBatchInput.toTitle());
					dialog.setLabels(sBatchInput.toLabels());
					dialog.setValues(sBatchInput.toValues());
					dialog.setValueTypes(sBatchInput.toTypes());

					final Object[] ret = dialog.open();
					if (ret != null) {
						MessageBox box = new MessageBox(mShell, SWT.ICON_INFORMATION | SWT.OK);
						box.setText("Batch");
						int flag = sBatchInput.setValues(ret);
						if (flag == -1) {
							final String message = batch(sBatchInput);
							box.setMessage(message);
						} else {
							box.setMessage("Batch failed!");
						}
						box.open();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		return menu;
	}

	protected final static BatchInput sBatchInput = new BatchInput();
	protected final LinkedList<BatchInput> sBatchList = new LinkedList<ElfBatch.BatchInput>();

	static boolean batchTransRes(final BatchInput input) {
		final String[] paths = FileHelper.getFullPaths(input.source);
		final LinkedList<String> notfoundListRet = new LinkedList<String>();
		
		for (String path : paths) {
			if (ResJudge.isXML(path) || ResJudge.isCocos(path)) { 
				final LinkedList<String> notfoundList = new CopyPanel().open(path, XMLVersionManage.getAllResids(path));
				if (!notfoundList.isEmpty()) {
					notfoundListRet.add(path+" not found resids below:");
					notfoundListRet.addAll(notfoundList);
				} 
			} 
		} 
		
		final String[] notFoundArray = new String[notfoundListRet.size()]; 
		notfoundListRet.toArray(notFoundArray); 
		final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>(" Not Existed Resids(" + notFoundArray.length + ")", false);
		dialog.open(notFoundArray, String[].class); 
		
		return true;
	}
	
	static boolean batchImportExport(final BatchInput input) {
		final String[] paths = FileHelper.getFullPaths(input.source);
		for (String path : paths) {
			if (path.endsWith(".xml")) { 
				final List<Object> objs = ImExport.readObjs(path);
				for(Object obj : objs) {
					if(obj instanceof CCActionData) {
						((CCActionData) obj).setIsRoot(true);
					} 
				}
				ImExport.writeObjs(objs, path);
			} 
		}

		return true;
	}

	static boolean batchTPPlist(final BatchInput input) {
		final String source = input.source;
		final TpPlistScaner plistScaner = new TpPlistScaner(source);
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();

		plistScaner.scanPlist(plistToIdMap, idToPlistMap, null, true);
		
		final LinkedList<String> paths = FileHelper.getFullPahIds(input.source, new String[]{".cocos"}, true);
		
		for (String path : paths) { 
			if (ResJudge.isCocos(path)) { 
				try {
					SAXReader saxReader = new SAXReader();
					Document document = null;
					document = saxReader.read(new File(path));
					final boolean[] modifiered = new boolean[] { false };

					final Document documentSave = document;

					document.accept(new VisitorSupport() { 
						public void visit(final Element element) {
							final int count = element.attributeCount();
							Attribute attr = null;
							for (int i = 0; i < count; i++) {
								attr = element.attribute(i);
								if (attr != null) { 
									final String attrName = attr.getName();
									final String attrValue = attr.getValue();

									if (attrName.contains("Resid") || attrName.contains("Pic")) {
										// ResidArray
										if (attrName.contains("Array")) {
											String readText = attrValue;
											readText = XMLConfig.fromXML(readText);
											final Object[] fromTextRet = Stringified.fromText(String[].class, readText);
											final String[] resids = (String[]) fromTextRet[0];
											for (int k = 0; k < resids.length; k++) {
												final String key = resids[k].substring( resids[k].lastIndexOf("#") +1);
												//
												
												final String plist = idToPlistMap.get(key);
												if (plist != null) {
													modifiered[0] = true;
													resids[k] = plist + '#' + key;
												}
											}
											
											readText = Stringified.toText(resids, false);
											readText = XMLConfig.toXML(readText);

											if (modifiered[0] && attrValue != null && !attrValue.equals(readText)) {
												attr.setValue(readText);
											}
										} else {
											String readText = attrValue;
											readText = XMLConfig.fromXML(readText);
											final Object[] fromTextRet = Stringified.fromText(String.class, readText);
											String resid = (String) fromTextRet[0];
											
											final String key = resid.substring( resid.lastIndexOf("#") +1);
											
											final String plist = idToPlistMap.get(key);
											if (plist != null) {
												modifiered[0] = true;
												resid = plist + '#' + key;
											}

											readText = Stringified.toText(resid, false);
											readText = XMLConfig.toXML(readText);

											if (modifiered[0] && attrValue != null && !attrValue.equals(readText)) {
												attr.setValue(readText);
											}
										}
									}
								}
							}
						}
					});

					if (modifiered[0]) {
						OutputFormat format = OutputFormat.createPrettyPrint();
						XMLWriter writer;
						try {
							writer = new XMLWriter(new FileOutputStream(path), format);
							writer.write(documentSave);
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.err.println("new "+path);
					} else {
						System.err.println("no change "+path);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	static boolean batchRemoveInPlist(final BatchInput input) {
		final String source = input.source;
		final TpPlistScaner plistScaner = new TpPlistScaner(source); 
		
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();

		LinkedList<String> warnings = plistScaner.scanPlist(plistToIdMap, idToPlistMap, null, false);
		
		if(!warnings.isEmpty()) { 
			//重复
			final String[] warnArray = new String[warnings.size()]; 
			warnings.toArray(warnArray); 
			final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>(" Warnings(" + warnArray.length + ")", false);
			dialog.open(warnArray, String[].class); 
		} else { 
			final String[] fullPaths = FileHelper.getFullPaths(input.source);
			final Set<String> totalSet = new HashSet<String>();
			for (String path : fullPaths) {
				if (ResJudge.isXML(path) || ResJudge.isCocos(path)) { 
					final Set<String> set = XMLVersionManage.getAllResids(path); 
					totalSet.addAll(set); 
				} 
			} 
			final String [] totalResids = new String[totalSet.size()];
			totalSet.toArray(totalResids);
			final AnalysisDialog<String[]> totalDialog = new AnalysisDialog<String[]>("Total Resid", false);
			totalDialog.open(totalResids, String[].class);
			
			//in plist
			final String [] paths = FileHelper.getSimplePaths(source); 
			for(String png : paths) { 
				if(ResJudge.isRes(png)) { 
					if( idToPlistMap.remove(png) != null) {
						final File f = new File(source + FileHelper.DECOLLATOR + png); 
						if(f.exists()) { 
							if(!f.delete()) { 
								System.err.println("plist : delete "+f.getAbsolutePath() + " failed!");
							} else {
								System.err.println("plist : delete "+f.getAbsolutePath() + " !");
							}
						} 
					}
					
					final String fullResid = PlatformHelper.toCurrentPath(png);
					if(!totalSet.contains(fullResid)) {
						final File f = new File(source + FileHelper.DECOLLATOR + png); 
						if(f.exists()) { 
							if(!f.delete()) { 
								System.err.println("unused : delete "+f.getAbsolutePath() + " failed!");
							} else {
								System.err.println("unused : delete "+f.getAbsolutePath() + " !");
							}
						} 
					}
				} 
			} 
			
			if(!idToPlistMap.isEmpty()) {
				//unused
				final LinkedList<String> unusedList = new LinkedList<String>();
				final Set<String> keySet = idToPlistMap.keySet();
				for(String key : keySet) { 
					unusedList.add(String.format("%s -> %s", idToPlistMap.get(key), key));
				} 
				Collections.sort(unusedList);
				
				final String[] unused = new String[unusedList.size()]; 
				unusedList.toArray(unused); 
				final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("Unused", true);
				dialog.open(unused, String[].class);
				
//				if( dialog.open(unused, String[].class) != null) { 
//					//remove
//					plistToIdMap.clear(); 
//					idToPlistMap.clear(); 
//					plistScaner.scan(plistToIdMap, idToPlistMap, null, true); 
//					MessageDialog message = new MessageDialog(); 
//					message.open("", "Has deleted images existed in plists!"); 
//				} 
			} 
		} 
		
		return true;
	}
	
	static boolean batchSearchResid(final BatchInput input) {
		if(input.key == null || input.key.length == 0) {
			System.err.println("Input a valid key!");
			return false;
		} 
		
		final LinkedList<String> paths = FileHelper.getFullPahIds(input.source, new String[]{".xml",".cocos"}, true);
		final LinkedList<String> xmlList = new LinkedList<String>();
		for (String path : paths) {
			final Set<String> set = XMLVersionManage.getAllResids(path); 
			
			final Set<String> newSet = new HashSet<String>();
			for(String id : set) {
				newSet.add(TransferRes.exportCompressPath(id, true));
			}
			
			for(String k : input.key) { 
				if(set.contains(k)) { 
					xmlList.add(path + " contains " + k); 
				} else { 
					System.err.println(path + " not contains " + k);
				}
			} 
			
			for(String k : input.key) { 
				if(newSet.contains(k)) { 
					xmlList.add(path + " contains " + k); 
				} else { 
					System.err.println(path + " not contains " + k);
				}
			} 
		} 
		
		final CollectDialog dialog = new CollectDialog("Found XMLs", false);
		dialog.open(xmlList);
		
		return true;
	}
	
	public static boolean batchPackHelper(final BatchInput input) {
		PackHelper.pack(input.source);
		return true;
	}
	
	public static boolean batchLuaPlist(final BatchInput input) {
		
		final String source = input.source;
		final TpPlistScaner plistScaner = new TpPlistScaner(source);
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();

		plistScaner.scanPlist(plistToIdMap, idToPlistMap, null, true);
		
		final LinkedList<String> luaPaths = FileHelper.getFullPahIds(input.source, new String[]{".lua"}, true);
		
		for(String path : luaPaths) {
			boolean modifiered = false; 
			try { 
				final LinkedList<String> lines = new LinkedList<String>();
				
				final BufferedReader reader = FileHelper.getReader(path);
				String line = null;
				final Set<String> keySet = idToPlistMap.keySet(); 
				
				while((line = reader.readLine()) != null) { 
					final String [] splits = line.split("\"",-1);
					
					if(splits != null && splits.length > 0) {
						for(int i=1; i<splits.length; i+=2) { 
							String pngPath = splits[i].substring(splits[i].lastIndexOf("#")+1); 
							
							for(String key : keySet) { 
								if(pngPath.equals(key)) { 
									final String plist = idToPlistMap.get(key); 
									if (plist != null) { 
										modifiered = true; 
										splits[i] = plist + '#' + key;
										break; 
									} 
								} 
							} 
						}
						
						line = splits[0]; 
						for(int i=1; i<splits.length; i++) { 
							if(i %2 == 1) { 
								line += "\""+splits[i]+"\"";
							} else { 
								line += splits[i];
							} 
						} 
						
						lines.add(line); 
					} 
				}
				
				reader.close();
				
				if(modifiered) {
					final BufferedWriter writer = FileHelper.getWriter(path);
					for(String l : lines) {
						writer.write(l+"\n");
					} 
					writer.flush();
					writer.close();
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
		
		return true;
	}
	
	public static String batch(final BatchInput input) {
		if (input == null) {
			return "None batchs!";
		} 
		switch (input.batchOp) {
		case ImportExport:
			batchImportExport(input);
			break;
		case COCOS_PLIST:
			batchTPPlist(input);
			break;
		case TRANS_RES:
			batchTransRes(input);
			break;
		case REMOVE_RES_IN_PLIST:
			batchRemoveInPlist(input);
			break;
		case SEARCH_RESID:
			batchSearchResid(input); 
			break;
		case PACK_HELPER:
			batchPackHelper(input); 
			break;
		case LUA_PLIST:
			batchLuaPlist(input);
			break;
		} 
		return String.format("Batch %s completetd!", input.toString()); 
	} 

	final static LinkedList<String> sResidAttrs = new LinkedList<String>();
	final static LinkedList<String> sResidArrayAttrs = new LinkedList<String>();
}
