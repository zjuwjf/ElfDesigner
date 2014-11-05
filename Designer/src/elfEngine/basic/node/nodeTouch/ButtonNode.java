package elfEngine.basic.node.nodeTouch;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.enumTypes.ButtonState;
import com.ielfgame.stupidGame.platform.PlatformHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ButtonEventDecoder;
import elfEngine.basic.touch.ButtonEventDecoder.IButton;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.extend.ElfOnClickListener;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;

public class ButtonNode extends TouchNode implements IButton{

	protected String mPressedResid, mNormalResid, mInvalidResid; 
	protected String mLuaPath; 
	protected ButtonState mButtonState = ButtonState.Normal; 
	protected final ButtonEventDecoder mButtonEventDecoder = new ButtonEventDecoder();
	
	public ButtonNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setTouchEnable(true);

		setName("#button");
		
		setTouchDecoder(mButtonEventDecoder);
		
		setPriorityLevel(BUTTON_PRIORITY);
	}
	
	public String [] getSelfResids() {
		return new String[]{mNormalResid, mPressedResid, mInvalidResid};
	}
	
	public void setSelfResids(final String[] resids) {
		if(resids != null) {
			if(resids.length > 0) {
				this.setNormalResid(resids[0]);
			}
			if(resids.length > 1) {
				this.setPressedResid(resids[1]);
			}
			if(resids.length > 2) {
				this.setInvalidResid(resids[2]);
			}
		}
	}

	public void setNormalResid(final String id) {
		mNormalResid = PlatformHelper.toCurrentPath( id );
		refreshResids();
	}
	public String getNormalResid() {
		return mNormalResid;
	}
	protected static final int REF_NormalResid = DEFAULT_SHIFT + DROP_RESID_SHIFT;

	public void setPressedResid(final String id) {
		mPressedResid = PlatformHelper.toCurrentPath( id );
	}
	public String getPressedResid() {
		return mPressedResid;
	}
	protected static final int REF_PressedResid = DEFAULT_SHIFT + DROP_RESID_SHIFT;

	public void setInvalidResid(final String id) {
		mInvalidResid = PlatformHelper.toCurrentPath( id );
	}
	public String getInvalidResid() {
		return mInvalidResid;
	}
	protected static final int REF_InvalidResid = DEFAULT_SHIFT + DROP_RESID_SHIFT;
	
	public void setResids(final String normalId, final String pressedId) {
		setNormalResid(normalId);
		setPressedResid(pressedId);
	} 
	public void setResids(final String normalId, final String pressedId, final String invalidId) {
		setResids(normalId, pressedId);
		setInvalidResid(invalidId);
	} 
	
	public void setResids(final String [] ids) {
		try {
			setNormalResid(ids[0]);
			setPressedResid(ids[1]);
			setInvalidResid(ids[2]);
		} catch (Exception e) {
		}
	}
	public String [] getResids() { 
		return new String[] {mNormalResid, mPressedResid, mInvalidResid} ;
	} 
	protected static final int REF_Resids = DEFAULT_SHIFT;
	

	/**
	 * swap normalResid and pressedResid
	 */
	public void swapResids() {
		final String tmpId = mPressedResid;
		mPressedResid = mNormalResid;
		mNormalResid = tmpId;
		
		refreshResids();
	}

	public void setTouchEnable(boolean touchable) {
		if( touchable != super.getTouchEnable() ) { 
			super.setTouchEnable(touchable); 
			if( touchable ) { 
				mButtonState = ButtonState.Normal; 
			} else { 
				mButtonState = ButtonState.Invalid; 
			} 
			refreshResids(); 
		} 
	} 

	public void setSound(final int soundId, final float soundVolume) {
//		mButtonMotion.setSoundId(soundId);
//		mButtonMotion.setSoundVolume(soundVolume);
	} 

	private ElfOnClickListener mOnClickListener ;
	public void setOnClickListener(ElfOnClickListener elfOnClickListener) {
		mOnClickListener = elfOnClickListener;
	}

	protected final void refreshResids() {
		switch (mButtonState) { 
		case Normal: 
			this.setResid(mNormalResid);break; 
		case Pressed: 
			this.setResid(mPressedResid);break; 
		case Invalid: 
			if(mInvalidResid == null) { 
				this.setResid(mNormalResid);break; 
			} else { 
				this.setResid(mInvalidResid);break; 
			} 
		} 
	} 
	
	protected void drawPic() { 
		final String resid = getResid(); 
		switch (mButtonState) { 
		case Normal: 
			GLManage.draw(resid, mGLid);
			break; 
		case Pressed: 
		case Invalid:
			if(ResJudge.isRes(resid)) {
				GLManage.draw(resid, mGLid);
			} else {
				GLManage.draw(getNormalResid(), mGLid, Texture.IMAGE_GRAY);
			}
			break; 
		} 
	}
	
	protected String getSizeResid() {
		return mNormalResid;
	}
	
	public void trigger(ElfNode node, ElfEvent event) { 
		if(mOnClickListener != null) {
			mOnClickListener.onClick(node.getId(), event);
		}
	} 

	public void onPressed() { 
		mButtonState = ButtonState.Pressed; 
		refreshResids();
	}

	public void onReleased() { 
		mButtonState = ButtonState.Normal; 
		refreshResids();
	} 
	
	public void setTouchGiveUpOnMoveOrOutOfRange(boolean set) {
		mButtonEventDecoder.setTouchGiveUpOnMoveOrOutOfRange(set);
	}
	public boolean getTouchGiveUpOnMoveOrOutOfRange() {
		return mButtonEventDecoder.getTouchGiveUpOnMoveOrOutOfRange();
	}
	protected final static int REF_TouchGiveUpOnMoveOrOutOfRange = DEFAULT_SHIFT;
} 
