package elfEngine.basic.node.nodeAnimate.timeLine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import com.ielfgame.stupidGame.design.hotSwap.flash.FlashMainNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.MotionNode2FlashNode;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode.AnimateFrameNode;

public class ElfMotionNode extends ElfNode {
	private final String sKeyStorageName = "KeyStorage";

	private ElfNode mKeyStorage;
	private final MotionCounter mMotionCounter = new MotionCounter(0, 5000);

	private boolean mPauseMotion = true;

	public ElfMotionNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public ElfNode getKeyStorageNode() {
		return mKeyStorage;
	}
	
	public void setVisible(boolean visible) {
		if(visible && !getVisible()) {
			this.setProgress(0);
			this.setPauseMotion(false);
		}
		super.setVisible(visible);
	}

	private String mKeyStoreName;

	public String getKeyStorage() {
		if (mKeyStorage == null) {
			return null;
		}
		return mKeyStorage.getName();
	}

	public void setKeyStorage(final String name) {
		mKeyStoreName = name;
	}
	public String [] arrayKeyStorage() { 
		final LinkedList<String> list = new LinkedList<String>(); 
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				final String name = node.getName(); 
				if(name!=null && name.contains(sKeyStorageName)) { 
					list.add(name); 
				} 
				return false; 
			} 
		}); 
		final String ret [] = new String[list.size()]; 
		list.toArray(ret); 
		return ret; 
		
	}
	protected final static int REF_KeyStorage = DEFAULT_SHIFT;

	public void setInterType(InterType type) {
		mMotionCounter.setInterType(type);
	}

	public InterType getInterType() {
		return mMotionCounter.getInterType();
	}

	protected final static int REF_InterType = DEFAULT_SHIFT;

	public void setLoopMode(LoopMode mode) {
		mMotionCounter.setLoopMode(mode);
	}

	public LoopMode getLoopMode() {
		return mMotionCounter.getLoopMode();
	}

	protected final static int REF_LoopMode = DEFAULT_SHIFT;

	public void setProgress(int progress) { 
		mMotionCounter.setProgress(progress); 
		//calc
		updateProgress(); 
	} 

	public int getProgress() {
		return mMotionCounter.getProgress();
	}

	protected final static int REF_Progress = DEFAULT_FACE_SHIFT;

	public void setLoopStart(int start) {
		mMotionCounter.setLoopStart(start);
	}

	public int getLoopStart() {
		return mMotionCounter.getLoopStart();
	}

	protected final static int REF_LoopStart = DEFAULT_SHIFT;
	
	public void setLoopEnd(int end) {
		mMotionCounter.setLoopEnd(end);
	}

	public int getLoopEnd() {
		return mMotionCounter.getLoopEnd();
	}

	protected final static int REF_LoopEnd = DEFAULT_SHIFT;

	public void setPauseMotion(boolean pauseMotion) {
		mPauseMotion = pauseMotion;
	}

	public boolean getPauseMotion() {
		return mPauseMotion;
	}

	protected final static int REF_PauseMotion = DEFAULT_SHIFT;
	
	void updateProgress() {
		if (mKeyStoreName != null) {
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(final ElfNode node) {
					if (node.getName() != null && node.getName().equals(mKeyStoreName)) {
						mKeyStorage = node;
						return true;
					}
					return false;
				}
			});
			mKeyStoreName = null;
		}

		final int progress = mMotionCounter.getProgress();
		
		final ElfNode[] childs = this.getChilds();
		final NodePropertyType[] types = NodePropertyType.values();
		for (ElfNode child : childs) {
			if (child != mKeyStorage) {
				child.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {
						for (NodePropertyType type : types) { 
							final ElfMotionKeyNode[] keys = findKeysByNode(node, type.toString());
							if(keys == null || keys.length < 2) {
								//do nothing
								continue;
							} 
							
							int real_progress = progress;
							final ElfMotionKeyNode first = keys[0];
							final ElfMotionKeyNode last = keys[keys.length-1]; 
							final int firstTime = first.getTime(); 
							final int lastTime = last.getTime();
							
							if(real_progress < firstTime) {
								//do nothing
								type.modifier(first, first, node, 0); 
								continue;
							} 
							
							if(lastTime < real_progress) {
								switch(last.getLoopMode()) {
								case ENDLESS:
									real_progress = progress;
									break;
								case LOOP:
									real_progress = firstTime+ ((progress-firstTime) % (lastTime-firstTime));
									break;
								case RELOOP:
									real_progress = firstTime+ ((progress-firstTime) % ((lastTime-firstTime)*2));
									if(real_progress > lastTime) { 
										real_progress = lastTime*2  - real_progress;
									} 
									break;
								case STAY:
									real_progress = lastTime;
									break;
								} 
							} 
							
							boolean flag = true;
							for (int i = 1; i < keys.length && flag; i++) {
								final ElfMotionKeyNode prev = keys[i - 1];
								final ElfMotionKeyNode next = keys[i];
								if (prev.getTime() <= real_progress && next.getTime() > real_progress) { 
									type.modifier(prev, next, node, real_progress); 
									flag = false; 
								} 
							} 

							if(flag) {
								if (real_progress >= lastTime) { 
									final ElfMotionKeyNode prev = keys[keys.length - 2]; 
									final ElfMotionKeyNode next = last; 
									type.modifier(prev, next, node, real_progress);
								} 
							}
							
//							if (flag) {
//								if (keys.length == 1) {
//									type.modifier(keys[0], keys[0], node, 0); 
//								} else if (real_progress < keys[0].getTime()) {
//									final ElfMotionKeyNode prev = keys[0]; 
//									final ElfMotionKeyNode next = keys[1]; 
//									type.modifier(prev, next, node, real_progress);
//								} else if (real_progress >= keys[keys.length - 1].getTime()) {
//									final ElfMotionKeyNode prev = keys[keys.length - 2];
//									final ElfMotionKeyNode next = keys[keys.length - 1];
//									type.modifier(prev, next, node, real_progress);
//								} 
//							} 
						} 
						return false; 
					}
				});
			}
		}
	}

	public void calc(float pMsElapsed) {
		super.calc(pMsElapsed); 
		if (!mPauseMotion) { 
			mMotionCounter.count(Math.round(pMsElapsed * mMotionSpeed)); 
		} 
		try {
			updateProgress(); 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}

	private float mMotionSpeed = 1.0f;
	public void setMotionSpeed(float speed) {
		mMotionSpeed = speed;
	}
	public float getMotionSpeed() {
		return mMotionSpeed;
	}
	protected final static int REF_MotionSpeed = DEFAULT_SHIFT;
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();
		
		mKeyStorage = null;

		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				final String name = node.getName();
				if (name != null && name.contains(sKeyStorageName)) {
					mKeyStorage = node;
				}
				return mKeyStorage != null;
			}
		}); 

		if (mKeyStorage == null) {
			mKeyStorage = new ElfNode(this, 0);
			mKeyStorage.setName(sKeyStorageName);
			mKeyStorage.addToParent();

			mKeyStorage.setVisible(false);
		}
	}

	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		mKeyStorage = null;

		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				final String name = node.getName();
				if (name != null && name.contains(sKeyStorageName)) {
					mKeyStorage = node;
				}
				return mKeyStorage != null;
			}
		});

		if (mKeyStorage == null) {
			mKeyStorage = new ElfNode(this, 0);
			mKeyStorage.setName(sKeyStorageName);
			mKeyStorage.addToParent();

			mKeyStorage.setVisible(false);
		}
	}

	public boolean isRecurFatherOfAndNotKeys(ElfNode node) {
		return this.isRecurFatherOf(node) && (mKeyStorage == null || (mKeyStorage != node && !mKeyStorage.isRecurFatherOf(node)));
	}

	boolean removeKey(final ElfNode node, final String type, final int time) {
		final ElfNode keysParent = findKeysParent(node, type);
		if (keysParent != null) {
			final ElfMotionKeyNode[] keys = findKeysByParent(keysParent);
			if (keys != null) {
				for (ElfMotionKeyNode key : keys) {
					if (key.getTime() == time) {
						key.removeFromParentView(true);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public ElfMotionKeyNode [] getKeyNodesByTime(final ElfNode node , final int time) {
		final NodePropertyType [] types = NodePropertyType.values();
		final ElfMotionKeyNode [] ret = new ElfMotionKeyNode[types.length];
		
		for(int i=0; i<ret.length; i++) {
			final NodePropertyType type = types[i];
			final ElfMotionKeyNode key = findKeysByNodeAndTime(node, type.toString(), time);
			ret[i] = key;
		}
		
		return ret;
	}
	
	ElfMotionKeyNode findKeysByNodeAndTime(final ElfNode node , final String type, final int time) {
		final ElfMotionKeyNode[]  keys = findKeysByNode(node, type.toString());
		if(keys != null) {
			for(int i=0; i<keys.length; i++) {
				if(keys[i].getTime() == time) {
					return keys[i];
				}
			}
		}
		
		return null;
	}

	ElfMotionKeyNode addKey(final ElfNode node, final NodePropertyType type, final int time) {
		ElfNode keysParent = findKeysParent(node, type.toString());
		if (keysParent == null) {
			keysParent = new ElfNode(mKeyStorage, 0);
			keysParent.setName(toKeysParentName(node, type.toString()));
			keysParent.addToParentView();
		}

		ElfMotionKeyNode[] keys = findKeysByParent(keysParent);

		final ElfMotionKeyNode keyNode = new ElfMotionKeyNode(keysParent, 0);
		keyNode.setName("#key");

		// check time ???
		if (keys != null) {
			for (ElfMotionKeyNode key : keys) {
				if (key.getTime() == time) {
					return null;
				}
			}
		}

		if (keys != null && keys.length > 1) {
			Arrays.sort(keys, new Comparator<ElfMotionKeyNode>() {
				public int compare(ElfMotionKeyNode o1, ElfMotionKeyNode o2) {
					return o1.getTime() - o2.getTime();
				}
			});

			ElfMotionKeyNode prev = keys[0];
			ElfMotionKeyNode next = keys[1];
			
			if(keys[0].getTime() > time) {
				type.modifier(keys[0], keys[0], keyNode, time);
			} else if(keys[keys.length-1].getTime() < time) {
				type.modifier(keys[keys.length-1], keys[keys.length-1], keyNode, time);
			} else {
				for (int i = 1; i < keys.length; i++) {
					if (time > keys[i - 1].getTime() && time <= keys[i].getTime()) {
						prev = keys[i - 1];
						next = keys[i];
						break;
					}
				}

				type.modifier(prev, next, keyNode, time);
			}
		} else {
			keyNode.setPosition(node.getPosition());
			keyNode.setRotate(node.getRotate());
			keyNode.setColor(node.getColor());
			keyNode.setScale(node.getScale());
			keyNode.setVisible(node.getVisible());
		}

		keyNode.setCouldMove(false);

		keyNode.setTime(time);

		keyNode.addToParentView();

		return keyNode;
	}

	ElfNode findKeysParent(final ElfNode node, final String type) {
		final ElfNode[] ret = new ElfNode[1];
		if (mKeyStorage != null) {
			mKeyStorage.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode keysParent) {
					if (keysParent.getName().equals(toKeysParentName(node, type))) {
						ret[0] = keysParent;
						return true;
					}
					return false;
				}
			});
		}
		return ret[0];
	}

	ElfMotionKeyNode[] findKeysByParent(final ElfNode keysParent) {
		if (keysParent != null) {
			final LinkedList<ElfMotionKeyNode> list = new LinkedList<ElfMotionKeyNode>();
			keysParent.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode key) {
					if (key instanceof ElfMotionKeyNode) {
						list.add((ElfMotionKeyNode) key);
					}
					return false;
				}
			});
			ElfMotionKeyNode[] ret = new ElfMotionKeyNode[list.size()];
			list.toArray(ret);
			
			sort(ret);
			
			return ret;
		}
		return null;
	}

	public ElfMotionKeyNode[] findKeysByNode(final ElfNode node, final String type) {
		final ElfNode keysParent = findKeysParent(node, type);
		return findKeysByParent(keysParent);
	}

	void sort(ElfMotionKeyNode[] keys) {
		Arrays.sort(keys, new Comparator<ElfMotionKeyNode>() {
			public int compare(ElfMotionKeyNode o1, ElfMotionKeyNode o2) {
				return o1.getTime() - o2.getTime();
			}
		});
		for (int i = 0; i < keys.length; i++) {
			keys[i].setName("#key" + i);
		}
	}

	String toKeysParentName(final ElfNode node, String type) {
		return node.getName() + type;
	}

	AnimateNode toAnimateNode(final int fps) {
		final AnimateNode ret = new AnimateNode(this.getParent(), 0);
		
		ret.setName(this.getName()); 
		ret.onCreateRequiredNodes(); 
		
		final int start = this.getLoopStart();
		int end = this.getLoopEnd();
		
		final LoopMode mode = this.getLoopMode();
		switch (mode) {
		case LOOP:
		case STAY:
		case ENDLESS:
			break;
		case RELOOP:
			end = end + (end-start);
			break;
		} 
		
		final int step = Math.round(1000.0f/fps);
		
		this.setProgress(start);
		for(int i = start; i<=end; i+=step) { 
			final AnimateFrameNode frame = new AnimateFrameNode(ret, 0);
			frame.setFrameTime(step/1000f);
			
			frame.onCreateRequiredNodes();
			
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					final String name = node.getName();
					if(name != null && name.contains("KeyStorage")) {
						//nothing
					} else { 
						//copy
						final ElfNode child = node.copyDeep(frame); 
						child.addToParent(); 
						//convert 
						//SimpleAnimateNode to ElfNode
						//AnimateNode to ElfNode
					} 
					return false;
				} 
			});
			
			frame.addToParent();
			
			this.calc(step);
		} 
		
		return ret;
	} 
	
	public void setConvertToFlashMainNode() { 
		final FlashMainNode fm = MotionNode2FlashNode.convert(this);
		fm.addToParentView(Integer.MAX_VALUE);
	} 
	protected final static int REF_ConvertToFlashMainNode = FACE_SET_SHIFT; 
}
