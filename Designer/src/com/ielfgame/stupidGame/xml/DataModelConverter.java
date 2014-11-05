package com.ielfgame.stupidGame.xml;

import java.util.ArrayList;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

public class DataModelConverter extends AbstractXMLItemConverter implements DataModelConverterConstants {

	public DataModelConverter() {
		super(DataModel.class);
	}
	
	public Object createData(final Object parent) { 
		return PowerMan.getSingleton(DataModel.class);
	}

	@Deprecated
	public void setElemenAttr(Object data, Element element) {
//		final DataModel dataModel = (DataModel) data;
//		final ArrayList<String> paths = dataModel.getPicPathList();
//		String value = "";
//		for (String path : paths) {
//			value += path + SPLIT;
//		}
//		element.addAttribute(ATTR_PATH, value);
//
//		final ArrayList<String> pathHeads = new ArrayList<String>(); 
//		value = "";
//		for (String path : pathHeads) {
//			value += path + SPLIT;
//		}
//		element.addAttribute(ATTR_PATH_HEAD, value);
//
//		element.addAttribute(ATTR_LAST_IMPORT, "" + dataModel.getLastImportPath());
//		element.addAttribute(ATTR_TRANSFER_DIR, dataModel.getTransferDir());
//		element.addAttribute(ATTR_TRANSFER_JPG2PNG, "" + dataModel.isConvertJpg2Png());
//		element.addAttribute(ATTR_AUTO_SAVE, "" + dataModel.getAutoSaveTime());
//		element.addAttribute(ATTR_ERR_LOG, "" + dataModel.getErrToLog());
//		
//		System.err.println("setElemenAttr"); 
	}

	public void setDataProperty(final Object select, final Element element) {
		final DataModel dataModel = PowerMan.getSingleton(DataModel.class);
		final ArrayList<String> paths = dataModel.getPicPathList();
		final String value = readAttrString(element, ATTR_PATH);
		final String[] values = value.split(SPLIT);
		for (String path : values) {
			paths.add(path);
		}
		
		final ArrayList<String> pathHeads = new ArrayList<String>();
		
		final String value2 = readAttrString(element, ATTR_PATH_HEAD);
		final String[] values2 = value2.split(SPLIT);
		for (String head : values2) {
			pathHeads.add(head);
		}
		
		final String [] pathArray = new String[pathHeads.size()];
		pathHeads.toArray(pathArray);
//		PowerMan.getSingleton(TransferConfig.class).pathHeads = pathArray;
//		dataModel.setLastImportPath(readAttrString(element, ATTR_LAST_IMPORT));
//		dataModel.setTransferDir(readAttrString(element, ATTR_TRANSFER_DIR));
//		dataModel.setConvertJpg2Png(readAttrBoolean(element, ATTR_TRANSFER_JPG2PNG));

		dataModel.setAutoSaveTime(readAttrFloat(element, ATTR_AUTO_SAVE));
		dataModel.setErrToLog(readAttrBoolean(element, ATTR_ERR_LOG));
		
		System.err.println("setDataProperty");
	} 

	// read float, int, boolean, string
	final static String readAttrString(final Element element, final String attr) {
		final Attribute attribute = element.attribute(attr);
		if (attribute != null) {
			return attribute.getValue();
		} else {
			return "";
		}
		// return element.attribute(attr).getValue();
	}

	final static float readAttrFloat(final Element element, final String attr) {
		final Attribute attribute = element.attribute(attr);
		if (attribute != null) {
			return Float.valueOf(attribute.getValue());
		} else {
			return 0;
		}
		// return Float.valueOf(element.attribute(attr).getValue());
	}

	final static int readAttrInt(final Element element, final String attr) {
		final Attribute attribute = element.attribute(attr);
		if (attribute != null) {
			return Integer.valueOf(attribute.getValue());
		} else {
			return 0;
		}
		// return Integer.valueOf(element.attribute(attr).getValue());
	}

	final static boolean readAttrBoolean(final Element element, final String attr) {
		final Attribute attribute = element.attribute(attr);
		if (attribute != null) {
			return attribute.getValue().equalsIgnoreCase("true");
		} else {
			return false;
		}
		// return element.attribute(attr).getValue().equalsIgnoreCase("true");
	}
}

interface DataModelConverterConstants {
	static final String ATTR_PATH = "picPaths";
	static final String ATTR_LAST_IMPORT = "import";
	static final String ATTR_TRANSFER_DIR = "transferDir";
	static final String ATTR_TRANSFER_JPG2PNG = "jpg2png";
	static final String SPLIT = ";";
	static final String ATTR_PATH_HEAD = "pathHeads";

	static final String ATTR_AUTO_SAVE = "AutoSaveMinute";

	static final String ATTR_ERR_LOG = "ErrToLog";
}
