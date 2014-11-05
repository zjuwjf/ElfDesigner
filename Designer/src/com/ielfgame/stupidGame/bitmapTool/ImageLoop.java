//package com.ielfgame.stupidGame.bitmapTool;
//
//import java.io.File;
//
//import org.eclipse.swt.graphics.ImageData;
//import org.eclipse.swt.graphics.RGB;
//
//import com.ielfgame.stupidGame.utils.FileHelper;
//
//import elfEngine.opengl.BlendMode;
//
//public class ImageLoop {
//	
//	
//	//
//	public static void main(final String [] args) { 
//		final String path = "D:\\pic\\lkx\\xiaoguo1"; 
//		make(path, BlendMode.ACTIVLE); 
//	} 
//	
//	static void make(final String path, BlendMode mode) { 
//		final File file = new File(path+"\\����");
//		file.mkdir();
//		
//		final String [] resids = FileHelper.getFullPaths(path); 
//		final int len = resids.length; 
//		
//		for(int i=0; i<len; i++) { 
//			final ImageData data0 = ImageHelper.read(resid0(resids, i));
//			final ImageData data1 = ImageHelper.read(resid1(resids, i));
//			final ImageData data2 = ImageHelper.read(resid2(resids, i));
//			
//			final int width = data0.width;
//			final int height = data0.height; 
//			
//			final ImageData output = ImageHelper.create(width, height);
//			
//			final ImageData dataAdd = (data1==null)? data2:data1;
//			
//			for(int x=0; x<width; x++) { 
//				for(int y=0; y<height; y++) { 
//					final int alpah0 = data0.getAlpha(x, y);
//					final int pixel0 = data0.getPixel(x, y);
//					final RGB rgb0 = data0.palette.getRGB(pixel0);
//					
//					final RGB rgbAdd;
//					final int alphaAdd; 
//					if(dataAdd != null) { 
//						alphaAdd = dataAdd.getAlpha(x, y);
//						final int p = dataAdd.getPixel(x, y);
//						rgbAdd = dataAdd.palette.getRGB(p);
//					} else { 
//						alphaAdd = 0;
//						rgbAdd = new RGB(0,0,0);
//					} 
//					
//					final int alphaOut ;
//					final RGB rgbOut;
//					if(mode == BlendMode.ACTIVLE) {
//						alphaOut = Math.min(255, alpah0 + alphaAdd);
//						final int red = rgb0.red + rgbAdd.red;
//						final int green = rgb0.green + rgbAdd.green;
//						final int blue = rgb0.blue + rgbAdd.blue;
//						
//						rgbOut = new RGB(Math.min(255, red), Math.min(255, green), Math.min(255, blue));
//					} else { 
//						final float rate = alphaAdd/255f; 
//						final float rate2 = 1-rate; 
//						alphaOut = Math.min(255, Math.round(alpah0*rate2 + alphaAdd*rate));
//						final int red = Math.round(rgb0.red*rate2 + rgbAdd.red*rate);
//						final int green = Math.round(rgb0.green*rate2 + rgbAdd.green*rate);
//						final int blue = Math.round(rgb0.blue*rate2 + rgbAdd.blue*rate);
//						
//						rgbOut = new RGB(Math.min(255, red), Math.min(255, green), Math.min(255, blue));
//					} 
////					
//					final int pixel = output.palette.getPixel(rgbOut);
//					output.setAlpha(x, y, alphaOut);
//					output.setPixel(x, y, pixel);
//				} 
//			}
//			
//			ImageHelper.save(output, path+"\\����\\"+toNum(i)+".png");
//		} 
//	} 
//	
//	static String resid0(String [] resids, int i) {
//		return resids[i];
//	} 
//	
//	static String resid1(String [] resids, int i) {
//		final int len = resids.length;
//		final int index = (len+1)/2+i;
//		if(index < len) {
//			return resids[index];
//		} else { 
//			return null; 
//		}
//	}
//	static String resid2(String [] resids, int i) {
//		final int len = resids.length; 
//		final int index = i-(len+1)/2; 
//		if(index >= 0) { 
//			return resids[index];
//		} else { 
//			return null; 
//		}  
//	}
//	
//	static String toNum(int i) {
//		if(i<10) {
//			return "00"+i;
//		} else if(i<100) {
//			return "0"+i;
//		} else {
//			return ""+i;
//		}
//	}
//} 
