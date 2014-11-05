package elfEngine.basic.node.nodeList;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;

public class PullDownNode extends ClipNode{
	
	public PullDownNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 

		this.setUseSettedSize(true); 
		
		this.setPullDownMode(PullDownMode.PullDown); 
	} 

	public enum PullDownMode { 
		PullDown, PullUp, PullLeft, PullRight, PullHori, PullVert, PullFull
	}

	private final ElfPointf mExtendRate = new ElfPointf();
	
	private PullDownMode mPullDownMode;

	public void setPullDownMode(PullDownMode mode) {
		if (mode != null && mode != mPullDownMode) {
			mPullDownMode = mode;
			
			ElfPointf oldAnchor = this.getAnchorPosition();
			ElfPointf size = this.getSize();
			
			switch (mPullDownMode) {
			case PullDown:
				this.setAnchorPosition(0.5f, 1);
				mExtendRate.setPoint(0, 1);
				break;
			case PullUp:
				this.setAnchorPosition(0.5f, 0);
				mExtendRate.setPoint(0, 1);
				break;
			case PullVert:
				this.setAnchorPosition(0.5f, 0.5f);
				mExtendRate.setPoint(0, 1);
				break;
			case PullLeft:
				this.setAnchorPosition(0, 0.5f);
				mExtendRate.setPoint(1, 0);
				break;
			case PullRight:
				this.setAnchorPosition(1, 0.5f);
				mExtendRate.setPoint(1, 0);
				break;
			case PullHori:
				this.setAnchorPosition(0.5f, 0.5f);
				mExtendRate.setPoint(1, 0);
				break;
			case PullFull:
				this.setAnchorPosition(0.5f, 0.5f);
				mExtendRate.setPoint(1, 1);
				break;
			}
			
			ElfPointf newAnchor = this.getAnchorPosition();
			this.translate((newAnchor.x-oldAnchor.x)*size.x, (newAnchor.y-oldAnchor.y)*size.y); 
			update(); 
		} 
	}

	public PullDownMode getPullDownMode() { 
		return mPullDownMode; 
	} 
	protected final static int REF_PullDownMode = DEFAULT_SHIFT;
	
	private final ElfPointf mOriginalSize = new ElfPointf(100, 100);
	
	public void setOriginalSize(ElfPointf size) { 
		mOriginalSize.set(size); 
	} 

	public ElfPointf getOriginalSize() { 
		return new ElfPointf(mOriginalSize); 
	} 

	protected final static int REF_OriginalSize = DEFAULT_SHIFT;

	private int mMinLength = 0;
	
	public void setMinLength(int minLength) {
		mMinLength = minLength;
	}
	public int getMinLength() {
		return mMinLength;
	}
	protected final static int REF_MinLength = DEFAULT_SHIFT;
	
	private float mPercentage = 50; 
	private float mPercentageEnd = 100; 
	
	private float mPercentageStart = 0; 
	
	private InterType mInterType = InterType.Viscous; 
	
	public void setPercentage(float percentage) {
		mPercentage = percentage;
		update();
	} 

	public float getPercentage() {
		return mPercentage;
	} 

	protected final static int REF_Percentage = DEFAULT_SHIFT;
	
	public void setPercentageStart(float percentage) {
		mPercentageStart = percentage;
	} 
	public float getPercentageStart() { 
		return mPercentageStart;
	}
	protected final static int REF_PercentageStart = DEFAULT_SHIFT;

	public void setPercentageEnd(float percentage) {
		mPercentageEnd = percentage;
	}
	public float getPercentageEnd() {
		return mPercentageEnd;
	}
	protected final static int REF_PercentageEnd = DEFAULT_SHIFT;

	public void setInterType(InterType type) {
		mInterType = type;
	}
	public InterType getInterType() {
		return mInterType;
	}
	protected final static int REF_InterType = DEFAULT_SHIFT;
	
	void update() { 
		final ElfPointf size = new ElfPointf(mOriginalSize);
		
		final float rate = 1-mPercentage / 100.0f;
		final ElfPointf change = new ElfPointf(rate, rate);
		change.mult(size.x, size.y);
		change.mult(mExtendRate.x, mExtendRate.y);
		
		size.translate(-change.x, -change.y);
		
		this.setSize(size);
	}

	private final float getMinPercentage() {
		switch (mPullDownMode) {
		case PullUp:
		case PullVert:
		case PullDown: 
			return 100*mMinLength/mOriginalSize.y;
		case PullHori:
		case PullLeft:
		case PullRight:
			return 100*mMinLength/mOriginalSize.x;
		case PullFull:
			return 100*mMinLength/mOriginalSize.x;
		}
		
		return 0;
	}
			
	public void setToggle() { 
		mAnimateProgress = 0; 
		mPercentageStart = Math.max(getMinPercentage(), mPercentage); 
		
		if(mPercentageEnd == 100) { 
			mPercentageEnd = getMinPercentage(); 
		} else { 
			mPercentageEnd = 100;
		} 
		this.setVisible(true);
	}
	protected final static int REF_Toggle = FACE_SET_SHIFT;

	private float mAnimateLife = 1;
	private float mAnimateProgress = -1;

	public void setAnimateLife(float life) {
		if (life > 0) {
			mAnimateLife = life;
		}
	}

	public float getAnimateLife() {
		return mAnimateLife;
	}
	protected final static int REF_AnimateLife = DEFAULT_SHIFT;
	
	
	public void calc(float pMsElapsed) {
		super.calc(pMsElapsed);
		
		//animate
		if (mAnimateProgress < 0) {
			// quiet
		} else if (mAnimateProgress < mAnimateLife) {
			mAnimateProgress += pMsElapsed / 1000;

			final float rate = mInterType.getInterpolation(Math.min(mAnimateProgress / mAnimateLife, 1));
			this.setPercentage(Math.round( rate * mPercentageEnd + (1-rate)*mPercentageStart ));
			
		} else if (mAnimateProgress >= mAnimateLife) { 
			this.setPercentage(mPercentageEnd); 
			mAnimateProgress = -1; 
			
			if (mPercentageEnd <= getMinPercentage() ) { 
				this.setVisible(false);  
			} 
		} 
		
		//children position
		final ElfPointf size = this.getSize(); 
		final ElfPointf anchor = this.getAnchorPosition(); 
		size.mult(anchor.x-0.5f, anchor.y-0.5f); 
		
		final ElfNode [] children = this.getChilds(); 
		for(ElfNode child : children) { 
			child.setAnchorPosition(anchor); 
			child.setPosition(size); 
		} 
	} 
}
