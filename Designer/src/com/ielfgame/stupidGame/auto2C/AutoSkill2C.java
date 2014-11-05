package com.ielfgame.stupidGame.auto2C;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.utils.FileHelper;

public class AutoSkill2C {
	public static void main(final String[] args) {
		// print("Skill");
		// printFuncs("GraphOnFunc");

		// printInterface("IName_class");
		// printAdapter("Adapter_class", "logic_typedef");
		// printAdapter("GraphAdapter_class", "graph_typedef");
		// printAdapter("GraphAdapter_class");
		
		createSkills();
		// makeSkillFactoryInclude();
		//makeSkillFactoryIfElse();
		// compareSkill();
	} 
	
	static void printFuncs(String name) {
		BufferedReader reader = null;
		String line = null;

		final ArrayList<String> template = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, name);
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				template.add(line);
			}
		} catch (Exception e) {
		}

		reader = FileHelper.getReader(AutoSkill2C.class, "skillList");
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				line = line.replace(" ", "");
				line = line.replace("\r", "");
				final String py = SpellHelper.getUpEname(line);

				for (String text : template) {
					text = text.replace("skill_zh", line);
					text = text.replace("skill_en", py);
					System.err.println(text);
				}
			}
		} catch (Exception e) {
		}
	}

	static void printInterface(final String fileName) {
		// returnType
		// parameters
		// name
		BufferedReader reader = null;
		String line = null;

		final ArrayList<String> template = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, fileName);
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				template.add(line);
			}
		} catch (Exception e) {
		}

		reader = FileHelper.getReader(AutoSkill2C.class, "typedef_func");
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				try {
					final String returnType = line.split(" ")[1];
					final String parameters = line.substring(line.lastIndexOf('(') + 1, line.lastIndexOf(')'));
					final String name = line.substring(line.indexOf('*') + 1, line.indexOf('_'));
					for (String text : template) {
						text = text.replace("Type", returnType);
						text = text.replace("Parameters", parameters);
						text = text.replace("Name", name);
						System.err.println(text);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void printAdapter(final String fileName, final String typedef) {
		// returnType
		// parameters
		// name
		BufferedReader reader = null;
		String line = null;

		final ArrayList<String> template = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, fileName);
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				template.add(line);
			}
		} catch (Exception e) {
		}

		reader = FileHelper.getReader(AutoSkill2C.class, typedef);
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				try {
					final String returnType = line.split(" ")[1];
					final String parameters = line.substring(line.lastIndexOf('(') + 1, line.lastIndexOf(')'));
					final String name = line.substring(line.indexOf('*') + 1, line.indexOf('_'));
					final String func = line.substring(line.indexOf('*') + 1, line.indexOf(')'));
					final String inputs = parametersToInputs(parameters);
					final String members = parametersToMembers(parameters);

					for (String text : template) {
						text = text.replace("Type", returnType);
						text = text.replace("Parameters", parameters);
						text = text.replace("Name", name);
						text = text.replace("Func", func);
						text = text.replace("Inputs", inputs);
						text = text.replace("Members", members);
						System.err.println(text);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String parametersToInputs(final String parameters) {
		String ret = "";
		final String[] splits = parameters.split(",");
		for (String value : splits) {
			final String[] bs = value.split(" ");
			ret += bs[bs.length - 1].replace("[]", "") + ", ";
		}

		ret = ret.substring(0, ret.length() - 2);
		return ret;
	}

	static String parametersToMembers(final String parameters) {
		String ret = "";
		final String[] splits = parameters.split(",");
		for (String value : splits) {
			final String[] bs = value.split(" ");
			String v = bs[bs.length - 1].replace("[]", "");
			v = 'm' + (Character.toUpperCase(v.charAt(0)) + v.substring(1));
			ret += v + ", ";
		}

		ret = ret.substring(0, ret.length() - 2);
		return ret;
	}

	static void createSkills() {
		final String dist = "D:\\svn-code\\NewWorld\\MyTest\\Extends\\skill\\";

		BufferedReader reader = null;
		String line = null;

		final ArrayList<String> hTemplate = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, "Skill.h_class");
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				hTemplate.add(line);
			}
		} catch (Exception e) {
		}

		final ArrayList<String> cppTemplate = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, "Skill.cpp_class");
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				cppTemplate.add(line);
			}
		} catch (Exception e) {
		}
		
		reader = FileHelper.getReader(AutoSkill2C.class, "果实");
		final HashMap<String, Integer> skillMap = new HashMap<String, Integer>();
		try {
			int count = 1;
			while ((line = reader.readLine()) != null) {
				if (line.contains("skillname=")) {
					final String name = SpellHelper.getUpEname(line.substring(line.indexOf('"') + 1, line.lastIndexOf('"')));
					skillMap.put(name, count);
					count++;
				} 
			}
		} catch (Exception e) {
		}

		reader = FileHelper.getReader(AutoSkill2C.class, "skillList");
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				line = line.replace(" ", "");
				line = line.replace("\r", "");
				final String py = SpellHelper.getUpEname(line);
				final BufferedWriter hWrite = FileHelper.getWriter(dist + "_" + py + ".h");
				final BufferedWriter cppWrite = FileHelper.getWriter(dist + "_" + py + ".cpp");

				for (String text : hTemplate) {
					text = text.replace("SkillZH", line);
					text = text.replace("SkillEN", py);
					text = text.replace("SkillID", "" + skillMap.get(py));
					hWrite.write(text + "\n");
				}

				for (String text : cppTemplate) {
					text = text.replace("SkillZH", line);
					text = text.replace("SkillEN", py);
					cppWrite.write(text + "\n");
				}

				hWrite.flush();
				cppWrite.flush();
			}
		} catch (Exception e) {
		}
	}

	static void makeSkillFactoryInclude() {
		BufferedReader reader = null;
		String line = null;
		reader = FileHelper.getReader(AutoSkill2C.class, "skillList");

		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				line = line.replace(" ", "");
				line = line.replace("\r", "");
				final String py = SpellHelper.getUpEname(line);
				System.err.println("#include \"_" + py + ".h\"");
			}
		} catch (Exception e) {
		}
	}

	static void makeSkillFactoryIfElse() {
		BufferedReader reader = null;
		String line = null;
		
		final ArrayList<String> cppTemplate = new ArrayList<String>();
		reader = FileHelper.getReader(AutoSkill2C.class, "SkillFactory.cpp_class");
		line = null;
		try {
			line = null;
			while ((line = reader.readLine()) != null) {
				cppTemplate.add(line);
			}
		} catch (Exception e) {
		}
		
		reader = FileHelper.getReader(AutoSkill2C.class, "果实");
		try {
			int count = 1;
			while ((line = reader.readLine()) != null) {
				if (line.contains("skillname=")) {
					final String name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					for(String text: cppTemplate) {
						text = text.replace("SkillId", SpellHelper.getUpEname(name));
						text = text.replace("I", ""+(count));
						System.err.println(text);
					}
					count++;
				}
			}
		} catch (Exception e) {
		}

		// SkillId
	}

	static void compareSkill() {
		BufferedReader reader = null;
		String line = null;
		reader = FileHelper.getReader(AutoSkill2C.class, "果实");
		final ArrayList<String> guoshiList = new ArrayList<String>();
		try {
			while ((line = reader.readLine()) != null) {
				if (line.contains("skillname=")) {
					final String name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					guoshiList.add(name);
				}
			}
		} catch (Exception e) {
		}

		reader = FileHelper.getReader(AutoSkill2C.class, "skillList");
		final ArrayList<String> jinengList = new ArrayList<String>();
		try {
			while ((line = reader.readLine()) != null) {
				line = line.replace(" ", "");
				line = line.replace("\r", "");
				jinengList.add(line);
			}
		} catch (Exception e) {
		}

		for (String key : guoshiList) {
			if (!jinengList.contains(key)) {
				System.err.println("not found " + key);
			}
		}
	}
}