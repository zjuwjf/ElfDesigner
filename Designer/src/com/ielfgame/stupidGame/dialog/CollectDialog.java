package com.ielfgame.stupidGame.dialog;

import java.util.Collection;

public class CollectDialog extends AnalysisDialog<String[]>{

	public CollectDialog(String title, boolean isEditable) {
		super(title, isEditable);
	}
	
	public String []  open(Collection<String> collection) { 
		final String [] values = collection.toArray(new String[collection.size()]);
		return open(values, String[].class);
	}
}
