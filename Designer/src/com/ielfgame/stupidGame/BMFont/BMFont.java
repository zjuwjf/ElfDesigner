package com.ielfgame.stupidGame.BMFont;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

/**
 * A simple font implementation using text files from BMFont. A more robust
 * solution might use distance field rendering for smooth size-independent
 * scaling.
 * 
 * @author davedes
 */
public class BMFont {
	
	private static HashMap<String, BMFont> sSharedFontManager = new HashMap<String, BMFont>();
	
	public static BMFont getBMFont(final String fontPath) {
		BMFont ret = sSharedFontManager.get(fontPath);
		
		if(ret == null) {
			try {
				final String root = ResManager.getSingleton().getDesignerBMFontAsset();

				final String head = root + FileHelper.DECOLLATOR;

				final String pngPath = fontPath.replace(".fnt", ".png");

				ret = new BMFont(head + fontPath, head + pngPath);
				
				sSharedFontManager.put(fontPath, ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	

	// TODO: fix up baseLine, ascent, descent, etc.
	int lineHeight;
	int baseLine;
	int descent;
	int pages;
	Glyph[] glyphs;
	TextureRegion[] texturePages;

	public static class Glyph {
		int chr;
		int x, y, width, height;
		int xoffset, yoffset, xadvance;
		int[] kerning;
		int page;
		TextureRegion region;

		/**
		 * Get the kerning offset between this character and the specified
		 * character.
		 * 
		 * @param c
		 *            The other code point
		 * @return the kerning offset
		 */
		public int getKerning(int c) {
			if (kerning == null)
				return 0;
			return kerning[c];
		}

		void updateRegion(TextureRegion tex) {
			if (region == null)
				region = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
			region.set(tex, x, y, width, height);
		}
	}

	public BMFont(String fontDef, String texture) throws IOException {
		this(fontDef, new Texture(texture));
	}

	public BMFont(String fontDef, Texture texture) throws IOException {
		this(new FileInputStream(fontDef), new TextureRegion(texture));
	}

	public BMFont(String fontDef, TextureRegion texture) throws IOException {
		this(new FileInputStream(fontDef), texture);
	}

	public BMFont(InputStream fontDef, TextureRegion texture) throws IOException {
		this(fontDef, new TextureRegion[] { texture });
	}

	public BMFont(InputStream fontDef, TextureRegion[] texturePages) throws IOException {
		this(fontDef, Charset.defaultCharset(), texturePages);
	}

	public BMFont(InputStream fontDef, Charset charset, TextureRegion[] texturePages) throws IOException {
		this.texturePages = texturePages;
		parseFont(fontDef, charset);
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public float[] getSize(int designWidth, String text) {
		final CharSequence [] lines = getLines(text, designWidth);
		return getSize(designWidth, text, lines);
	}
	
	private float[] getSize(int designWidth, String text, final CharSequence [] lines) {
		final float[] ret = new float[2];
		
//		if (designWidth <= 0) {
//			float maxWidth = 0;
//			for(CharSequence l : lines) {
//				maxWidth = Math.max(maxWidth, getWidth(l));
//			}
//			ret[0] = maxWidth;
//			ret[1] = getLineHeight() * lines.length;
//		} else {
//			ret[0] = designWidth;
//			ret[1] = getLineHeight() * lines.length;
//		}
		
		float maxWidth = 0;
		for(CharSequence l : lines) {
			maxWidth = Math.max(maxWidth, getWidth(l));
		}
		
		ret[0] = maxWidth;
		ret[1] = getLineHeight() * lines.length;
		
		return ret;
	}
	
	public TextureRegion[] getTexturePages() {
		return texturePages;
	}

	public void drawText(CharSequence text, int x, int y) {
		drawText(text, x, y, 0, text.length());
	}

	public void drawText(CharSequence text, int x, int y, int start, int end) {
		Glyph lastGlyph = null;
		for (int i = start; i < end; i++) {
			char c = text.charAt(i);
			// TODO: make unsupported glyphs a bit cleaner...
			if (c > glyphs.length || c < 0)
				continue;
			Glyph g = glyphs[c];
			if (g == null)
				continue;

			if (lastGlyph != null)
				x += lastGlyph.getKerning(c);

			lastGlyph = g;

			g.region.draw(x + g.xoffset, y + g.yoffset, 1, 1, 0);

			x += g.xadvance;
		}
	}

	public CharSequence[] getLines(CharSequence text, int designWidth) {
		if (designWidth <= 0) {
			designWidth = Integer.MAX_VALUE;
		}
		
		{
			final ArrayList<CharSequence> array = new ArrayList<CharSequence>();
			
			int stepStart = 0;
			for (int i = stepStart; i < text.length(); i++) {
				final char c = text.charAt(i);
				
				if( c == '\n') {
					if(stepStart < i) {
						final CharSequence ct = text.subSequence(stepStart, i );
						array.add(ct);
						stepStart = i + 1;
					} else {
						array.add("");
						stepStart = i + 1;
					}
					
					continue;
				}
				
				//包括第i
				final int width = getWidth(text, stepStart, i+1);

				if (designWidth < width ) {
					if (stepStart <= i ) {
						final CharSequence ct = text.subSequence(stepStart, i );
						array.add(ct);
						stepStart = i;
//						continue;
					} else {
						break;
					}
				}

				//最后一项了
				if (i == text.length() - 1) {
					if(stepStart < i+1) {
						final CharSequence ct = text.subSequence(stepStart, i+1 );
						array.add(ct);
					} else {
						array.add("");
					}
				}
			}

			return array.toArray(new CharSequence[array.size()]);
		}
	}

	public void drawTextInWidth(String text, int designWidth, HorizontalTextAlign align) {

		final CharSequence[] lines = getLines(text, designWidth);
		
		final float [] size = getSize(designWidth, text, lines);
		
		final float maxWidth = size[0];
		final float totalHeight = size[1];
		
		final float startY = (totalHeight) / 2f - getLineHeight() / 2f;
		final float startX = -maxWidth/2f;
		
		for (int l = 0; l < lines.length; l++) {
			final CharSequence ltext = lines[l];
			final float myY = startY - (l) * getLineHeight();
			// final float myY = 0;
			
			final float myWidth = getWidth(ltext);
			float offsetX = 0;
			
			switch (align) {
			case CENTER:
				offsetX = (maxWidth-myWidth)/2;
				break;
			case RIGHT:
				offsetX = (maxWidth-myWidth);
				break;
			}
			
			final float myX = startX + offsetX; // 水平对齐方式

			Glyph lastGlyph = null;

			float x = myX;
			float y = myY;

			for (int i = 0; i < ltext.length(); i++) {
				final char c = ltext.charAt(i);
				// TODO: make unsupported glyphs a bit cleaner...
				if (c > glyphs.length || c < 0)
					continue;
				final Glyph g = glyphs[c];

				if (g == null)
					continue;

				if (lastGlyph != null)
					x += lastGlyph.getKerning(c);

				lastGlyph = g;

				final float hw = g.region.getWidth() / 2f;
				final float hh = g.region.getHeight() / 2f;

				g.region.draw(x + hw + g.xoffset, y + (getLineHeight() / 2f) - g.yoffset - hh, 1, 1, 0);
				x += g.xadvance;
			}
		}
	}

	public int getBaseline() {
		return baseLine;
	}

	public int getWidth(CharSequence text) {
		return getWidth(text, 0, text.length());
	}

	public int getWidth(CharSequence text, int start, int end) {
		Glyph lastGlyph = null;
		int width = 0;
		for (int i = start; i < end; i++) {
			char c = text.charAt(i);
			// TODO: make unsupported glyphs a bit cleaner...
			if (c > glyphs.length || c < 0)
				continue;
			Glyph g = glyphs[c];
			if (g == null)
				continue;

			if (lastGlyph != null)
				width += lastGlyph.getKerning(c);

			lastGlyph = g;
			// width += g.width + g.xoffset;

			// width += g.width + g.xoffset;
			width += g.xadvance;
		}
		return width;
	}

	private static String parse(String line, String tag) {
		tag += "=";
		int start = line.indexOf(tag);
		if (start == -1)
			return "";
		int end = line.indexOf(' ', start + tag.length());
		if (end == -1)
			end = line.length();
		return line.substring(start + tag.length(), end);
	}

	private static int parseInt(String line, String tag) throws IOException {
		try {
			// System.err.println(line);
			// System.err.println(parse(line, tag));
			return Integer.parseInt(parse(line, tag));
		} catch (NumberFormatException e) {
			throw new IOException("data for " + tag + " is corrupt/missing: " + parse(line, tag));
		}
	}

	protected void parseFont(InputStream fontFile, Charset charset) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(fontFile, charset), 512);
		// String info =
		br.readLine();
		String common = br.readLine();
		lineHeight = parseInt(common, "lineHeight");
		baseLine = parseInt(common, "base");
		pages = parseInt(common, "pages");

		// ignore file name, let user specify texture ...

		String line = "";

		ArrayList<Glyph> glyphsList = null;

		int maxCodePoint = 0;

		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			if (line.startsWith("chars")) {
				// System.out.println(line);
				int count = parseInt(line, "count");
				glyphsList = new ArrayList<Glyph>(count);
				continue;
			}
			if (line.startsWith("kernings "))
				break;
			if (!line.startsWith("char "))
				continue;

			Glyph glyph = new Glyph();

			StringTokenizer tokens = new StringTokenizer(line, " =");
			tokens.nextToken();
			tokens.nextToken();
			int ch = Integer.parseInt(tokens.nextToken());
			if (ch > Character.MAX_VALUE)
				continue;
			if (glyphsList == null) // incase some doofus deleted a line in the
									// font def
				glyphsList = new ArrayList<Glyph>();
			glyphsList.add(glyph);
			glyph.chr = ch;
			if (ch > maxCodePoint)
				maxCodePoint = ch;
			tokens.nextToken();
			glyph.x = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.y = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.width = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.height = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.xoffset = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.yoffset = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			glyph.xadvance = Integer.parseInt(tokens.nextToken());
			// page ID
			tokens.nextToken();
			glyph.page = Integer.parseInt(tokens.nextToken());

			if (glyph.page > texturePages.length)
				throw new IOException("not enough texturePages supplied; glyph " + glyph.chr + " expects page index " + glyph.page);
			glyph.updateRegion(texturePages[glyph.page]);

			if (glyph.width > 0 && glyph.height > 0)
				descent = Math.min(baseLine + glyph.yoffset, descent);
		}

		glyphs = new Glyph[maxCodePoint + 1];
		for (Glyph g : glyphsList)
			glyphs[g.chr] = g;

		// int kernCount = parseInt(line, "count");
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			if (!line.startsWith("kerning "))
				break;

			StringTokenizer tokens = new StringTokenizer(line, " =");
			tokens.nextToken();
			tokens.nextToken();
			int first = Integer.parseInt(tokens.nextToken());
			tokens.nextToken();
			int second = Integer.parseInt(tokens.nextToken());
			if (first < 0 || first > Character.MAX_VALUE || second < 0 || second > Character.MAX_VALUE)
				continue;

			Glyph glyph = glyphs[first];
			tokens.nextToken();
			int offset = Integer.parseInt(tokens.nextToken());

			if (glyph.kerning == null) {
				glyph.kerning = new int[maxCodePoint + 1];
			}
			glyph.kerning[second] = offset;

		}
		try {
			fontFile.close();
			br.close();
		} catch (IOException e) {
			// silent
		}
	}

	/**
	 * Disposes all texture pages associated with this font.
	 */
	public void dispose() {
		for (TextureRegion t : getTexturePages())
			t.getTexture().dispose();
	}
}
