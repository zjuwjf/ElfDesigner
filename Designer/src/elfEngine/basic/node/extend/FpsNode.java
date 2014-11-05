package elfEngine.basic.node.extend;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.richLabel.SharedLabelNode;


public class FpsNode extends ElfNode{
	private float mStatisticsPeriod = 1000;//default
	private float mHasElapsed = 0;
	private int mHasCountFrames = 0;
	private String mPreFix="";
	private SharedLabelNode mCurrentTextNode; 
	private long mLastTime = -1;
	
	
	public FpsNode(ElfNode father, int ordinal) { 
		super(father, ordinal);
		
		mCurrentTextNode = new SharedLabelNode(this, 0);
		mCurrentTextNode.setFillColor(new ElfColor());
		mCurrentTextNode.setFontSize(30);
		mCurrentTextNode.setStrokeColor(new ElfColor(0.3f, 0.3f, 0.3f, 1));
		mCurrentTextNode.setStrokeSize(2);
	} 
	
	public void calc(float dt) {
		super.calc(dt);
		
		update();
	}
	
	public void update(){ 
		if(mLastTime < 0) {
			mLastTime = System.currentTimeMillis();
		} else {
			final long now = System.currentTimeMillis();
			mHasElapsed += (now-mLastTime); 
			mLastTime = now;
			mHasCountFrames++;
			if(mHasElapsed >= mStatisticsPeriod){ 
				final int frames = Math.round(mHasCountFrames*1000f/mHasElapsed);
				final String key = mPreFix+frames;
				mCurrentTextNode.setText(key);
				
				mHasElapsed = 0; 
				mHasCountFrames = 0; 
			} 
		}
	} 
	
	public void drawSelf() {
		if(mCurrentTextNode != null) {
			mCurrentTextNode.calcSprite(0);
			mCurrentTextNode.drawSprite();
		}
	}
	
	public void reset(){
		super.reset();
		mHasElapsed = 0;
		mHasCountFrames = 0;
		mLastTime = -1;
	}

	public float getStatisticsPeriod() {
		return mStatisticsPeriod;
	}

	public void setStatisticsPeriod(float mStatisticsPeriod) {
		this.mStatisticsPeriod = mStatisticsPeriod;
	} 

	public String getPreFix() {
		return mPreFix;
	}

	public void setPreFix(String prefix){
		mPreFix = prefix;
	}
}
