package elfEngine.basic.node.nodeText.richLabel;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import javax.imageio.ImageIO;

import _Res.code.InnerRes;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.utils.FileHelper;

public class AWTFontImageHelper {
	private static Font DefaultFont;
	static { 
		try {
			
			Locale.setDefault(Locale.ENGLISH); 
			
			final InputStream is = AWTFontImageHelper.class.getResourceAsStream("Helvetica.ttf"); 
			DefaultFont = Font.createFont(Font.TRUETYPE_FONT, is); 
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
	
	/***
	 * font + image
	 */
	
	private final static HashMap<String, Font> BasicFontMap = new HashMap<String, Font>();
	private final static HashMap<String, Font> FontMap = new HashMap<String, Font>();
	
	public final static String [] getFontNames() {
		final String artDir = ResManager.getSingleton().getDesignerFontAsset();
		final LinkedList<String> ttfList = FileHelper.getSimplePahIds(new File(artDir), new String[]{".ttf"}, true);
		final String[] ret = new String[ttfList.size() + 1];
		
		ret[0] = "default";
		for(int i=1; i<ret.length; i++) {
			ret[i] = FileHelper.getSimpleNameWithSuffix( ttfList.get(i-1) );
		} 
		
		return ret;
	}
	
	private static final Font getBasicFontByName(final String name) {
		if(name == null || name.equals("default") || name.equals("null")) {
			return DefaultFont;
		}
		
		Font f = BasicFontMap.get(name); 
		if(f == null) {
			try {
				final String fontPath = ResManager.getSingleton().getDesignerFontAsset() + FileHelper.DECOLLATOR + name; 
				//name like fontA.ttf 
				
				final File file = new File(fontPath); 
				if(file.exists() && file.isFile() && fontPath.endsWith(".ttf")) { 
					f = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)); 
					
					System.err.println(new StringBuilder(name).append("->").append(f.getFamily()).append(":").append(f.getFontName())); 
					
					BasicFontMap.put(name, f); 
				} else {
					final InputStream is = InnerRes.getFontInputStream(name);
					f = Font.createFont(Font.TRUETYPE_FONT, is); 
					is.close();
					BasicFontMap.put(name, f);
				}
				
				
			} catch (Exception e) { 
				System.err.println(name);
				e.printStackTrace();
			} 
		}
		
		if(f == null) { 
			f = DefaultFont; 
		} 
		
		return f;
	}
	
	public static Font getAwtFont(String name, float size, int style) {
		final Font f = FontMap.get(name); 
		if(f != null) { 
			return f.deriveFont(style, size); 
		} else {
			final Font basicFont = getBasicFontByName(name);
			return basicFont.deriveFont(style, size); 
		}
	} 
	
	public static BufferedImage getAWTBufferedImage(String name) {
		try {
			final String imagePath = ResManager.getSingleton().getDesignerImageAsset() +FileHelper.DECOLLATOR+ name; 
			if(ResJudge.isLegalResAndExisted(imagePath)) {
				try { 
					return ImageIO.read(new File(imagePath));
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void clearCache() {
		BasicFontMap.clear();
		FontMap.clear();
	}
}
