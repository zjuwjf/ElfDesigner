package elfEngine.basic.node.nodeShape;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

public class GradientNode extends ElfNode {
	
	private boolean enableGradient;
	private final ElfColor gradientStartColor = new ElfColor();
	private final ElfColor gradientEndColor = new ElfColor();
	private final ElfPointf gradientAlongVector = new ElfPointf(100, 100);
	private boolean compressedInterpolation = true;
	
	private final ElfColor ltColor = new ElfColor();
	private final ElfColor lbColor = new ElfColor();
	private final ElfColor rtColor = new ElfColor();
	private final ElfColor rbColor = new ElfColor();

	public GradientNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void drawSelf() { 
		if(enableGradient) { 
			//ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt
			drawPic(rbColor,lbColor,ltColor,rtColor);
		} else {
			super.drawSelf();
		}
	}

	public boolean getEnableGradient() {
		return enableGradient;
	}

	public void setEnableGradient(boolean enableGradient) {
		this.enableGradient = enableGradient;
		updateColor();
	}
	protected final static int REF_EnableGradient = DEFAULT_SHIFT;

	public ElfColor getGradientStartColor() {
		return gradientStartColor;
	}

	public void setGradientStartColor(ElfColor gradientStartColor) {
		this.gradientStartColor.set(gradientStartColor);
		updateColor();
	}
	protected final static int REF_GradientStartColor = DEFAULT_SHIFT;

	public ElfColor getGradientEndColor() {
		return gradientEndColor;
	}

	public void setGradientEndColor(ElfColor gradientEndColor) {
		this.gradientEndColor.set(gradientEndColor);
		updateColor();
	}
	protected final static int REF_GradientEndColor = DEFAULT_SHIFT;

	public ElfPointf getGradientAlongVector() {
		return gradientAlongVector;
	}

	public void setGradientAlongVector(ElfPointf gradientAlongVector) {
		this.gradientAlongVector.set(gradientAlongVector);
		updateColor();
	}
	protected final static int REF_GradientAlongVector = DEFAULT_SHIFT;
	
	public boolean getCompressedInterpolation() {
		return compressedInterpolation;
	}

	public void setCompressedInterpolation(boolean compressedInterpolation) {
		this.compressedInterpolation = compressedInterpolation;
		updateColor();
	}
	protected final static int REF_CompressedInterpolation = DEFAULT_SHIFT;
	
	private final void updateColor() {
		if(!enableGradient) {
			return ;
		}
		
		float h = (float)gradientAlongVector.getLength();
	    if (h <= 0)
	        return;

	    final float c = (float)Math.sqrt(2.0f);
	    final ElfPointf u = new ElfPointf(gradientAlongVector.x / h, gradientAlongVector.y / h);

	    // Compressed Interpolation mode
	    if (compressedInterpolation)
	    {
	        final float h2 = 1 / ( Math.abs(u.x) + Math.abs(u.y) );
	        u.mult(h2 * (float)c);
	    }
	    
	    final ElfColor S = gradientStartColor;
	    final ElfColor E = gradientEndColor;

	    // (-1, -1) bl
	    lbColor.red = E.red + (S.red - E.red) * ((c + u.x + u.y) / (2.0f * c));
	    lbColor.green = E.green + (S.green - E.green) * ((c + u.x + u.y) / (2.0f * c));
	    lbColor.blue = E.blue + (S.blue - E.blue) * ((c + u.x + u.y) / (2.0f * c));
	    lbColor.alpha = E.alpha + (S.alpha - E.alpha) * ((c + u.x + u.y) / (2.0f * c));
	    // (1, -1) br
	    rbColor.red = E.red + (S.red - E.red) * ((c - u.x + u.y) / (2.0f * c));
	    rbColor.green = E.green + (S.green - E.green) * ((c - u.x + u.y) / (2.0f * c));
	    rbColor.blue = E.blue + (S.blue - E.blue) * ((c - u.x + u.y) / (2.0f * c));
	    rbColor.alpha = E.alpha + (S.alpha - E.alpha) * ((c - u.x + u.y) / (2.0f * c));
	    // (-1, 1) tl
	    ltColor.red = E.red + (S.red - E.red) * ((c + u.x - u.y) / (2.0f * c));
	    ltColor.green = E.green + (S.green - E.green) * ((c + u.x - u.y) / (2.0f * c));
	    ltColor.blue = E.blue + (S.blue - E.blue) * ((c + u.x - u.y) / (2.0f * c));
	    ltColor.alpha = E.alpha + (S.alpha - E.alpha) * ((c + u.x - u.y) / (2.0f * c));
	    // (1, 1) tr
	    rtColor.red = E.red + (S.red - E.red) * ((c - u.x - u.y) / (2.0f * c));
	    rtColor.green = E.green + (S.green - E.green) * ((c - u.x - u.y) / (2.0f * c));
	    rtColor.blue = E.blue + (S.blue - E.blue) * ((c - u.x - u.y) / (2.0f * c));
	    rtColor.alpha = E.alpha + (S.alpha - E.alpha) * ((c - u.x - u.y) / (2.0f * c));
	}
	
}
