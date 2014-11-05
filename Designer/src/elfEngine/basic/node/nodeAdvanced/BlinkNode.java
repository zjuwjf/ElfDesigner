package elfEngine.basic.node.nodeAdvanced;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.DrawHelper;
import elfEngine.opengl.GLHelper;

public class BlinkNode extends ElfNode { 
	
	public BlinkNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
		this.setName("#blink"); 
	} 
	
	public enum BlinkType{ 
		Blink, Normal ;
	} 
	
	private BlinkType mBlinkType = BlinkType.Blink; 
	public BlinkType getBlinkType() {
		return mBlinkType;
	} 
	public void setBlinkType(BlinkType mBlinkType) {
		this.mBlinkType = mBlinkType;
	} 
	protected final static int REF_BlinkType = DEFAULT_SHIFT;
	
	private final ElfColor mBlinkColor = new ElfColor(); 
	public void setBlinkColor(ElfColor color) {
		if(color != null) {
			mBlinkColor.set(color);
		}
	}
	public ElfColor getBlinkColor() { 
		return mBlinkColor;
	}
	protected final static int REF_BlinkColor = DEFAULT_SHIFT;
	
	public String getBlendHelp() {
		String ret = ""; 
		ret += "self blend : " + BlendMode.BLEND05.sourceBlendFunction + " , " + BlendMode.BLEND05.destinationBlendFunction;
		ret += "\n";
		ret += "rect blend : " + BlendMode.BLEND06.sourceBlendFunction + " , " + BlendMode.BLEND06.destinationBlendFunction;
		ret += "\n";
		ret += "shape blend : " + BlendMode.BLEND86.sourceBlendFunction + " , " + BlendMode.BLEND86.destinationBlendFunction;
		ret += "\n"; 
		return ret; 
	} 
	protected final static int REF_BlendHelp = FACE_GET_SHIFT;
	
	
	public void drawSelf() { 
		if(mBlinkType == BlinkType.Blink) { 
			//drawSelf
			GLHelper.glBlendFunc(BlendMode.BLEND05.sourceBlendFunction, BlendMode.BLEND05.destinationBlendFunction); 
			drawPic(); 
			
			//drawRect 
			GLHelper.glBlendFunc(BlendMode.BLEND06.sourceBlendFunction, BlendMode.BLEND06.destinationBlendFunction); 
			GLHelper.glColor4f(mBlinkColor.red, mBlinkColor.green, mBlinkColor.blue, mBlinkColor.alpha); 
			DrawHelper.fillRect(this.getWidth(), this.getHeight()); 
			
			//drawShape 
			GLHelper.glBlendFunc(BlendMode.BLEND86.sourceBlendFunction, BlendMode.BLEND86.destinationBlendFunction); 
			final ElfColor myColor = this.getColor();
			GLHelper.glColor4f(myColor.red, myColor.green, myColor.blue, myColor.alpha); 
			drawPic(); 
		} else { 
			super.drawSelf(); 
		} 
	} 
	
	//
} 
