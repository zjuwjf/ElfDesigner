package com.ielfgame.stupidGame.res;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import com.ielfgame.stupidGame.utils.FileHelper;

public class TemplateCreator {

	public static class TemplateStruct {
		public String[] lines;
		public String name;
	}

	public interface ITemplateDealer {
		/***
		 * for global
		 */
		public String replaceKey(String key);

		public String dealWithTemplate(final TemplateStruct temlate);
		
	}

	public static boolean create(final String outPutFileName, final LinkedList<String> lines, final ITemplateDealer dealer) {
		
		try {
			final LinkedList<String> outLines = new LinkedList<String>();
			String template = "";
			boolean findTemplate = false;

			for (String line : lines) {
				if (line.contains("<template")) {
					findTemplate = true;
					template = line;
				} else if (line.contains("</template>")) {
					findTemplate = false;
					template += line;
					
					 final TemplateStruct ret = dealTemplate(template);
					 
					 final String model = dealer.dealWithTemplate(ret);
					
					 if(model != null) {
						 outLines.add(model);
					 }
				} else if (findTemplate) {
					template += (line + "\n");
				} else {
					final ArrayList<String> keys = getKeys(line);
					for (String key : keys) {
						line = line.replace(key, dealer.replaceKey(key));
					}
					outLines.add(line);
				}
			}
			
			FileHelper.writeUTF8(outLines, new File(outPutFileName));
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private static final TemplateStruct dealTemplate(final String template) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(template);
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		
		if(document != null) { 
			final Element element = document.getRootElement(); 
			final String name = element.attributeValue("name");
			
			final TemplateStruct ret = new TemplateStruct();
			
			element.accept(new VisitorSupport() { 
				public void visit(CDATA arg0) { 
					final String text = arg0.getText(); 
					ret.lines = text.split("\n");
				} 
			});
			
			ret.name = name;
			return ret;
		} 
		
		return null;
	}

	private static ArrayList<String> getKeys(final String line) {
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
}
