package com.ielfgame.stupidGame.res;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.power.PowerMan;

public class ViewInfoDialog {
	
	private final AnalysisDialog<String> mAnalysisDialog = new AnalysisDialog<String>("View Info", false);
	private final Display mDisplay = PowerMan.getSingleton(MainDesigner.class).getShell().getDisplay();
	
	private ViewInfoDialog() {
	}
	
	public void setInfo(final String text) { 
		if(mAnalysisDialog.isValid()) { 
			final StyledText styledText = mAnalysisDialog.getStyledText();
			styledText.setText(text);
			styledText.setSelection(styledText.getCharCount());
		} else { 
			mAnalysisDialog.open(text, String.class);
		} 
	} 
	
	public void appendInfo(final String text, final StyleRange sr) { 
		try { 
			mDisplay.asyncExec(new Runnable() {
				public void run() {
					if(mAnalysisDialog.isValid()) { 
						final StyledText styledText = mAnalysisDialog.getStyledText();
						
						final StyleRange [] oldArray = styledText.getStyleRanges();
						
						styledText.append("\n");
						
						final String old = styledText.getText();
						
						if(sr != null) {
							sr.start = old.length();
							sr.length = text.length();
						}
						
						styledText.append(text);
						
						if(oldArray != null) {
							for(StyleRange oldsr : oldArray) {
								styledText.setStyleRange(oldsr);
							}
						}
						
						if(sr != null) { 
							styledText.setStyleRange(sr);
						} 
						
						styledText.setSelection(styledText.getCharCount());
					} else { 
						mAnalysisDialog.open(text, String.class);
						final StyledText styledText = mAnalysisDialog.getStyledText();
						if(styledText != null && !styledText.isDisposed()) {
							styledText.setSelection(styledText.getCharCount());
							if(sr != null) {
								sr.start = 0;
								sr.length = text.length();
								styledText.setStyleRange(sr);
							}
						}
					} 
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private static ViewInfoDialog sPackerInfoDialog = new ViewInfoDialog();
	public static ViewInfoDialog getSingleton() {
		return sPackerInfoDialog;
	} 
	
}
