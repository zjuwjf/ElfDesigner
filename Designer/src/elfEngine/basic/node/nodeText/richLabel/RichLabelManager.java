package elfEngine.basic.node.nodeText.richLabel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GraphicAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
import com.ielfgame.stupidGame.enumTypes.TextFont;
import com.ielfgame.stupidGame.enumTypes.VerticalTextAlign;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.nodeText.richLabel.RichTextHelper.FontSpan;
import elfEngine.basic.node.nodeText.richLabel.RichTextHelper.Span;

public class RichLabelManager {
	/***
	 * 
	 * font: fontName, fontSize, fontStyle dimensions font align
	 * 
	 */
	/***
	 * image string -> AlphaImageGraphicAttribute data ?
	 */
	private final static float _MIN_HALF = 0.099999999f;

	private final static float LEADING_RATE = 0 / 5f;
	
	public enum FontStyleType {
		Normal, Bold, Italic
	}

	public static class LabelDefine {
		public final ElfPointi dimension = new ElfPointi();

		public String fontName;
		public float fontSize = 20;
		/***
		 * fontStyle just support for rich text.
		 */
		public int fontStyle;
		public final ElfColor fillColor = new ElfColor();

		public HorizontalTextAlign horizontalAlign = HorizontalTextAlign.LEFT; // left,
																				// center,
																				// right
		public VerticalTextAlign verticalAlign = VerticalTextAlign.TOP; // top,
																		// center,
																		// bottom
		public boolean enableStroke;
		public int strokeSize;
		public final ElfColor strokeColor = new ElfColor();

		public boolean enableShadow;
		public final ElfPointi shadowOffset = new ElfPointi();
		public final ElfColor shadowColor = new ElfColor();

		/***
		 * just support for rich text.
		 */
		// public boolean enableGradient;
		// public final ElfColor gradientStartColor = new ElfColor();
		// public final ElfColor gradientEndColor = new ElfColor();
		// public final ElfPointf gradientAlongVector = new ElfPointf(100, 100);

		public LabelDefine copy() {
			final LabelDefine copy = new LabelDefine();
			copy.dimension.set(this.dimension);
			copy.fontName = this.fontName;
			copy.fontSize = this.fontSize;
			copy.fontStyle = this.fontStyle;
			copy.fillColor.set(this.fillColor);
			copy.horizontalAlign = this.horizontalAlign;
			copy.verticalAlign = this.verticalAlign;

			copy.enableStroke = this.enableStroke;
			copy.strokeSize = this.strokeSize;
			copy.strokeColor.set(this.strokeColor);
			copy.enableShadow = this.enableShadow;
			copy.shadowOffset.set(this.shadowOffset);
			copy.shadowColor.set(this.shadowColor);
			return copy;
		}

		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append(toName(dimension)).append(fontName).append(fontSize).append(fontStyle).append(toName(fillColor)).append(horizontalAlign).append(verticalAlign);
			sb.append(enableStroke).append(strokeSize).append(toName(strokeColor)).append(enableShadow).append(toName(shadowOffset)).append(toName(shadowColor));
			return sb.toString();
		}

		private static String toName(ElfPointi p) {
			return new StringBuilder().append(p.x).append("-").append(p.y).toString();
		}

		private static String toName(ElfColor p) {
			return new StringBuilder().append(p.red).append("-").append(p.green).append("-").append(p.blue).append("-").append(p.alpha).toString();
		}
	}

	private static int float2int(float r) {
		return Math.min(Math.max(Math.round(r * 255), 0), 255);
	}

	private static Color elfColor2awtColor(final ElfColor elfColor) {
		return new Color(float2int(elfColor.red), float2int(elfColor.green), float2int(elfColor.blue), float2int(elfColor.alpha));
	}

	// RGBA
	private static Color int2color(final int rgba) {
		final int r = (rgba >> 24) & 0xff;
		final int g = (rgba >> 16) & 0xff;
		final int b = (rgba >> 8) & 0xff;
		final int a = (rgba >> 0) & 0xff;
		final Color color = new Color(r, g, b, a);
		return color;
	}

	private static final float getOriginX(HorizontalTextAlign horizontal, int width, final float advance, float stroke, float shadowX) {
		final int myWidth = getTextLayoutWidth(advance, stroke, shadowX);

		switch (horizontal) {
		case LEFT: {
			return stroke / 2.0f + (shadowX > 0 ? 0 : Math.abs(shadowX));
		}
		case CENTER: {
			return stroke / 2.0f + (shadowX > 0 ? 0 : Math.abs(shadowX)) + (width - myWidth) / 2;
		}
		case RIGHT: {
			return stroke / 2.0f + (shadowX > 0 ? 0 : Math.abs(shadowX)) + (width - myWidth);
		}
		}
		return 0;
	}

	private static final int getTextLayoutWidth(float width, float stroke, float shadowX) {
		return Math.round(width + stroke + Math.abs(shadowX) + _MIN_HALF);
	}

	private static final Point getTextLayoutArraySize(TextLayout[] tls, final float stroke, final float shadowX, final float shadowY) {
		final Point point = new Point(0, 0);
		for (TextLayout tl : tls) {

			final boolean old = AlphaImageGraphicAttribute.getUseOutlineShape();

			AlphaImageGraphicAttribute.setUseOutlineShape(true);

			final int width = getTextLayoutWidth(tl.getAdvance(), stroke, shadowX);
			point.x = Math.max(point.x, width);

			AlphaImageGraphicAttribute.setUseOutlineShape(true);

			// tl.getBaseline()

			final float ascent = tl.getAscent();
			final float descent = tl.getDescent();

			// System.err.println("advance="+tl.getAdvance());
			// System.err.println("ascent="+ascent);
			// System.err.println("descent="+descent);
			// System.err.println("leading="+tl.getLeading());
			// System.err.println("Baseline="+tl.getBaseline());
			// final float [] offsets = tl.getBaselineOffsets();
			// for(int i=0; i<offsets.length; i++) {
			// System.err.println("BaselineOffset="+tl.getBaselineOffsets()[i]);
			// }

			point.y += tl.getLeading() + (ascent + descent) + (ascent + descent) * LEADING_RATE;

			AlphaImageGraphicAttribute.setUseOutlineShape(old);
		}

		point.y += Math.round(stroke + Math.abs(shadowY) + _MIN_HALF);
		return point;
	}

	private static final TextLayout[] getTextLayoutArray(final String text, final FontSpan defaultFontSpan, final int maxWidth, final boolean richOrPool) {
		final ArrayList<TextLayout> array = new ArrayList<TextLayout>();
		if (text == null || text.length() <= 0) {
			return new TextLayout[] {};
		}

		final ArrayList<Span> spans = new ArrayList<Span>();
		final String showText = RichTextHelper.buildSpan(text, spans, richOrPool);

		final FontSpan[] fontSpanArray = new FontSpan[showText.length()];
		for (int i = 0; i < fontSpanArray.length; i++) {
			fontSpanArray[i] = defaultFontSpan.copy();
		}
		final ArrayList<Span> notFontSpans = RichTextHelper.makeSpanPairs(spans, showText, fontSpanArray);

		// Hashtable map = new Hashtable();
		// map.put(TextAttribute.SIZE, new Float(32.0f));
		// AttributedString as = new AttributedString(s, map);

		final AttributedString attribString = new AttributedString(showText);

		/***
		 * make fonts
		 */
		for (int i = 0; i < fontSpanArray.length;) {
			final FontSpan fs = fontSpanArray[i];
			if (fs != null) {
				attribString.addAttribute(TextAttribute.FONT, AWTFontImageHelper.getAwtFont(fs.fontName, fs.fontSize * TextFont.getCurrentFont2MacFontRate(), fs.fontStyle), fs.pos, fs.endPos);
				i = fs.endPos;
			} else {
				i++;
			}
		}

		for (Span sp : notFontSpans) {
			switch (sp.type) {
			case BOLD:
			case ITALIC:
			case FONT:
			case SIZE:
				// do nothing
				break;
			case UNDERLINE:
				attribString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, sp.pos, sp.endPos);
				break;
			case LINK:
				attribString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, sp.pos, sp.endPos);
				attribString.addAttribute(TextAttribute.BACKGROUND, int2color(sp.normalBgColor), sp.pos, sp.endPos);
				break;
			case COLOR:
				attribString.addAttribute(TextAttribute.FOREGROUND, int2color(sp.color), sp.pos, sp.endPos);
				break;
			case IMAGE: {
				final BufferedImage bufferedImage = AWTFontImageHelper.getAWTBufferedImage(sp.imageName);
				if (bufferedImage != null) {
					final AlphaImageGraphicAttribute iga = new AlphaImageGraphicAttribute(bufferedImage, GraphicAttribute.ROMAN_BASELINE, 0, bufferedImage.getHeight());
					attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT, iga, sp.pos, sp.pos + 1);
					if (sp.endPos > sp.pos + 1) {
						attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT, new EmptyGraphicAttribute(), sp.pos + 1, sp.endPos);
					}
				}
			}
				break;
			case UNKNOWN:
				break;
			}
		}

		/***
		 * Graphics2D g; // Initialized elsewhere Font f; // Initialized
		 * elsewhere String message = "Hello World!"; // The text to measure and
		 * display Rectangle2D box; // The display box: initialized elsewhere
		 * 
		 * // Measure the font and the message FontRenderContext frc =
		 * g.getFontRenderContext(); Rectangle2D bounds =
		 * f.getStringBounds(message, frc); LineMetrics metrics =
		 * f.getLineMetrics(message, frc); float width = (float)
		 * bounds.getWidth(); // The width of our text float lineheight =
		 * metrics.getHeight(); // Total line height float ascent =
		 * metrics.getAscent(); // Top of text to baseline
		 * 
		 * // Now display the message centered horizontally and vertically in
		 * box float x0 = (float) (box.getX() + (box.getWidth() - width)/2);
		 * float y0 = (float) (box.getY() + (box.getHeight() - lineheight)/2 +
		 * ascent); g.setFont(f); g.drawString(message, x0, y0);
		 */

		final AttributedCharacterIterator attribCharIterator = attribString.getIterator();
		FontRenderContext frc = new FontRenderContext(null, true, true);

		final int lineWidth = maxWidth <= 0 ? 99999 : maxWidth;
		{
			final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(attribCharIterator, frc);

			final int totalLength = showText.length();
			while (lineMeasurer.getPosition() < attribCharIterator.getEndIndex()) {
				final int next = lineMeasurer.nextOffset(lineWidth);
				int limit = next;
				final int start = lineMeasurer.getPosition();
				for (int i = start; i < totalLength && i < next; ++i) {
					final char c = showText.charAt(i);
					if (c == '\n') {
						limit = i;
						break;
					}
				}

				final TextLayout layout = lineMeasurer.nextLayout(lineWidth, limit + 1, false);

				array.add(layout);
			}
		}

		final TextLayout[] ret = new TextLayout[array.size()];
		array.toArray(ret);
		return ret;
	}

	private static final void draw(final Graphics2D g2, final TextLayout[] tls, final int dimensionX, final int dimensionY, final int textWidth, final int textHeight, final HorizontalTextAlign horizontal, final VerticalTextAlign vertical, float stroke, final float shadowX,
			final float shadowY, final Paint tintColor, final Paint strokeColor, final Paint shadowColor, final boolean richOrPool) {
		// assert (stroke >= 0);
		stroke = stroke > 0 ? stroke : 0;
		final boolean strokeEnable = stroke > 0;
		final boolean shadowEnable = shadowX != 0 || shadowY != 0;

		// translate Y
		switch (vertical) {
		case BOTTOM:
			g2.translate(0, dimensionY - textHeight);
			break;
		case CENTER:
			g2.translate(0, (dimensionY - textHeight) / 2f);
			break;
		case TOP:
			break;
		}

		/***
		 * stroke/2 > |shadow| or shadow > 0
		 */
		AlphaImageGraphicAttribute.setUseOutlineShape(true);
		// shadow
		if (shadowEnable) {
			g2.setPaint(shadowColor);
			float y = stroke / 2;
			for (int i = 0; i < tls.length; i++) {

				final TextLayout tl = tls[i];

				final Shape shape = tl.getOutline(null);

				final float ascent = tl.getAscent();
				final float descent = tl.getDescent();

				final float leading = (ascent + descent) * LEADING_RATE;

				final float off = 0;

				y += ascent + off;

				final float offX = getOriginX(horizontal, dimensionX, tl.getAdvance(), stroke, shadowX) + shadowX;
				final float offY = y + shadowY;

				g2.translate(offX, offY);

				if (strokeEnable) {
					AlphaImageGraphicAttribute.setUseOutlineShape(false);
					final Shape shape2 = tl.getOutline(null);
					final Stroke old = g2.getStroke();
					g2.setStroke(new BasicStroke(stroke));
					// shape
					g2.draw(shape2);
					g2.setStroke(old);
					AlphaImageGraphicAttribute.setUseOutlineShape(true);
				}

				g2.fill(shape);

				g2.translate(-offX, -offY);
				y += descent + leading + tl.getLeading() - off;
			}
		}

		AlphaImageGraphicAttribute.setUseOutlineShape(false);

		if (strokeEnable) {
			float y = stroke / 2;
			for (int i = 0; i < tls.length; i++) {
				final TextLayout tl = tls[i];
				final Shape shape = tl.getOutline(null);

				final float ascent = tl.getAscent();
				final float descent = tl.getDescent();

				final float leading = (ascent + descent) * LEADING_RATE;

				final float off = 0;

				y += ascent + off;

				final float offX = getOriginX(horizontal, dimensionX, tl.getAdvance(), stroke, shadowX);
				final float offY = y;
				g2.translate(offX, offY);

				g2.setPaint(strokeColor);
				final Stroke old = g2.getStroke();
				g2.setStroke(new BasicStroke(stroke));
				// shape
				g2.draw(shape);
				g2.setStroke(old);

				g2.translate(-offX, -offY);
				y += descent + leading + tl.getLeading() - off;
			}
		}

		AlphaImageGraphicAttribute.setUseOutlineShape(true);

		// normal
		{
			float y = stroke / 2;
			for (int i = 0; i < tls.length; i++) {
				final TextLayout tl = tls[i];

				final float ascent = tl.getAscent();
				final float descent = tl.getDescent();

				final float leading = (ascent + descent) * LEADING_RATE;

				final float off = 0;

				y += ascent + off;

				final float offX = getOriginX(horizontal, dimensionX, tl.getAdvance(), stroke, shadowX);
				final float offY = y;
				g2.translate(offX, offY);

				g2.setPaint(tintColor);
				tl.draw(g2, 0, 0);

				g2.translate(-offX, -offY);
				y += descent + leading + tl.getLeading() - off;
			}
		}
	}

	private static final void renderLabel(final String string, final LabelDefine define, final String savePngPath, boolean richOrPool) {
		// clean

		// bitmap path ?
		final FontSpan defaultFontSpan = new FontSpan();

		defaultFontSpan.fontName = define.fontName;
		defaultFontSpan.fontSize = define.fontSize * TextFont.getCurrentFont2MacFontRate();
		defaultFontSpan.fontStyle = define.fontStyle;

		final TextLayout[] tls = getTextLayoutArray(string, defaultFontSpan, define.dimension.x, richOrPool);
		final Point size = getTextLayoutArraySize(tls, define.enableStroke ? define.strokeSize : 0, define.enableShadow ? define.shadowOffset.x : 0, define.enableShadow ? define.shadowOffset.y : 0);

		// calc size
		if (define.dimension.x <= 0) {
			define.dimension.x = size.x;
		}
		if (define.dimension.y <= 0) {
			define.dimension.y = size.y;
		}

		// make bitmap
		BufferedImage bffImg = new BufferedImage(Math.max(define.dimension.x, 1), Math.max(define.dimension.y, 1), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gfx = bffImg.createGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gfx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		// draw
		final Paint fillColor = elfColor2awtColor(define.fillColor);

		draw(gfx, tls, define.dimension.x, define.dimension.y, size.x, size.y, define.horizontalAlign, define.verticalAlign, define.enableStroke ? define.strokeSize : 0, define.enableShadow ? define.shadowOffset.x : 0, define.enableShadow ? define.shadowOffset.y : 0, fillColor,
				elfColor2awtColor(define.strokeColor), elfColor2awtColor(define.shadowColor), richOrPool);

		bffImg.flush();

		// save image
		try {
			final File destDir = new File(FileHelper.getDirPath(savePngPath));

			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			ImageIO.write(bffImg, "PNG", new File(savePngPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void renderRichLabel(final String string, final LabelDefine define, final String savePngPath) {
		renderLabel(string, define, savePngPath, true);
	}

	public static void renderPoolLabel(final String string, final LabelDefine define, final String savePngPath) {
		renderLabel(string, define, savePngPath, false);
	}

	public static void main(final String[] args) {
		//
		final TextLayout tl = new TextLayout("hello", AWTFontImageHelper.getAwtFont("", 0, 0), new FontRenderContext(null, false, false));

		final Field[] fs = tl.getClass().getDeclaredFields();
		for (Field f : fs) {
			System.err.println("@" + f.getName());
		}

	}
}
