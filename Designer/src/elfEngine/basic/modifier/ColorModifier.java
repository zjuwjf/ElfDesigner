package elfEngine.basic.modifier;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;

/**
 * @author zju_wjf
 * 
 */
public class ColorModifier extends BasicNodeModifier{
	
	private float mRedBeg, mRedOffset;
	private float mGreenBeg, mGreenOffset;
	private float mBlueBeg, mBlueOffset;
	private float mAlphaBeg, mAlphaOffset;
	
	public ColorModifier(float rb, float re, float gb, float ge, float bb, float be, float ab, float ae,float life) {
		super(0, 1, life, life, null, null);
		// TODO Auto-generated constructor stub
		mRedBeg = rb;
		mRedOffset = re-rb;
		mGreenBeg = gb;
		mGreenOffset = ge-gb;
		mBlueBeg = bb;
		mBlueOffset = be-bb;
		mAlphaBeg = ab;
		mAlphaOffset = ae-ab;
	}

	@Override
	public void modifier(ElfNode node) {
		// TODO Auto-generated method stub
		final float rate = getValue();
		
		final float alpha = mAlphaBeg+mAlphaOffset*rate;
		final float red = mRedBeg+mRedOffset*rate;
		final float green = mGreenBeg+mGreenOffset*rate;
		final float blue = mBlueBeg+mBlueOffset*rate;
		node.setColor(new ElfColor(red, green, blue, alpha));
	}
}
