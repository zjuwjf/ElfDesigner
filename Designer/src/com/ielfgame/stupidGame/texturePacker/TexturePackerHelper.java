package com.ielfgame.stupidGame.texturePacker;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.shellRunner.ConsoleHandle;
import com.ielfgame.stupidGame.shellRunner.ShellRunner;
import com.ielfgame.stupidGame.utils.FileHelper;

public class TexturePackerHelper { 
	
	final static String TP_CMD_DEFAULT = "TexturePacker --format cocos2d  --texture-format pvr2ccz --opt RGBA4444 --size-constraints NPOT  --dither-atkinson-alpha --data Output.plist  --sheet Output.pvr.ccz Input";
	static String TP_CMD;
	final static ShellRunner sShellRunner = new ShellRunner();
	final static TPHandle sTPHandle = new TPHandle();
	static {
		sShellRunner.setConsoleHandle(sTPHandle);// ���ÿ���̨������
		
		BufferedReader br = null;
		try {
			br = FileHelper.getReader(FileHelper.getUserDir()+FileHelper.DECOLLATOR+"tp-cmd");
			if(br != null) { 
				String tmp = null; 
				try { 
					tmp = br.readLine(); 
					br.close(); 
				} catch (Exception e) { 
					e.printStackTrace(); 
				} 
				
				if(tmp != null) { 
					TP_CMD = tmp; 
				} else { 
					TP_CMD = TP_CMD_DEFAULT; 
				} 
			} else { 
				TP_CMD = TP_CMD_DEFAULT; 
			} 
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	} 
	
	private static void refreshCmd() {
		final BufferedReader br = FileHelper.getReader(FileHelper.getUserDir()+FileHelper.DECOLLATOR+"tp-cmd");
		if(br != null) { 
			try { 
				TP_CMD = br.readLine(); 
				br.close(); 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
			
			if(TP_CMD == null) { 
				TP_CMD = TP_CMD_DEFAULT; 
			} 
		} else { 
			TP_CMD = TP_CMD_DEFAULT; 
		} 
		
		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static TPHandle getTPHandle() { 
		return sTPHandle;
	} 
	
	public static final String pack(String outputDir, 
			String outputPlistDir, 
			final String outName, final Collection<String> inputs, final int num, 
			final String format, 
			final String dither,
			final float scale) { 
		
		if(num <= 0) { 
			return "TexturePacker::Error, sprits must be more than 0 !!!";
		} 
		
		if(outputDir == null) { 
//			outputDir = PowerMan.getSingleton(DataModel.class).getTransferDir();
		} 
		
		if(outputPlistDir == null) { 
//			outputPlistDir = PowerMan.getSingleton(DataModel.class).getTransferDir();
		} 
		
		if(outputDir == null) { 
			return "TexturePacker::Error, check your transfer path, it did not exist!!!";
		} 
		
		final String inputStrings = toInputStrings(outputDir, inputs, num);
		final String outputString = outputPlistDir+FileHelper.DECOLLATOR+outName;
		
		final String cmd = TP_CMD.replace("ScaleInput", ""+scale).replace("FormatInput", format).replace("Output", outputString).replace("Input", inputStrings).replace("DitherFormat", dither);
		
		System.err.println(cmd); 
		
		sShellRunner.setCmdline(cmd); 
		final int runRet = sShellRunner.run(); 
		
		if(runRet != 0) { 
			return "TexturePacker::ERROR"; 
		} 
		
		//check all published ? 
		final LinkedList<String> plistStrings = TpPlistScaner.readResidsFromPlist(outputString+".plist",outputDir, false); 
		inputs.removeAll(plistStrings); 
		
		System.err.println("TexturePacker : " + outputString + " compeleted!");
		
		if(!inputs.isEmpty()) { 
			return pack( outputDir, outputPlistDir, getNextName(outName), inputs, num, format, dither, scale); 
		} 
		
		return null; 
	} 
	
	private static class TPHandle implements ConsoleHandle { 
		
		final ArrayList<String> mOutList = new ArrayList<String>();
		final ArrayList<String> mErrList = new ArrayList<String>();
		
		public void onOutRead(String consoleMessage, boolean isErrorOut) {
			if (isErrorOut) { 
				mErrList.add(consoleMessage);
			} else { 
				mOutList.add(consoleMessage);
			} 
		} 
		
		public void clear() { 
			mErrList.clear();
			mOutList.clear();
		} 
		
		public ArrayList<String> getOut() {
			return mOutList;
		}
		public ArrayList<String> getErr() {
			return mErrList;
		} 
	} 
	
	private static final String toInputStrings(final String outputDir, final Collection<String> inputs, int num) {
		String ret = "";
		for(String input : inputs) { 
			if(num > 0) { 
				if(FileHelper.IS_WINDOWS) {
					final String p = "\""+outputDir+FileHelper.DECOLLATOR+input+"\"";
					ret += (" "+p);
				} else {
					final String p = outputDir+FileHelper.DECOLLATOR+input;
					ret += (" "+p);
				}
			} else {
				break;
			} 
			num--; 
		} 
		return ret;
	} 
	
	private static String getNextName(final String name) {
		if(name == null) { 
			return "null";
		} 
		
		int len = name.length(); 
		int lastIndex = len; 
		for (int i = len - 1; i >= 0; i--) {
			if (!Character.isDigit(name.charAt(i))) {
				lastIndex = i;
				break;
			}
		}

		String subName;
		int num;
		if (lastIndex == len - 1) {
			subName = name;
			num = 0;
		} else if (lastIndex == len) {
			subName = "";
			String numStr = name;
			num = Integer.valueOf(numStr);
		} else {
			subName = name.substring(0, lastIndex + 1);
			String numStr = name.substring(lastIndex + 1);
			num = Integer.valueOf(numStr);
		} 
		
		return subName + (++num);
	}
	
	
	/***
	 * C:\Program Files (x86)\ARM\Mali Developer Tools\Mali Texture Compression Tool v4.2.0\bin>etcpack "D:\svn_project_pet\develop\editor\Resources\image-android\batt
le\skin\002\002.PNG" "D:\svn_project_pet\develop\editor\Resources\image-android\battle\skin\002" -c etc1 -aa -e nonperceptual
	 */
	
	private final static String ETC_PACKER = "etcpack.exe";
	private final static String ETC_PACKER_DIR = "C:\\Program Files (x86)\\ARM\\Mali Developer Tools\\Mali Texture Compression Tool v4.2.0\\bin\\";
	
	public static void runETCAlpha(final String path) {
		/***
		 * 
		 * 	<key>realTextureFileName</key>
            <string>002.PNG</string>
            <key>textureFileName</key>
            <string>002.PNG</string>
		 */
	}
	
	private static int doETCAlpha(final String pngFile) {
		
		sShellRunner.setRunpath(new File(ETC_PACKER_DIR));
		final String dir = FileHelper.getDirPath(pngFile);
		final String cmd = String.format("%s \"%s\" \"%s\" -c etc1 -aa -e nonperceptual", ETC_PACKER, pngFile, dir);
//		System.err.println(cmd);
		sShellRunner.setCmdline(cmd); 
		final int runRet = sShellRunner.run(); 
		
		//replace remove
		final ArrayList<String> keys = new ArrayList<String>();
		final ArrayList<String> values = new ArrayList<String>();
		
		final String key = FileHelper.getSimpleNameWithSuffix(pngFile);
		keys.add(key);
		values.add(key.replace(".PNG", ".pkm"));
		
		final String plistPath = pngFile.replace(".PNG", ".plist");
		FileHelper.replaceFileKey(plistPath, keys, values);
		
		FileHelper.removeFile(pngFile);
		
		return runRet;
	}
	
	public static void main(final String [] args) {
//		final String line = "中国好声音!";
//		final int length = line.length();
//		System.err.println("length : "+length);
//		for(int i=0; i<length; i++) {
//			System.err.println(line.charAt(i));
//		}
		final String pngPath = "D:\\svn_project_pet\\develop\\editor\\Resources\\image-android\\battle\\skin\\001\\001.PNG";
		doETCAlpha(pngPath);
	}
} 
