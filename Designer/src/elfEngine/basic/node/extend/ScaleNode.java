package elfEngine.basic.node.extend;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.list.ElfList;
import elfEngine.basic.modifier.AlphaModifier;
import elfEngine.basic.modifier.BasicNodeModifier;
import elfEngine.basic.modifier.INodeModifier.ModifierListener;
import elfEngine.basic.modifier.PathModifier;
import elfEngine.basic.modifier.ScaleModifier;
import elfEngine.basic.modifier.SequenceModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.HoldNode;
import elfEngine.basic.node.nodeTouch.slider.SliderNode;
import elfEngine.basic.node.nodeTouch.slider.SliderNode.IPercentageListener;
import elfEngine.basic.touch.BasicEventDecoder.TriggerListener;
import elfEngine.basic.touch.ElfEvent;

public class ScaleNode extends XMLNode {
	
	private final class RemoveLineModifier extends BasicNodeModifier{
		public RemoveLineModifier(float life) { 
			super(0, 1, life, life, null, null);
			
			this.setModifierListener(new ModifierListener() {
				public void onFinished(ElfNode node) {
					node.removeFromParent();
					mTotalLines.remove((SliderNode)node);
				}
			});
		}
		
		@Override
		public void modifier(ElfNode node) {
			final float value = this.getValue();
			node.setAlpha(1-value);
		}
	}
	
	private long mLastTime = System.currentTimeMillis();
	private ElfNode mSublinesControl;
	private HoldNode mLeftHoldNode;
	private HoldNode mTopHoldNode;
	private boolean mIsUseSublinesControl;
	
	private final ElfList<SliderNode> mTotalLines = new ElfList<SliderNode>();
	
	public ScaleNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setUseSettedSize(true);
		this.setIsInRunningNode(true);
		this.setTouchEnable(true);
		this.setTouchShielded(false);
		this.setUseModifier(true);
		this.setUseDelayHandler(true);
		this.setName("#");
		
		mIsUseSublinesControl = false;
		mSublinesControl = null;
		mLeftHoldNode = null;
		mTopHoldNode = null;
		
		this.runWithDelay(new Runnable() {
			public void run() {
				loadXML("SublinesControl.xml");
				
				mSublinesControl = ScaleNode.this.findByName("SublinesControl");
				mSublinesControl.setUseModifier(true);
				mSublinesControl.setPosition(getSublinesControlPosition());
				mSublinesControl.setVisible(false);
				
				mLeftHoldNode = (HoldNode) ScaleNode.this.findByName("SublinesControl_Left_Hold");
				mLeftHoldNode.setPosition(getLeftHoldPosition());
				setHoldAndLine(mLeftHoldNode, "@X");

				mTopHoldNode = (HoldNode) ScaleNode.this.findByName("SublinesControl_Top_Hold");
				mTopHoldNode.setPosition(getTopHoldPosition());
				setHoldAndLine(mTopHoldNode, "@Y");
				
				mIsUseSublinesControl = true;
			}
		}, 100);
	}
	
	public void calc(float dt) {
		final ElfScreenNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
		final float sx = screenNode.getScale().x;
		final float sy = screenNode.getScale().y;
		final ElfList<SliderNode>.Iterator it = mTotalLines.iterator(true);
		while(it.hasNext()) {
			final SliderNode line = it.next();
			final ElfPointf size = line.getSize();
			if(size.x > size.y) {
				line.setScale(10, 1/sy);
			} else {
				line.setScale( 1/sx, 10); 
			}
		}
	}
	
	public boolean onNextSelectTouch(final ElfEvent event) {
		final ElfList<SliderNode>.Iterator it = mTotalLines.iterator(false);
		while(it.hasPrev()) {
			final SliderNode line = it.prev();
			if(line.onNextSelectTouch(event)) {
				return true;
			}
		}
		return super.onNextSelectTouch(event);
	}
	
	ElfPointf getSublinesControlPosition() {
		final ProjectSetting config = PowerMan.getSingleton(ProjectSetting.class);
		return new ElfPointf(config.getLogicWidth()/2, config.getLogicHeight()/2);
	}
	
	ElfPointf getLeftHoldPosition() {
		final ProjectSetting config = PowerMan.getSingleton(ProjectSetting.class);
		return new ElfPointf(-config.getLogicWidth()/2, 0);
	}
	
	ElfPointf getTopHoldPosition() {
		final ProjectSetting config = PowerMan.getSingleton(ProjectSetting.class);
		return new ElfPointf(0, config.getLogicHeight()/2);
	}
	
	final void setHoldAndLine(final HoldNode holdNode, final String lineName) {
		holdNode.setUseModifier(true);
		holdNode.addListener(new TriggerListener() {
			public void trigger(ElfNode node, ElfEvent event) {
				holdNode.clearModifiers();
				final SequenceModifier sm = new SequenceModifier(new ScaleModifier(1, 2, 1, 2, 500, 500, null, InterType.EaseBackIn), new ScaleModifier(2, 1, 2, 1, 1000, 1000, null, InterType.EaseBackOut));
				holdNode.addModifier(sm);
				
				final SliderNode linenode = (SliderNode)getDynamicNode(lineName);
				linenode.setUseModifier(true);
				
				linenode.setParent(PowerMan.getSingleton(DataModel.class).getScreenNode()); 
				linenode.addToParent(Integer.MAX_VALUE);
				
				mTotalLines.insertLast(linenode);
				
				final boolean isXOrY = lineName.contains("X");
				if(isXOrY) {
					linenode.setPoint1(new ElfPointf(-1000000f, linenode.getPoint1().y));
					linenode.setPoint2(new ElfPointf(+1000000f, linenode.getPoint2().y));
				} else {
					linenode.setPoint1(new ElfPointf(linenode.getPoint1().x, -1000000f));
					linenode.setPoint2(new ElfPointf(linenode.getPoint2().x, +1000000f));
				}
				
				linenode.setCatched(true);
				linenode.setPositionInScreen(holdNode.getPositionInScreen());
				linenode.setPercentageListener(new IPercentageListener() {
					public void onPercentageChange(float percentage, boolean innerCall) {
						linenode.setAlpha(1);
						linenode.clearModifiers();
						
						final ElfPointf screenPos = linenode.getPositionInScreen();
						final ElfPointf holdPos = holdNode.getPositionInScreen();
						if( (isXOrY && screenPos.x <= (holdPos.x + holdNode.getSize().x)) || (!isXOrY && screenPos.y >= (holdPos.y - holdNode.getSize().y))) {
							final RemoveLineModifier rlm = new RemoveLineModifier(1000);
							linenode.addModifier(rlm);
						} else { 
						} 
					}
				});
			}
		});
	}

	public void update() {
		final long now = System.currentTimeMillis();
		final float escaped = now - mLastTime;
		mLastTime = now;
		this.calcSprite(escaped);
	} 

	public void toggle() { 
		if(mIsUseSublinesControl) {
			mSublinesControl.clearModifiers();
			mLeftHoldNode.clearModifiers();
			mTopHoldNode.clearModifiers();
			
			if(mSublinesControl.getVisible()) {
				final AlphaModifier alphaModifier = new AlphaModifier(1, 0, 500, 500, null, null);
				alphaModifier.setModifierListener(new ModifierListener() {
					public void onFinished(ElfNode node) {
						node.setVisible(false);
					}
				});
				mSublinesControl.addModifier(alphaModifier);
				
				mLeftHoldNode.addModifier(new PathModifier(500, 500, null, InterType.Viscous, mLeftHoldNode.getPosition(), new ElfPointf(getLeftHoldPosition()).translate(-100, 0)));
				mTopHoldNode.addModifier(new PathModifier(500, 500, null, InterType.Viscous, mTopHoldNode.getPosition(), new ElfPointf(getTopHoldPosition()).translate(0, 100)));
				
				final ElfList<SliderNode>.Iterator it = mTotalLines.iterator(false);
				while(it.hasPrev()) {
					final SliderNode line = it.prev();
					line.clearModifiers();
					final AlphaModifier lineAlphaModifier = new AlphaModifier(1, 0, 500, 500, null, null);
					alphaModifier.setModifierListener(new ModifierListener() {
						public void onFinished(ElfNode node) {
							node.setVisible(false);
						}
					});
					line.addModifier(lineAlphaModifier);
				}
			} else {
				mSublinesControl.setVisible(true);
				mSublinesControl.setAlpha(0);
				final AlphaModifier alphaModifier = new AlphaModifier(0, 1, 500, 500, null, null);
				mSublinesControl.addModifier(alphaModifier);
				
				mLeftHoldNode.addModifier(new PathModifier(500, 500, null, InterType.Viscous, mLeftHoldNode.getPosition(), getLeftHoldPosition()));
				mTopHoldNode.addModifier(new PathModifier(500, 500, null, InterType.Viscous, mTopHoldNode.getPosition(), getTopHoldPosition()));
			
				final ElfList<SliderNode>.Iterator it = mTotalLines.iterator(false);
				while(it.hasPrev()) {
					final SliderNode line = it.prev();
					line.clearModifiers();
					line.setVisible(true);
					line.setAlpha(0);
					final AlphaModifier lineAlphaModifier = new AlphaModifier(0, 1, 500, 500, null, null);
					line.addModifier(lineAlphaModifier);
				}
			} 
		}
	}
	
	public void clearAllLines() {
		final ElfList<SliderNode>.Iterator it = mTotalLines.iterator(false);
		while(it.hasPrev()) {
			final SliderNode line = it.prev();
			line.clearModifiers();
			final AlphaModifier lineAlphaModifier = new AlphaModifier(1, 0, 500, 500, null, null);
			lineAlphaModifier.setModifierListener(new ModifierListener() {
				public void onFinished(ElfNode node) {
					node.removeFromParent();
					mTotalLines.remove(line);
				}
			});
			line.addModifier(lineAlphaModifier);
		}
	}
}
