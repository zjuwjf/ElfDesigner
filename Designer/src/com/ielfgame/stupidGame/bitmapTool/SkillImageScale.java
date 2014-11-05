package com.ielfgame.stupidGame.bitmapTool;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;

import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.data.Stringified.ErrorStruct;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLConfig;
import com.ielfgame.stupidGame.xml.XMLManifest;

public class SkillImageScale {

	public static void main(final String[] args) {
		
		final String inputXmlPath = "C:\\Users\\zju_wjf\\Desktop\\xml\\SkillAnimate.xml";
		final String outputXmlPath = "C:\\Users\\zju_wjf\\Desktop\\xml\\SkillAnimate-new.xml";
		
		final Element element = XMLManifest.readManifest(inputXmlPath);
		
		final HashMap<String, Float> scaleMap = new HashMap<String, Float>();
		final HashMap<String, HashSet<String>> folderSet = new HashMap<String, HashSet<String>>();
		
		element.accept(new VisitorSupport() {
			public void visit(final Element e) {
				final String label = e.getName();
				if (label.equals("SimpleAnimateNode")) {
					// check
					String text = e.attributeValue("ResidArray");
					// Escape Sequence
					text = XMLConfig.fromXML(text);
					
					Object[] ret = Stringified.fromText(String[].class, text);
					final String[] value = (String[]) ret[0];
					final ErrorStruct errorRet = (ErrorStruct) ret[1];
					if (errorRet == null && value != null) {
						// String folder = e.attributeValue("ResidFolder");
						String folder = FileHelper.getDirPath(value[0]);

						final String strScale = XMLConfig.fromXML(e.attributeValue("Scale"));
						ret = Stringified.fromText(ElfPointf.class, strScale);
						ElfPointf scalePoint = (ElfPointf) ret[0];

						float scale = Math.abs(scalePoint.x);
						
						if(!folderSet.containsKey(folder)) {
							folderSet.put(folder, new HashSet<String>());
						} 
						final HashSet<String> hashset = folderSet.get(folder);
						for(int i=0; i<value.length; i++) { 
							hashset.add(value[i]);
						} 

						if (scaleMap.containsKey(folder)) {
							final float oldScale = scaleMap.get(folder);
							if (scale > oldScale && scale <= 1) {
								// System.err.println(e.attributeValue("FullName")
								// + " scale " + strScale);
								scaleMap.put(folder, scale);
							}
						} else {
							if (scale > 1) {
								// System.err.println(">1  "+e.attributeValue("FullName")
								// + " scale " + strScale);
								scaleMap.put(folder, 1f);
							} else {
								// System.err.println("first"
								// +e.attributeValue("FullName") + " scale " +
								// strScale);
								scaleMap.put(folder, scale);
							}
						}
					}
				}
			}
		});

		//

		//
		// TransferRes.exportCompressPath(id, true);

		// TexturePackerHelper.pack(outName, inputs, 1, "RGBA8888", 1);
//		new DataModel();
//		XMLFactory.checks();
//		ConfigImExport.importConfigs();
//		final Set<String> set = folderSet.keySet();
//		for (String dir : set) {
//			final Set<String> usedIds = folderSet.get(dir);
//			final ArrayList<String> array = new ArrayList<String>();
//			for(String id : usedIds) { 
//				array.add(TransferRes.exportCompressPath(id, true));
//			} 
//			TexturePackerHelper.pack(TransferRes.exportCompressPath(dir, false), array, array.size(), "RGBA8888", 0.5f);
//		}
		
		element.accept(new VisitorSupport() {
			public void visit(final Element e) {
				final String label = e.getName();
				if (label.equals("SimpleAnimateNode")) {
					// check
					String text = e.attributeValue("ResidArray");
					// Escape Sequence
					text = XMLConfig.fromXML(text);

					Object[] ret = Stringified.fromText(String[].class, text);
					final String[] value = (String[]) ret[0];
					final ErrorStruct errorRet = (ErrorStruct) ret[1];
					if (errorRet == null && value != null) {
						// String folder = e.attributeValue("ResidFolder");
						String folder = FileHelper.getDirPath(value[0]);
						
						final String strScale = XMLConfig.fromXML(e.attributeValue("Scale"));
						ret = Stringified.fromText(ElfPointf.class, strScale);
						ElfPointf scalePoint = (ElfPointf) ret[0];

						float standardScale = scaleMap.get(folder);

						scalePoint.x /= standardScale;
						scalePoint.y /= standardScale;
						
						scalePoint.x *= 2;
						scalePoint.y *= 2;

						e.attribute("Scale").setValue(XMLConfig.toXML(Stringified.toText(scalePoint, false)));
					}
				}
			}
		});

		// final Set<String> set = scaleMap.keySet();
		// for(String dir : set) {
		// final String [] paths = FileHelper.getFullPaths(dir);
		// final float scale = scaleMap.get(dir);
		// for(String path : paths) {
		// try {
		// final ImageData data = new ImageData(new FileInputStream(path));
		// final ImageData dataSave =
		// data.scaledTo(Math.round(data.width*scale),
		// Math.round(data.height*scale));
		//
		// final ImageLoader loader = new ImageLoader();
		// loader.data = new ImageData[]{dataSave};
		//
		// loader.save(path, SWT.IMAGE_PNG);
		// System.err.println("Save " + path);
		// } catch (Exception e) {
		// System.err.println("ERROR " + path);
		// e.printStackTrace();
		// }
		// }
		// }

		XMLManifest.writeDocument(element.getDocument(), outputXmlPath);

		if (true) {
//			return;
		}

		element.accept(new VisitorSupport() {
			public void visit(final Element e) {
				final String label = e.getName();
				if (label.equals("SimpleAnimateNode")) {
					final String name = e.attributeValue("Name");
					final String parentlabel = e.getParent().getName();
					if (parentlabel.equals("SimpleAnimateNode")) {
						String text = e.attributeValue("ResidArray");
						// Escape Sequence
						text = XMLConfig.fromXML(text);

						final Object[] ret = Stringified.fromText(String[].class, text);
						final String[] value = (String[]) ret[0];
						final ErrorStruct errorRet = (ErrorStruct) ret[1];
						if (errorRet == null && value != null) {
							final String scaleText = e.attributeValue("Scale");
							final Object[] scaleRet = Stringified.fromText(ElfPointf.class, scaleText);
							final ElfPointf scale = (ElfPointf) scaleRet[0];
							if (scale.x != 1 || scale.y != 1) {
								try {
									final String parentName = e.getParent().attributeValue("Name");
									final String selfName = name.substring(2);

									String dist;
									if (selfName == null || selfName.length() == 0) {
										dist = "D:\\pic\\gyx\\����\\" + parentName + "\\";
										final File dir = new File(dist);
										if (!dir.exists()) {
											dir.mkdir();
										}
									} else {
										dist = "D:\\pic\\gyx\\����\\" + parentName + "\\";
										File dir = new File(dist);
										if (!dir.exists()) {
											dir.mkdir();
										}

										dist = "D:\\pic\\gyx\\����\\" + parentName + "\\����" + selfName + "\\";

										dir = new File(dist);
										if (!dir.exists()) {
											dir.mkdir();
										}
									}

									float setScale = 1;

									for (int i = 0; i < value.length; i++) {
										final ImageData data = new ImageData(new FileInputStream(value[i]));
										final ImageData dataSave = data.scaledTo(Math.round(data.width * scale.x * setScale), Math.round(data.height * scale.y * setScale));

										final ImageLoader loader = new ImageLoader();
										loader.data = new ImageData[] { dataSave };

										final String output;
										if ((i + 1) < 10) {
											output = dist + "0" + (i + 1) + ".png";
										} else {
											output = dist + (i + 1) + ".png";
										}

										loader.save(output, SWT.IMAGE_PNG);
										System.err.println("Save " + value[i]);

										value[i] = output;
									}

									e.attribute("ResidFolder").setValue(dist);
									String out = Stringified.toText(value, false);
									out = XMLConfig.toXML(out);
									e.attribute("ResidArray").setValue(out);
									e.attribute("Resid").setValue("null");
									e.attribute("Scale").setValue("" + 1 / setScale + ", " + 1 / setScale + "");

								} catch (Exception exc) {
									exc.printStackTrace();
								}
							}
						}
					}
				}
			}
		});

		XMLManifest.writeDocument(element.getDocument(), outputXmlPath);
	}

}
