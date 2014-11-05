package com.ielfgame.stupidGame.lua;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import com.ielfgame.stupidGame.config.LuaTempleConfig;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLManifest;

public class ConvertXMLToLua implements ConvertXMLToLuaConstants {

	private final static HashSet<String> sIgnoredList = new HashSet<String>();
	static {
		sIgnoredList.add("KeyStorageNode");
		sIgnoredList.add("KeyFrameArrayNode");
		sIgnoredList.add("KeyFrameNode");

		sIgnoredList.add("KeyFramerNode");
	}

	private static boolean isIgnored(final String type) {
		return sIgnoredList.contains(type);
	}

	static String path1 = "C:\\Users\\zju_wjf\\Desktop\\HeroList.xml";
	static String path2 = "C:\\Users\\zju_wjf\\Desktop\\HeroList.lua";

	static final String USER_DIR = System.getProperty("user.dir");

	final static ArrayList<Union> sAllFullNames = new ArrayList<Union>();
	final static ArrayList<Union> sCommentAllFullNames = new ArrayList<Union>();

	final static LinkedHashMap<String, LinkedHashSet<Union>> sAllTables = new LinkedHashMap<String, LinkedHashSet<Union>>();

	final static ArrayList<String> sListItems = new ArrayList<String>();

	private static String XML_SIMPLE_NAME;

	private static int sReleaseTableCallNum = -1;
	private static int sReleaseCallNum = -1;

	public static class Union {
		public final String name;
		public final String type;
		public boolean isTable;

		public Union(String name, String type) {
			super();
			this.name = name;
			this.type = type;
		}
	}

	public static ArrayList<ArrayList<Union>> convertToJava(final String xmlPath) {
		final ArrayList<Union> allFullNames = new ArrayList<Union>();
		final ArrayList<Union> allCommentFullNames = new ArrayList<Union>();

		Element doc = XMLManifest.readManifest(xmlPath);

		if (doc != null) {
			doc.accept(new VisitorSupport() {
				public void visit(Element arg0) {
					final String typeName = arg0.getName();

					if (typeName.endsWith("Node")) {
						final String fullName = arg0.attributeValue(FULL_NAME);

						if (fullName.contains("@") || fullName.contains("##")) {
							if (!fullName.substring(2).contains("#")) {
								allCommentFullNames.add(new Union(fullName, typeName));
							}
						} else if (fullName != null && !fullName.contains("#") && !fullName.contains("@")) {
							allFullNames.add(new Union(fullName, typeName));
						}

					} else if (typeName.equals("CCActionData")) {
						final String actionName = arg0.attributeValue("name");
						final boolean isRoot = Boolean.valueOf(arg0.attributeValue("IsRoot"));
						if (isRoot && actionName != null && !actionName.contains("#") && !actionName.contains("@")) {
							allFullNames.add(new Union(actionName, "ElfAction"));
						}
					}
				}
			});
		}

		final ArrayList<ArrayList<Union>> ret = new ArrayList<ArrayList<Union>>();
		ret.add(allFullNames);
		ret.add(allCommentFullNames);
		return ret;
	}

	private final static String TEMPLE_DIR = USER_DIR + FileHelper.DECOLLATOR + "template/";

	public enum LuaTempleType {
		GleeCPlusPlusTemple(new String[] { "View_%s.h", "View_%s.cpp" }, new String[] { TEMPLE_DIR + "make_glee_h", TEMPLE_DIR + "make_glee_cpp" }),
		// LuaMenu(new String[] { "%s.lua" }, new String[] { TEMPLE_DIR +
		// "make_menu_lua" }),
		// LuaScene(new String[] { "%s.lua" }, new String[] { TEMPLE_DIR +
		// "make_scene_lua" }),
		GleeControllerTemple(new String[] { "%s.lua" }, new String[] { TEMPLE_DIR + "make_glee_controller_lua" }), GleeLayerTemple(new String[] { "%s.lua" }, new String[] { TEMPLE_DIR + "make_glee_layer_lua" }),
		// GleeDialogTemple(new String[]{"%s.lua"}, new
		// String[]{TEMPLE_DIR+"make_glee_dialog_lua"}),
		;

		public final String[] templePaths;
		public final String[] templeNames;

		LuaTempleType(String[] names, String[] path) {
			templeNames = names;
			templePaths = path;
		}

		public boolean isForCPlusPlus() {
			return this == GleeCPlusPlusTemple;
		}
		// static final String LUA_PATH =
		// FileHelper.DECOLLATOR+"make_lua.template";
	};

	public static void convert(final String xmlPath, final String exportDir) {
		// check if existed
		sAllFullNames.clear();
		sCommentAllFullNames.clear();
		sListItems.clear();
		sAllTables.clear();

		XML_SIMPLE_NAME = "";
		sReleaseCallNum = -1;
		sReleaseTableCallNum = -1;

		int begIndex = xmlPath.lastIndexOf(FileHelper.DECOLLATOR) + 1;
		int endIndex = xmlPath.lastIndexOf(".");
		XML_SIMPLE_NAME = xmlPath.substring(begIndex, endIndex);

		Element doc = XMLManifest.readManifest(xmlPath);
		if (doc != null) {
			doc.accept(new VisitorSupport() {
				public void visit(Element arg0) {
					final String simpleName = arg0.attributeValue("Name");
					final String typeName = arg0.getName();

					if(isIgnored(typeName)) {
						//nothing
					} else if (typeName.endsWith("Node")) {
						final String fullName = arg0.attributeValue(FULL_NAME);

						if (fullName.contains("@") || fullName.contains("##")) {
							if (!fullName.substring(2).contains("#")) {
								sCommentAllFullNames.add(new Union(fullName, typeName));
							}
						} else if (simpleName.contains("&")) {
							final LinkedHashSet<Union> set = new LinkedHashSet<Union>();
							final Union u = new Union(fullName, typeName);
							u.isTable = true;
							set.add(u);
							sAllTables.put(fullName, set);
						} else if (fullName.contains("&") && !fullName.contains("#")) {
							for (String key : sAllTables.keySet()) {
								if (fullName.startsWith(key)) {
									final LinkedHashSet<Union> set = sAllTables.get(key);
									final Union u = new Union(fullName, typeName);
									u.isTable = false;
									set.add(u);
								}
							}
						} else if (fullName != null && !fullName.contains("#") && !fullName.contains("@")) {
							sAllFullNames.add(new Union(fullName, typeName));
						}
					} else if (typeName.equals("CCActionData")) {
						final String actionName = arg0.attributeValue("name");
						final boolean isRoot = Boolean.valueOf(arg0.attributeValue("IsRoot"));
						if (isRoot && actionName != null && !actionName.contains("#") && !actionName.contains("@")) {
							sAllFullNames.add(new Union(actionName, "ElfAction"));
						}
					} else {
					}
				}
			});
		}

		final LuaTempleConfig luaTempleConfig = PowerMan.getSingleton(LuaTempleConfig.class);

		if (!luaTempleConfig.DoNotShowAgain) {
			final MultiLineDialog dialog = new MultiLineDialog();
			final Object[] ret = dialog.open(luaTempleConfig);
			if (ret != null) {
				luaTempleConfig.setValues(ret);
			}
		}

		for (int i = 0; i < luaTempleConfig.LuaTemple.templePaths.length; i++) {
			final String templePath = luaTempleConfig.LuaTemple.templePaths[i];
			final String templeName = String.format(luaTempleConfig.LuaTemple.templeNames[i], XML_SIMPLE_NAME);

			LinkedList<String> templeList = null;

			final File templeFile = new File(templePath);

			if (!templeFile.exists()) {
				// System.err.println("not found:" + templePath);
				// return;
				final String simpleTemplePath = FileHelper.getSimpleName(luaTempleConfig.LuaTemple.templePaths[i]);
				try {
					final InputStream stream = ConvertXMLToLua.class.getResourceAsStream(simpleTemplePath);
					templeList = FileHelper.utf8read(stream);
				} catch (Exception e) {
					System.err.println("not found:" + simpleTemplePath);
					return;
				}

			} else {
				templeList = FileHelper.utf8Read(templeFile);
			}

			final File luaFile = new File(new File(exportDir), templeName);
			final LinkedList<String> templeOutList = FileHelper.utf8Read(luaFile);

			if (!luaFile.exists()) {
				templeOutList.clear();
				templeOutList.addAll(fromTemple(templeList));
			} else {
				// 1.find @@@@[[[[ ------ @@@@]]]]
				final int[] rangeLua = findAutoCreated(0, templeOutList);
				final int[] rangeTemple = findAutoCreated(0, templeList);

				if (rangeLua != null && rangeTemple != null) {
					final List<String> subList = templeList.subList(rangeTemple[0], rangeTemple[1] + 1);
					final List<String> fromSubTemple = fromTemple(subList);

					for (int j = rangeLua[1]; j >= rangeLua[0]; j--) {
						templeOutList.remove(j);
					}
					templeOutList.addAll(rangeLua[0], fromSubTemple);

				} else if (rangeTemple != null) {
					// lua == null
					// over
					// luaList.clear();
					// add at last
					// final List<String> subList =
					// templeList.subList(rangeTemple[0], rangeTemple[1]+1);
					// final List<String> fromSubTemple = fromTemple(subList);

					templeOutList.clear();
					templeOutList.addAll(fromTemple(templeList));

				} else if (rangeLua != null) {
					// temple == null
					// over
					templeOutList.clear();
					templeOutList.addAll(fromTemple(templeList));
				} else {
					// over
					templeOutList.clear();
					templeOutList.addAll(fromTemple(templeList));
				}
				//
			}

			final LinkedList<String> outputList = new LinkedList<String>();

			for (String longline : templeOutList) {
				// final modifier for glee .h .cpp
				final String[] lines = longline.split("\n");
				for (String line : lines) {
					if (LuaTempleType.GleeCPlusPlusTemple == luaTempleConfig.LuaTemple) {
						if (line.contains("_@") || line.contains("_##")) {
							if (line.contains("_@root") || line.contains("_##root")) {
								line = line.replace("--		", "\t");
								line = line.replace("_@root", "_root");
								line = line.replace("_##root", "_root");
							} else {
								// line = line.replace("--		", "\t//");
								line = "//" + line;
							}
						} else {
						}
					}
					outputList.add(line);
				}
			}

			// output
			FileHelper.writeUTF8(outputList, luaFile);
		}
	}

	private static int[] findAutoCreated(final int searchStart, final List<String> contexts) {
		if (contexts != null) {
			int index0 = -1, index1 = -1, index = 0, num = 0;
			for (String line : contexts) {
				if (searchStart <= index && index0 == -1 && line.contains("@@@@[[[[")) {
					index0 = index;
					final int start = line.indexOf("@@@@[[[[") + 8;
					final int end = line.indexOf(" ", start);
					try {
						num = Integer.valueOf(line.substring(start, end));
					} catch (Exception e) {
						// e.printStackTrace();
						num = 0;
					}
				}
				if (searchStart <= index && index1 == -1 && (line.contains("@@@@]]]]") || line.contains("]]]]@@@@"))) {
					index1 = index;
				}
				index++;
			}

			if (index0 != -1 && index1 != -1) {
				return new int[] { index0, index1, num };
			}
		}

		return null;
	}

	static LinkedList<String> fromTemple(final List<String> templeList) {
		final LinkedList<String> retList = new LinkedList<String>();

		String template = "";
		boolean findTemplate = false;

		for (String line : templeList) {
			if (line.contains("<template")) {
				findTemplate = true;
				template = line;
			} else if (line.contains("</template>")) {
				findTemplate = false;
				template += line;
				final String ret = dealTemplate(template);

				if (ret != null && ret.length() > 0) {
					retList.add(ret);
				}
			} else if (findTemplate) {
				template += (line + "\n");
			} else {
				final ArrayList<String> keys = getKeys(line);
				for (String key : keys) {
					line = line.replace(key, replaceKey(key));
				}
				retList.add(line);
			}
		}

		return retList;
	}

	static String dealTemplate(final String template) {
		// <FULL_NAME>
		// <TYPE>
		// <List_NAME>
		// <ITEM_NUM>
		// <ACTION_NAME>
		// <ACTION_EXPRESSION>

		// <BUTTONS>
		// <MAPS>
		// <TEXTS>
		// <ELFNODES>
		// <LISTS>
		// <LAYOUTS>
		String ret = "";

		Document document = null;
		try {
			document = DocumentHelper.parseText(template);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (document != null) {
			final Element element = document.getRootElement();
			final String name = element.attributeValue("name");
			final String[] templateIn = new String[10];

			element.accept(new VisitorSupport() {
				public void visit(CDATA arg0) {

					final String text = arg0.getText();

					final String[] lines = text.split("\n");
					for (int i = 0; i < lines.length; i++) {
						templateIn[i] = lines[i] + "\n";
					}
				}
			});

			if (templateIn[0] != null) {
				if (name.equals("fields")) {
					for (Union fullName : sAllFullNames) {
						String newTemple = templateIn[0];
						newTemple = newTemple.replace("<FULL_NAME>", "" + fullName.name);
						newTemple = newTemple.replace("<TYPE>", fullName.type);
						ret += newTemple;
					}

					for (Union fullName : sCommentAllFullNames) {
						String newTemple = templateIn[0];
						newTemple = newTemple.replace("<FULL_NAME>", "" + fullName.name);
						newTemple = newTemple.replace("<TYPE>", fullName.type);
						ret += "--" + newTemple;
					}
				} else if (name.equals("tables")) {
					for (String key : sAllTables.keySet()) {
						final LinkedHashSet<Union> set = sAllTables.get(key);

						final String relativeName = key.replace("&", "");
						String newTemple = templateIn[0].replace("<RELATIVE_NAME>", relativeName);
						ret += newTemple;

						for (Union union : set) {
							if (union.isTable) {
								newTemple = templateIn[1].replace("<RELATIVE_NAME>", relativeName).replace("<FULL_NAME>", union.name).replace("<TYPE>", union.type);
								ret += newTemple;
							} else {
								newTemple = templateIn[2].replace("<RELATIVE_NAME>", relativeName).replace("<FULL_NAME>", union.name).replace("<TYPE>", union.type).replace("<FULL_NAME_M>", union.name.substring(key.length() + 1).replace("&", ""));
								ret += newTemple;
							}
						}
					}
				} else if (name.equals("createListItem")) {
					for (String fullName : sListItems) {
						String newTemple = templateIn[0];
						newTemple = newTemple.replace("<LIST_ITEM_NAME>", "" + fullName);
						ret += newTemple;
					}
				} else if (name.equals("release")) {
					final String func = element.attributeValue("func");
					final String funcEnd = element.attributeValue("funcEnd");
					final int maxLen = 50;

					boolean needEnd = false;

					for (int i = 0; i < sAllFullNames.size(); i++) {
						final String fullName = sAllFullNames.get(i).name;
						if (i % maxLen == 0) {
							needEnd = true;
							if (i != 0) {
								ret += funcEnd + "\n\n";
							}
							sReleaseCallNum = (i / maxLen);

							ret += func.replace("#XML_NAME#", XML_SIMPLE_NAME).replace("#NUM#", "" + sReleaseCallNum) + "\n";

							String newTemple = templateIn[0];
							newTemple = newTemple.replace("<FULL_NAME>", "" + fullName);
							ret += newTemple;
						} else {
							String newTemple = templateIn[0];
							newTemple = newTemple.replace("<FULL_NAME>", "" + fullName);
							ret += newTemple;
						}
					}

					if (needEnd) {
						ret += funcEnd + "\n\n";
					}
				} else if (name.equals("call_release")) {
					String newTemple = templateIn[0];
					for (int i = 0; i <= sReleaseCallNum; i++) {
						ret += newTemple.replace("#NUM#", "" + i);
					}
				} else if (name.equals("release_table")) {
					final String func = element.attributeValue("func");
					final String funcEnd = element.attributeValue("funcEnd");
					final int maxLen = 50;

					final int size = sAllTables.size();
					final String[] keys = new String[size];
					sAllTables.keySet().toArray(keys);

					boolean needEnd = false;
					for (int i = 0; i < size; i++) {
						final String fullName = keys[i].replace("&", "");

						if (i % maxLen == 0) {
							needEnd = true;
							if (i != 0) {
								ret += funcEnd + "\n\n";
							}
							sReleaseTableCallNum = (i / maxLen);
							ret += func.replace("#NUM#", "" + sReleaseTableCallNum) + "\n";

							String newTemple = templateIn[0];
							newTemple = newTemple.replace("<RELATIVE_NAME>", "" + fullName);
							ret += newTemple;
						} else {
							String newTemple = templateIn[0];
							newTemple = newTemple.replace("<RELATIVE_NAME>", "" + fullName);
							ret += newTemple;
						}
					}

					if (needEnd) {
						ret += funcEnd + "\n\n";
					}
				} else if (name.equals("call_release_table")) {
					String newTemple = templateIn[0];
					for (int i = 0; i <= sReleaseTableCallNum; i++) {
						ret += newTemple.replace("#NUM#", "" + i);
					}
				}

				else {
					// final Set<String> keys = sNodeMap.keySet();
					// if(keys.contains(name)) {
					// String newTemple = templateIn[0];
					// final ArrayList<String> nameList = sNodeMap.get(name);
					// final String names = getNames(nameList);
					// if(names != null && names.length() > 1) {
					// newTemple = newTemple.replace("<"+name+">", names);
					// ret += newTemple;
					// }
					// }

					System.err.println(name);
				}
			}
		}

		if (ret == null || ret.equals("")) {
			// System.out.println(template);
		}

		return ret;
	}

	static String getNames(ArrayList<String> list) {
		String ret = "";
		for (String name : list) {
			ret += "_" + name + ",";
		}
		return ret;
	}

	private static String replaceKey(String key) {
		if (key.equals("<XML_NAME>")) {
			return XML_SIMPLE_NAME;
		} else if (key.equals("<XML_DATE>")) {
//			return new Date().toString() + " By " + FileHelper.getUserName();
			return "";
		} else {
			System.err.println("not found key:" + key);
		}

		return key;
	}

	static ArrayList<String> getKeys(final String line) {
		final ArrayList<String> keys = new ArrayList<String>();
		if (line != null) {
			final int length = line.length();
			int beg = -1;
			for (int i = 0; i < length; i++) {
				if (line.charAt(i) == '<') {
					beg = i;
				} else if (line.charAt(i) == '>') {
					if (beg < 0) {
						System.err.println("no match <> in " + line);
					} else {
						keys.add(line.substring(beg, i + 1));
						beg = -1;
					}
				}
			}
		}

		return keys;
	}

	public static void main(String[] args) {
		convert(path1, path2);
	}
}

interface ConvertXMLToLuaConstants {
	final static String FULL_NAME = "FullName";
}
