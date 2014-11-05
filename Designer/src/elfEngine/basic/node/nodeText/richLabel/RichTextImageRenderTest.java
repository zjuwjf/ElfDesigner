//package elfEngine.basic.node.nodeText.richLabel;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.GradientPaint;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Paint;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.RenderingHints;
//import java.awt.Shape;
//import java.awt.Stroke;
//import java.awt.font.FontRenderContext;
//import java.awt.font.GraphicAttribute;
//import java.awt.font.LineBreakMeasurer;
//import java.awt.font.TextAttribute;
//import java.awt.font.TextLayout;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.text.AttributedCharacterIterator;
//import java.text.AttributedString;
//import java.util.ArrayList;
//
//import javax.imageio.ImageIO;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
//
//import elfEngine.basic.node.nodeText.richLabel.RichTextHelper.FontSpan;
//import elfEngine.basic.node.nodeText.richLabel.RichTextHelper.Span;
//
//public class RichTextImageRenderTest extends JPanel {
//	/**
//	 * Font Manager
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private RichTextImageRenderTest() { 
//		setPreferredSize(new Dimension(800, 400));
//	}
//	
//	private static Color int2color(final int rgba) {
//		final int r = (rgba >> 24) & 0xff;
//		final int g = (rgba >> 16) & 0xff;
//		final int b = (rgba >> 8) & 0xff;
//		final int a = rgba & 0xff;
//		final Color color = new Color(r, g, b, a);
//		return color;
//	}
//
//	private final static float _MIN_HALF = 0.499999999f;
//	
//	private final static int MAX_WIDTH = 350;
//	private final static HorizontalTextAlign Horizontal = HorizontalTextAlign.RIGHT;
//	
//	private static final float getOriginX(Rectangle rext, HorizontalTextAlign horizontal, int width, float stroke, float shadowX) {
//		final int myWidth = getTextLayoutWidth(rext.getBounds().width, stroke, shadowX);
//		switch (horizontal) {
//		case LEFT: {
//			return -rext.x + Math.max(stroke/2f, -shadowX);
//		}
//		case CENTER: {
//			return -rext.x + Math.max(stroke/2f, -shadowX) + (width-myWidth)/2f;
//		}
//		case RIGHT: {
//			return -rext.x + Math.max(stroke/2f, -shadowX) + (width-myWidth);
//		}
//		}
//		return 0;
//	}
//
//	private static final int getTextLayoutWidth(float width, float stroke, float shadowX) {
//		return Math.round(width + stroke / 2f + Math.max(Math.abs(shadowX), stroke / 2f) + _MIN_HALF);
//	} 
//
//	private static final Point getTextLayoutArraySize(TextLayout[] tls, final float stroke, final float shadowX, final float shadowY) {
//		final Point point = new Point(0, 0);
//		for (TextLayout tl : tls) {
//			final int width = getTextLayoutWidth((float) tl.getBounds().getWidth(), stroke, shadowX);
//			point.x = Math.max(point.x, width);
//			point.y += tl.getAscent() + tl.getDescent() + tl.getLeading();
//		}
//
//		point.y +=  Math.round((tls.length - 1) * stroke +stroke / 2f + Math.max(Math.abs(shadowY), stroke / 2f) + _MIN_HALF);
//		return point;
//	}
//
//	private static final TextLayout[] getTextLayoutArray(final String text, final FontSpan defaultFontSpan, final int maxWidth, boolean richOrPool) {
//		final ArrayList<TextLayout> array = new ArrayList<TextLayout>();
//		final ArrayList<Span> spans = new ArrayList<Span>();
//		final String showText = RichTextHelper.buildSpan(text, spans, richOrPool);
//		final FontSpan[] fontSpanArray = new FontSpan[showText.length()];
//		for (int i = 0; i < fontSpanArray.length; i++) {
//			fontSpanArray[i] = defaultFontSpan.copy();
//		}
//
//		final ArrayList<Span> notFontSpans = RichTextHelper.makeSpanPairs(spans, showText, fontSpanArray);
//		final AttributedString attribString = new AttributedString(showText);
//
//		/***
//		 * make fonts
//		 */
//		for (int i = 0; i < fontSpanArray.length;) {
//			final FontSpan fs = fontSpanArray[i];
//			if (fs != null) {
//				attribString.addAttribute(TextAttribute.FONT, AWTFontImageHelper.getAwtFont(fs.fontName, fs.fontSize, fs.fontStyle), fs.pos, fs.endPos);
//				i = fs.endPos;
//			} else {
//				i++;
//			}
//		}
//
//		for (Span sp : notFontSpans) {
//			switch (sp.type) {
//			case BOLD:
//			case ITALIC:
//			case FONT:
//			case SIZE:
//				break;
//			case UNDERLINE:
//				attribString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, sp.pos, sp.endPos);
//				break;
//			case LINK:
//				attribString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, sp.pos, sp.endPos);
//				attribString.addAttribute(TextAttribute.BACKGROUND, int2color(sp.normalBgColor), sp.pos, sp.endPos);
//				break;
//			case COLOR:
//				attribString.addAttribute(TextAttribute.FOREGROUND, int2color(sp.color), sp.pos, sp.endPos);
//				break;
//			case IMAGE: {
//				final BufferedImage bufferedImage = AWTFontImageHelper.getAWTBufferedImage(sp.imageName);
//				if (bufferedImage != null) {
//					final AlphaImageGraphicAttribute iga = new AlphaImageGraphicAttribute(bufferedImage, GraphicAttribute.HANGING_BASELINE, 0, bufferedImage.getHeight()/4);
//					attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT, iga, sp.pos, sp.pos+1);
//					if(sp.endPos > sp.pos+1) {
//						attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT, new EmptyGraphicAttribute(), sp.pos+1, sp.endPos);
//					}
//				}
//			}
//				break;
//			case UNKNOWN:
//				break;
//			}
//		}
//
//		final AttributedCharacterIterator attribCharIterator = attribString.getIterator();
//		FontRenderContext frc = new FontRenderContext(null, true, true);
//		
//		final int lineWidth = maxWidth <= 0 ? 99999 : maxWidth;
//		{
//			final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(attribCharIterator, frc);
//			final int totalLength = showText.length();
//			while (lineMeasurer.getPosition() < attribCharIterator.getEndIndex()) {
//
//				int next = lineMeasurer.nextOffset(lineWidth);
//				int limit = next;
//				if (limit < totalLength) {
//					for (int i = lineMeasurer.getPosition(); i < next; ++i) {
//						char c = showText.charAt(i);
//						if (c == '\n') {
//							limit = i;
//							break;
//						}
//					}
//				}
//
//				final TextLayout layout = lineMeasurer.nextLayout(lineWidth, limit + 1, false);
//				array.add(layout);
//			}
//		}
//		
//		final TextLayout[] ret = new TextLayout[array.size()];
//		array.toArray(ret);
//		return ret;
//	}
//
//	private static final void draw(final Graphics2D g2, final TextLayout[] tls, final float stroke, final float shadowX, final float shadowY, final Paint tintColor, final Paint strokeColor, final Paint shadowColor) {
//		assert (stroke >= 0);
////		final Point size = getTextLayoutArraySize(tls, stroke, shadowX, shadowY); 
//
//		final boolean strokeEnable = stroke > 0;
//		final boolean shadowEnable = shadowX != 0 || shadowY != 0;
//		/***
//		 * stroke/2 > |shadow| or shadow > 0
//		 */
//		
//		//right ?
//		AlphaImageGraphicAttribute.setUseOutlineShape(true);
//
//		// shadow
//		if (shadowEnable) {
//			g2.setPaint(shadowColor);
//			float y = 0;
//			for (int i = 0; i < tls.length; i++) {
//				final TextLayout tl = tls[i];
//				final Shape shape = tl.getOutline(null);
//				final Rectangle rect = shape.getBounds();
//				y += tl.getAscent() + stroke / 2f;
////				final float offX = -rect.x + shadowX;
//				final float offX = getOriginX(rect, Horizontal, MAX_WIDTH, stroke, shadowX) + shadowX;
//				final float offY = y + shadowY;
//				
//				g2.translate(offX, offY);
//				g2.fill(shape);
//
//				g2.translate(-offX, -offY);
//				y += tl.getDescent() + tl.getLeading() + stroke / 2f;
//			}
//		}
//
//		AlphaImageGraphicAttribute.setUseOutlineShape(false);
//		// normal
//		{
//			float y = 0;
//			for (int i = 0; i < tls.length; i++) {
//				g2.setPaint(tintColor);
//				
//				final TextLayout tl = tls[i];
//				final Shape shape = tl.getOutline(null);
//				
//				AlphaImageGraphicAttribute.setUseOutlineShape(true);
//				final Shape shape2 = tl.getOutline(null);
//				AlphaImageGraphicAttribute.setUseOutlineShape(false);
//				final Rectangle rect = shape2.getBounds();
//
//				y += tl.getAscent() + stroke / 2f;
//				//+ (280-rect.width-shadowX)
////				final float offX = -rect.x ;
//				final float offX = getOriginX(rect, Horizontal, MAX_WIDTH, stroke, shadowX);
//				final float offY = y;
//				g2.translate(offX, offY);
//
//				tl.draw(g2, 0, 0);
//				// stroke
//				if (strokeEnable) {
//					g2.setPaint(strokeColor);
//					
//					Stroke old = g2.getStroke();
//					g2.setStroke(new BasicStroke(stroke));
//					g2.draw(shape);
//					g2.setStroke(old);
//				}
//
//				g2.translate(-offX, -offY);
//				y += tl.getDescent() + tl.getLeading() + stroke / 2f;
//			}
//		}
//	}
//
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//
//		final String string = "[font font.ttf]Im a [color ffff00ff][size 60]rich[/size][/color] text!\na\nb\nc\nd\ne\nf[/font][u][i]中[b]文测[/b]试[/i][/u], [image golden.png]####[/image] [size 20]hello [i]J[/i]ava![/size]laught laught laught laught laught";
//		final FontSpan defaultFontSpan = new FontSpan();
//		defaultFontSpan.fontName = "";
//		defaultFontSpan.fontSize = 40;
//		defaultFontSpan.fontStyle = 0;
//
//		Graphics2D g2 = (Graphics2D) g.create();
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//		final float stroke = 1;
//		final float shadowX = 6;
//		final float shadowY = 6;
//
//		final GradientPaint paint = new GradientPaint(0, 0, Color.yellow, 50, 50, Color.blue, true);
//		
//		final int measureWith = MAX_WIDTH - Math.round(Math.max(Math.abs(shadowX), stroke/2) + stroke/2 + _MIN_HALF);
//		final TextLayout[] tls = getTextLayoutArray(string, defaultFontSpan, measureWith, true);
//		
//		draw(g2, tls, stroke, shadowX, shadowY, paint, int2color(0xff0000ff), int2color(0x80808080));
//		
//		final Point size = getTextLayoutArraySize(tls, stroke, shadowX, shadowY);
//		g2.setPaint(Color.black);
//		g2.drawRect(0, 0, MAX_WIDTH - 1, size.y - 1);
//		
//		BufferedImage bffImg = new BufferedImage(MAX_WIDTH, size.y, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D gfx = bffImg.createGraphics();
//		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		draw(gfx, tls, stroke, shadowX, shadowY, paint, int2color(0xff0000ff), int2color(0x80808080));
//		bffImg.flush();
//		
//		try {
//			ImageIO.write(bffImg, "PNG", new File("d:\\yourImageName2.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) {
//		JFrame f = new JFrame("Test");
//		Component c = new RichTextImageRenderTest();
//		f.getContentPane().add(c);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.pack();
//		f.setVisible(true);
//	}
//}