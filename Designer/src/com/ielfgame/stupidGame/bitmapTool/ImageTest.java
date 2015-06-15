package com.ielfgame.stupidGame.bitmapTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.texturePacker.pvr.PVRCCZTexture;
import com.ielfgame.stupidGame.utils.FileHelper;


public class ImageTest {
	
	private static PVRCCZTexture readPVRFormat(final File pvr) throws IllegalArgumentException, IOException {
		final PVRCCZTexture pvrccz = new PVRCCZTexture() {
			protected InputStream onGetInputStream() throws IOException {
				return new FileInputStream(pvr);
			}
		};

		return pvrccz;
	}
	
	public static void searchBg(final String rootPath) {
		final LinkedList<String> paths = FileHelper.getFullPahIdsWithIgnore(new File(rootPath), new String []{".png", ".jpg"}, true);
		final int index = rootPath.length();
		for(String path : paths) {
			final ElfPointi size = ImageHelper.readImageSize(new File(path));
			if(size != null) {
				if(size.x >= 960 && size.y >= 640) {
					System.err.println(path.substring(index));
				}
			}
		}
	}
	
	public static void main(String args[]) {
		final String head = "D:\\svn_project_pet\\develop\\editor\\Resources\\image\\";
		searchBg(head);
	}
	
	public static void main2(String args[]) {
		final String head = "D:\\svn_project_pet\\develop\\editor\\Resources\\image\\battle\\skin\\";
		
		final File dir = new File(head);
		
		final File [] fs = dir.listFiles();
		for(File skinF : fs) {
			if(skinF.isDirectory()) {
				
				//change name 
				//001
				//face_1_normal.png
				//skin_1t.png
				//001.plist
				final String skinName = skinF.getName();
				final int id = StringHelper.readLastInt(skinName);
				final String tou = String.format("skin_%dt.png", id);
				final String normal = String.format("face_%d_normal.png", id);
				FileHelper.contentReplace(new File(skinF, skinName+".plist"), normal, tou);
				new File(skinF, normal).renameTo(new File(skinF, tou));
				final File lastFile = new File(skinF, ".last");
				new File(lastFile, normal).renameTo(new File(lastFile, tou));
				FileHelper.contentReplace(new File(lastFile, skinName+".plist"), normal, tou);
				
//				try {
//					final PVRCCZTexture pvr = readPVRFormat(new File(skinF, skinF.getName()+".pvr.ccz"));
//					if(pvr.getWidth() > 512 || pvr.getHeight() > 512) {
//						System.err.println( skinF.getName() );
//						System.err.println(String.format("%d, %d", pvr.getWidth(),pvr.getHeight() ));
//					}
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				
//				final File faceFile = new File(skinF, "face");
//				final File [] faces = faceFile.listFiles();
//				
//				if(faces != null) {
//					for(File face:faces) {
//						if(face.isFile()) {
//							if (face.getName().startsWith("face")) {
//								face.renameTo(new File(skinF, face.getName()));
//							}
//						}
//					}
//				}
//				
//				FileHelper.removeFile(faceFile);
//				
//				final int id = StringHelper.readLastInt(skinF.getName());
//				final String normal = String.format("skin_%dt.png", id);
//				final String dead = String.format("face_%d_dead.png", id);
//				
//				FileHelper.removeFile(new File(skinF, normal));
//				FileHelper.removeFile(new File(skinF, dead));
				
			}
		}
	}
}
