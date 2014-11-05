package elfEngine.basic.node.nodeAnimate.timeLine;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.design.hotSwap.flash.MotionNode2FlashNode;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class TimeViewMenu extends AbstractMenu{

	protected final TimeCanvasView mTimeCanvasView;
	public TimeViewMenu(final TimeCanvasView view) {
		super(null);
		mTimeCanvasView = view;
		
		this.checkInMenuItem(new AbstractMenuItem("LoopMode"){
			public void onClick(SelectionEvent e) {
				final TimeData data = TimeData.getSelectTimeData();
				if(data != null) {
					final ElfMotionKeyNode keyNode = finKeyNode();
					if(keyNode != null) {
						final MultiLineDialog dialog = new MultiLineDialog();
						dialog.setTitle("Modifier LoopMode");
						
						final Object [] ret = dialog.open(keyNode.getLoopMode());
						if(ret != null && ret.length == 1) {
							if(ret[0] instanceof LoopMode) {
								keyNode.setLoopMode((LoopMode)ret[0]);
							}
						}
					}
				}
			} 
			
			public boolean isShow() {
				final TimeData data = TimeData.getSelectTimeData();
				if(data != null) {
					final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
					final ElfMotionKeyNode key = data.getBindKeyNode();
					if(motionNode != null && key != null) {
						final ElfNode parent = key.getParent();
						ElfMotionKeyNode[] keys = motionNode.findKeysByParent(parent);
						if(keys != null && keys.length > 0) {
							if(keys[keys.length-1] == key) {
								return true;
							} else {
								return false;
							}
						}
					}
				} else {
				}
				return false;
			}
			
			ElfMotionKeyNode finKeyNode() {
				final TimeData data = TimeData.getSelectTimeData();
				final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
				if(data != null && motionNode != null) {
					final ElfMotionKeyNode key = data.getBindKeyNode();
					return key;
				}
				return null;
			}
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("InterType"){
			public void onClick(SelectionEvent e) {
				final ElfMotionKeyNode keyNode = finKeyNode();
				if(keyNode != null) {
					final MultiLineDialog dialog = new MultiLineDialog();
					dialog.setTitle("Modifier InterType");
					
					dialog.setLabels(new String[]{"InterType"});
					dialog.setValues(new Object[]{keyNode.getInterType()});
					dialog.setValueTypes(new Class<?>[]{InterType.class});
					
					final Object [] ret = dialog.open();
					if(ret != null && ret.length == 1) {
						if(ret[0] instanceof InterType) {
							keyNode.setInterType((InterType)ret[0]);
						}
					}
				}
			} 
			public boolean isShow() {
				final TimeData data = TimeData.getSelectTimeData();
				if(data != null) {
					final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
					final ElfMotionKeyNode key = data.getBindKeyNode();
					if(motionNode != null && key != null) {
						final ElfNode parent = key.getParent();
						ElfMotionKeyNode[] keys = motionNode.findKeysByParent(parent);
						if(keys != null && keys.length > 0) {
							if(keys[keys.length-1] == key) {
								return false;
							} else {
								return true;
							}
						} else {
						}
					}
				} else {
				}
				return false;
			}
			
			ElfMotionKeyNode finKeyNode() {
				final TimeData data = TimeData.getSelectTimeData();
				final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
				if(data != null && motionNode != null) {
					final ElfMotionKeyNode key = data.getBindKeyNode();
					return key;
				}
				return null;
			}
		});
	
		this.checkInMenuItem(new AbstractMenuItem("导出帧动画") {
			public void onClick(SelectionEvent e) {
				final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
				if(motionNode != null) {
					MotionNode2FlashNode.exportImages(motionNode);
				}
			}
			
			public boolean isShow() {
				final ElfMotionNode motionNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
				if(motionNode != null) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
	
	public void onClick(SelectionEvent e) {
		
	}

}
