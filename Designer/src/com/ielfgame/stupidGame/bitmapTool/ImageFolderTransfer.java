package com.ielfgame.stupidGame.bitmapTool;

import java.io.File;

import com.ielfgame.stupidGame.trans.TransferRes;

public class ImageFolderTransfer {
	public static void main(final String [] args) { 
		final String touxiangPath = "C:\\Users\\zju_wjf\\Desktop\\touxiang\\";
		
		final String dist = "D:\\pic\\gyx\\Ó¢ÐÛ\\";
		
		final String [] list = new File(dist).list();
		
		for(int i=0; i<list.length; i++) {
			final String touxiang = touxiangPath + "hero" + (i+1) + ".png";
//			final String dst = dist + list[i] + "\\Í·Ïñ.png";
			
			TransferRes.copyFileQuickly(new File(touxiang), new File(dist + list[i]), "Í·Ïñ.png");
		} 
	} 
} 
