package com.ielfgame.stupidGame.bitmapTool;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

public class CompareImages {
	
	public static ImageData sub(final ImageData advanced, final ImageData base) {
		assert(base != null);
		assert(advanced != null);
		
		assert(base.width == advanced.width);
		assert(base.height == advanced.height);
		
		ImageData newImage = ImageHelper.create(base.width, base.height);
		
		int minV = 255;
		int maxV = 0;
		
		for(int i=0; i<base.width; i++) {
			for(int j=0; j<base.height; j++) {
				final int baseAlpha = base.getAlpha(i, j);
				final int basePixel = base.getPixel(i, j);
				final RGB baseRrb= base.palette.getRGB(basePixel);
				
				final int advancedAlpha = advanced.getAlpha(i, j);
				final int advancedPixel = advanced.getPixel(i, j);
				final RGB advancedRgb = advanced.palette.getRGB(advancedPixel);
				
				final int subAlpha = advancedAlpha - baseAlpha;
				final int subR = advancedRgb.red - baseRrb.red;
				final int subG = advancedRgb.green - baseRrb.green;
				final int subB = advancedRgb.blue - baseRrb.blue;
				
				if (subAlpha > maxV) maxV = subAlpha;  
                if (subAlpha < minV) minV = subAlpha; 
                
                if (subR > maxV) maxV = subR;  
                if (subR < minV) minV = subR; 
                
                if (subG > maxV) maxV = subG;  
                if (subG < minV) minV = subG; 
                
                if (subB > maxV) maxV = subB;  
                if (subB < minV) minV = subB; 
				
			}
		}
		
		minV = Math.abs(minV);  
	    maxV += minV;  
	    
	    for(int i=0; i<base.width; i++) {
			for(int j=0; j<base.height; j++) {
				final int baseAlpha = base.getAlpha(i, j);
				final int basePixel = base.getPixel(i, j);
				final RGB baseRrb= base.palette.getRGB(basePixel);
				
				final int advancedAlpha = advanced.getAlpha(i, j);
				final int advancedPixel = advanced.getPixel(i, j);
				final RGB advancedRgb = advanced.palette.getRGB(advancedPixel);
				
				int subA = advancedAlpha - baseAlpha;
				int subR = advancedRgb.red - baseRrb.red;
				int subG = advancedRgb.green - baseRrb.green;
				int subB = advancedRgb.blue - baseRrb.blue;
				
				subA += minV;
				subR += minV;
				subG += minV;
				subB += minV;
				
				subA = (int)(subA * ((double)255/(double)maxV));
				subR = (int)(subR * ((double)255/(double)maxV));
				subG = (int)(subG * ((double)255/(double)maxV));
				subB = (int)(subB * ((double)255/(double)maxV));
				
				if(baseAlpha != advancedAlpha || basePixel != advancedPixel) {
					final int subPixel = newImage.palette.getPixel(new RGB(subR, subG, subB));
	                
	                newImage.setAlpha(i, j, subA);
					newImage.setPixel(i, j, subPixel);
				} else {
	                newImage.setAlpha(i, j, 0);
					newImage.setPixel(i, j, 0);
				}
                
			}
		}
		
		return newImage;
	}
	
	
	public static void main(final String [] args) {
		
		final String head = "D:/svn_project_pet/develop/editor/Resources/image/battle/skin/012/";
		final String basePath = head+"skin_12t.png";
		final String advancedPath = head+"face_12_atk.png";
		
		final String savePath = head + "test.png";
		
		final ImageData baseData = ImageHelper.read(basePath);
		final ImageData advancedData = ImageHelper.read(advancedPath);
		
		final ImageData newImageData = sub(advancedData, baseData);
		ImageHelper.save(newImageData, savePath);
	}
	
}
