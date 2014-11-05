package com.ielfgame.stupidGame;

public interface ActionDataConfig {
	public final static String[] sTableLabels = {
		// "Name",
		"Name", "Type", "StartValue", "EndValue", "Mode", 
//		"Start Time",
		"Life", "Period", "Interpolor"};
	
	public final static String[] sEditorKeys = {
		// "Name_Editor",
		"Name_Editor", "Type_Editor", 
		"Start Value_Editor",
		"End Value_Editor", "Mode_Editor", 
//		"Start Time_Editor",
		"Life_Editor", "Period_Editor", "Interpolor_Editor"};
	
	public final static int INDEX_NAME = 0;
	public final static int INDEX_TYPE = 1;
	public final static int INDEX_START_VALUE = 2;
	public final static int INDEX_END_VALUE = 3;
	public final static int INDEX_MODE = 4;
//	public final static int INDEX_START_TIME = 5;
	public final static int INDEX_END_TIME = 5;
	public final static int INDEX_PERIOD = 6;
	public final static int INDEX_INTERPOLOR = 7;
	
	public final static String ACTIONDATA_KEY = "ACTIONDATA_KEY";
	
}
