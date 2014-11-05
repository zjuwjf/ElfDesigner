package com.ielfgame.stupidGame;

import java.util.ArrayList;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

public class Redirect {
	
	final static Color color = new Color(null, 255, 0, 0);
	
	private static ArrayList<String> sList = new ArrayList<String>();

	public static void outPrintln(final String out) {

		if (PowerMan.getSingleton(OutputWorkSpaceTab.class) != null && PowerMan.getSingleton(OutputWorkSpaceTab.class).mOutput != null) {
			final StyledText text = PowerMan.getSingleton(OutputWorkSpaceTab.class).mOutput;

			for (String str : sList) {
				final String addText = str + "\n";
				text.append(addText);
			}
			sList.clear();

			final String addText = out + "\n";
			text.append(addText);

			text.setSelection(text.getCharCount());

		} else {
			sList.add(out);
		}

		// System.out.println(out);
	}

	public static void errPrintln(final String err) {
		if (PowerMan.getSingleton(OutputWorkSpaceTab.class) != null && PowerMan.getSingleton(OutputWorkSpaceTab.class).mOutput != null) {
			final StyledText text = PowerMan.getSingleton(OutputWorkSpaceTab.class).mOutput;

			final String addText = err + "\n";
			text.append(addText);

			text.setLineBackground(text.getLineCount() - 2, 1, new Color(null, 255, 0, 0));
			
			// text.setSelection(text.getCharCount());
		} 

		System.err.println(err);
	}

	static final String USER_DIR = System.getProperty("user.dir");
	static final String LOG_PATH = FileHelper.DECOLLATOR+"log.txt";
	
	public static void redirect() { 
//		final boolean errToLog = PowerMan.getSingleton(DataModel.class).getErrToLog();
//		if(false) { 
//			try { 
//				PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(USER_DIR + LOG_PATH))),true);
//				System.setErr(out); 
//			} catch (Exception e) { 
//				System.err.println("catch");
//				e.printStackTrace();
//			} 
//		} 
	}
}
