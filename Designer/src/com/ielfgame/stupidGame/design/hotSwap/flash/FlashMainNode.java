package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.widgets.FileDialog;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.swf.SwfSplit;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.utils.FileHelper;

import elf.flash.test.MySwf;
import elf.flash.test.SwfFrameData;
import elfEngine.basic.node.AddColorNode;
import elfEngine.basic.node.ElfNode;

public class FlashMainNode extends ElfNode implements IFlashMain {

	private KeyStorageNode mCurrKeyStorage, mNextKeyStorage, mTransitionKeyStorage;
	private boolean mPlaying = true;
	private String mKeyStorageName;
	private int mTransitionMills = 200;
	private int mTransitionProgress = 0;

	public FlashMainNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		final ProjectSetting ps = PowerMan.getSingleton(ProjectSetting.class);
		if(ps != null) {
			mResetPoint.setPoint(ps.getPhysicWidth()/2, ps.getPhysicHeight()/2);
		}
	}

	public void setTransitionMills(int mills) {
		mTransitionMills = mills;
	}

	public int getTransitionMills() {
		return mTransitionMills;
	}

	protected final static int REF_TransitionMills = DEFAULT_SHIFT;

	public void setPlaying(boolean play) {
		mPlaying = play;
	}
	
	public boolean isPlaying() {
		return mPlaying;
	}

	public boolean getPlaying() {
		return this.mPlaying;
	}

	public void calc(final float dt) {
		this.autoReducedOrAll(!PowerMan.getSingleton(FlashManager.class).getShowAll());
		this.update(Math.round(dt * this.getSpeedRate()));
	}

	public void update(int dmills) {
		if (mPlaying) {
			if (mNextKeyStorage != null && mTransitionKeyStorage != null) {
				this.mTransitionProgress += dmills;
				if (this.mTransitionProgress >= this.mTransitionMills) {

					mCurrKeyStorage = mNextKeyStorage;
					mKeyStorageName = mCurrKeyStorage.getName();
					mCurrKeyStorage.recheckTargets();
					this.setProgressTime(0);

					mTransitionKeyStorage = null;
					mNextKeyStorage = null;
				} else {
					mTransitionKeyStorage.setProgressTime(this.mTransitionProgress);
				}
			} else {
				setProgressTime(this.getProgressTime() + dmills);
			}
		}
	}

	public String getAnimateName() {
		if (mCurrKeyStorage == null) {
			return null;
		}
		return mCurrKeyStorage.getName();
	}

	public void setAnimateName(final String name) {

		final KeyStorageNode nextksnode = findKeyStorageByName(name);

		if (this.mPlaying && mTransitionMills > 0 && nextksnode != null) {
			mNextKeyStorage = nextksnode;
			mTransitionProgress = 0;

			mTransitionKeyStorage = new KeyStorageNode(this, 0);

			final int frame0 = 0;
			final int frame1 = mTransitionMills * mTransitionKeyStorage.getFPS() / (1000);

			final ElfNode[] normalChilds = getNormalChilds();
			for (final ElfNode child : normalChilds) {
				child.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {
						final KeyFrameArrayNode kfa = new KeyFrameArrayNode(mTransitionKeyStorage, 0);
						kfa.addToParentView();
						kfa.setTarget(node);

						final KeyFrameNode key = kfa.createFlashKeyByFrame(frame0);
						key.setPropertiesFromElfNode(node);
						kfa.addFlashKey(key);

						return false;
					}
				});
			}

			mNextKeyStorage.recheckTargets();
			mNextKeyStorage.setProgressTime(0);

			for (final ElfNode child : normalChilds) {
				child.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {
						final KeyFrameArrayNode kfa = mTransitionKeyStorage.findKeyFrameArrayNodeByTarget(node);

						final KeyFrameNode key = kfa.createFlashKeyByFrame(frame1);
						key.setPropertiesFromElfNode(node);
						kfa.addFlashKey(key);

						return false;
					}
				});
			}

			// mTransitionKeyStorage.recheckTargets();
			mTransitionKeyStorage.setProgressTime(0);

		} else {
			mKeyStorageName = name;
			mCurrKeyStorage = nextksnode;
			if (mCurrKeyStorage != null) {
				mCurrKeyStorage.recheckTargets();
			}
			this.setProgressTime(0);
		}
	}

	public String[] arrayAnimateName() {
		final LinkedList<String> list = new LinkedList<String>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node instanceof KeyStorageNode) {
					final String name = node.getName();
					if (name != null) {
						list.add(name);
					}
				}

				return false;
			}
		});
		final String ret[] = new String[list.size()];
		list.toArray(ret);
		return ret;
	}

	protected final static int REF_AnimateName = DEFAULT_SHIFT;

	public int getFPS() {
		if (mCurrKeyStorage != null) {
			return mCurrKeyStorage.getFPS();
		}
		return 1;
	}

	public void setFPS(int fps) {
		if (mCurrKeyStorage != null) {
			mCurrKeyStorage.setFPS(fps);
		}
	}

	public int getMaxF() {
		if (mCurrKeyStorage != null) {
			return mCurrKeyStorage.getMaxF();
		}
		return 1;
	}

	public void setMaxF(int maxF) {
		if (mCurrKeyStorage != null) {
			mCurrKeyStorage.setMaxF(maxF);
		}
	}

	public int getMaxFIndex() {
		return this.getMaxF() + 1;
	}

	public void setMaxFIndex(int i) {
		this.setMaxF(i - 1);
	}

	private final void checkKeyStorage() {
		mCurrKeyStorage = null;
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node instanceof KeyStorageNode) {
					if (node.getName().equals(mKeyStorageName)) {
						mCurrKeyStorage = (KeyStorageNode) node;
					}
				}
				return mCurrKeyStorage != null;
			}
		});
		if (mCurrKeyStorage == null) {
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if (node instanceof KeyStorageNode) {
						mCurrKeyStorage = (KeyStorageNode) node;
					}
					return mCurrKeyStorage != null;
				}
			});
		}

		// if (mKeyStorage == null) {
		// mKeyStorage = new KeyStorageNode(this, 0);
		// mKeyStorage.addToParent();
		// }
	}

	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();
		checkKeyStorage();
	}

	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		checkKeyStorage();
	}

	private void reduced() {
		// check and delete unused frames
		boolean isSelfRecycled = PowerMan.getSingleton(DataModel.class).getRootRecycle().isRecurFatherOf(this);
		if (isSelfRecycled) {
			PowerMan.getSingleton(FlashManager.class).bindFlashMain(null);
			return;
		}

		final KeyFrameArrayNode[] kfans = this.getUsedKeyFrameArrayNodes();
		for (KeyFrameArrayNode kfan : kfans) {
			if (kfan.getFlashKeys().length == 0) {
				final ElfNode target = kfan.getTarget();
				if (target == null || !target.isSelected()) {
					kfan.removeFromParentView(false);
				}
			} else {
				kfan.checkTarget();
				ElfNode target = kfan.getTarget();

				if (target != null) {
					if (!PowerMan.getSingleton(DataModel.class).getRootScreen().isRecurFatherOf(target)) {
						kfan.removeFromParentView(false);
					}
				} else {
					kfan.removeFromParentView(false);
				}
			}
		}
	}

	private void all() {
		if (mCurrKeyStorage != null) {
			final ElfNode[] childs = this.getNormalChilds();
			for (ElfNode child : childs) {
				child.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {

						final KeyFrameArrayNode kfan = findFlashKeyArrayByTarget(node);
						if (kfan == null) {
							final KeyFrameArrayNode newkfan = new KeyFrameArrayNode(mCurrKeyStorage, -1);
							newkfan.setTarget(node);
							newkfan.addToParentView();
						}

						return false;
					}
				});
			}
		}
	}

	public void autoReducedOrAll(boolean reduced) {
		if (reduced) {
			reduced();
		} else {
			all();
		}
	}

	private final KeyFrameArrayNode[] getUsedKeyFrameArrayNodes() {
		if (mCurrKeyStorage != null) {
			return mCurrKeyStorage.getKeyFrameArrayNodes();
		} else {
			return new KeyFrameArrayNode[0];
		}
	}

	public KeyFrameArrayNode[] getFlashKeyArrays() {
		if (mCurrKeyStorage != null) {

			final ArrayList<ElfNode> array = this.getMyNormalSelectNodes();
			for (final ElfNode node : array) {
				final KeyFrameArrayNode kfan = findFlashKeyArrayByTarget(node);
				if (kfan == null) {
					final KeyFrameArrayNode newkfan = new KeyFrameArrayNode(mCurrKeyStorage, -1);
					newkfan.setTarget(node);
					newkfan.addToParentView();
				}
			}
			return mCurrKeyStorage.getKeyFrameArrayNodes();
		}
		return new KeyFrameArrayNode[0];
	}

	private ArrayList<ElfNode> getMyNormalSelectNodes() {
		final ArrayList<ElfNode> myList = new ArrayList<ElfNode>();
		final ArrayList<ElfNode> selectList = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (ElfNode node : selectList) {
			if (isRecurFatherOfAndNotKeys(node)) {
				if (node instanceof KeyFrameNode || node instanceof KeyFrameArrayNode || node instanceof KeyStorageNode) {
				} else {
					myList.add(node);
				}
			}
		}
		return myList;
	}

	private boolean isRecurFatherOfAndNotKeys(ElfNode node) {
		return this.isRecurFatherOf(node) && (mCurrKeyStorage == null || (mCurrKeyStorage != node && !mCurrKeyStorage.isRecurFatherOf(node)));
	}

	public ElfNode findTargetBySimpleName(final String name) {
		final ElfNode[] nodes = this.getChilds();
		for (final ElfNode node : nodes) {
			if (!(node instanceof KeyStorageNode)) {

				final ElfNode target = node.findBySimpleName(name);
				if (target != null) {
					return target;
				}
			}
		}
		return null;
	}

	// private void updateFlash(final int mills) {
	// final KeyFrameArrayNode[] kfas = this.getUsedKeyFrameArrayNodes();
	// final float fp = (mills * this.getFPS() / 1000.0f) % this.getMaxF();
	//
	// for (final KeyFrameArrayNode kfa : kfas) {
	// kfa.setFrame(fp);
	// }
	// }

	// add key frame -. empty, last,
	public void addKeyFrame(ElfNode node, int frame) {
		final KeyFrameArrayNode[] kfas = this.getUsedKeyFrameArrayNodes();
		for (final KeyFrameArrayNode kfa : kfas) {
			if (kfa.getTarget() == node) {
				final KeyFrameNode[] kfs = kfa.getFlashKeys();
				for (int i = 0; i < kfs.length; i++) {
					final KeyFrameNode kf = kfs[i];
					if (kf.getFrame() == frame) {
						kf.setPropertiesFromElfNode(node);
						return;
					} else if (kf.getFrame() > frame) {
						break;
					}
				}

				final KeyFrameNode newkf = new KeyFrameNode(kfa, frame);
				newkf.addToParentView();
				newkf.setPropertiesFromElfNode(node);
				return;
			}
		}
	}

	public void play() {
		this.setProgressTime(0);
		this.setPlaying(true);
	}

	public void stop() {
		this.setPlaying(false);
	}

	public int getProgressTime() {
		if (mCurrKeyStorage != null) {
			return mCurrKeyStorage.getProgressTime();
		}
		return 0;
	}

	public void setProgressTime(int mills) {
		if (mCurrKeyStorage != null) {
			mCurrKeyStorage.setProgressTime(mills);
		}
	}

	protected final static int REF_ProgressTime = DEFAULT_FACE_SHIFT;

	public int getLoopTime() {
		return Math.round(this.getMaxF() * (1000.0f / this.getFPS()));
	}

	// public void calc(float dt) {
	// update(dt);
	// }

	public KeyFrameArrayNode findFlashKeyArrayByTarget(ElfNode target) {
		if (target != null && mCurrKeyStorage != null) {
			return mCurrKeyStorage.findFlashKeyArrayByTarget(target);
		}
		return null;
	}

	public KeyFrameNode[] getAllFlashKeys() {
		final ArrayList<KeyFrameNode> list = new ArrayList<KeyFrameNode>();

		final KeyFrameArrayNode[] kfans = this.getFlashKeyArrays();
		for (KeyFrameArrayNode kfan : kfans) {
			KeyFrameNode[] kfns = kfan.getFlashKeys();
			for (KeyFrameNode kfn : kfns) {
				list.add(kfn);
			}
		}

		return list.toArray(new KeyFrameNode[list.size()]);
	}

	/***
	 * sorted
	 */
	public IFlashKey[] getAllSelectedFlashKeys() {
		final ArrayList<IFlashKey> list = new ArrayList<IFlashKey>();
		final KeyFrameArrayNode[] kfas = getFlashKeyArrays();
		for (final KeyFrameArrayNode kfa : kfas) {
			final KeyFrameNode[] kfns = kfa.getFlashKeys();
			for (final KeyFrameNode kfn : kfns) {
				if (kfn.getFrameSelect()) {
					list.add(kfn);
				}
			}
		}

		FlashStruct.sortList(list, true);

		return list.toArray(new IFlashKey[list.size()]);
	}

	public void setAllFlashKeySelected(boolean selected) {
		KeyFrameNode[] all = getAllFlashKeys();
		for (KeyFrameNode kf : all) {
			kf.setFrameSelect(selected);
		}
	}

	public void check() {
		this.checkKeyStorage();

		final KeyFrameArrayNode[] kfas = this.getFlashKeyArrays();
		for (KeyFrameArrayNode k : kfas) {
			k.checkTarget();
		}
	}

	public int getCurrentFrame() {
		return (int) (this.getProgressTime() / (1000.0f / this.getFPS()));
	}

	// public boolean iterateChildsDeepForFace(IIterateChilds iterator) {
	//
	// final ElfList<ElfNode>.Iterator it = getChildList().iterator(true);
	// while (it.hasNext()) {
	// final ElfNode node = it.next();
	//
	// if(node instanceof KeyStorageNode) {
	// continue;
	// }
	//
	// if (iterator.iterate(node)) {
	// return true;
	// }
	// if (node.iterateChildsDeepForFace(iterator)) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	public ElfNode[] getNormalChilds() {
		final LinkedList<ElfNode> list = new LinkedList<ElfNode>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(final ElfNode node) {
				if (!(node instanceof KeyStorageNode)) {
					list.add(node);
				}
				return false;
			}
		});
		return list.toArray(new ElfNode[list.size()]);
	}

	public KeyStorageNode findKeyStorageByName(final String name) {
		final KeyStorageNode[] ret = new KeyStorageNode[1];
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node instanceof KeyStorageNode) {
					if (node.getName().equals(name)) {
						ret[0] = (KeyStorageNode) node;
					}
				}
				return ret[0] != null;
			}
		});

		return ret[0];
	}

	public void addAnimate(String name) {
		if (name != null) {
			KeyStorageNode ks = findKeyStorageByName(name);
			if (ks == null) {
				ks = new KeyStorageNode(this, 0);
				ks.setName(name);
				ks.addToParentView();
			}
		}
	}

	public void copyCurrentAnimate(String name) {
		if (name != null) {
			KeyStorageNode ks = findKeyStorageByName(name);
			if (ks == null) {
				KeyStorageNode copy = findKeyStorageByName(this.getAnimateName());
				if (copy != null) {
					ks = (KeyStorageNode) copy.copyDeep(copy.getParent());
					ks.setName(name);
					ks.addToParentView();
				}
			}
		}
	}

	public void deleteAnimate(String name) {
		if (name != null) {
			KeyStorageNode ks = findKeyStorageByName(name);
			if (ks != null) {
				ks.removeFromParent();
			}
		}
	}

	/***
	 * 扁平化 处理
	 */
	public void setFlatten() {
		/**
		 * 
		 */
		// position
		// scale
		// rotate
		// visible

		// this.

		if (this.mCurrKeyStorage != null) {
			this.mCurrKeyStorage.recheckTargets();
		}

		final ElfNode[] childs = this.getChilds();
		for (final ElfNode child : childs) {
			if (!(child instanceof KeyStorageNode)) {
				final ElfNode newChild = child.copyDeep();
				newChild.addToParentView();

				final int[] index = new int[] { 0 };

				final LinkedList<ElfNode> list = new LinkedList<ElfNode>();

				newChild.iterateChildsDeep(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {
						if (node.getParent() != newChild) {
							node.removeFromParent();

							node.setOrdinal(index[0]);
							node.setParent(newChild);

							list.add(node);
						}
						index[0]++;
						return false;
					}
				});

				for (ElfNode node : list) {
					node.addToParent(node.ordinal());
				}

				// 手左， 手右
				for (int i = 0; i < this.getMaxF(); i++) {
					final int ii = i;
					this.setProgressTime(Math.round((i * 1000 / this.getFPS())));

					child.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(final ElfNode node) {

							final ElfNode newNode = newChild.findBySimpleName(node.getName());

							final ElfPointf pos = node.getPositionInScreen();
							final ElfPointf scale = getScale(node, child);
							final float rotate = getRotate(node, child);
							final boolean visible = getVisible(node, child);
							final float alpha = getAlpha(node, child);

							newNode.setPositionInScreen(pos);
							setRotate(rotate, newNode, newChild);
							setScale(scale, newNode, newChild);
							setVisible(visible, newNode, newChild);
							setAlpha(alpha, newNode, newChild);

							KeyFrameArrayNode kfa = findFlashKeyArrayByTarget(node);
							if (kfa == null) {
								kfa = new KeyFrameArrayNode(mCurrKeyStorage, 0);
								kfa.addToParentView();
							}

							KeyFrameNode kf = kfa.findFlashKeyByFrame(ii);
							if (kf == null) {
								kf = new KeyFrameNode(kfa, 0);
								kf.setFrame(ii);

								kf.setPosition(newNode.getPosition());
								kf.setRotate(newNode.getRotate());
								kf.setScale(newNode.getScale());
								kf.setVisible(newNode.getVisible());
								kf.setResid(newNode.getResid());
								kf.setColor(newNode.getColor());

								kfa.addFlashKey(kf);
							} else {
								kf.setPosition(newNode.getPosition());
								kf.setRotate(newNode.getRotate());
								kf.setScale(newNode.getScale());
								kf.setVisible(newNode.getVisible());
								kf.setResid(newNode.getResid());
								kf.setColor(newNode.getColor());
							}

							return false;
						}
					});

				}
				child.removeFromParentView(true);

			}
		}

		if (this.mCurrKeyStorage != null) {
			this.mCurrKeyStorage.recheckTargets();
		}
	}

	// protected static final int REF_Flatten = FACE_SET_SHIFT;

	private static ElfPointf getScale(ElfNode child, ElfNode parent) {
		final ElfPointf scale = child.getScale();
		while (child.getParent() != null && child.getParent() != parent) {
			child = child.getParent();
			final ElfPointf tmp = child.getScale();
			scale.mult(tmp.x, tmp.y);
		}
		return scale;
	}

	private static void setScale(ElfPointf scale, ElfNode child, ElfNode parent) {
		// final ElfPointf old = getScale(child, parent);
		// final ElfPointf origin = child.getScale();
		//
		// origin.mult(scale.x / old.x, scale.y / old.y);
		child.setScale(scale);
	}

	private static float getRotate(ElfNode child, ElfNode parent) {
		float rotate = child.getRotate();
		while (child.getParent() != null && child.getParent() != parent) {
			child = child.getParent();
			rotate += child.getRotate();
		}
		return rotate;
	}

	private static void setRotate(float rotate, ElfNode child, ElfNode parent) {
		// final float old = getRotate(child, parent);
		// final float origin = child.getRotate();
		child.setRotate(rotate);
	}

	private static boolean getVisible(ElfNode child, ElfNode parent) {
		boolean visible = child.getVisible();
		while (child.getParent() != null && child.getParent() != parent) {
			child = child.getParent();
			visible = (visible && child.getVisible());
		}
		return visible;
	}

	private static void setVisible(boolean visible, ElfNode child, ElfNode parent) {
		// final boolean old = getVisible(child, parent);
		child.setVisible(visible);
	}

	private static float getAlpha(ElfNode child, ElfNode parent) {
		float alpha = child.getAlpha();
		while (child.getParent() != null && child.getParent() != parent) {
			child = child.getParent();
			alpha *= child.getAlpha();
		}
		return alpha;
	}

	private static void setAlpha(float alpha, ElfNode child, ElfNode parent) {
		// final float old = getAlpha(child, parent);
		// final float origin = child.getAlpha();
		child.setRotate(alpha);
	}

	public void setSpeedRate(float rate) {
		if (mCurrKeyStorage != null) {
			mCurrKeyStorage.setSpeedRate(rate);
		}
	}

	public float getSpeedRate() {
		if (mCurrKeyStorage != null) {
			return mCurrKeyStorage.getSpeedRate();
		}
		return 1;
	}
	
	private String mResetName = "腿左";
	public void setFixedNodeName(String name) {
		mResetName = name;
	}
	public String getFixedNodeName() {
		return mResetName;
	}
	protected final static int REF_FixedNodeName = FACE_ALL_SHIFT | PASTE_SHIFT | COPY_SHIFT | UNDO_SHIFT ;
	
	
	private ElfPointf mResetPoint = new ElfPointf(284, 160);
	
	public final void setFixedPosition(final ElfPointf resetPos) {
		mResetPoint.set(resetPos);
		this.resetPosition(mResetName, resetPos);
	}
	
	public ElfPointf getFixedPosition() {
		return new ElfPointf(mResetPoint);
	}
	protected final static int REF_FixedPosition = REF_FixedNodeName;
	
	public void autoReplenish() {
		KeyStorageNode [] array = this.getKeyStorageNodeArray();
		for(final KeyStorageNode ksn:array) {
			if (ksn != null) {
				ksn.recheckTargets();
				final ElfNode[] childs = this.getNormalChilds();
				for (final ElfNode child : childs) {
					child.iterateChildsDeepWithSelf(new IIterateChilds() {
						public boolean iterate(final ElfNode node) {
							KeyFrameArrayNode kfan = ksn.findFlashKeyArrayByTarget(node);
							if (kfan == null) {
								final KeyFrameArrayNode newkfan = new KeyFrameArrayNode(ksn, -1);
								newkfan.setTarget(node);
								newkfan.setName(node.getName());
								newkfan.addToParentView();
								
								kfan = newkfan;
							}
							
							KeyFrameNode [] kfns = kfan.getFlashKeys();
							if(kfns.length <= 0) {
								final KeyFrameNode kfn0 = new KeyFrameNode(kfan, 0);
								kfn0.setFrame(0);
								kfn0.setVisible(false);
								kfn0.addToParentView();
								
								final KeyFrameNode kfn1 = new KeyFrameNode(kfan, 0);
								kfn1.setFrame(ksn.getMaxF());
								kfn1.setVisible(false);
								kfn1.addToParentView();
							}

							return false;
						}
					});
				}
			}
		}
	}
	
	private final void resetPosition(final String name, final ElfPointf resetPos) {
		final ElfNode node = this.findTargetBySimpleName(name);
		if(node != null) {
			final KeyStorageNode [] ksnarray = getKeyStorageNodeArray();
			final ElfNode [] childs = this.getNormalChilds();
			
			for(KeyStorageNode ksn : ksnarray) {
				ksn.recheckTargets();
				ksn.setProgressTime(0);
				
				final ElfPointf pos = node.getPositionInScreen();
				final ElfPointf offset = new ElfPointf(resetPos).translate(-pos.x, -pos.y);
				
				final ElfPointf scale = PowerMan.getSingleton(ProjectSetting.class).ScreenScale;
				offset.mult(1/scale.x, 1/scale.y);
				
				for(ElfNode child : childs) {
					final KeyFrameArrayNode kfan = ksn.findFlashKeyArrayByTarget(child);
					if(kfan != null) {
						final KeyFrameNode [] kfns = kfan.getFlashKeys();
						for(KeyFrameNode kfn : kfns) {
							kfn.translate(offset.x, offset.y);
						}
					}
				}
			}
			
			this.setProgressTime(this.getProgressTime());
		}
	}
	
	private final void scaleKeyStorageNode(KeyStorageNode ksn, final float scale) {
		try {
			final KeyFrameArrayNode[] kfans = ksn.getKeyFrameArrayNodes();
			for(KeyFrameArrayNode kfan : kfans) {
				final  KeyFrameNode[] kfns = kfan.getFlashKeys();
				for(KeyFrameNode kfn : kfns) {
					final ElfPointf pos = kfn.getPosition();
					pos.mult(scale);
					kfn.setPosition(pos);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private KeyStorageNode[] getKeyStorageNodeArray() {
		final LinkedList<KeyStorageNode> list = new LinkedList<KeyStorageNode>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node instanceof KeyStorageNode) {
					list.add((KeyStorageNode)node);
				}
				return false;
			}
		});
		return list.toArray(new KeyStorageNode[list.size()]);
	}
	
	public void setBoneScaleSingle(final float scale) {
		if(scale > 0 && this.mCurrKeyStorage != null) {
			final float myScale = scale;
			scaleKeyStorageNode(this.mCurrKeyStorage, myScale);
			this.setProgressTime( this.getProgressTime() );
		}
	}
	
	/***
	 * 缩放骨骼
	 */
	private float mBoneScale = 1;
	public void setBoneScale(final float scale) {
		if(scale > 0) {
			final float myScale = scale/mBoneScale;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if (node instanceof KeyStorageNode) {
						scaleKeyStorageNode((KeyStorageNode)node, myScale);
					}
					return false;
				}
			});
			
			mBoneScale = scale;
			
			this.setProgressTime( this.getProgressTime() );
		}
	}
	
	public float getBoneScale() {
		return mBoneScale;
	}
	
	private final void replaceSkinKeyStorageNode(KeyStorageNode ksn, final int id) {
		try {
			final KeyFrameArrayNode[] kfans = ksn.getKeyFrameArrayNodes();
			for(KeyFrameArrayNode kfan : kfans) {
				final  KeyFrameNode[] kfns = kfan.getFlashKeys();
				for(KeyFrameNode kfn : kfns) {
					final String resid = kfn.getResid();
					final String myresid = SkinManager.getSkinById(resid, id);
					kfn.setResid(myresid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 替换皮肤 
	 * 替换规则
	 * default map
	 * pppNfff.png or pppNfff.jpg
	 */
	public void replaceSkinById(final int id) {
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node instanceof KeyStorageNode) {
					replaceSkinKeyStorageNode((KeyStorageNode)node, id);
				}
				return false;
			}
		});
		
		this.setProgressTime( this.getProgressTime() );
	}
	
	//增加最左,最右关键帧
	public void addLRKeys(final String nameSrc, final String nameDst) {
		final KeyStorageNode keyStorageNodeSrc = this.findKeyStorageByName(nameSrc);
		final KeyStorageNode keyStorageNodeDst = this.findKeyStorageByName(nameDst);
		
		if(keyStorageNodeSrc != null && keyStorageNodeDst != null && keyStorageNodeDst != keyStorageNodeSrc) {
			
			UndoRedoManager.checkInUndo();
			
			final KeyFrameArrayNode[] kfaSrc = keyStorageNodeSrc.getKeyFrameArrayNodes();
			final KeyFrameArrayNode[] kfaDst = keyStorageNodeDst.getKeyFrameArrayNodes();
			for(KeyFrameArrayNode kfa : kfaSrc) {
				final String name = kfa.getName();
				for(KeyFrameArrayNode kfa2 : kfaDst) {
					final String name2 = kfa2.getName();
					if(name.equals(name2)) {
						final KeyFrameNode[] kfns = kfa.getFlashKeys();
						if(kfns != null && kfns.length >= 2) {
							KeyFrameNode first = null;
							KeyFrameNode last = kfns[kfns.length-1];
							for(KeyFrameNode k : kfns) {
								if(k.getFrame()>=0) {
									first = k;
									break;
								}
							}
							
							if(first != null && last != null && last.getFrame() > first.getFrame()) {
								final int left = 0;
								final int right = keyStorageNodeDst.getMaxF();
								
								final KeyFrameNode copy1 = first.copyKey();
								copy1.setFrame(left);
								
								final KeyFrameNode old1 = kfa2.findFlashKeyByFrame(left);
								if(old1 != null) {
									kfa2.removeFlashKey(old1);
								}
								kfa2.addFlashKey(copy1);
								
								final KeyFrameNode copy2 = last.copyKey();
								copy2.setFrame(right);
								
								final KeyFrameNode old2 = kfa2.findFlashKeyByFrame(right);
								if(old2 != null) {
									kfa2.removeFlashKey(old2);
								}
								kfa2.addFlashKey(copy2);
							}
						}
						break;
					}
				}
			}
		}
	}
	
	private boolean hasAddColor(final ArrayList<SwfFrameData> array) {
		for(SwfFrameData data : array) {
			if(data.addColor != null) {
				return true;
			}
		}
		return false;
	}
	
	
	public void setLoadSwf() {
		final FileDialog fd = new FileDialog(PowerMan.getSingleton(MainDesigner.class).getShell());
		fd.setFilterExtensions(new String[]{"*.swf", "*.SWF"});
		fd.setFileName(ResManager.getSingleton().getDesignerSWFAsset());
		
		final String swfPath = fd.open();
		if(swfPath != null) {
			loadSwf(swfPath);
			MessageDialog md = new MessageDialog();
			md.open("", "加载SWF成功!");
		}
	}
	
	protected final static int REF_LoadSwf = FACE_SET_SHIFT;
	
	public void loadSwf(final String swfPath) {
		/***
		 */
		final MySwf mySwf = SwfSplit.exportImages(swfPath);
		
		if(mySwf != null) {
			final String nameHead = "Swf_"+SpellHelper.getUpEname( FileHelper.getSimpleNameNoSuffix(swfPath) ) + "-";
			
			//SwfReader.exportImages(swf, dir+FileHelper.DECOLLATOR + SpellHelper.getUpEname(name));
			final HashMap<Integer, ArrayList<SwfFrameData>> dataMap = mySwf.depthData;
			final Set<Integer> set = dataMap.keySet();
			final ArrayList<Integer> keyArray = new ArrayList<Integer>(set);
			Collections.sort(keyArray);
			
			final KeyStorageNode ksNode = new KeyStorageNode(this, 0);
			ksNode.setApplyAnchor(true);
			ksNode.setFixedFrameEnable(true);
			ksNode.setName("swf");
			ksNode.addToParentView();
			
			ksNode.setMaxF(mySwf.stageData.maxF);
			ksNode.setFPS(mySwf.stageData.fps);
			
			final ElfNode root = new ElfNode(this, 0);
			root.setName("root");
			root.addToParentView();
			
			for(int key : keyArray) {
				final ArrayList<SwfFrameData> array = dataMap.get(key);
				final boolean hasAC = hasAddColor(array);
				final ElfNode target;
				if(hasAC) {
					target = new AddColorNode(root, 0);
				} else {
					target = new ElfNode(root, 0); 
				}
				target.addToParentView();
				
				target.setName("tag-"+key);
				
				final KeyFrameArrayNode kaNode = new KeyFrameArrayNode(ksNode, 0);
				kaNode.setTarget(target);
				kaNode.setName(target.getName());
				kaNode.addToParentView();
				
				KeyFrameNode last = null;
				for(final SwfFrameData data : array) {
					final KeyFrameNode kfNode;
					
					if(last == null && data.frame > 1) {
						KeyFrameNode first = new KeyFrameNode(kaNode, 0);
						first.setVisible(false);
						first.addToParentView();
					}
					
					if(last != null) {
						kfNode = last.copyKey();
					} else {
						kfNode = new KeyFrameNode(kaNode, 0);
					}
					kfNode.addToParentView();
					
					kfNode.setFrame(data.frame-1);
					
					if(data.position != null) {
						kfNode.setPosition(-mySwf.stageData.width/2+data.position[0], mySwf.stageData.height/2-data.position[1]);
					}
					if(data.scale != null) {
						kfNode.setScale(data.scale[0], data.scale[1]);
					}
					if(data.alpha != null) {
						kfNode.setAlpha(data.alpha[0]/255f);
					}
					if(data.rotation != null) {
						kfNode.setRotate(data.rotation[0]);
					}
					if(data.visible != null) {
						kfNode.setVisible(data.visible[0]);
					}
					if(data.anchor != null) {
//						target.setAnchorPosition(data.anchor[0], data.anchor[1]);
						kfNode.setAnchorPosition(data.anchor[0], data.anchor[1]);
					}
					if(data.color != null) {
						kfNode.setColorNoAplha(new ElfColor(data.color[0]/255f, data.color[1]/255f, data.color[2]/255f, 1));
					}
					if(data.shapeId != null) {
						kfNode.setResid(nameHead+data.shapeId[0]+".png");
					}
					if(data.addColor != null) {
						//暂时不支持
					}
					last = kfNode;
				}
			}
			
			this.setAnimateName(ksNode.getName());
//			mySwf.shapeMap;
		}
	}
	
	public String toLuaString( String name ) {
		final KeyStorageNode[] ksNodes = this.getKeyStorageNodeArray();
		final StringBuilder sb = new StringBuilder();
		sb.append("{ ['name'] = ").append("'").append(name).append("',");
		
		for(final KeyStorageNode ks:ksNodes) {
			final int loop = Math.round( ks.getMaxF() * (1000.0f/ks.getFPS()) /ks.getSpeedRate() );
			sb.append("\t['").append(ks.getName()).append("'] = ").append(loop).append(",");
		}
		
		sb.append("\t}");
		return sb.toString();
	}
	
	public static class BoneDialogData extends ElfDataDisplay {
		public String cloneBone = "输入被克隆的骨骼";
		public String newBone = "输入新的骨骼";
		public String image = "输入图片";
	}
	public void setTriggerBoneClone() {
		final BoneDialogData data = new BoneDialogData();
//		final 
		MultiLineDialog dialog = new MultiLineDialog();
		final Object [] objs = dialog.open(data);
		if(objs != null) {
			data.setValues(objs);
			if( bindBone(data.cloneBone, data.newBone, data.image) ) {
				MessageDialog md = new MessageDialog();
				md.open("", "绑定新骨骼成功!");
			} else {
				MessageDialog md = new MessageDialog();
				md.open("", "输入参数错误!");
			}
		}
	}
	protected final static int REF_TriggerBoneClone = FACE_SET_SHIFT;
	
	//增加绑定骨骼
	public boolean bindBone(final String oldBone, final String newBone, final String resid) {
		if(oldBone!=null && newBone!=null && !oldBone.equals(newBone)) {
			
			final ElfNode oldNode = this.findTargetBySimpleName(oldBone);
			ElfNode newNode = this.findTargetBySimpleName(newBone);
			if(oldNode != null ) {
				if(newNode == null) {
					newNode = oldNode.copySelf(oldNode.getParent());
					newNode.addToParentView();
				}
				
				ElfNode.copyFrom(newNode, oldNode);
				
				newNode.setName(newBone);
				newNode.setResid(resid);
				
				final KeyStorageNode[] ksarray = this.getKeyStorageNodeArray();
				for(final KeyStorageNode ks : ksarray) {
					final KeyFrameArrayNode kfa = ks.findFlashKeyArrayByTargetName(oldBone);
					final KeyFrameArrayNode kfa2 = ks.findFlashKeyArrayByTargetName(newBone);
					if(kfa2 != null) {
						kfa2.removeFromParentView(false);
					}
					
					if(kfa != null) {
						final KeyFrameArrayNode newkfa = (KeyFrameArrayNode)kfa.copyDeep();
						newkfa.setName(newBone);
						final KeyFrameNode[] kfnarray = newkfa.getFlashKeys();
						for(KeyFrameNode kfn : kfnarray) {
							kfn.setResid(resid);
						}
						
						newkfa.setParent(kfa.getParent());
						newkfa.addToParentView();
						newkfa.recheckTarget();
					}
				}
				return true;
			}
		}
		return false;
	}
}
