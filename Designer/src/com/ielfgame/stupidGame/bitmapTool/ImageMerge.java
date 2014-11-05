//package com.ielfgame.stupidGame.bitmapTool;
//
//import org.eclipse.swt.graphics.ImageData;
//
//public class ImageMerge {
//	//
//	public static void main(final String [] args) {
//		final String basePath1 = "D:\\pic\\gyx\\UI\\ս�������ְ�\\�˺�����\\";
//		final String [] resids1 = {
//				basePath1+"-.png",
//				basePath1+"0.png",
//				basePath1+"1.png",
//				basePath1+"2.png",
//				basePath1+"3.png",
//				basePath1+"4.png",
//				basePath1+"5.png",
//				basePath1+"6.png",
//				basePath1+"7.png",
//				basePath1+"8.png",
//				basePath1+"9.png",
//		};
//		final String baseDist1 = "D:\\pic\\gyx\\UI\\ս�������ְ�\\�˺�����\\merge.png";
//		
//		merge(resids1, baseDist1);
//		
//		final String basePath2 = "D:\\pic\\gyx\\UI\\ս�������ְ�\\��������\\";
//		final String [] resids2 = {
//				basePath2+"+.png",
//				basePath2+"0.png",
//				basePath2+"1.png",
//				basePath2+"2.png",
//				basePath2+"3.png",
//				basePath2+"4.png",
//				basePath2+"5.png",
//				basePath2+"6.png",
//				basePath2+"7.png",
//				basePath2+"8.png",
//				basePath2+"9.png",
//		};
//		final String baseDist2 = "D:\\pic\\gyx\\UI\\ս�������ְ�\\��������\\merge.png";
//		merge(resids2, baseDist2);
//		
//		//D:\pic\gyx\UI\�غ�����
//		final String basePath3 = "D:\\pic\\gyx\\UI\\�غ�����\\";
//		final String [] resids3 = {
//				basePath3+"0.png",
//				basePath3+"1.png",
//				basePath3+"2.png",
//				basePath3+"3.png",
//				basePath3+"4.png",
//				basePath3+"5.png",
//				basePath3+"6.png",
//				basePath3+"7.png",
//				basePath3+"8.png",
//				basePath3+"9.png",
//		};
//		final String baseDist3 = "D:\\pic\\gyx\\UI\\�غ�����\\merge.png"; 
//		merge(resids3, baseDist3); 
//	} 
//	
//	static void merge(final String [] resids, final String path) {
//		final ImageData [] datas = new ImageData[resids.length];
//		for(int i=0; i<datas.length; i++) {
//			datas[i] = ImageHelper.read(resids[i]);
//		} 
//		
//		final int width = datas[0].width;
//		final int height = datas[0].height;
//		final ImageData dist = ImageHelper.create(width*datas.length, height);
//		
//		for(int i=0; i<datas.length; i++) {
//			for(int x=0; x<width; x++) {
//				for(int y=0; y<height; y++) {
//					final int alpha = datas[i].getAlpha(x, y);
//					final int pixel = datas[i].getPixel(x, y);
//					
//					dist.setAlpha(x+i*width, y, alpha);
//					dist.setPixel(x+i*width, y, pixel);
//				}
//			}
//		} 
//		
//		ImageHelper.save(dist, path);
//	} 
//}
