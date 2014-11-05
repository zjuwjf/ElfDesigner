package com.ielfgame.stupidGame;

import com.ielfgame.stupidGame.dialog.PopDialog.PopType;

public class Constants {
	//err output
	public static final String ERR_OUTPUT = "elfDesigner.err.log";
	public static final String OUT_OUTPUT = "elfDesigner.out.log";
	
	public static final String TREEITEMDATA_STUB = "tree.stub";
	public static final String TREEITEMDATA_NODE = "tree.node";
	public static final String TREEITEMDATA_IMAGEEXPANDED = "tree.image.expanded";
	public static final String TREEITEMDATA_IMAGECOLLAPSED = "tree.image.collapsed";
	public static final String TREEITEMDATA_IMAGEASSCENCE = "tree.image.scence";
	
	public static final String NODE_EXPLORER_SCREEN = "screen";
	public static final String NODE_EXPLORER_RECYCLE = "recycle";
	
	public static final String VIEW_NODE_EXPLORER = "Node View";
	public static final String VIEW_RES_EXPLORER = "Res View";
	public static final String VIEW_PORPERTY_EXPLORER = "Property View";
	
	//menu
	private static int sInitId = 0;
	
	public static final String MENU_SET_AS_SCENCE = "Set As Scene";
	public static final int    MENU_SET_AS_SCENCE_ID = sInitId++;
	
	public static final String MENU_SEPERATE = "";
	public static final int    MENU_SEPERATE_ID = sInitId++;
	
	public static final String MENU_NEW = "New Node";
	public static final int    MENU_NEW_ID = sInitId++;
	
	public static final String MENU_NEW_BASIC = "Basic Node";
	public static final String MENU_NEW_TEXT = "Text Node";
	public static final String MENU_NEW_BUTTON = "Button Node";
	public static final String MENU_NEW_PARTICLE = "Particle Node";
	public static final String MENU_NEW_MAP = "Map Node";
	public static final String MENU_NEW_LAYLOUT = "Layout Node";
	public static final String MENU_NEW_LISTNODE = "List Node";
	public static final String MENU_NEW_TABNODE = "Tab Node";
	public static final String MENU_NEW_BARNODE = "Bar Node";
	public static final String MENU_NEW_INPUTNODE = "Input Node";
	public static final String MENU_NEW_SWIPNODE = "Swip Node";
	public static final String MENU_NEW_CIRCLENODE = "Circle Node";
	
	public static final String MENU_RENAME = "Rename";
	public static final int    MENU_RENAME_ID = sInitId++;
	
	public static final String MENU_DELETE = "Delete";
	public static final int    MENU_DELETE_ID = sInitId++;
	
	public static final String MENU_COPY = "Copy";
	public static final int    MENU_COPY_ID = sInitId++;
	
	public static final String MENU_COPY_DEEP = "Copy Deep";
	public static final int    MENU_COPY_DEEP_ID = sInitId++;
	
	public static final String MENU_REFRESH_NODE = "Refresh Node";
	public static final int    MENU_REFRESH_NODE_ID = sInitId++;
	
	public static final String MENU_COMMENT_NODE = "Comment Node";
	public static final int    MENU_COMMENT_NODE_ID = sInitId++;
	
	public static final String MENU_UN_COMMENT_NODE = "Uncomment Node";
	public static final int    MENU_UN_COMMENT_NODE_ID = sInitId++;
	
	public static final String MENU_SAME_NODES = "Same Nodes";
	public static final int    MENU_SAME_NODES_ID = sInitId++;
	
	public static class Pair{ 
		public final String text;
		public final int id;
		public final PopType type;
		public Pair(String text, int id, PopType type) {
			super();
			this.text = text;
			this.id = id;
			this.type = type;
		}
	}
	
	public static final Pair [] MENU_PAIRS = {
		new Pair(MENU_SET_AS_SCENCE, MENU_SET_AS_SCENCE_ID, null),
		new Pair(MENU_SEPERATE, MENU_SEPERATE_ID, null),
		new Pair(MENU_NEW, MENU_NEW_ID, PopType.NEW_NODE),
		new Pair(MENU_RENAME, MENU_RENAME_ID, PopType.RENAME),
		new Pair(MENU_DELETE, MENU_DELETE_ID, null),
		new Pair(MENU_COPY, MENU_COPY_ID, PopType.COPY),
		new Pair(MENU_COPY_DEEP, MENU_COPY_DEEP_ID, PopType.COPY_DEEP),
		new Pair(MENU_REFRESH_NODE, MENU_REFRESH_NODE_ID, null),
		new Pair(MENU_COMMENT_NODE, MENU_COMMENT_NODE_ID, null),
		new Pair(MENU_UN_COMMENT_NODE, MENU_UN_COMMENT_NODE_ID, null),
		new Pair(MENU_SAME_NODES, MENU_SAME_NODES_ID, null),
		};
	
	
	//popDialog
	public static final String POP_DIALOG_NEW_NODE = "New Node";
	public static final String POP_DIALOG_RENAME = "Rename Node";
	public static final String POP_DIALOG_DELETE = "Delete Node";
	public static final String POP_DIALOG_SET_INDEX = "Set Index";
	public static final String POP_DIALOG_COPY = "Copy A Node";
	public static final String POP_DIALOG_COPY_DEEP = "Copy A Node Deep";
	public static final String POP_DIALOG_REFRESH_NODE = "Refresh Node";
	
	public static final String POP_DIALOG_LABEL_NEW_NODE = "New Node Name";
	public static final String POP_DIALOG_TEXT_NEW_NODE = "#node";
	public static final String POP_DIALOG_LABEL_RENAME = "Rename Node";
	public static final String POP_DIALOG_LABEL_DELETE = "Delete Node";
	public static final String POP_DIALOG_LABEL_SET_INDEX = "Set Node Index";
	public static final String POP_DIALOG_LABEL_COPY = "Copy Node";
	
	
	public static final String POP_DIALOG_OK = "Ok";
	public static final String POP_DIALOG_CANCEL = "Cancel";
	
	//property
	public static final String PROPERTY_BASIC = "Basic";
	public static final String PROPERTY_ANDVANCE = "Andvance";
	public static final String PROPERTY_CHILDS = "Childs";
	public static final String PROPERTY_ACTIONS = "Actions";
	
	public static final String PROPERTY_POSITION = "Position";
	public static final String PROPERTY_POSITION_IN_SCREEN = "Position In Screen";
	public static final String PROPERTY_SCALE = "Scale";
	public static final String PROPERTY_SCALE_CENTER = "Scale Center";
	public static final String PROPERTY_ANCHOR_CENTER = "Anchor Center";
	public static final String PROPERTY_ROTATE = "Rotate";
	public static final String PROPERTY_ROTATE_CENTER = "Rotate Center";
	public static final String PROPERTY_COLOR = "Color";
	public static final String PROPERTY_FIXED_MOVE = "Fixed";
	public static final String PROPERTY_SIZE = "Size";
	public static final String PROPERTY_USE_SET_SIZE = "Use Setted Size";
	
	public static final String PROPERTY_TEXT = "Text";
	public static final String PROPERTY_TEXT_SIZE = "Text Size";
	public static final String PROPERTY_TEXT_FONT = "Text Font";
	public static final String PROPERTY_TEXT_STYLE = "Text Style";
	public static final String PROPERTY_TEXT_ALIGN = "Text Align";
	public static final String PROPERTY_TEXT_VERTICAL_ALIGN = "Text Verticle Align";
	public static final String PROPERTY_TEXT_DIMENSION = "Text Dimension";
	public static final String PROPERTY_INPUT_TEXT_CURSOR_COLOR = "Cursor Color";
	public static final String PROPERTY_INPUT_TEXT_PASSWORD = "Password";
//	public static final String PROPERTY_BUTTON = "Pic1";
//	public static final String PROPERTY_BUTTON2 = "Pic2";
//	public static final String PROPERTY_BUTTON3 = "Pic3";
	
	public static final String PROPERTY_MAP_SIZE = "Map Size";
	public static final String PROPERTY_WINDOW_SIZE = "Window Size";
	public static final String PROPERTY_USE_PIC_AS_MAP = "Use Pic As Map";
	
	public static final String PROPERTY_LAYOUT_SPACE = "space";
	public static final String PROPERTY_LAYOUT_MODE = "mode";
	
	public static final String PROPERTY_PARTICLE_PIC = "Particle Pic";
	public static final String PROPERTY_PARTICLE_PLIST = "Particle Plist";
	
	public static final String PROPERTY_LISTNODE_CLIPABLE = "clipable";
	public static final String PROPERTY_LISTNODE_DIRECTION = "direction";
	
	//swip node
	public static final String PROPERTY_SWIPNODE_STAY_POINTS = "Stay Points";
	public static final String PROPERTY_SWIPNODE_RESTRICT = "Restrict";
	public static final String PROPERTY_SWIPNODE_MIN_MAX_POINT = "Min Max";
	public static final String PROPERTY_SWIPNODE_ANIMATE_TIME = "Animate Time";
	public static final String PROPERTY_SWIPNODE_STAY_INDEX = "Stay Index";
	
	//circle node
	public static final String PROPERTY_CIRCLE_STAY_POINTS = "Stay Rotates";
	public static final String PROPERTY_CIRCLE_MIN_MAX_POINT = "Min Max";
	public static final String PROPERTY_CIRCLE_ANIMATE_TIME = "Animate Time";
	public static final String PROPERTY_CIRCLE_STAY_INDEX = "Stay Index";
	
	public static final String PROPERTY_RESID = "Res";
	public static final String PROPERTY_VISIBLE = "Visible";
	public static final String PROPERTY_PAUSE = "Pause";
	
	public static final String PROPERTY_BLEND_MODE = "Blend";
	
	public static final String VALUE_TRUE = "True";
	public static final String VALUE_FALSE = "False";
	public static final String VALUE_NULL = "Null";
	
	public static final String MODIFIER = "Modifier ";
	public static final String LOCAL_X = "Local X";
	public static final String LOCAL_Y = "Local Y";
	
	public static final String SCALE = "Scale";
	public static final String SCALE_X = "Scale X";
	public static final String SCALE_Y = "Scale Y";
	public static final String SIZE_X = "Size X";
	public static final String SIZE_Y = "Size Y";
	
	public static final String SCALE_CENTER_X = "Scale Center X";
	public static final String SCALE_CENTER_Y = "Scale Center Y";
	public static final String ANCHOR_CENTER_X = "Anchor Center X";
	public static final String ANCHOR_CENTER_Y = "Anchor Center Y";
	
	public static final String ROTATE = "Rotate"; 
	public static final String ROTATE_CENTER_X = "Rotate Center X"; 
	public static final String ROTATE_CENTER_Y = "Rotate Center Y"; 
	
	public static final String RED = "Red";
	public static final String GREEN = "Green";
	public static final String BLUE = "Blue";
	public static final String ALPHA = "Alpha";
	
	//file bar
	public static final String FILE_BAR_SET = "Set";
	public static final String FILE_BAR_SET_SCREEN = "Set Screen";
	public static final String FILE_BAR_SET_TRANSFER = "Set Transfer";
	
	public static final String FILE_BAR_SET_SCREEN_PHYSICAL_WIDTH = "Physical Width";
	public static final String FILE_BAR_SET_SCREEN_PHYSICAL_HEIGHT = "Physical Height";
	public static final String FILE_BAR_SET_SCREEN_LOGICAL_WIDTH = "Logical Width";
	public static final String FILE_BAR_SET_SCREEN_LOGICAL_HEIGHT = "Logical Height";
	
	//button
	//pic0, pic1, pic2
	public static final String PROPERTY_BUTTON_PIC0 = "pic0";
	public static final String PROPERTY_BUTTON_PIC1 = "pic1";
	public static final String PROPERTY_BUTTON_PIC2 = "pic2";
	public static final String PROPERTY_BUTTON_LUA = "lua";
	
	public static final String PROPERTY_BAR_LENGTH = "length";
	public static final String PROPERTY_BAR_LEN_RANGE = "range";
	public static final String PROPERTY_BAR_EXTEND_DIR = "extend dir";
	
	//scroll node item
	public static final String PROPERTY_FIRST_LAST_POINT = "p1, p2";
	
	//particle
//	public static final String PROPERTY_PARTICLE_PIC = "";
}
