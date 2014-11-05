package elfEngine.basic.node.nodeText.richLabel;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GraphicAttribute;
import java.awt.font.ImageGraphicAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class AlphaImageGraphicAttribute extends GraphicAttribute {
	private final Area createArea(final int maxTransparency) {
		final Area area = new Area();
		final Rectangle rectangle = new Rectangle();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				rgb = rgb >>> 24;
				if (rgb >= maxTransparency) {
					rectangle.setBounds(x, y, 1, 1);
					area.add(new Area(rectangle));
				}
			}
		}
		
		final AffineTransform t = new AffineTransform();
		t.translate(-this.originX, -this.originY);
		area.transform(t);

		return area;
	}

	private static boolean sUseOutlineShape;

	private final BufferedImage image;
	private final float originX;
	private final float originY;
	private final Area area;

	public AlphaImageGraphicAttribute(BufferedImage image, int alignment) {
		this(image, alignment, 0, 0);
	}

	public AlphaImageGraphicAttribute(BufferedImage image, int alignment, float originX, float originY) {
		super(alignment);
		this.image = image;
		this.originX = originX;
		this.originY = originY;

		this.area = createArea(1);
		
	}

	public Shape getOutline(final AffineTransform tx) {
		if (sUseOutlineShape) { 
			if (tx != null) { 
				return area.createTransformedArea(tx);
			}
			return area;
		} else { 
			return new Area();
		} 
	}

	public static void setUseOutlineShape(boolean use) {
		sUseOutlineShape = use;
	}
	
	public static boolean getUseOutlineShape() {
		return sUseOutlineShape;
	}

	public void draw(Graphics2D g, float x, float y) {
		g.drawImage(image, (int) (x - originX), (int) (y - originY), null);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ImageGraphicAttribute))
			return false;
		return equals((ImageGraphicAttribute) obj);
	}

	public boolean equals(AlphaImageGraphicAttribute rhs) {
		return ((this == rhs) || ((this.getAscent() == rhs.getAscent()) && (this.getAdvance() == rhs.getAdvance()) && (this.getAlignment() == rhs.getAlignment()) && (this.getBounds().equals(rhs.getBounds())) && (this.getDescent() == rhs.getDescent())
				&& (this.hashCode() == rhs.hashCode()) && (this.image.equals(rhs.image)) && (this.originX == rhs.originX) && (this.originY == rhs.originY)));
	}

	public float getAdvance() {
//		System.err.println("Advance called");
		return Math.max(0, image.getWidth(null) - originX);
	}

	public float getAscent() {
//		System.err.println("Ascent called");
//		if(!sUseOutlineShape) {
//			return 0;
//		}
		return Math.max(0, originY);
	}
	
//	public float getLeading() {
//		return 0;
//	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Float(-originX, -originY, image.getWidth(null), image.getHeight(null));
	}

	public float getDescent() {
//		System.err.println("Descent called");
//		if(!sUseOutlineShape) {
//			return 0;
//		}
		return Math.max(0, image.getHeight(null) - originY);
	}

	public int hashCode() {
		return image.hashCode();
	}
}
