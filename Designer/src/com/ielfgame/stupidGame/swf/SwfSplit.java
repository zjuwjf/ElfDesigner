package com.ielfgame.stupidGame.swf;

import java.io.FileInputStream;

import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.tags.base.ImageTag;

import elf.flash.test.MySwf;
import elf.flash.test.SwfReader;
import elf.flash.test.SwfReader.IGetName;

public class SwfSplit {
	
	
	public static MySwf split(final String path) {
		try {
			final SWF swf = new SWF(new FileInputStream(path), true);
			
			swf.file = SpellHelper.getUpEname(FileHelper.getSimpleNameNoSuffix(path));
			
			final String name = FileHelper.getSimpleNameNoSuffix(path);
			final String dir = FileHelper.getDirPath(path);
			
			SwfReader.exportImages(swf, dir+FileHelper.DECOLLATOR + SpellHelper.getUpEname(name), new IGetName() {
				public String getName(String dir, SWF swf, ImageTag tag) {
					return SwfSplit_getName(dir, swf, tag);
				}
			});
			
			final MySwf mySwf = SwfReader.read(swf.dumpInfo);
			mySwf.reduce();
			mySwf.printForLua( dir+FileHelper.DECOLLATOR + SpellHelper.getUpEname(name) + FileHelper.DECOLLATOR+"Swf_"+SpellHelper.getUpEname(name)+".lua"  );
			
			return mySwf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static MySwf exportImages(final String path) {
		try {
			final SWF swf = new SWF(new FileInputStream(path), true);
			
			swf.file = SpellHelper.getUpEname(FileHelper.getSimpleNameNoSuffix(path));
			
			final String name = FileHelper.getSimpleNameNoSuffix(path);
			final String dir = FileHelper.getDirPath(path);
			
			SwfReader.exportImages(swf, dir+FileHelper.DECOLLATOR + SpellHelper.getUpEname(name), new IGetName() {
				public String getName(String dir, SWF swf, ImageTag tag) {
					return SwfSplit_getName(dir, swf, tag);
				}
			});
			
			final MySwf mySwf = SwfReader.read(swf.dumpInfo);
			return mySwf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String SwfSplit_getName(final String dir, final SWF swf, ImageTag tag) {
		return new StringBuilder().append(dir).append("/").append("Swf_").append(swf.file).append("-").append(tag.getCharacterId()+1).append(".png").toString();
	}
	
	
}
