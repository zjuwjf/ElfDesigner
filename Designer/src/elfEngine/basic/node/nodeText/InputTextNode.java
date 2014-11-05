package elfEngine.basic.node.nodeText;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.TextAlign;
import com.ielfgame.stupidGame.enumTypes.TextFont;
import com.ielfgame.stupidGame.enumTypes.VerticalTextAlign;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;

public class InputTextNode extends ButtonNode {
	
	public enum KeyboardReturnType {
	    KeyboardReturnTypeDefault,
	    KeyboardReturnTypeDone,
	    KeyboardReturnTypeSend,
	    KeyboardReturnTypeSearch,
	    KeyboardReturnTypeGo
	}; 

	public enum EditBoxInputMode {
	    EditBoxInputModeAny,
	    EditBoxInputModeEmailAddr,
	    EditBoxInputModeNumeric,
	    EditBoxInputModePhoneNumber,
	    EditBoxInputModeUrl,
	    EditBoxInputModeDecimal,
	    EditBoxInputModeSingleLine
	};

	public enum EditBoxInputFlag { 
	    EditBoxInputFlagPassword,
	    EditBoxInputFlagSensitive,
	    EditBoxInputFlagInitialCapsWord,
	    EditBoxInputFlagInitialCapsSentence,
	    EditBoxInputFlagInitialCapsAllCharacters
	};
	
	private KeyboardReturnType mKeyboardReturnType = KeyboardReturnType.KeyboardReturnTypeDefault;
	private EditBoxInputMode mEditBoxInputMode = EditBoxInputMode.EditBoxInputModeAny;
	private EditBoxInputFlag mEditBoxInputFlag = EditBoxInputFlag.EditBoxInputFlagInitialCapsAllCharacters;
	private int mMaxLength = -1; 
	
	public KeyboardReturnType getKeyboardReturnType() {
		return mKeyboardReturnType;
	}
	public void setKeyboardReturnType(KeyboardReturnType mKeyboardReturnType) {
		this.mKeyboardReturnType = mKeyboardReturnType;
	}
	protected final static int REF_KeyboardReturnType = DEFAULT_SHIFT;
	
	public EditBoxInputMode getEditBoxInputMode() {
		return mEditBoxInputMode;
	}
	public void setEditBoxInputMode(EditBoxInputMode mEditBoxInputMode) {
		this.mEditBoxInputMode = mEditBoxInputMode;
	}
	protected final static int REF_EditBoxInputMode = DEFAULT_SHIFT;
	
	public EditBoxInputFlag getEditBoxInputFlag() {
		return mEditBoxInputFlag;
	}
	public void setEditBoxInputFlag(EditBoxInputFlag mEditBoxInputFlag) { 
		this.mEditBoxInputFlag = mEditBoxInputFlag;
	} 
	protected final static int REF_EditBoxInputFlag = DEFAULT_SHIFT;
	
	public void setMaxLength(int length) {
		mMaxLength = length;
	}
	public int getMaxLength() {
		return mMaxLength;
	}
	protected final static int REF_MaxLength = DEFAULT_SHIFT;
	
	public InputTextNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setName("#input");
		
		setPriorityLevel(INPUT_PRIORITY);
		
		mTextHold.setSelected(true);
	} 
	
	public void drawSelf() { 
		super.drawSelf(); 
		final float w = this.getWidth() - mMarginLeft - mMarginRight; 
		final float h = this.getHeight() - mMarginTop - mMarginBottom; 
		
		mTextHold.setTextSize(Math.round(h / (2*TextFont.getDefaultFontSizeRate()))); 
		mTextHold.setVerticlaAlign(VerticalTextAlign.TOP); 
		mTextHold.setTextAlign(TextAlign.LEFT); 
		mTextHold.setAnchorPosition(0.5f, 0.5f); 
		mTextHold.setDimensions(new ElfPointf(w, h)); 
		mTextHold.setSingleLine(true);
		mTextHold.setPosition((mMarginLeft-mMarginRight)/2, (mMarginBottom-mMarginTop)/2); 
		
		mTextHold.drawSprite(); 
		mTextHold.drawSpriteSelected(); 
	}
	private final TextNode mTextHold = new TextNode(this, 0); 
	
	public void setText(final String text) { 
		mTextHold.setText(text);
	} 
	public String getText() { 
		return mTextHold.getText();
	} 
	protected final static int REF_Text = DEFAULT_SHIFT;
	
	public boolean getDrawTextBound() {
		return mTextHold.isSelected();
	}
	public void setDrawTextBound(final boolean draw) {
		mTextHold.setSelected(draw);
	}
	protected final static int REF_DrawTextBound = DEFAULT_SHIFT;
	
	protected int mMarginTop; 
	protected int mMarginBottom; 
	protected int mMarginLeft; 
	protected int mMarginRight; 
	
	public int getMarginTop() {
		return mMarginTop;
	}
	public void setMarginTop(int mMarginTop) {
		this.mMarginTop = mMarginTop;
	}
	protected final static int REF_MarginTop = DEFAULT_SHIFT;

	public int getMarginBottom() {
		return mMarginBottom;
	}
	public void setMarginBottom(int mMarginBottom) {
		this.mMarginBottom = mMarginBottom;
	}
	protected final static int REF_MarginBottom = DEFAULT_SHIFT;
	
	public int getMarginLeft() {
		return mMarginLeft;
	}
	public void setMarginLeft(int mMarginLeft) {
		this.mMarginLeft = mMarginLeft;
	}
	protected final static int REF_MarginLeft = DEFAULT_SHIFT;
	
	public int getMarginRight() {
		return mMarginRight;
	}
	public void setMarginRight(int mMarginRight) {
		this.mMarginRight = mMarginRight;
	}
	protected final static int REF_MarginRight = DEFAULT_SHIFT;
}
