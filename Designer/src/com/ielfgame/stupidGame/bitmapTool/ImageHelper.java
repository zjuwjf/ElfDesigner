package com.ielfgame.stupidGame.bitmapTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import elfEngine.opengl.BufferHelper;

public class ImageHelper {

	public static ByteBuffer imageToBuffer(final ImageData source, int power2Width, int power2Height) {
		final ByteBuffer sByteBuffer = BufferHelper.sByteBuffer;
		sByteBuffer.clear();

		final int width = source.width;
		final int height = source.height;

		for (int y = 0; y < power2Height; y++) {
			for (int x = 0; x < power2Width; x++) {
				if (x < width && y < height) {
					int index = (y * power2Width + x) * 4;

					int pixel = source.getPixel(x, y);
					RGB rgb = source.palette.getRGB(pixel);
					boolean hasAlpha = false;
					int alphaValue = 0;
					if (source.alphaData != null && source.alphaData.length > 0) {
						hasAlpha = true;
						alphaValue = source.getAlpha(x, y);
					}

					sByteBuffer.position(index);
					sByteBuffer.put((byte) (rgb.red));
					sByteBuffer.put((byte) (rgb.green));
					sByteBuffer.put((byte) (rgb.blue));

					if (hasAlpha) {
						sByteBuffer.put((byte) (alphaValue));
					} else {
						sByteBuffer.put((byte) (255));
					}
				} else {
					final int index = (y * power2Width + x) * 4;
					sByteBuffer.position(index);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
				}
			}
		}

		sByteBuffer.flip();
		return sByteBuffer;
	}

	public static ImageData read(final String resid) {
		try {
			final FileInputStream fileInputStream = new FileInputStream(resid);
			final ImageData data = new ImageData(fileInputStream);

			try {
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void save(ImageData data, final String path) {
		final ImageLoader loader = new ImageLoader();
		/**
		 * When saving jpeg files, the value is from 1 to 100, where 1 is very
		 * high compression but low quality, and 100 is no compression and high
		 * quality; default is 75.
		 */
		// loader.compression = 1;

		loader.data = new ImageData[] { data };
		if (path.endsWith(".png")) {
			System.err.println("save " + path);
			loader.save(path, SWT.IMAGE_PNG);
		} else if (path.endsWith(".jpg")) {
			System.err.println("save " + path);
			loader.save(path, SWT.IMAGE_JPEG);
		} else {
			System.err.println("save " + path + ".png");
			loader.save(path + ".png", SWT.IMAGE_PNG);
		}
	}
	
	private final static ImageData createImageDataFromFileName(final String name) {
		final ImageLoader loader = new ImageLoader();
		return loader.load(name)[0];
	}
	
	private final static void saveImageData(ImageData data, final OutputStream os,final int compression, final int type) {
		final ImageLoader loader = new ImageLoader();
		loader.compression = compression;
		loader.data = new ImageData[]{data};
		loader.save(os, type);
	}
	
	public static void create2JPGfromPng(final ImageData pngData, final OutputStream jpg1, final OutputStream jpg2, final int compression) {
		/***
		 * rgba
		 */
		final ImageData data = pngData;
		final ImageData data_mask = new ImageData(data.width, data.height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		for (int i = 0; i < data.width; i++) {
			for (int j = 0; j < data.height; j++) {
				final RGB rgb = new RGB(data.getAlpha(i, j), data.getAlpha(i, j), data.getAlpha(i, j));
				data_mask.setPixel(i, j, data_mask.palette.getPixel(rgb));
			}
		}
		
		saveImageData(data, jpg1, compression, SWT.IMAGE_JPEG);
		saveImageData(data_mask, jpg2, compression, SWT.IMAGE_JPEG);
	}

	public static void create2JPGfromPng(final String pngPath, final String jpg1, final String jpg2, final int quality) {
		final ImageLoader loader = new ImageLoader();
		final ImageData[] datas = loader.load(pngPath);
		/***
		 * rgba
		 */
		final ImageData data = datas[0];
		final ImageData data_mask = new ImageData(data.width, data.height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		for (int i = 0; i < data.width; i++) {
			for (int j = 0; j < data.height; j++) {
				final RGB rgb = new RGB(data.getAlpha(i, j), data.getAlpha(i, j), data.getAlpha(i, j));
				data_mask.setPixel(i, j, data_mask.palette.getPixel(rgb));
			}
		}

		loader.data = new ImageData[] { data };
		loader.compression = quality;
		loader.save(jpg1, SWT.IMAGE_JPEG);

		loader.data = new ImageData[] { data_mask };
		loader.compression = quality;
		loader.save(jpg2, SWT.IMAGE_JPEG);
	}
	
	public static void createPngFrom2Jpg(final ImageData jpg1, final ImageData jpgmask, final OutputStream png, final int compression) {
		final ImageData data = jpg1;
		final ImageData data_mask = jpgmask;
		
		assert(data.width==data_mask.width && data.height==data_mask.height);
		
		final ImageData newData = new ImageData(data.width, data.height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));

		for (int i = 0; i < data.width; i++) {
			for (int j = 0; j < data.height; j++) {
				int pixel = data_mask.getPixel(i, j);
				RGB rgb = data_mask.palette.getRGB(pixel);
				
				final int pixel2 = data.getPixel(i, j);
				RGB rgb2 = data.palette.getRGB(pixel2);
				
				newData.setPixel(i, j, newData.palette.getPixel(rgb2));
				newData.setAlpha(i, j, rgb.blue);
			}
		}
		
		saveImageData(newData, png, compression, SWT.IMAGE_PNG);
	}

	public static void createPngFrom2Jpg(final String jpg1, final String jpgmask, final String png) {
		final ImageLoader loader = new ImageLoader();
		ImageData[] datas = loader.load(jpg1);
		final ImageData data = datas[0];

		datas = loader.load(jpgmask);
		final ImageData data_mask = datas[0];
		
		assert(data.width==data_mask.width && data.height==data_mask.height);
		
		final ImageData newData = new ImageData(data.width, data.height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));

		for (int i = 0; i < data.width; i++) {
			for (int j = 0; j < data.height; j++) {
				int pixel = data_mask.getPixel(i, j);
				RGB rgb = data_mask.palette.getRGB(pixel);
				
				final int pixel2 = data.getPixel(i, j);
				RGB rgb2 = data.palette.getRGB(pixel2);
				
				newData.setPixel(i, j, newData.palette.getPixel(rgb2));
				newData.setAlpha(i, j, rgb.blue);
			}
		}

		loader.data = new ImageData[] { newData };
		loader.compression = 0;
		loader.save(png, SWT.IMAGE_PNG);
	}
	
	public static void createPng2png(final String png1, final String png2) {
		final ImageLoader loader = new ImageLoader();
		
		ImageData[] datas = loader.load(png1);
		final ImageData data = datas[0];
		
		loader.data = new ImageData[]{ data };
		loader.compression = 0;
		loader.save(png2, SWT.IMAGE_PNG);
	}

	public static void main(final String[] args) {
		final String head = "D:\\transfer\\";
//		create2JPGfromPng(head + "D_pic_gyx_UI_Map_jitan.png", head + "D_pic_gyx_UI_Map_jitan.jpg", head + "D_pic_gyx_UI_Map_jitan.mask.jpg", 75);
//		createPngFrom2Jpg(head + "D_pic_gyx_UI_Map_jitan.jpg", head + "D_pic_gyx_UI_Map_jitan.mask.jpg", head + "D_pic_gyx_UI_Map_jitan.recover.png");
//		createPng2png(head + "D_pic_gyx_UI_Map_jitan.png", head + "D_pic_gyx_UI_Map_jitan222.png");
		
		createGTfromPng(head + "D_pic_gyx_UI_Map_jitan.png", head + "D_pic_gyx_UI_Map_jitan.png.gt", 75);
		createPngFromGT(head + "D_pic_gyx_UI_Map_jitan.png.gt", head + "D_pic_gyx_UI_Map_jitan.gt.png", 0);
	}

	/***
	 * png -> glee texture
	 */
	public static void createGTfromPng(final String pngPath, final String gtPath, final int quality) {
		
		final ByteArrayOutputStream rgb_baos = new ByteArrayOutputStream();
		final ByteArrayOutputStream alpha_baos = new ByteArrayOutputStream();
		
		create2JPGfromPng(createImageDataFromFileName(pngPath), rgb_baos, alpha_baos, quality);
		
		final ByteArrayInputStream rgb_bais = new ByteArrayInputStream(rgb_baos.toByteArray());
		final ByteArrayInputStream alpha_bais = new ByteArrayInputStream(alpha_baos.toByteArray());
		
		try {
			final File f = new File(gtPath);
			if(!f.getParentFile().isDirectory()) {
				f.getParentFile().mkdirs();
			}
			
			final int size1 = rgb_bais.available();
			
			final FileOutputStream fos = new FileOutputStream(f);
			
			fos.write((size1&0xff000000) >> 24);
			fos.write((size1&0xff0000) >> 16);
			fos.write((size1&0xff00) >> 8);
			fos.write((size1&0xff) >> 0);
			
			int c;
			while((c=rgb_bais.read()) != -1) {
				fos.write( c );
			}
			
			while((c=alpha_bais.read()) != -1) {
				fos.write(c);
			}
			fos.flush();
			fos.close();
			
			rgb_bais.close();
			alpha_bais.close();
			rgb_baos.close();
			alpha_baos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createPngFromGT(final String gtPath, final String pngPath, int compress) {
		try {
			final FileInputStream fis = new FileInputStream(new File(gtPath));
//			final int totalsize = fis.available();
			
			final int a = fis.read();
			final int b = fis.read();
			final int c = fis.read();
			final int d = fis.read();
			
			final int rgbsize = (a<<24) + (b<<16) + (c<<8) + d;
			
			final InputStream isRgb = new InputStream() {
				int readCount = 0;
				public int read() throws IOException {
					if(readCount >= rgbsize) {
						return -1;
					}
					readCount++;
					return fis.read();
				}
			};
			
			final InputStream isAlpha = new InputStream() {
				public int read() throws IOException {
					return fis.read();
				}
			};
			
			ImageLoader il = new ImageLoader();
			final ImageData rgbdata = il.load(isRgb)[0];
			final ImageData alphadata = il.load(isAlpha)[0];
			
			final FileOutputStream fos = new FileOutputStream(pngPath);
			createPngFrom2Jpg(rgbdata, alphadata, fos, compress);
			
			fos.flush();
			fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ImageData create(final int width, final int height) {
		return new ImageData(width, height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	}
}
