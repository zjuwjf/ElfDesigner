//package com.ielfgame.stupidGame.bitmapTool;
//
//import java.io.File;
//import java.io.FileInputStream;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.ImageData;
//import org.eclipse.swt.graphics.ImageLoader;
//import org.eclipse.swt.graphics.PaletteData;
//import org.eclipse.swt.graphics.RGB;
//
//import com.ielfgame.stupidGame.data.ElfPointi;
//import com.ielfgame.stupidGame.utils.FileHelper;
//
//import elfEngine.basic.utils.ElfRandom;
//
//public class ImageBreaker {
//	
//	public static void breakImage(final ImageData data, final String imagePath, final ElfPointi grid, final int max) { 
//		try {
//			final int index = imagePath.lastIndexOf(FileHelper.DECOLLATOR); 
//			final String prePath = imagePath.substring(0, index); 
//			final String newPath = prePath + imagePath.substring(index).replace(".png", ""); 
//			final File file = new File(newPath); 
//			if(!file.exists()) { 
//				file.mkdir(); 
//			} 
//			
//			final int width = data.width;
//			final int height = data.height;
//			final int cellWidth = width/grid.x;
//			final int cellHeight = height/grid.y;
//			
//			for(int i=0; i<max; i++) { 
//				final int x = i%grid.x;
//				final int y = i/grid.x;
//				
//				final ImageData newData = new ImageData(cellWidth, cellHeight, 32, new PaletteData( 0xFF0000, 0xFF00, 0xFF ));
//				for(int ix=0; ix<cellWidth; ix++) {
//					for(int iy=0; iy<cellHeight; iy++) {
//						final int alhpa = data.getAlpha(x*cellWidth+ix, y*cellHeight+iy);
//						newData.setAlpha(ix, iy, alhpa);
//						
//						final int pix = data.getPixel(x*cellWidth+ix, y*cellHeight+iy);
//						final RGB rgb = data.palette.getRGB(pix);
//						
//						final int newPix = newData.palette.getPixel(rgb);
//						newData.setPixel(ix, iy, newPix);
//					} 
//				} 
//				final ImageLoader loader = new ImageLoader(); 
//				loader.data = new ImageData[]{newData}; 
//				
//				loader.save(newPath+FileHelper.DECOLLATOR+i+".png", SWT.IMAGE_PNG); 
//			}
//		} catch (Exception e) { 
//			e.printStackTrace();
//		} 
//	} 
//	
//	static void makeShape() {
//		final String path = "D:\\pic\\gyx\\Map\\dituzonglan\\tututuut\\";
//		final String name = "shade";
//		final String replace = "shape";
//		try {
//			for(int n=1; n<5; n++) {
//				final ImageData data = new ImageData(new FileInputStream(new File(path + name + n +".png"))); 
//				
//				final ImageData newData = new ImageData(data.width, data.height, 32, new PaletteData( 0xFF0000, 0xFF00, 0xFF ));
//				for(int i=0; i<data.width; i++) { 
//					for(int j=0; j<data.height; j++) { 
//						final RGB rgb = new RGB(255,0,0);
//						final int color = newData.palette.getPixel(rgb);
//						newData.setPixel(i, j, color); 
//						
//						final int alpha = data.getAlpha(i, j); 
//						if(alpha >= 255) {
//							newData.setAlpha(i, j, 255);  
//						} else {
//							newData.setAlpha(i, j, 0);  
//						} 
//					} 
//				} 
//				
//				final ImageLoader loader = new ImageLoader();  
//				loader.data = new ImageData[]{newData}; 
//				
//				loader.save(path + replace + n +".png", SWT.IMAGE_PNG); 
//			}
//		} catch (Exception e) { 
//		} 
//	} 
//	
//	public static void makeImage(final String input, final String output) {
//		try {
//			final ImageData data = new ImageData(new FileInputStream(new File(input))); 
//			
//			final int radius = 50; 
//			
//			final ImageData newData = new ImageData(data.width, data.height, 32, new PaletteData( 0xFF0000, 0xFF00, 0xFF ));
//			for(int i=0; i<data.width; i++) { 
//				for(int j=0; j<data.height; j++) { 
//					
//					final int pix = data.getPixel(i, j);
//					newData.setPixel(i, j, pix); 
//					
//					int alphaCount = 0;  
//					int alphaSum = 0; 
//					
//					for(int x=-radius; x<=radius; x++) {
//						for(int y=-radius; y<=radius; y++) { 
//							final int ix = i + x;
//							final int jy = j + y;
//							if((ix>=0 && ix<data.width) && (jy>=0 && jy<data.height)) {
//								alphaSum += data.getAlpha(ix, jy); 
//								alphaCount++; 
//							} 
//						} 
//					} 
//					
//					final int alpha = Math.round((float)alphaSum/alphaCount);
//					if(alphaCount == 0) { 
//						System.err.println("not expected!" + i + "," + j); 
//						newData.setAlpha(i, j, 1);  
//					} else { 
//						if(alpha > 255) { 
//							newData.setAlpha(i, j, 255);  
//						} else if(alpha < 0) { 
//							newData.setAlpha(i, j, 0);  
//						} else { 
//							newData.setAlpha(i, j, alpha);  
//						} 
//					} 
//				} 
//			} 
//			
//			final ImageLoader loader = new ImageLoader();  
//			loader.data = new ImageData[]{newData};
//			
//			loader.save(output, SWT.IMAGE_PNG); 
//			
//		} catch (Exception e) { 
//		} 
//	} 
//	
//	static void transit(String pathStart, String pathEnd, String pathFolder, String label) {
//		try {
//			ImageData dataStart = new ImageData(new FileInputStream(new File(pathStart))); 
//			ImageData dataEnd = new ImageData(new FileInputStream(new File(pathEnd))); 
//			
//			final int width = dataStart.width;
//			final int height = dataStart.height;
//			
//			int count = 1; 
//			boolean flag = true; 
//			
//			while(flag) { 
//				flag = false; 
//				
//				ImageData newData = new ImageData(width, height, 32, new PaletteData( 0xFF0000, 0xFF00, 0xFF ));
//				
//				for(int i=0; i<width; i++) { 
//					for(int j=0; j<height; j++) { 
//						
//						final int alphaStart = dataStart.getAlpha(i, j);
//						final int alphaEnd = dataEnd.getAlpha(i, j);
//						
//						final RGB rgb = new RGB(255,0,0);
//						final int color = newData.palette.getPixel(rgb);
//						newData.setPixel(i, j, color); 
//						
//						int newAlpha = 0;
//						int newAlphaCount = 0;
//						final int radius = ElfRandom.nextInt(0, 5); 
//						
//						for(int x=-radius; x<=radius; x++) { 
//							for(int y=-radius; y<=radius; y++) { 
//								final int ix = i+x; 
//								final int jy = j+y; 
//								if(ix>=0 && ix<width && jy>=0 && jy<height) { 
//									final int a = dataStart.getAlpha(ix, jy); 
//									if(a > 0) {
//										newAlpha += 255;
//									} 
//									
//									newAlphaCount++; 
//								} 
//							} 
//						} 
//						
//						if(j == 0 && count == 1) { 
//							System.err.println("x "+i+", alpha "+newAlpha+", count "+newAlphaCount);
//						}
//						
//						newAlpha = Math.round(((float)newAlpha/newAlphaCount));
//						
//						if(alphaEnd >= 255) {
//							newData.setAlpha(i, j, 255); 
//						} else if(alphaStart>=255 && newAlpha < 255) { 
//							flag = true; 
//							newData.setAlpha(i, j, 0); 
//						} else { 
//							newData.setAlpha(i, j, alphaStart); 
//						}
//					} 
//				} 
//				
//				
//				
//				final ImageLoader loader = new ImageLoader();  
//				loader.data = new ImageData[]{newData}; 
//				
//				loader.save(pathFolder+FileHelper.DECOLLATOR+label+count+".png", SWT.IMAGE_PNG); 
//				
//				System.err.println("count " + count); 
//				count++;
//				
//				dataStart = newData;
//			}
//			
//		} catch (Exception e) { 
//			e.printStackTrace(); 
//		} 
//	} 
//	
//	static ImageData merge(ImageData data1, float rate1, ImageData data2, float rate2) {
//		if(data1 != null && data2 != null && data1.width==data2.width && data1.height == data2.height) { 
//			final int width = data1.width;
//			final int height = data1.height;
//			
//			ImageData newData = new ImageData(width, height, 32, new PaletteData( 0xFF0000, 0xFF00, 0xFF ));
//			
//			for(int x=0; x<width; x++) {
//				for(int y=0; y<height; y++) {
//					final int pixel1 = data1.getPixel(x, y); 
//					final int pixel2 = data2.getPixel(x, y); 
//					final RGB rgb1 = data1.palette.getRGB(pixel1);
//					final RGB rgb2 = data2.palette.getRGB(pixel2);
//					final int alpha1 = data1.getAlpha(x, y); 
//					final int alpha2 = data2.getAlpha(x, y); 
//					
//					final int r = Math.min(255, Math.round(rgb1.red*rate1 + rgb2.red*rate2));
//					final int g = Math.min(255, Math.round(rgb1.green*rate1 + rgb2.green*rate2));
//					final int b = Math.min(255, Math.round(rgb1.blue*rate1 + rgb2.blue*rate2));
//					final int a = Math.min(255, Math.round(alpha1*rate1 + alpha2*rate2));
//					final RGB newRGB = new RGB(r, g, b); 
//					final int newPixel = newData.palette.getPixel(newRGB);
//					
//					newData.setPixel(x, y, newPixel); 
//					newData.setAlpha(x, y, a); 
//				} 
//			} 
//			
//			return newData;
//		} 
//		
//		return null;
//	} 
//	
//	public static void mergeImages(final String path) { 
//		final String s1 = path+"\\1";
//		final String s2 = path+"\\2";
//		final String s3 = path+"\\3";
//		final String s4 = path+"\\output\\"; 
//		
//		final File f4 = new File(s4);
//		f4.mkdir();
//		
//		final File file1 = new File(s1); 
//		final File file2 = new File(s2); 
//		final File file3 = new File(s3); 
//		
//		//
//		final File [] fs2 = file2.listFiles();
//		for(int i=0; i<fs2.length; i++) {
//			final File f = fs2[i];
//			try {
//				final ImageData data = new ImageData(new FileInputStream(f)); 
//				final ImageLoader loader = new ImageLoader();  
//				loader.data = new ImageData[]{data}; 
//				final String num = toNum(i+1);
//				loader.save(s4+num+".png", SWT.IMAGE_PNG); 
//			} catch (Exception e) {
//			} 
//		} 
//		
//		final File [] fs1 = file1.listFiles();
//		final File [] fs3 = file3.listFiles();
//		if(fs1.length == fs3.length) { 
//			for(int i=0;i<fs1.length; i++ ) { 
//				try { 
//					final ImageData data1 = new ImageData(new FileInputStream(fs1[i])); 
//					final ImageData data3 = new ImageData(new FileInputStream(fs3[i])); 
//					
//					final float rate = (1f+i)/(fs1.length+1f) ;
//					final ImageData data2 = merge(data1, rate, data3, 1-rate); 
//					
//					final ImageLoader loader = new ImageLoader();  
//					loader.data = new ImageData[]{data2}; 
//					final String num = toNum(fs2.length+i+1);
//					loader.save(s4+num+".png", SWT.IMAGE_PNG); 
//				} catch (Exception e) { 
//				}
//			}
//		}
//	} 
//	
//	static String toNum(int i) {
//		return (i>=100) ? ""+(i):(i>=10)?"0"+(i):"00"+(i);
//	}
//	
//	public static void main(final String [] args) {
////		final String path = "D:\\pic\\gyx\\RPG maker\\RMVXA\\RPG Maker VX Ace\\RTP\\Graphics\\Animations\\";
////		
////		final File file = new File(path);
////		final String [] files = file.list();
////		
////		for(String p : files) { 
////			final String newPath = path+p;
////			try { 
////				ImageData data = new ImageData(new FileInputStream(new File(newPath))); 
////				final int x = data.width/192;
////				final int y = data.height/192;
////				breakImage(data, newPath, new ElfPointi(x, y), x*y);
////			} catch (Exception e) { 
////				e.printStackTrace(); 
////			} 
////		} 
//		
////		makeImage();
////		final String path = "D:\\pic\\gyx\\Map\\dituzonglan\\tututuut\\";
////		final String shape1 = "shape1.png"; 
////		final String shape2 = "shape2.png"; 
////		final String dst = "shape1-2"; 
////		transit(path+shape1, path+shape2, path+dst, "s1s2_");
//		
////		makeShape(); 
//		
////		File [] files = new File(path + dst).listFiles();
////		for(File f : files) {
////			final String p = f.getAbsolutePath();
////			makeImage(p, p);
////			System.err.println(p + " finished!");
////		}
//		
////		mergeImages("D:\\pic\\gyx\\��Ч\\������Ч");
//		
//		final String path = "D:\\pic\\A图片\\";
//		final String [] fullPaths = FileHelper.getFullPaths(path);
//		
//		for(String p : fullPaths) {
//			breakImage(ImageHelper.read(p), p, new ElfPointi(12, 8), 96); 
//		} 
//		
//		System.out.println("breakImage done!");
//	} 
//} 
